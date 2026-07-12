package com.dam.kineo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dam.kineo.data.local.entity.ChallengeEntity;

import java.util.List;

@Dao
public interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertAll(List<ChallengeEntity> challenges);

    @Query("SELECT * FROM challenges WHERE status IN ('pending_acceptance','active') ORDER BY endsAt ASC")
    LiveData<List<ChallengeEntity>> getActiveChallenges();

    @Query("SELECT * FROM challenges WHERE status IN ('finished','cancelled') ORDER BY endsAt DESC")
    LiveData<List<ChallengeEntity>> getFinishedChallenges();
}
