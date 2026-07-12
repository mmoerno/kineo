package com.dam.kineo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dam.kineo.data.local.entity.StepSessionEntity;

import java.util.List;

@Dao
public interface StepSessionDao {
    @Query("SELECT 'daily_' || date AS id, date, COALESCE(SUM(steps), 0) AS steps, " +
            "COALESCE(SUM(calories), 0) AS calories, COALESCE(SUM(distanceMeters), 0) AS distanceMeters, " +
            "COALESCE(SUM(activeMinutes), 0) AS activeMinutes FROM step_sessions WHERE date = :date GROUP BY date")
    LiveData<List<StepSessionEntity>> getSessionsByDate(String date);

    @Query("SELECT COALESCE(SUM(steps), 0) FROM step_sessions WHERE date = :date")
    LiveData<Integer> getTotalStepsByDate(String date);

    @Query("SELECT COALESCE(SUM(steps), 0) FROM step_sessions WHERE date = :date")
    int getTotalStepsByDateSync(String date);

    @Query("SELECT COALESCE(SUM(steps), 0) FROM step_sessions")
    int getTotalStepsSync();

    @Query("SELECT COALESCE(MAX(day_steps), 0) FROM (" +
            "SELECT COALESCE(SUM(steps), 0) AS day_steps FROM step_sessions GROUP BY date)")
    int getMaxDailyStepsSync();

    @Query("SELECT COALESCE(SUM(distanceMeters), 0) FROM step_sessions")
    double getTotalDistanceMetersSync();

    @Query("SELECT * FROM step_sessions WHERE id = :id LIMIT 1")
    StepSessionEntity getSessionByIdSync(String id);

    @Query("SELECT 'daily_' || date AS id, date, COALESCE(SUM(steps), 0) AS steps, " +
            "COALESCE(SUM(calories), 0) AS calories, COALESCE(SUM(distanceMeters), 0) AS distanceMeters, " +
            "COALESCE(SUM(activeMinutes), 0) AS activeMinutes FROM step_sessions " +
            "GROUP BY date ORDER BY date DESC LIMIT 7")
    LiveData<List<StepSessionEntity>> getLast7Days();

    @Query("SELECT 'daily_' || date AS id, date, COALESCE(SUM(steps), 0) AS steps, " +
            "COALESCE(SUM(calories), 0) AS calories, COALESCE(SUM(distanceMeters), 0) AS distanceMeters, " +
            "COALESCE(SUM(activeMinutes), 0) AS activeMinutes FROM step_sessions " +
            "GROUP BY date ORDER BY date DESC LIMIT 30")
    LiveData<List<StepSessionEntity>> getLast30Days();

    @Query("SELECT 'daily_' || date AS id, date, COALESCE(SUM(steps), 0) AS steps, " +
            "COALESCE(SUM(calories), 0) AS calories, COALESCE(SUM(distanceMeters), 0) AS distanceMeters, " +
            "COALESCE(SUM(activeMinutes), 0) AS activeMinutes FROM step_sessions " +
            "GROUP BY date ORDER BY date DESC LIMIT 30")
    List<StepSessionEntity> getLast30DaysSync();

    @Query("SELECT COALESCE(SUM(steps), 0) FROM step_sessions WHERE date >= :fromDate")
    LiveData<Integer> getStepsSince(String fromDate);

    @Query("SELECT COUNT(DISTINCT date) FROM step_sessions WHERE steps >= 5000 AND date >= :fromDate")
    int countDaysWithStepsAbove5000(String fromDate);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSession(StepSessionEntity entity);

    @Update
    void updateSession(StepSessionEntity entity);

    @Delete
    void deleteSession(StepSessionEntity entity);
}
