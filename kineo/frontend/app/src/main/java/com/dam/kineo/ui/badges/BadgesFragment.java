package com.dam.kineo.ui.badges;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dam.kineo.databinding.FragmentBadgesBinding;
import com.dam.kineo.domain.model.Badge;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.DateFormat;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BadgesFragment extends Fragment {

    private FragmentBadgesBinding binding;
    private BadgesViewModel badgesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBadgesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        badgesViewModel = new ViewModelProvider(this).get(BadgesViewModel.class);

        binding.rvBadges.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        BadgesAdapter adapter = new BadgesAdapter(badge -> {
            if (!badge.isUnlocked()) {
                return;
            }
            Long at = badge.getUnlockedAt();
            String dateStr = at != null
                    ? DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                    .format(new Date(at))
                    : "—";
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(badge.getName())
                    .setMessage(badge.getDescription() + "\n\nDesbloqueado: " + dateStr)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        });

        binding.rvBadges.setAdapter(adapter);

        badgesViewModel.allBadges.observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
