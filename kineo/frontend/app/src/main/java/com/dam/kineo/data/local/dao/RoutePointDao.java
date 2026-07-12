package com.dam.kineo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dam.kineo.data.local.entity.RoutePointEntity;

import java.util.List;

@Dao
public interface RoutePointDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPoints(List<RoutePointEntity> points);

    @Query("SELECT * FROM route_points WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    LiveData<List<RoutePointEntity>> getPointsBySession(long sessionId);
}
