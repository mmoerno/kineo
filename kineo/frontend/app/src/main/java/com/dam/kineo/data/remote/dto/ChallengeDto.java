package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class ChallengeDto {
    @SerializedName("id")
    private String id;
    @SerializedName("creator_id")
    private String creatorId;
    @SerializedName("challenger_id")
    private String challengerId;
    @SerializedName("creator_username")
    private String creatorUsername;
    @SerializedName("challenger_username")
    private String challengerUsername;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("target_steps")
    private int targetSteps;
    @SerializedName("metric")
    private String metric;
    @SerializedName("period")
    private String period;
    @SerializedName("starts_at")
    private long startsAt;
    @SerializedName("ends_at")
    private long endsAt;
    @SerializedName("status")
    private String status;
    @SerializedName("winner_id")
    private String winnerId;
    @SerializedName("created_at")
    private long createdAt;
    @SerializedName("updated_at")
    private long updatedAt;

    public ChallengeDto() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }
    public String getChallengerId() { return challengerId; }
    public void setChallengerId(String challengerId) { this.challengerId = challengerId; }
    public String getCreatorUsername() { return creatorUsername; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }
    public String getChallengerUsername() { return challengerUsername; }
    public void setChallengerUsername(String challengerUsername) { this.challengerUsername = challengerUsername; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getTargetSteps() { return targetSteps; }
    public void setTargetSteps(int targetSteps) { this.targetSteps = targetSteps; }
    public String getMetric() { return metric; }
    public void setMetric(String metric) { this.metric = metric; }
    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }
    public long getStartsAt() { return startsAt; }
    public void setStartsAt(long startsAt) { this.startsAt = startsAt; }
    public long getEndsAt() { return endsAt; }
    public void setEndsAt(long endsAt) { this.endsAt = endsAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getWinnerId() { return winnerId; }
    public void setWinnerId(String winnerId) { this.winnerId = winnerId; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
