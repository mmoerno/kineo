package com.dam.kineo.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.ChallengeDao;
import com.dam.kineo.data.local.entity.ChallengeEntity;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.ChallengeDto;
import com.dam.kineo.domain.model.Challenge;
import com.dam.kineo.domain.repository.ChallengeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeRepositoryImpl implements ChallengeRepository {
    private final ChallengeDao challengeDao;
    private final KineoApiService api;

    @Inject
    public ChallengeRepositoryImpl(ChallengeDao challengeDao, KineoApiService api) {
        this.challengeDao = challengeDao;
        this.api = api;
    }

    @Override
    public LiveData<List<Challenge>> getActiveChallenges() {
        return Transformations.map(challengeDao.getActiveChallenges(), entities -> {
            List<Challenge> domainList = new ArrayList<>();
            if (entities != null) {
                for (ChallengeEntity entity : entities) {
                    domainList.add(mapToDomain(entity));
                }
            }
            return domainList;
        });
    }

    @Override
    public LiveData<List<Challenge>> getFinishedChallenges() {
        return Transformations.map(challengeDao.getFinishedChallenges(), entities -> {
            List<Challenge> domainList = new ArrayList<>();
            if (entities != null) {
                for (ChallengeEntity entity : entities) {
                    domainList.add(mapToDomain(entity));
                }
            }
            return domainList;
        });
    }

    @Override
    public void syncChallenges(String token) {
        api.getChallenges("Bearer " + token).enqueue(new Callback<List<ChallengeDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<ChallengeDto>> call, @NonNull Response<List<ChallengeDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KineoDatabase.databaseWriteExecutor.execute(() -> {
                        List<ChallengeEntity> entities = new ArrayList<>();
                        for (ChallengeDto dto : response.body()) {
                            entities.add(mapToEntity(dto));
                        }
                        challengeDao.upsertAll(entities);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ChallengeDto>> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public void createChallenge(String token, ChallengeDto dto) {
        api.createChallenge("Bearer " + token, dto).enqueue(new Callback<ChallengeDto>() {
            @Override
            public void onResponse(@NonNull Call<ChallengeDto> call, @NonNull Response<ChallengeDto> response) {
                if (response.isSuccessful()) syncChallenges(token);
            }

            @Override
            public void onFailure(@NonNull Call<ChallengeDto> call, @NonNull Throwable t) {}
        });
    }

    @Override
    public void acceptChallenge(String token, String challengeId) {
        api.acceptChallenge("Bearer " + token, challengeId).enqueue(new Callback<ChallengeDto>() {
            @Override
            public void onResponse(@NonNull Call<ChallengeDto> call, @NonNull Response<ChallengeDto> response) {
                if (response.isSuccessful()) syncChallenges(token);
            }

            @Override
            public void onFailure(@NonNull Call<ChallengeDto> call, @NonNull Throwable t) {}
        });
    }

    private Challenge mapToDomain(ChallengeEntity entity) {
        return new Challenge(entity.id, entity.creatorId, entity.challengerId, entity.creatorUsername, entity.challengerUsername, entity.title, entity.description,
                entity.targetSteps, entity.metric, entity.period, entity.startsAt, entity.endsAt, entity.status, entity.winnerId);
    }

    private ChallengeEntity mapToEntity(ChallengeDto dto) {
        return new ChallengeEntity(dto.getId(), dto.getCreatorId(), dto.getChallengerId(), dto.getCreatorUsername(), dto.getChallengerUsername(), dto.getTitle(), dto.getDescription(),
                dto.getTargetSteps(), dto.getMetric(), dto.getPeriod(), dto.getStartsAt(), dto.getEndsAt(), dto.getStatus(), dto.getWinnerId(), dto.getCreatedAt(), dto.getUpdatedAt());
    }
}
