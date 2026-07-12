package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class LeaderboardEntryDto {
    @SerializedName("user_id")
    private String userId;
    @SerializedName("name")
    private String name;
    @SerializedName("total_steps")
    private int totalSteps;
    @SerializedName("rank")
    private int rank;

    public LeaderboardEntryDto() {}

    public LeaderboardEntryDto(String userId, String name, int totalSteps, int rank) {
        this.userId = userId;
        this.name = name;
        this.totalSteps = totalSteps;
        this.rank = rank;
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalSteps() { return totalSteps; }
    public void setTotalSteps(int totalSteps) { this.totalSteps = totalSteps; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
}
