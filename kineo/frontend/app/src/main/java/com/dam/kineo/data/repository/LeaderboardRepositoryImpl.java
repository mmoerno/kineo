package com.dam.kineo.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.LeaderboardDao;
import com.dam.kineo.data.local.entity.LeaderboardEntryEntity;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.LeaderboardEntryDto;
import com.dam.kineo.domain.model.LeaderboardEntry;
import com.dam.kineo.domain.repository.LeaderboardRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardRepositoryImpl implements LeaderboardRepository {
    private final LeaderboardDao leaderboardDao;
    private final KineoApiService api;

    @Inject
    public LeaderboardRepositoryImpl(LeaderboardDao leaderboardDao, KineoApiService api) {
        this.leaderboardDao = leaderboardDao;
        this.api = api;
    }

    @Override
    public LiveData<List<LeaderboardEntry>> getLeaderboard(String period) {
        return Transformations.map(leaderboardDao.getLeaderboardByPeriod(period), entities -> {
            List<LeaderboardEntry> domainList = new ArrayList<>();
            if (entities != null) {
                for (LeaderboardEntryEntity entity : entities) {
                    domainList.add(new LeaderboardEntry(
                            entity.userId,
                            entity.name,
                            entity.totalSteps,
                            entity.rank
                    ));
                }
            }
            return domainList;
        });
    }

    @Override
    public void syncLeaderboard(String token, String period) {
        api.getLeaderboard("Bearer " + token, period).enqueue(new Callback<List<LeaderboardEntryDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<LeaderboardEntryDto>> call,
                                   @NonNull Response<List<LeaderboardEntryDto>> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                List<LeaderboardEntryEntity> entities = new ArrayList<>();
                for (LeaderboardEntryDto dto : response.body()) {
                    entities.add(new LeaderboardEntryEntity(
                            dto.getUserId(),
                            dto.getName(),
                            dto.getTotalSteps(),
                            dto.getRank(),
                            period
                    ));
                }

                KineoDatabase.databaseWriteExecutor.execute(() -> leaderboardDao.insertEntries(entities));
            }

            @Override
            public void onFailure(@NonNull Call<List<LeaderboardEntryDto>> call,
                                  @NonNull Throwable t) {
            }
        });
    }
}
