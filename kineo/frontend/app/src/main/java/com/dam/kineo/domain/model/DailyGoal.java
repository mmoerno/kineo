package com.dam.kineo.domain.model;

public class DailyGoal {
    private int stepsGoal;
    private double caloriesGoal;

    public DailyGoal() {
        this.stepsGoal = 8000;
        this.caloriesGoal = 320.0;
    }

    public DailyGoal(int stepsGoal, double caloriesGoal) {
        this.stepsGoal = stepsGoal;
        this.caloriesGoal = caloriesGoal;
    }

    public int getStepsGoal() { return stepsGoal; }
    public void setStepsGoal(int stepsGoal) { this.stepsGoal = stepsGoal; }
    public double getCaloriesGoal() { return caloriesGoal; }
    public void setCaloriesGoal(double caloriesGoal) { this.caloriesGoal = caloriesGoal; }
}
