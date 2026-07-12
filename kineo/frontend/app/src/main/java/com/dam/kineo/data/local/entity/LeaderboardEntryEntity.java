package com.dam.kineo.data.local.entity;

import androidx.room.Entity;
import androidx.annotation.NonNull;

@Entity(tableName = "leaderboard_entries", primaryKeys = {"userId", "period"})
public class LeaderboardEntryEntity {
    @NonNull
    public String userId;
    public String name;
    public int totalSteps;
    public int rank;
    @NonNull
    public String period;

    public LeaderboardEntryEntity() {}

    public LeaderboardEntryEntity(@NonNull String userId, String name, int totalSteps, int rank, @NonNull String period) {
        this.userId = userId;
        this.name = name;
        this.totalSteps = totalSteps;
        this.rank = rank;
        this.period = period;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @NonNull
    public String getPeriod() {
        return period;
    }

    public void setPeriod(@NonNull String period) {
        this.period = period;
    }
}
