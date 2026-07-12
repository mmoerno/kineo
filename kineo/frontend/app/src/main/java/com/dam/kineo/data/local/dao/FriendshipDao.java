package com.dam.kineo.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dam.kineo.data.local.entity.FriendshipEntity;

import java.util.List;

@Dao
public interface FriendshipDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void upsertAll(List<FriendshipEntity> friendships);

    @Query("SELECT * FROM friendships WHERE status = 'accepted' AND (requesterId = :myUserId OR addresseeId = :myUserId)")
    LiveData<List<FriendshipEntity>> getAcceptedFriends(String myUserId);

    @Query("SELECT * FROM friendships WHERE status = 'pending' AND addresseeId = :myUserId")
    LiveData<List<FriendshipEntity>> getPendingRequests(String myUserId);

    @Query("DELETE FROM friendships WHERE (requesterId = :a AND addresseeId = :b) OR (requesterId = :b AND addresseeId = :a)")
    void deleteFriendship(String a, String b);
}
