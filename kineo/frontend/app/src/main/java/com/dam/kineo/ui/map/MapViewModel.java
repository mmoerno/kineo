package com.dam.kineo.ui.map;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.dam.kineo.data.local.dao.RoutePointDao;
import com.dam.kineo.data.local.entity.RoutePointEntity;
import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.remote.dto.PointDto;
import com.dam.kineo.domain.repository.RouteRepository;
import com.dam.kineo.service.StepCounterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MapViewModel extends ViewModel {

    private final MutableLiveData<Long> routeSessionId = new MutableLiveData<>(-1L);

    public final LiveData<List<RoutePointEntity>> routePoints;

    private final MutableLiveData<Boolean> _isTracking = new MutableLiveData<>(false);
    private final MutableLiveData<String> _routeDistance = new MutableLiveData<>("0.0 km");
    private final MutableLiveData<String> _routeTime = new MutableLiveData<>("00:00");

    private final MutableLiveData<StepCounterService.StepServiceBinder> stepBinder = new MutableLiveData<>();

    public final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof StepCounterService.StepServiceBinder) {
                stepBinder.setValue((StepCounterService.StepServiceBinder) service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            stepBinder.setValue(null);
        }
    };

    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final RouteRepository routeRepository;
    private final AuthPreferences authPreferences;
    private long timerStartElapsedRealtime;
    private long timerAccumulatedMs;
    private boolean timerRunning;

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (!timerRunning) {
                return;
            }
            long elapsedMs = timerAccumulatedMs + (SystemClock.elapsedRealtime() - timerStartElapsedRealtime);
            _routeTime.postValue(formatElapsed(elapsedMs));
            timerHandler.postDelayed(this, 1000L);
        }
    };

    @Inject
    public MapViewModel(RoutePointDao routePointDao, RouteRepository routeRepository, AuthPreferences authPreferences) {
        this.routeRepository = routeRepository;
        this.authPreferences = authPreferences;
        this.routePoints = Transformations.switchMap(routeSessionId, id -> {
            if (id == null || id < 0) {
                MutableLiveData<List<RoutePointEntity>> empty = new MutableLiveData<>();
                empty.setValue(new ArrayList<>());
                return empty;
            }
            return routePointDao.getPointsBySession(id);
        });
    }

    public void startNewRouteSession() {
        long newSessionId = System.currentTimeMillis();
        routeSessionId.setValue(newSessionId);
    }

    public long getCurrentSessionId() {
        Long id = routeSessionId.getValue();
        return (id != null && id >= 0) ? id : 0L;
    }

    public void setRouteSessionId(long sessionId) {
        routeSessionId.setValue(sessionId);
    }

    public LiveData<Boolean> getIsTracking() {
        return _isTracking;
    }

    public LiveData<String> getRouteDistance() {
        return _routeDistance;
    }

    public LiveData<String> getRouteTime() {
        return _routeTime;
    }

    public LiveData<StepCounterService.StepServiceBinder> getStepBinder() {
        return stepBinder;
    }

    public void setIsTracking(boolean tracking) {
        _isTracking.setValue(tracking);
    }

    public void onRoutePointsUpdated(List<PointDto> points) {
        if (points == null || points.size() < 2) {
            _routeDistance.setValue("0.0 km");
            return;
        }
        double totalMeters = 0d;
        for (int i = 1; i < points.size(); i++) {
            PointDto a = points.get(i - 1);
            PointDto b = points.get(i);
            totalMeters += haversineMeters(a.getLatitude(), a.getLongitude(), b.getLatitude(), b.getLongitude());
        }
        double km = totalMeters / 1000d;
        _routeDistance.setValue(String.format(Locale.getDefault(), "%.1f km", km));
    }

    public void uploadRoutePoints(List<PointDto> points) {
        String token = authPreferences.getToken();
        if (token == null || points == null || points.isEmpty()) {
            return;
        }
        String sessionId = String.valueOf(getCurrentSessionId());
        routeRepository.syncRoute(token, sessionId, points);
    }

    private static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        final double earthRadiusM = 6371000d;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double rLat1 = Math.toRadians(lat1);
        double rLat2 = Math.toRadians(lat2);
        double h = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * earthRadiusM * Math.asin(Math.min(1d, Math.sqrt(h)));
    }

    public void startTimer() {
        stopTimerInternal(false);
        timerStartElapsedRealtime = SystemClock.elapsedRealtime();
        timerRunning = true;
        timerHandler.post(timerRunnable);
    }

    public void stopTimer() {
        stopTimerInternal(true);
        _routeTime.setValue("00:00");
    }

    private void stopTimerInternal(boolean resetAccumulated) {
        timerHandler.removeCallbacks(timerRunnable);
        if (timerRunning) {
            timerAccumulatedMs += SystemClock.elapsedRealtime() - timerStartElapsedRealtime;
        }
        timerRunning = false;
        if (resetAccumulated) {
            timerAccumulatedMs = 0L;
        }
    }

    private static String formatElapsed(long elapsedMs) {
        long totalSeconds = elapsedMs / 1000L;
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
