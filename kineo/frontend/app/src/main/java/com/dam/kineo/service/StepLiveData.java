package com.dam.kineo.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public final class StepLiveData {
    private static final MutableLiveData<Integer> TODAY_STEPS = new MutableLiveData<>(0);

    private StepLiveData() {}

    public static LiveData<Integer> todaySteps() {
        return TODAY_STEPS;
    }

    public static void postTodaySteps(int steps) {
        TODAY_STEPS.postValue(steps);
    }
}
