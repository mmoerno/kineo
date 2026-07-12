package com.dam.kineo.ui.social;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.remote.dto.ChallengeDto;
import com.dam.kineo.domain.model.Challenge;
import com.dam.kineo.domain.model.Friend;
import com.dam.kineo.domain.repository.ChallengeRepository;
import com.dam.kineo.domain.repository.FriendRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SocialViewModel extends ViewModel {

    public final LiveData<List<Friend>> acceptedFriends;
    public final LiveData<List<Friend>> pendingRequests;
    public final LiveData<List<Challenge>> activeChallenges;
    public final LiveData<List<Challenge>> finishedChallenges;

    private final FriendRepository friendRepository;
    private final ChallengeRepository challengeRepository;
    private final AuthPreferences authPreferences;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>();

    @Inject
    public SocialViewModel(
            FriendRepository friendRepository,
            ChallengeRepository challengeRepository,
            AuthPreferences authPreferences
    ) {
        this.friendRepository = friendRepository;
        this.challengeRepository = challengeRepository;
        this.authPreferences = authPreferences;

        String myUserId = authPreferences.getUserId() != null ? authPreferences.getUserId() : "";
        acceptedFriends = friendRepository.getAcceptedFriends();
        pendingRequests = friendRepository.getPendingRequests(myUserId);
        activeChallenges = challengeRepository.getActiveChallenges();
        finishedChallenges = challengeRepository.getFinishedChallenges();
    }

    public LiveData<Boolean> getIsLoading() {
        return _isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return _errorMessage;
    }

    public void syncAll() {
        String token = authPreferences.getToken();
        if (token == null) {
            _errorMessage.setValue("No hay sesión iniciada");
            return;
        }
        friendRepository.syncFriends(token);
        challengeRepository.syncChallenges(token);
    }

    public void sendFriendRequest(String username) {
        String token = requireToken();
        if (token == null) {
            return;
        }
        friendRepository.sendFriendRequest(token, username);
    }

    public void acceptFriendRequest(String userId) {
        String token = requireToken();
        if (token == null) {
            return;
        }
        friendRepository.acceptFriendRequest(token, userId);
    }

    public void createChallenge(
            String challengerId,
            String title,
            String description,
            int targetSteps,
            String metric,
            String period,
            long startsAt,
            long endsAt
    ) {
        String token = requireToken();
        if (token == null) {
            return;
        }
        String creatorId = authPreferences.getUserId() != null ? authPreferences.getUserId() : "";
        ChallengeDto dto = new ChallengeDto();
        dto.setCreatorId(creatorId);
        dto.setChallengerId(challengerId);
        dto.setTitle(title);
        dto.setDescription(description);
        dto.setTargetSteps(targetSteps);
        dto.setMetric(metric);
        dto.setPeriod(period);
        dto.setStartsAt(startsAt);
        dto.setEndsAt(endsAt);
        dto.setStatus("pending");
        challengeRepository.createChallenge(token, dto);
    }

    public void createChallenge(
            String challengerId,
            String metric,
            String period,
            long startsAt,
            long endsAt
    ) {
        String title = "Reto de " + metric;
        String description = "Compite con tu amigo en " + period + ".";
        int targetSteps = "distance".equals(metric) ? 10000 : 10000;
        createChallenge(challengerId, title, description, targetSteps, metric, period, startsAt, endsAt);
    }

    public void acceptChallenge(String challengeId) {
        String token = requireToken();
        if (token == null) {
            return;
        }
        challengeRepository.acceptChallenge(token, challengeId);
    }

    private String requireToken() {
        String token = authPreferences.getToken();
        if (token == null) {
            _errorMessage.setValue("No hay sesión iniciada");
        }
        return token;
    }
}
