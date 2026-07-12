package com.dam.kineo.data.remote.dto;

import com.google.gson.annotations.SerializedName;

public class StepCountsDto {
    @SerializedName("weekly_total")
    private int weeklyTotal;
    @SerializedName("monthly_total")
    private int monthlyTotal;

    public StepCountsDto() {}

    public StepCountsDto(int weeklyTotal, int monthlyTotal) {
        this.weeklyTotal = weeklyTotal;
        this.monthlyTotal = monthlyTotal;
    }

    public int getWeeklyTotal() {
        return weeklyTotal;
    }

    public void setWeeklyTotal(int weeklyTotal) {
        this.weeklyTotal = weeklyTotal;
    }

    public int getMonthlyTotal() {
        return monthlyTotal;
    }

    public void setMonthlyTotal(int monthlyTotal) {
        this.monthlyTotal = monthlyTotal;
    }
}
