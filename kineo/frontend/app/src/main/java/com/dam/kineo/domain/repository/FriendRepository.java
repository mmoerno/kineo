package com.dam.kineo.domain.repository;

import androidx.lifecycle.LiveData;
import com.dam.kineo.domain.model.Friend;
import java.util.List;

public interface FriendRepository {
    LiveData<List<Friend>> getAcceptedFriends();
    LiveData<List<Friend>> getPendingRequests(String myUserId);
    void syncFriends(String token);
    void sendFriendRequest(String token, String targetUsername);
    void acceptFriendRequest(String token, String requesterId);
    void removeFriend(String token, String userId);
}
