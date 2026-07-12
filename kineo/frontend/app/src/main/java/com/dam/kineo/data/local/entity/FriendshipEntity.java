package com.dam.kineo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "friendships", primaryKeys = {"requesterId", "addresseeId"})
public class FriendshipEntity {
    @NonNull
    public String requesterId;
    @NonNull
    public String addresseeId;
    public String displayName;
    public String username;
    public String status;
    public int todaySteps;
    public long updatedAt;

    public FriendshipEntity() {}

    @Ignore
    public FriendshipEntity(@NonNull String requesterId, @NonNull String addresseeId, String displayName, String username, String status, int todaySteps, long updatedAt) {
        this.requesterId = requesterId;
        this.addresseeId = addresseeId;
        this.displayName = displayName;
        this.username = username;
        this.status = status;
        this.todaySteps = todaySteps;
        this.updatedAt = updatedAt;
    }

    @NonNull public String getRequesterId() { return requesterId; }
    public void setRequesterId(@NonNull String requesterId) { this.requesterId = requesterId; }
    @NonNull public String getAddresseeId() { return addresseeId; }
    public void setAddresseeId(@NonNull String addresseeId) { this.addresseeId = addresseeId; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getTodaySteps() { return todaySteps; }
    public void setTodaySteps(int todaySteps) { this.todaySteps = todaySteps; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
