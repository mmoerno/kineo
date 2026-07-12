package com.dam.kineo.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.dam.kineo.R;
import com.dam.kineo.data.local.prefs.AuthPreferences;
import com.dam.kineo.databinding.ActivityMainBinding;
import com.dam.kineo.service.StepCounterService;
import com.dam.kineo.ui.auth.LoginActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Inject
    AuthPreferences authPreferences;

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                    result -> startStepServiceIfPossible());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        requestPermissionLauncher.launch(requiredPermissions());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment, R.id.historyFragment, R.id.mapaFragment,
                    R.id.socialFragment, R.id.badgesFragment)
                    .build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.bottomNav, navController);
        }

        startStepServiceIfPossible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startStepServiceIfPossible();
    }

    private String[] requiredPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{
                    Manifest.permission.ACTIVITY_RECOGNITION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
            };
        }
        return new String[]{
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
    }

    private void startStepServiceIfPossible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Intent serviceIntent = new Intent(this, StepCounterService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        stopService(new Intent(this, StepCounterService.class));

        authPreferences.clearAuthData();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        finish();
    }
}
