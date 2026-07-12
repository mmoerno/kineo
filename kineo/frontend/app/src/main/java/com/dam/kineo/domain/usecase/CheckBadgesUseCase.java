package com.dam.kineo.domain.usecase;

import com.dam.kineo.domain.repository.BadgeRepository;
import javax.inject.Inject;

public class CheckBadgesUseCase {
    private final BadgeRepository repository;

    @Inject
    public CheckBadgesUseCase(BadgeRepository repository) {
        this.repository = repository;
    }

    public void execute(int totalSteps, int streakDays) {
        repository.checkAndUnlockBadges(totalSteps, streakDays);
    }

    public void execute(int totalSteps, int maxDailySteps, int streakDays,
                        double totalDistanceMeters, boolean earlyBird) {
        repository.checkAndUnlockBadges(totalSteps, maxDailySteps, streakDays,
                totalDistanceMeters, earlyBird);
    }
}
