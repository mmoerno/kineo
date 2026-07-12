package com.dam.kineo.domain.repository;

import androidx.lifecycle.LiveData;
import com.dam.kineo.domain.model.StepSession;
import java.util.List;

public interface StepRepository {
    LiveData<StepSession> getTodaySession(String date);
    LiveData<Integer> getTodayTotalSteps(String date);
    LiveData<List<StepSession>> getLast7Days();
    LiveData<List<StepSession>> getLast30Days();
    LiveData<Integer> getWeeklyStepsTotal();
    LiveData<Integer> getMonthlyStepsTotal();
    void syncTodayFromApi(String token);
    void syncHistory(String token, int days);
    void syncStepsWithApi();
    void saveOrUpdateSteps(int steps);
}
