package com.dam.kineo.domain.usecase;

import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.LeaderboardEntryDto;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Callback;

public class GetLeaderboardUseCase {
    private final KineoApiService api;
    private final AuthPreferences authPreferences;

    @Inject
    public GetLeaderboardUseCase(KineoApiService api, AuthPreferences authPreferences) {
        this.api = api;
        this.authPreferences = authPreferences;
    }

    public void execute(String period, Callback<List<LeaderboardEntryDto>> callback) {
        String token = authPreferences.getToken();
        if (token != null) {
            api.getLeaderboard("Bearer " + token, period).enqueue(callback);
        }
    }
}
