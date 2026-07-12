package com.dam.kineo.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.BadgeDao;
import com.dam.kineo.data.local.entity.BadgeEntity;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.BadgeDto;
import com.dam.kineo.domain.model.Badge;
import com.dam.kineo.domain.repository.BadgeRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BadgeRepositoryImpl implements BadgeRepository {
    private final BadgeDao badgeDao;
    private final KineoApiService api;

    @Inject
    public BadgeRepositoryImpl(BadgeDao badgeDao, KineoApiService api) {
        this.badgeDao = badgeDao;
        this.api = api;
    }

    @Override
    public LiveData<List<Badge>> getAllBadges() {
        return Transformations.map(badgeDao.getAllBadges(), entities -> {
            List<Badge> domainList = new ArrayList<>();
            if (entities != null) {
                for (BadgeEntity entity : entities) {
                    domainList.add(new Badge(entity.id, entity.name, entity.description, entity.iconRes, entity.unlockedAt, entity.isUnlocked));
                }
            }
            return domainList;
        });
    }

    @Override
    public void initDefaultBadges() {
        KineoDatabase.databaseWriteExecutor.execute(() -> {
            List<BadgeEntity> defaults = new ArrayList<>();
            defaults.add(new BadgeEntity("first_1000", "Primeros Pasos", "1000 pasos acumulados", "ic_badge_steps", null, false));
            defaults.add(new BadgeEntity("five_k", "5K Runner", "5000 pasos en un día", "ic_badge_5k", null, false));
            defaults.add(new BadgeEntity("ten_k", "10K Club", "10000 pasos en un día", "ic_badge_10k", null, false));
            defaults.add(new BadgeEntity("week_streak", "Semana Activa", "7 días con >5000 pasos", "ic_badge_streak", null, false));
            defaults.add(new BadgeEntity("early_bird", "Madrugador", "primera sesión antes de las 8:00", "ic_badge_morning", null, false));
            defaults.add(new BadgeEntity("marathon", "Andarín de Utrera", "42195 metros acumulados", "ic_badge_marathon", null, false));
            defaults.add(new BadgeEntity("andarin_utrera", "Andarin de Utrera", "70000 pasos acumulados", "ic_badge_marathon", null, false));
            defaults.add(new BadgeEntity("cachimber_compostela", "Cachimber de Compostela", "100000 pasos acumulados", "ic_badge_streak", null, false));
            badgeDao.insertDefaults(defaults);
        });
    }

    @Override
    public void checkAndUnlockBadges(int totalSteps, int streakDays) {
        checkAndUnlockBadges(totalSteps, totalSteps, streakDays, 0d, false);
    }

    @Override
    public void checkAndUnlockBadges(int totalSteps, int maxDailySteps, int streakDays,
                                     double totalDistanceMeters, boolean earlyBird) {
        KineoDatabase.databaseWriteExecutor.execute(() -> {
            long timestamp = System.currentTimeMillis();
            if (totalSteps >= 1000) badgeDao.unlockBadge("first_1000", timestamp);
            if (totalSteps >= 70000) badgeDao.unlockBadge("andarin_utrera", timestamp);
            if (totalSteps >= 100000) badgeDao.unlockBadge("cachimber_compostela", timestamp);
            if (maxDailySteps >= 5000) badgeDao.unlockBadge("five_k", timestamp);
            if (maxDailySteps >= 10000) badgeDao.unlockBadge("ten_k", timestamp);
            if (totalDistanceMeters >= 42195d) badgeDao.unlockBadge("marathon", timestamp);
            if (streakDays >= 7) badgeDao.unlockBadge("week_streak", timestamp);
            if (earlyBird) badgeDao.unlockBadge("early_bird", timestamp);
        });
    }

    @Override
    public void syncBadges(String token) {
        api.getBadges("Bearer " + token).enqueue(new Callback<List<BadgeDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<BadgeDto>> call,
                                   @NonNull Response<List<BadgeDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KineoDatabase.databaseWriteExecutor.execute(() -> {
                        List<BadgeEntity> entities = new ArrayList<>();
                        for (BadgeDto dto : response.body()) {
                            entities.add(mapToEntity(dto));
                        }
                        badgeDao.insertAll(entities);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BadgeDto>> call,
                                  @NonNull Throwable t) {
            }
        });
    }

    private BadgeEntity mapToEntity(BadgeDto dto) {
        BadgeEntity entity = new BadgeEntity();
        entity.id = dto.getId();
        entity.name = dto.getName();
        entity.description = dto.getDescription();
        entity.iconRes = dto.getIconUrl();
        entity.unlockedAt = dto.getUnlockedAt();
        entity.isUnlocked = dto.isUnlocked();
        return entity;
    }
}
