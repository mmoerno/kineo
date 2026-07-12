package com.dam.kineo.domain.repository;

import androidx.lifecycle.LiveData;
import com.dam.kineo.domain.model.Badge;
import java.util.List;

public interface BadgeRepository {
    LiveData<List<Badge>> getAllBadges();
    void initDefaultBadges();
    void checkAndUnlockBadges(int totalSteps, int streakDays);
    void checkAndUnlockBadges(int totalSteps, int maxDailySteps, int streakDays,
                              double totalDistanceMeters, boolean earlyBird);
    void syncBadges(String token);
}
