package com.dam.kineo.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.dam.kineo.databinding.ActivityRegisterBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        authViewModel.getAuthSuccess().observe(this, response -> {
            if (response != null) {
                authViewModel.logout();
                startActivity(new Intent(this, LoginActivity.class));
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
                binding.btnRegister.setEnabled(!loading);
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            binding.tvError.setVisibility(View.GONE);
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String displayName = binding.etDisplayName.getText() != null ? binding.etDisplayName.getText().toString().trim() : "";
            String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString() : "";
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(displayName)
                    || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                binding.tvError.setText("Completa todos los campos");
                binding.tvError.setVisibility(View.VISIBLE);
                return;
            }
            authViewModel.register(username, email, password, displayName);
        });

        binding.tvGoLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
