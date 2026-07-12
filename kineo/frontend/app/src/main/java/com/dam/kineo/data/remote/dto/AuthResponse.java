package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("username")
    private String username;

    @SerializedName("user")
    private UserOut user;

    public AuthResponse() {}

    public String getAccessToken() {
        if (accessToken != null) return accessToken;
        return token;
    }

    public String getUserId() {
        if (userId != null) return userId;
        if (user != null) return user.getId();
        return null;
    }

    public String getUsername() {
        if (username != null) return username;
        if (user != null) return user.getUsername();
        return null;
    }

    public UserOut getUser() { return user; }
    public void setUser(UserOut user) { this.user = user; }
    public void setToken(String token) { this.token = token; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }

    public static class UserOut {
        @SerializedName("id")
        private String id;
        @SerializedName("username")
        private String username;
        @SerializedName("email")
        private String email;

        public UserOut() {}
        public String getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public void setId(String id) { this.id = id; }
        public void setUsername(String username) { this.username = username; }
        public void setEmail(String email) { this.email = email; }
    }
}
