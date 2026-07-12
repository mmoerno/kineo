package com.dam.kineo.ui.social;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.kineo.databinding.ItemChallengeBinding;
import com.dam.kineo.domain.model.Challenge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChallengesAdapter extends RecyclerView.Adapter<ChallengesAdapter.ViewHolder> {

    private final List<Challenge> items = new ArrayList<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public void submitList(List<Challenge> challenges) {
        items.clear();
        if (challenges != null) {
            items.addAll(challenges);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChallengeBinding binding = ItemChallengeBinding.inflate(
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemChallengeBinding binding;

        ViewHolder(ItemChallengeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Challenge challenge) {
            binding.tvChallengeMetric.setText(challenge.getMetric());
            
            String creator = challenge.getCreatorUsername() != null ? challenge.getCreatorUsername() : challenge.getCreatorId();
            binding.tvCreator.setText("Creador: " + (creator != null ? creator : "—"));
            
            String challenger = challenge.getChallengerUsername() != null ? challenge.getChallengerUsername() : challenge.getChallengerId();
            binding.tvChallenger.setText("Retador: " + (challenger != null ? challenger : "—"));

            binding.tvChallengePeriod.setText(challenge.getPeriod());
            String status = challenge.getStatus() != null ? challenge.getStatus().toLowerCase(Locale.ROOT) : "";
            binding.tvChallengeStatus.setText(status.isEmpty() ? "—" : status);

            int bg;
            if ("active".equals(status)) {
                bg = Color.parseColor("#2E7D32");
            } else if ("pending_acceptance".equals(status) || status.contains("pending")) {
                bg = Color.parseColor("#FF9800");
            } else if ("finished".equals(status) || "completed".equals(status)) {
                bg = Color.parseColor("#9E9E9E");
            } else {
                bg = Color.parseColor("#757575");
            }
            GradientDrawable chip = new GradientDrawable();
            chip.setShape(GradientDrawable.RECTANGLE);
            chip.setCornerRadius(24f);
            chip.setColor(bg);
            binding.tvChallengeStatus.setBackground(chip);
            binding.tvChallengeStatus.setTextColor(Color.WHITE);

            binding.tvChallengeEnds.setText("Termina: " + dateFormat.format(new Date(challenge.getEndsAt())));
        }
    }
}
