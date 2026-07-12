package com.dam.kineo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.domain.model.Badge;
import com.dam.kineo.domain.model.StepSession;
import com.dam.kineo.domain.repository.BadgeRepository;
import com.dam.kineo.domain.repository.StepRepository;
import com.dam.kineo.domain.usecase.CheckBadgesUseCase;
import com.dam.kineo.domain.usecase.UpdateStepsUseCase;
import com.dam.kineo.service.StepLiveData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    public final int dailyGoal = 8000;

    private final UpdateStepsUseCase updateStepsUseCase;
    @SuppressWarnings("unused")
    private final CheckBadgesUseCase checkBadgesUseCase;
    private final AuthPreferences authPreferences;
    private final StepRepository stepRepository;

    private final MutableLiveData<StepSession> _todaySession = new MutableLiveData<>();
    private final MutableLiveData<List<Badge>> _badges = new MutableLiveData<>();
    private final MutableLiveData<Float> _progressPercent = new MutableLiveData<>(0f);
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();

    private final LiveData<StepSession> todaySessionSource;
    private final LiveData<List<Badge>> badgesSource;

    private final Observer<StepSession> todaySessionObserver = this::onTodaySessionChanged;
    private final Observer<List<Badge>> badgesObserver = _badges::setValue;
    private final Observer<Integer> liveStepsObserver = this::onLiveStepsChanged;

    @Inject
    public HomeViewModel(
            StepRepository stepRepository,
            BadgeRepository badgeRepository,
            UpdateStepsUseCase updateStepsUseCase,
            CheckBadgesUseCase checkBadgesUseCase,
            AuthPreferences authPreferences
    ) {
        this.updateStepsUseCase = updateStepsUseCase;
        this.checkBadgesUseCase = checkBadgesUseCase;
        this.authPreferences = authPreferences;
        this.stepRepository = stepRepository;

        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        todaySessionSource = stepRepository.getTodaySession(today);
        todaySessionSource.observeForever(todaySessionObserver);
        StepLiveData.todaySteps().observeForever(liveStepsObserver);

        badgesSource = badgeRepository.getAllBadges();
        badgesSource.observeForever(badgesObserver);

        String token = authPreferences.getToken();
        if (token != null) {
            stepRepository.syncTodayFromApi(token);
            badgeRepository.syncBadges(token);
        }
    }

    private void onTodaySessionChanged(StepSession session) {
        _todaySession.setValue(session);
        int steps = session != null ? session.getSteps() : 0;
        float percent = Math.min(1f, steps / (float) dailyGoal) * 100f;
        _progressPercent.setValue(percent);
    }

    private void onLiveStepsChanged(Integer steps) {
        if (steps == null) {
            return;
        }
        StepSession current = _todaySession.getValue();
        if (current == null) {
            current = new StepSession();
            current.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        }
        current.setSteps(steps);
        current.setCalories(steps * 0.04);
        current.setDistanceMeters(steps * 0.7);
        onTodaySessionChanged(current);
    }

    public LiveData<StepSession> getTodaySession() {
        return _todaySession;
    }

    public LiveData<List<Badge>> getBadges() {
        return _badges;
    }

    public LiveData<Float> getProgressPercent() {
        return _progressPercent;
    }

    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    public void addSteps(int steps) {
        StepSession current = _todaySession.getValue();
        int currentSteps = current != null ? current.getSteps() : 0;
        updateStepsUseCase.execute(currentSteps + steps);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        todaySessionSource.removeObserver(todaySessionObserver);
        badgesSource.removeObserver(badgesObserver);
        StepLiveData.todaySteps().removeObserver(liveStepsObserver);
    }
}
