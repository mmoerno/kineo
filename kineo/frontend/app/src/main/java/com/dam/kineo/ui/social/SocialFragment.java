package com.dam.kineo.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dam.kineo.databinding.FragmentSocialBinding;
import com.google.android.material.tabs.TabLayout;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SocialFragment extends Fragment {

    private FragmentSocialBinding binding;
    private SocialViewModel vm;
    private FriendsAdapter friendsAdapter;
    private FriendRequestsAdapter requestsAdapter;
    private ChallengesAdapter activeChallengesAdapter;
    private ChallengesAdapter finishedChallengesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSocialBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(this).get(SocialViewModel.class);

        setupRecyclerViews();
        setupTabs();
        observeViewModel();

        binding.fabAddFriend.setOnClickListener(v -> showAddFriendDialog());
        
        // Forzar sincronización al entrar
        vm.syncAll();
    }

    private void setupRecyclerViews() {
        friendsAdapter = new FriendsAdapter(vm);
        binding.rvFriends.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFriends.setAdapter(friendsAdapter);

        requestsAdapter = new FriendRequestsAdapter(vm);
        binding.rvRequests.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvRequests.setAdapter(requestsAdapter);

        activeChallengesAdapter = new ChallengesAdapter();
        binding.rvActiveChallenges.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvActiveChallenges.setAdapter(activeChallengesAdapter);

        finishedChallengesAdapter = new ChallengesAdapter();
        binding.rvFinishedChallenges.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvFinishedChallenges.setAdapter(finishedChallengesAdapter);
    }

    private void setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabVisibility(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        updateTabVisibility(0);
    }

    private void updateTabVisibility(int position) {
        binding.layoutFriends.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        binding.layoutChallenges.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        binding.fabAddFriend.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }

    private void observeViewModel() {
        vm.acceptedFriends.observe(getViewLifecycleOwner(), friendsAdapter::submitList);
        vm.pendingRequests.observe(getViewLifecycleOwner(), requestsAdapter::submitList);
        vm.activeChallenges.observe(getViewLifecycleOwner(), activeChallengesAdapter::submitList);
        vm.finishedChallenges.observe(getViewLifecycleOwner(), finishedChallengesAdapter::submitList);

        vm.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void showAddFriendDialog() {
        EditText editText = new EditText(requireContext());
        editText.setHint("Username del usuario");
        new AlertDialog.Builder(requireContext())
                .setTitle("Añadir amigo")
                .setMessage("Introduce el username del usuario que quieres añadir")
                .setView(editText)
                .setPositiveButton("Enviar solicitud", (d, w) -> {
                    String username = editText.getText().toString().trim();
                    if (!username.isEmpty()) {
                        vm.sendFriendRequest(username);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
