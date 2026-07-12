package com.dam.kineo.domain.model;

public class LeaderboardEntry {
    private String userId;
    private String name;
    private int totalSteps;
    private int rank;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String userId, String name, int totalSteps, int rank) {
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
