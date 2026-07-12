package com.dam.kineo.data.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.FriendshipDao;
import com.dam.kineo.data.local.entity.FriendshipEntity;
import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.FriendDto;
import com.dam.kineo.data.remote.dto.FriendRequestDto;
import com.dam.kineo.domain.model.Friend;
import com.dam.kineo.domain.repository.FriendRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendRepositoryImpl implements FriendRepository {

    private final FriendshipDao friendshipDao;
    private final KineoApiService api;
    private final AuthPreferences authPreferences;

    @Inject
    public FriendRepositoryImpl(FriendshipDao friendshipDao,
                                 KineoApiService api,
                                 AuthPreferences authPreferences) {
        this.friendshipDao = friendshipDao;
        this.api = api;
        this.authPreferences = authPreferences;
    }

    private String myUserId() {
        String id = authPreferences.getUserId();
        return id != null ? id : "";
    }

    @Override
    public LiveData<List<Friend>> getAcceptedFriends() {
        return Transformations.map(friendshipDao.getAcceptedFriends(myUserId()), entities -> {
            List<Friend> domainList = new ArrayList<>();
            if (entities != null) {
                String me = myUserId();
                for (FriendshipEntity entity : entities) {
                    String friendId = entity.requesterId.equals(me)
                            ? entity.addresseeId : entity.requesterId;
                    String friendName = entity.displayName;
                    domainList.add(new Friend(friendId, friendName, entity.username, null,
                            null, entity.status, entity.todaySteps));
                }
            }
            return domainList;
        });
    }

    @Override
    public LiveData<List<Friend>> getPendingRequests(String myUserId) {
        return Transformations.map(
                friendshipDao.getPendingRequests(myUserId()), entities -> {
            List<Friend> domainList = new ArrayList<>();
            if (entities != null) {
                for (FriendshipEntity entity : entities) {
                    domainList.add(new Friend(entity.requesterId, entity.displayName,
                            entity.username, null, null, entity.status, entity.todaySteps));
                }
            }
            return domainList;
        });
    }

    @Override
    public void syncFriends(String token) {
        String me = myUserId();

        api.getFriends("Bearer " + token).enqueue(new Callback<List<FriendDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<FriendDto>> call,
                                   @NonNull Response<List<FriendDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KineoDatabase.databaseWriteExecutor.execute(() -> {
                        List<FriendshipEntity> entities = new ArrayList<>();
                            for (FriendDto dto : response.body()) {
                            // Si el DTO no trae IDs específicos, asumimos que el amigo es el "otro"
                            String requesterId = dto.getRequesterId() != null ? dto.getRequesterId() : dto.getId();
                            String addresseeId = dto.getAddresseeId() != null ? dto.getAddresseeId() : me;
                            
                            // Normalizamos: si yo soy el requester, el amigo es el addressee y viceversa
                            if (requesterId == null || addresseeId == null) {
                                continue;
                            }

                            String friendName = dto.getName() != null ? dto.getName() : "";
                            String friendUsername = dto.getUsername() != null ? dto.getUsername() : "";
                            
                            entities.add(new FriendshipEntity(
                                    requesterId, addresseeId, friendName, friendUsername,
                                    "accepted", dto.getTodaySteps(), System.currentTimeMillis()));
                        }
                        friendshipDao.upsertAll(entities);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FriendDto>> call,
                                  @NonNull Throwable t) {}
        });

        api.getFriendRequests("Bearer " + token).enqueue(new Callback<List<FriendDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<FriendDto>> call,
                                   @NonNull Response<List<FriendDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    KineoDatabase.databaseWriteExecutor.execute(() -> {
                        List<FriendshipEntity> entities = new ArrayList<>();
                        for (FriendDto dto : response.body()) {
                            String requesterId = dto.getRequesterId() != null
                                    ? dto.getRequesterId() : dto.getId();
                            String friendName = dto.getName() != null
                                    ? dto.getName() : "";
                            String friendUsername = dto.getUsername() != null
                                    ? dto.getUsername() : "";
                            entities.add(new FriendshipEntity(
                                    requesterId, me, friendName, friendUsername,
                                    "pending", dto.getTodaySteps(), System.currentTimeMillis()));
                        }
                        friendshipDao.upsertAll(entities);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FriendDto>> call,
                                  @NonNull Throwable t) {}
        });
    }

    @Override
    public void sendFriendRequest(String token, String targetUsername) {
        FriendRequestDto request = new FriendRequestDto(targetUsername);

        api.sendFriendRequest("Bearer " + token, request).enqueue(new Callback<FriendDto>() {
            @Override
            public void onResponse(@NonNull Call<FriendDto> call,
                                   @NonNull Response<FriendDto> response) {
                if (response.isSuccessful()) syncFriends(token);
            }

            @Override
            public void onFailure(@NonNull Call<FriendDto> call,
                                  @NonNull Throwable t) {}
        });
    }

    @Override
    public void acceptFriendRequest(String token, String requesterId) {
        api.acceptFriendRequest("Bearer " + token, requesterId)
                .enqueue(new Callback<FriendDto>() {
            @Override
            public void onResponse(@NonNull Call<FriendDto> call,
                                   @NonNull Response<FriendDto> response) {
                if (response.isSuccessful()) syncFriends(token);
            }

            @Override
            public void onFailure(@NonNull Call<FriendDto> call,
                                  @NonNull Throwable t) {}
        });
    }

    @Override
    public void removeFriend(String token, String userId) {
        api.removeFriend("Bearer " + token, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    KineoDatabase.databaseWriteExecutor.execute(() ->
                            friendshipDao.deleteFriendship(myUserId(), userId));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call,
                                  @NonNull Throwable t) {}
        });
    }
}
