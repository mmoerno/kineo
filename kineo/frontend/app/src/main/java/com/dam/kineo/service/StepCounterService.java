package com.dam.kineo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.dam.kineo.R;
import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.RoutePointDao;
import com.dam.kineo.data.local.dao.StepSessionDao;
import com.dam.kineo.data.local.entity.RoutePointEntity;
import com.dam.kineo.data.local.entity.StepSessionEntity;
import com.dam.kineo.data.remote.dto.PointDto;
import com.dam.kineo.domain.repository.BadgeRepository;
import com.dam.kineo.domain.repository.StepRepository;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;

@AndroidEntryPoint
public class StepCounterService extends Service implements SensorEventListener {

    private static final String CHANNEL_ID = "kineo_steps";
    private static final int NOTIFICATION_ID = 101;

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    public interface StepCounterServiceEntryPoint {
        StepRepository getStepRepository();
        BadgeRepository getBadgeRepository();
        StepSessionDao getStepSessionDao();
        RoutePointDao getRoutePointDao();
    }

    private StepRepository stepRepository;
    private BadgeRepository badgeRepository;
    private StepSessionDao stepSessionDao;
    private RoutePointDao routePointDao;

    private SensorManager sensorManager;
    private Sensor stepSensor;
    private float stepReference = -1f;
    private String currentSessionId;
    private int stepsInThisSession = 0;
    private int totalStepsToday = 0;
    private int lastSavedSteps = 0;
    private long lastSaveElapsedRealtime = 0L;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean sensorRegistered;

    private final StepServiceBinder binder = new StepServiceBinder();

    public class StepServiceBinder extends Binder {
        private boolean isTrackingRoute = false;
        private final List<PointDto> routePoints = new ArrayList<>();
        private long currentRouteSessionId = -1L;

        public StepCounterService getService() { return StepCounterService.this; }
        public boolean isTrackingRoute() { return isTrackingRoute; }
        public void setCurrentSessionId(long sessionId) { this.currentRouteSessionId = sessionId; }
        public long getCurrentSessionId() { return currentRouteSessionId; }

        public void startRouteTracking() {
            isTrackingRoute = true;
            routePoints.clear();
            LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 15_000L).build();
            try {
                fusedLocationClient.requestLocationUpdates(request, locationCallback, null);
            } catch (SecurityException e) {
                isTrackingRoute = false;
            }
        }

        public List<PointDto> stopRouteTracking() {
            isTrackingRoute = false;
            fusedLocationClient.removeLocationUpdates(locationCallback);
            return new ArrayList<>(routePoints);
        }

        void addRoutePoint(PointDto point) {
            if (isTrackingRoute) routePoints.add(point);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StepCounterServiceEntryPoint entryPoint = EntryPointAccessors.fromApplication(getApplicationContext(), StepCounterServiceEntryPoint.class);
        stepRepository = entryPoint.getStepRepository();
        badgeRepository = entryPoint.getBadgeRepository();
        stepSessionDao = entryPoint.getStepSessionDao();
        routePointDao = entryPoint.getRoutePointDao();

        currentSessionId = UUID.randomUUID().toString();

        createNotificationChannel();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        startForeground(NOTIFICATION_ID, getNotification(getString(R.string.app_name)));
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (!binder.isTrackingRoute()) return;
                for (Location location : locationResult.getLocations()) {
                    PointDto point = new PointDto(location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getTime());
                    binder.addRoutePoint(point);
                    RoutePointEntity entity = new RoutePointEntity();
                    entity.sessionId = binder.getCurrentSessionId();
                    entity.latitude = point.getLatitude();
                    entity.longitude = point.getLongitude();
                    entity.altitude = point.getAltitude();
                    entity.timestamp = point.getTimestamp();
                    KineoDatabase.databaseWriteExecutor.execute(() -> routePointDao.insertPoints(Collections.singletonList(entity)));
                }
            }
        };
        refreshTotalSteps();
        registerStepSensorIfPossible();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerStepSensorIfPossible();
        return START_STICKY;
    }

    private void registerStepSensorIfPossible() {
        if (sensorRegistered || sensorManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            updateNotificationText("Permiso de actividad pendiente");
            return;
        }
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor == null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        }
        if (stepSensor == null) {
            updateNotificationText("Sensor de pasos no disponible");
            return;
        }
        sensorRegistered = sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            stepsInThisSession += Math.max(1, (int) event.values[0]);
            if (SystemClock.elapsedRealtime() - lastSaveElapsedRealtime >= 1500L) {
                saveCurrentSession();
            }
            return;
        }

        if (event.sensor.getType() != Sensor.TYPE_STEP_COUNTER) return;

        if (stepReference < 0f) {
            stepReference = event.values[0];
            return;
        }

        stepsInThisSession = (int) (event.values[0] - stepReference);
        if (stepsInThisSession < 0) {
            stepReference = event.values[0];
            stepsInThisSession = 0;
        }

        if (stepsInThisSession > lastSavedSteps
                && SystemClock.elapsedRealtime() - lastSaveElapsedRealtime >= 1500L) {
            saveCurrentSession();
        }
    }

    private void saveCurrentSession() {
        KineoDatabase.databaseWriteExecutor.execute(() -> {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            StepSessionEntity session = new StepSessionEntity();
            session.id = currentSessionId;
            session.date = today;
            session.steps = stepsInThisSession;
            session.calories = stepsInThisSession * 0.04;
            session.distanceMeters = stepsInThisSession * 0.7;
            stepSessionDao.insertSession(session);
            lastSavedSteps = stepsInThisSession;
            lastSaveElapsedRealtime = SystemClock.elapsedRealtime();
            checkBadges(today);
            stepRepository.syncStepsWithApi();
            refreshTotalSteps();
        });
    }

    private void checkBadges(String today) {
        int totalSteps = stepSessionDao.getTotalStepsSync();
        int maxDailySteps = stepSessionDao.getMaxDailyStepsSync();
        int streakDays = stepSessionDao.countDaysWithStepsAbove5000(getDateDaysAgo(6));
        double totalDistanceMeters = stepSessionDao.getTotalDistanceMetersSync();
        int hour = Integer.parseInt(new SimpleDateFormat("HH", Locale.getDefault()).format(new Date()));
        boolean earlyBird = today != null && hour < 8 && stepsInThisSession > 0;
        badgeRepository.checkAndUnlockBadges(totalSteps, maxDailySteps, streakDays,
                totalDistanceMeters, earlyBird);
    }

    private String getDateDaysAgo(int daysAgo) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.add(java.util.Calendar.DAY_OF_YEAR, -daysAgo);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    private void refreshTotalSteps() {
        KineoDatabase.databaseWriteExecutor.execute(() -> {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            totalStepsToday = stepSessionDao.getTotalStepsByDateSync(today);
            StepLiveData.postTodaySteps(totalStepsToday);
            updateNotification();
        });
    }

    private void updateNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(NOTIFICATION_ID, getNotification(getString(R.string.app_name) + " — " + totalStepsToday + " pasos hoy"));
        }
    }

    private void updateNotificationText(String text) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(NOTIFICATION_ID, getNotification(text));
        }
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    @Override public IBinder onBind(Intent intent) { return binder; }

    private Notification getNotification(String text) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "kineo_steps", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        saveCurrentSession();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorRegistered = false;
        }
        super.onDestroy();
    }
}
