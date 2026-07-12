package com.dam.kineo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dam.kineo.data.local.entity.BadgeEntity;

import java.util.List;

@Dao
public interface BadgeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BadgeEntity> badges);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertDefaults(List<BadgeEntity> badges);

    @Query("UPDATE badges SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :badgeId")
    void unlockBadge(String badgeId, long timestamp);

    @Query("SELECT * FROM badges ORDER BY isUnlocked DESC, unlockedAt DESC")
    LiveData<List<BadgeEntity>> getAllBadges();
}
