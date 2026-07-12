package com.dam.kineo.domain.model;

public class StepSession {
    private String id;
    private String date;
    private int steps;
    private double calories;
    private double distanceMeters;
    private int activeMinutes;

    public StepSession() {}

    public StepSession(String id, String date, int steps, double calories, double distanceMeters, int activeMinutes) {
        this.id = id;
        this.date = date;
        this.steps = steps;
        this.calories = calories;
        this.distanceMeters = distanceMeters;
        this.activeMinutes = activeMinutes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getSteps() { return steps; }
    public void setSteps(int steps) { this.steps = steps; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public double getDistanceMeters() { return distanceMeters; }
    public void setDistanceMeters(double distanceMeters) { this.distanceMeters = distanceMeters; }
    public int getActiveMinutes() { return activeMinutes; }
    public void setActiveMinutes(int activeMinutes) { this.activeMinutes = activeMinutes; }
}
