package com.dam.kineo.data.local.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class AuthPreferences {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences prefs;

    @Inject
    public AuthPreferences(@ApplicationContext Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getUserId() {
        return prefs.getString(KEY_USER_ID, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void saveAuthData(String token, String userId) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    public void clearAuthData() {
        prefs.edit().clear().apply();
    }
}
