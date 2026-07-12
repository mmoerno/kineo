package com.dam.kineo.di;

import android.content.Context;

import com.dam.kineo.BuildConfig;
import com.dam.kineo.data.local.KineoDatabase;
import com.dam.kineo.data.local.dao.BadgeDao;
import com.dam.kineo.data.local.dao.ChallengeDao;
import com.dam.kineo.data.local.dao.FriendshipDao;
import com.dam.kineo.data.local.dao.LeaderboardDao;
import com.dam.kineo.data.local.dao.RoutePointDao;
import com.dam.kineo.data.local.dao.StepSessionDao;
import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.remote.KineoApiService;
//import com.dam.kineo.data.remote.MockInterceptor;
import com.dam.kineo.data.repository.BadgeRepositoryImpl;
import com.dam.kineo.data.repository.ChallengeRepositoryImpl;
import com.dam.kineo.data.repository.FriendRepositoryImpl;
import com.dam.kineo.data.repository.LeaderboardRepositoryImpl;
import com.dam.kineo.data.repository.RouteRepositoryImpl;
import com.dam.kineo.data.repository.StepRepositoryImpl;
import com.dam.kineo.domain.repository.BadgeRepository;
import com.dam.kineo.domain.repository.ChallengeRepository;
import com.dam.kineo.domain.repository.FriendRepository;
import com.dam.kineo.domain.repository.LeaderboardRepository;
import com.dam.kineo.domain.repository.RouteRepository;
import com.dam.kineo.domain.repository.StepRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public KineoDatabase provideDatabase(@ApplicationContext Context context) {
        return KineoDatabase.getDatabase(context);
    }

    @Provides
    public StepSessionDao provideStepSessionDao(KineoDatabase db) {
        return db.stepSessionDao();
    }

    @Provides
    public RoutePointDao provideRoutePointDao(KineoDatabase db) {
        return db.routePointDao();
    }

    @Provides
    public BadgeDao provideBadgeDao(KineoDatabase db) {
        return db.badgeDao();
    }

    @Provides
    public FriendshipDao provideFriendshipDao(KineoDatabase db) {
        return db.friendshipDao();
    }

    @Provides
    public ChallengeDao provideChallengeDao(KineoDatabase db) {
        return db.challengeDao();
    }

    @Provides
    public LeaderboardDao provideLeaderboardDao(KineoDatabase db) {
        return db.leaderboardDao();
    }

    @Provides
    @Singleton
    public AuthPreferences provideAuthPreferences(@ApplicationContext Context context) {
        return new AuthPreferences(context);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                //.addInterceptor(new MockInterceptor()) // ← añade esta línea
                .addInterceptor(logging)
                .build();
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public KineoApiService provideKineoApiService(Retrofit retrofit) {
        return retrofit.create(KineoApiService.class);
    }

    @Provides
    @Singleton
    public StepRepository provideStepRepository(StepRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public BadgeRepository provideBadgeRepository(BadgeRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public FriendRepository provideFriendRepository(FriendRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public ChallengeRepository provideChallengeRepository(ChallengeRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public LeaderboardRepository provideLeaderboardRepository(LeaderboardRepositoryImpl impl) {
        return impl;
    }

    @Provides
    @Singleton
    public RouteRepository provideRouteRepository(RouteRepositoryImpl impl) {
        return impl;
    }
}
