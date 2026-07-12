package com.dam.kineo.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "challenges")
public class ChallengeEntity {
    @PrimaryKey
    @NonNull
    public String id;
    public String creatorId;
    public String challengerId;
    public String creatorUsername;
    public String challengerUsername;
    public String title;
    public String description;
    public int targetSteps;
    public String metric;
    public String period;
    public long startsAt;
    public long endsAt;
    public String status;
    public String winnerId;
    public long createdAt;
    public long updatedAt;

    public ChallengeEntity() {}

    @Ignore
    public ChallengeEntity(@NonNull String id, String creatorId, String challengerId, String creatorUsername, String challengerUsername, String title, String description, int targetSteps, String metric, String period, long startsAt, long endsAt, String status, String winnerId, long createdAt, long updatedAt) {
        this.id = id;
        this.creatorId = creatorId;
        this.challengerId = challengerId;
        this.creatorUsername = creatorUsername;
        this.challengerUsername = challengerUsername;
        this.title = title;
        this.description = description;
        this.targetSteps = targetSteps;
        this.metric = metric;
        this.period = period;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.status = status;
        this.winnerId = winnerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @NonNull public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
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
