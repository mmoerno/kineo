package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class FriendDto {

    @SerializedName("id")
    private String id;

    @SerializedName("requester_id")
    private String requesterId;

    @SerializedName("addressee_id")
    private String addresseeId;

    @SerializedName("name")
    private String name;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("status")
    private String status;

    @SerializedName("today_steps")
    private int todaySteps;

    public FriendDto() {}

    public FriendDto(String id, String username, String name, String email,
                     String avatarUrl, String status, int todaySteps) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.todaySteps = todaySteps;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getRequesterId() { return requesterId; }
    public void setRequesterId(String requesterId) { this.requesterId = requesterId; }

    public String getAddresseeId() { return addresseeId; }
    public void setAddresseeId(String addresseeId) { this.addresseeId = addresseeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTodaySteps() { return todaySteps; }
    public void setTodaySteps(int todaySteps) { this.todaySteps = todaySteps; }
}
