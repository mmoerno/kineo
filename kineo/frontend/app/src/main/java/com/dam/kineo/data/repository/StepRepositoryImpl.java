package com.dam.kineo.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.StepSessionDao;
import com.dam.kineo.data.local.entity.StepSessionEntity;
import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.mapper.StepMapper;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.StepSessionDto;
import com.dam.kineo.domain.model.StepSession;
import com.dam.kineo.domain.repository.StepRepository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StepRepositoryImpl implements StepRepository {
    private final StepSessionDao stepSessionDao;
    private final AuthPreferences authPreferences;
    private final KineoApiService api;

    @Inject
    public StepRepositoryImpl(StepSessionDao stepSessionDao,
                               AuthPreferences authPreferences,
                               KineoApiService api) {
        this.stepSessionDao = stepSessionDao;
        this.authPreferences = authPreferences;
        this.api = api;
    }

    @Override
    public LiveData<StepSession> getTodaySession(String date) {
        return Transformations.map(stepSessionDao.getTotalStepsByDate(date), total -> {
            StepSession summary = new StepSession();
            summary.setDate(date);
            summary.setSteps(total != null ? total : 0);
            summary.setCalories(summary.getSteps() * 0.04);
            summary.setDistanceMeters(summary.getSteps() * 0.7);
            return summary;
        });
    }

    @Override
    public LiveData<Integer> getTodayTotalSteps(String date) {
        return stepSessionDao.getTotalStepsByDate(date);
    }

    @Override
    public LiveData<List<StepSession>> getLast7Days() {
        return Transformations.map(stepSessionDao.getLast7Days(), entities -> {
            List<StepSession> domainList = new ArrayList<>();
            if (entities != null) {
                for (StepSessionEntity entity : entities) {
                    domainList.add(StepMapper.toDomain(entity));
                }
            }
            return domainList;
        });
    }

    @Override
    public LiveData<List<StepSession>> getLast30Days() {
        return Transformations.map(stepSessionDao.getLast30Days(), entities -> {
            List<StepSession> domainList = new ArrayList<>();
            if (entities != null) {
                for (StepSessionEntity entity : entities) {
                    domainList.add(StepMapper.toDomain(entity));
                }
            }
            return domainList;
        });
    }

    @Override
    public LiveData<Integer> getWeeklyStepsTotal() {
        String fromDate = getDateDaysAgo(6);
        return stepSessionDao.getStepsSince(fromDate);
    }

    @Override
    public LiveData<Integer> getMonthlyStepsTotal() {
        String fromDate = getDateDaysAgo(29);
        return stepSessionDao.getStepsSince(fromDate);
    }

    private String getDateDaysAgo(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo);
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
    }

    @Override
    public void saveOrUpdateSteps(int steps) {
        // Implementación delegada al servicio mediante sesiones
    }

    @Override
    public void syncStepsWithApi() {
        KineoDatabase.databaseWriteExecutor.execute(() -> {
            List<StepSessionEntity> localSessions = stepSessionDao.getLast30DaysSync(); 
            if (localSessions == null || localSessions.isEmpty()) return;

            List<StepSessionDto> payload = new ArrayList<>();
            for (StepSessionEntity s : localSessions) {
                payload.add(new StepSessionDto(s.id, s.date, s.steps, s.calories, s.distanceMeters, s.activeMinutes));
            }

            String token = authPreferences.getToken();
            if (token != null) {
                api.syncSteps("Bearer " + token, payload).enqueue(new Callback<Void>() {
                    @Override public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {}
                    @Override public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {}
                });
            }
        });
    }

    @Override
    public void syncTodayFromApi(String token) {
        api.getTodaySteps("Bearer " + token).enqueue(new Callback<StepSessionDto>() {
            @Override
            public void onResponse(@NonNull Call<StepSessionDto> call, @NonNull Response<StepSessionDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    StepSessionDto dto = response.body();
                    KineoDatabase.databaseWriteExecutor.execute(() -> {
                        String id = dto.getId();
                        if (id == null) {
                            if (dto.getDate() == null) return;
                            id = "daily_" + dto.getDate();
                        }
                        
                        StepSessionEntity existing = stepSessionDao.getSessionByIdSync(id);
                        if (existing == null) {
                            StepSessionEntity entity = new StepSessionEntity();
                            entity.id = id;
                            entity.date = dto.getDate();
                            entity.steps = dto.getSteps();
                            entity.calories = dto.getCalories();
                            entity.distanceMeters = dto.getDistanceMeters();
                            entity.activeMinutes = dto.getActiveMinutes();
                            stepSessionDao.insertSession(entity);
                        } else {
                            existing.steps = dto.getSteps();
                            existing.calories = dto.getCalories();
                            existing.distanceMeters = dto.getDistanceMeters();
                            existing.activeMinutes = dto.getActiveMinutes();
                            stepSessionDao.updateSession(existing);
                        }
                    });
                }
            }
            @Override public void onFailure(@NonNull Call<StepSessionDto> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public void syncHistory(String token, int days) {
        api.getStepsHistory("Bearer " + token, days).enqueue(new Callback<List<StepSessionDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<StepSessionDto>> call, @NonNull Response<List<StepSessionDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KineoDatabase.databaseWriteExecutor.execute(() -> {
                        for (StepSessionDto dto : response.body()) {
                            String id = dto.getId();
                            if (id == null) {
                                if (dto.getDate() == null) continue;
                                id = "daily_" + dto.getDate();
                            }
                            
                            StepSessionEntity existing = stepSessionDao.getSessionByIdSync(id);
                            if (existing == null) {
                                StepSessionEntity entity = new StepSessionEntity();
                                entity.id = id;
                                entity.date = dto.getDate();
                                entity.steps = dto.getSteps();
                                entity.calories = dto.getCalories();
                                entity.distanceMeters = dto.getDistanceMeters();
                                entity.activeMinutes = dto.getActiveMinutes();
                                stepSessionDao.insertSession(entity);
                            } else {
                                existing.steps = dto.getSteps();
                                existing.calories = dto.getCalories();
                                existing.distanceMeters = dto.getDistanceMeters();
                                existing.activeMinutes = dto.getActiveMinutes();
                                stepSessionDao.updateSession(existing);
                            }
                        }
                    });
                }
            }
            @Override public void onFailure(@NonNull Call<List<StepSessionDto>> call, @NonNull Throwable t) {}
        });
    }
}
