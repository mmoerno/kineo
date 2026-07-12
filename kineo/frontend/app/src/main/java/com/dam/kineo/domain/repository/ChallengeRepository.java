package com.dam.kineo.domain.repository;

import androidx.lifecycle.LiveData;
import com.dam.kineo.data.remote.dto.ChallengeDto;
import com.dam.kineo.domain.model.Challenge;
import java.util.List;

public interface ChallengeRepository {
    LiveData<List<Challenge>> getActiveChallenges();
    LiveData<List<Challenge>> getFinishedChallenges();
    void syncChallenges(String token);
    void createChallenge(String token, ChallengeDto dto);
    void acceptChallenge(String token, String challengeId);
}
