package com.dam.kineo.ui.history;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.domain.model.StepSession;
import com.dam.kineo.domain.repository.StepRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HistoryViewModel extends ViewModel {

    public final LiveData<List<StepSession>> sessions7Days;
    public final LiveData<List<StepSession>> sessions30Days;
    public final LiveData<Integer> weeklyTotal;
    public final LiveData<Integer> monthlyTotal;

    private final MutableLiveData<Integer> _selectedPeriod = new MutableLiveData<>(7);

    private final MediatorLiveData<List<StepSession>> currentSessions = new MediatorLiveData<>();
    private final StepRepository stepRepository;
    private final AuthPreferences authPreferences;

    @Inject
    public HistoryViewModel(StepRepository stepRepository, AuthPreferences authPreferences) {
        this.stepRepository = stepRepository;
        this.authPreferences = authPreferences;
        
        sessions7Days = stepRepository.getLast7Days();
        sessions30Days = stepRepository.getLast30Days();
        weeklyTotal = stepRepository.getWeeklyStepsTotal();
        monthlyTotal = stepRepository.getMonthlyStepsTotal();

        currentSessions.addSource(_selectedPeriod, period -> emitCurrentSessions());
        currentSessions.addSource(sessions7Days, sessions -> emitCurrentSessions());
        currentSessions.addSource(sessions30Days, sessions -> emitCurrentSessions());
        
        syncHistory();
    }

    private void syncHistory() {
        String token = authPreferences.getToken();
        if (token != null) {
            stepRepository.syncHistory(token, 30);
        }
    }

    private void emitCurrentSessions() {
        Integer period = _selectedPeriod.getValue();
        if (period != null && period == 30) {
            currentSessions.setValue(sessions30Days.getValue());
        } else {
            currentSessions.setValue(sessions7Days.getValue());
        }
    }

    public LiveData<List<StepSession>> getCurrentSessions() {
        return currentSessions;
    }

    public void switchPeriod(int days) {
        _selectedPeriod.setValue(days);
        syncHistory(); // Opcional: Re-sincronizar al cambiar periodo
    }
}
