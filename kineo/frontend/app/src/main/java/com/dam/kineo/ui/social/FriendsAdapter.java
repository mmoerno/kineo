package com.dam.kineo.ui.social;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.kineo.databinding.ItemFriendBinding;
import com.dam.kineo.domain.model.Friend;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private final SocialViewModel socialViewModel;
    private final List<Friend> items = new ArrayList<>();

    public FriendsAdapter(SocialViewModel socialViewModel) {
        this.socialViewModel = socialViewModel;
    }

    public void submitList(List<Friend> friends) {
        items.clear();
        if (friends != null) {
            items.addAll(friends);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFriendBinding binding = ItemFriendBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemFriendBinding binding;

        ViewHolder(ItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Friend friend) {
            binding.tvFriendName.setText(friend.getName());
            binding.tvFriendSteps.setText(friend.getTodaySteps() + " pasos hoy");
            binding.btnChallenge.setOnClickListener(v -> showCreateChallengeDialog(
                    binding.getRoot().getContext(), friend));
        }

        private void showCreateChallengeDialog(Context context, Friend friend) {
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            int pad = (int) (16 * context.getResources().getDisplayMetrics().density);
            layout.setPadding(pad, pad, pad, pad);

            Spinner spMetric = new Spinner(context);
            String[] metrics = new String[]{"steps", "distance"};
            spMetric.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, metrics));

            Spinner spPeriod = new Spinner(context);
            String[] periods = new String[]{"daily", "weekly"};
            spPeriod.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, periods));

            layout.addView(spMetric);
            layout.addView(spPeriod);

            new MaterialAlertDialogBuilder(context)
                    .setTitle("Crear reto")
                    .setView(layout)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, (d, w) -> {
                        String metric = (String) spMetric.getSelectedItem();
                        String period = (String) spPeriod.getSelectedItem();
                        long now = System.currentTimeMillis();
                        long week = 7L * 24L * 60L * 60L * 1000L;
                        socialViewModel.createChallenge(friend.getId(), metric, period, now, now + week);
                    })
                    .show();
        }
    }
}
