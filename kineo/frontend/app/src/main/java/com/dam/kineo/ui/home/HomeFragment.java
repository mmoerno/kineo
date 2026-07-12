package com.dam.kineo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dam.kineo.R;
import com.dam.kineo.databinding.FragmentHomeBinding;
import com.dam.kineo.domain.model.StepSession;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.rvBadgesPreview.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        homeViewModel.getTodaySession().observe(getViewLifecycleOwner(), this::bindTodaySession);
        homeViewModel.getBadges().observe(getViewLifecycleOwner(), badges -> {
            if (badges == null) {
                return;
            }
            BadgePreviewAdapter adapter = new BadgePreviewAdapter(badges);
            binding.rvBadgesPreview.setAdapter(adapter);
        });
    }

    private void bindTodaySession(StepSession session) {
        int steps = session != null ? session.getSteps() : 0;
        binding.tvStepsCount.setText(String.valueOf(steps));
        binding.tvStepsGoal.setText(getString(R.string.label_daily_goal) + " (" + homeViewModel.dailyGoal + ")");
        binding.progressSteps.setMax(homeViewModel.dailyGoal);
        binding.progressSteps.setProgress(Math.min(steps, homeViewModel.dailyGoal));

        if (session != null) {
            binding.tvCalories.setText(String.format(
                    java.util.Locale.getDefault(), "%.0f kcal", session.getCalories()));
            double km = session.getDistanceMeters() / 1000d;
            binding.tvDistance.setText(String.format(java.util.Locale.getDefault(), "%.1f km", km));
            binding.tvActiveMinutes.setText(getString(R.string.label_active_minutes) + ": "
                    + session.getActiveMinutes() + " min");
        } else {
            binding.tvCalories.setText("0 kcal");
            binding.tvDistance.setText("0.0 km");
            binding.tvActiveMinutes.setText("0 min");
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
