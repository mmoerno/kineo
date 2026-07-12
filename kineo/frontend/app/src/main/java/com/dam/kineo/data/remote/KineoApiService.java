package com.dam.kineo.data.remote;

import com.dam.kineo.data.remote.dto.AuthResponse;
import com.dam.kineo.data.remote.dto.BadgeDto;
import com.dam.kineo.data.remote.dto.ChallengeDto;
import com.dam.kineo.data.remote.dto.FriendDto;
import com.dam.kineo.data.remote.dto.FriendRequestDto;
import com.dam.kineo.data.remote.dto.LeaderboardEntryDto;
import com.dam.kineo.data.remote.dto.LoginRequest;
import com.dam.kineo.data.remote.dto.RegisterRequest;
import com.dam.kineo.data.remote.dto.RouteDto;
import com.dam.kineo.data.remote.dto.PointDto;
import com.dam.kineo.data.remote.dto.StepCountsDto;
import com.dam.kineo.data.remote.dto.StepSessionDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface KineoApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @GET("steps/today")
    Call<StepSessionDto> getTodaySteps(@Header("Authorization") String token);

    @GET("steps/history")
    Call<List<StepSessionDto>> getStepsHistory(@Header("Authorization") String token, @Query("days") int days);

    @POST("steps/sync")
    Call<Void> syncSteps(@Header("Authorization") String token, @Body List<StepSessionDto> sessions);

    @GET("friends")
    Call<List<FriendDto>> getFriends(@Header("Authorization") String token);

    @GET("friends/requests")
    Call<List<FriendDto>> getFriendRequests(@Header("Authorization") String token);

    @POST("friends/request")
    Call<FriendDto> sendFriendRequest(@Header("Authorization") String token, @Body FriendRequestDto friend);

    @PUT("friends/{userId}/accept")
    Call<FriendDto> acceptFriendRequest(@Header("Authorization") String token, @Path("userId") String userId);

    @DELETE("friends/{userId}")
    Call<Void> removeFriend(@Header("Authorization") String token, @Path("userId") String userId);

    @GET("challenges")
    Call<List<ChallengeDto>> getChallenges(@Header("Authorization") String token);

    @POST("challenges")
    Call<ChallengeDto> createChallenge(@Header("Authorization") String token, @Body ChallengeDto challenge);

    @PUT("challenges/{id}/accept")
    Call<ChallengeDto> acceptChallenge(@Header("Authorization") String token, @Path("id") String challengeId);

    @GET("badges")
    Call<List<BadgeDto>> getBadges(@Header("Authorization") String token);

    @GET("leaderboard")
    Call<List<LeaderboardEntryDto>> getLeaderboard(@Header("Authorization") String token, @Query("period") String period);

    @GET("steps/counts")
    Call<StepCountsDto> getStepCounts(@Header("Authorization") String token);

    @POST("routes/sync")
    Call<Void> syncRoute(@Header("Authorization") String token, @Body RouteDto route);

    @GET("routes/{sessionId}")
    Call<List<PointDto>> getRoute(@Header("Authorization") String token, @Path("sessionId") String sessionId);
}
