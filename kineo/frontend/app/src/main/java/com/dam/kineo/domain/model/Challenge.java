package com.dam.kineo.domain.model;

public class Challenge {
    private String id;
    private String creatorId;
    private String challengerId;
    private String creatorUsername;
    private String challengerUsername;
    private String title;
    private String description;
    private int targetSteps;
    private String metric;
    private String period;
    private long startsAt;
    private long endsAt;
    private String status;
    private String winnerId;

    public Challenge() {}

    public Challenge(String id, String creatorId, String challengerId, String creatorUsername, String challengerUsername, String title, String description, int targetSteps, String metric, String period, long startsAt, long endsAt, String status, String winnerId) {
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
    }

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
}
