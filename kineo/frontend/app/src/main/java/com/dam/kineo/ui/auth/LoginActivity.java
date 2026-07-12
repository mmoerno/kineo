package com.dam.kineo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.databinding.ActivityLoginBinding;
import com.dam.kineo.ui.MainActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthPreferences authPreferences;

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (authPreferences.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getAuthSuccess().observe(this, response -> {
            if (response != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });

        authViewModel.getAuthError().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                binding.tvError.setText(message);
                binding.tvError.setVisibility(View.VISIBLE);
            } else {
                binding.tvError.setVisibility(View.GONE);
            }
        });

        authViewModel.getIsLoading().observe(this, loading -> {
            if (loading != null) {
                binding.btnLogin.setEnabled(!loading);
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            binding.tvError.setVisibility(View.GONE);
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                binding.tvError.setText("Completa correo y contraseña");
                binding.tvError.setVisibility(View.VISIBLE);
                return;
            }
            authViewModel.login(email, password);
        });

        binding.tvGoRegister.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));

        // SOLO PARA DESARROLLO — quitar antes de entregar
//        binding.tvGoRegister.setOnLongClickListener(v -> {
//            authPreferences.saveAuthData("mock_token_dev", "user_mock_123");
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//            return true;
//        });
    }
}
