package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class StepSessionDto {
    @SerializedName("id")
    private String id;
    @SerializedName("date")
    private String date;
    @SerializedName("steps")
    private int steps;
    @SerializedName("calories")
    private double calories;
    @SerializedName("distance_meters")
    private double distanceMeters;
    @SerializedName("active_minutes")
    private int activeMinutes;

    public StepSessionDto() {}

    public StepSessionDto(String id, String date, int steps, double calories, double distanceMeters, int activeMinutes) {
        this.id = id;
        this.date = date;
        this.steps = steps;
        this.calories = calories;
        this.distanceMeters = distanceMeters;
        this.activeMinutes = activeMinutes;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public int getSteps() { return steps; }
    public double getCalories() { return calories; }
    public double getDistanceMeters() { return distanceMeters; }
    public int getActiveMinutes() { return activeMinutes; }
}
