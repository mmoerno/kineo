package com.dam.kineo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dam.kineo.data.local.entity.LeaderboardEntryEntity;

import java.util.List;

@Dao
public interface LeaderboardDao {
    @Query("SELECT * FROM leaderboard_entries WHERE period = :period ORDER BY rank ASC")
    LiveData<List<LeaderboardEntryEntity>> getLeaderboardByPeriod(String period);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntries(List<LeaderboardEntryEntity> entries);
}
