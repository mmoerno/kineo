package com.dam.kineo.domain.repository;

import androidx.lifecycle.LiveData;

import com.dam.kineo.domain.model.LeaderboardEntry;

import java.util.List;

public interface LeaderboardRepository {
    LiveData<List<LeaderboardEntry>> getLeaderboard(String period);
    void syncLeaderboard(String token, String period);
}
