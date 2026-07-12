package com.dam.kineo.ui.auth;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.data.remote.KineoApiService;
import com.dam.kineo.data.remote.dto.AuthResponse;
import com.dam.kineo.data.remote.dto.LoginRequest;
import com.dam.kineo.data.remote.dto.RegisterRequest;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AuthViewModel extends ViewModel {

    private final KineoApiService api;
    private final AuthPreferences authPreferences;

    private final MutableLiveData<AuthResponse> _authSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> _authError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);

    @Inject
    public AuthViewModel(KineoApiService api, AuthPreferences authPreferences) {
        this.api = api;
        this.authPreferences = authPreferences;
    }

    public LiveData<AuthResponse> getAuthSuccess() {
        return _authSuccess;
    }

    public LiveData<String> getAuthError() {
        return _authError;
    }

    public LiveData<Boolean> getIsLoading() {
        return _isLoading;
    }

    public void login(String email, String password) {
        _isLoading.setValue(true);
        api.login(new LoginRequest(email, password)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                _isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse body = response.body();
                    authPreferences.saveAuthData(body.getAccessToken(), body.getUserId());
                    _authSuccess.postValue(body);
                } else {
                    _authError.postValue(errorMessage(response, "Error al iniciar sesión"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                _isLoading.postValue(false);
                _authError.postValue(failureMessage(t));
            }
        });
    }

    public void register(String username, String email, String password, String displayName) {
        _isLoading.setValue(true);
        api.register(new RegisterRequest(username, email, password, displayName)).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                _isLoading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse body = response.body();
                    authPreferences.saveAuthData(body.getAccessToken(), body.getUserId());
                    _authSuccess.postValue(body);
                } else {
                    _authError.postValue(errorMessage(response, "Error al registrarse"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                _isLoading.postValue(false);
                _authError.postValue(failureMessage(t));
            }
        });
    }

    public void logout() {
        authPreferences.clearAuthData();
    }

    public boolean isLoggedIn() {
        return authPreferences.isLoggedIn();
    }

    private static String errorMessage(Response<?> response, String fallback) {
        if (response.errorBody() != null) {
            try {
                return response.errorBody().string();
            } catch (Exception ignored) {
                return fallback;
            }
        }
        return fallback;
    }

    private static String failureMessage(Throwable t) {
        String msg = t.getMessage();
        return msg != null ? msg : "Fallo de conexión";
    }
}
