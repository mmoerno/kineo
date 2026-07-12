package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class FriendRequestDto {
    @SerializedName("username")
    private String username;

    public FriendRequestDto() {}

    public FriendRequestDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
