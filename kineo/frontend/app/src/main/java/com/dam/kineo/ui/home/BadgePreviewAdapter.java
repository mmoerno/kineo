package com.dam.kineo.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.kineo.databinding.ItemBadgePreviewBinding;
import com.dam.kineo.domain.model.Badge;

import java.util.ArrayList;
import java.util.List;

public class BadgePreviewAdapter extends RecyclerView.Adapter<BadgePreviewAdapter.ViewHolder> {

    private final List<Badge> badges;

    public BadgePreviewAdapter(List<Badge> badges) {
        this.badges = badges != null ? new ArrayList<>(badges) : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBadgePreviewBinding binding = ItemBadgePreviewBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(badges.get(position));
    }

    @Override
    public int getItemCount() {
        return badges.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBadgePreviewBinding binding;

        ViewHolder(ItemBadgePreviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Badge badge) {
            binding.tvBadgeName.setText(badge.getName());
            String iconName = badge.getIconRes();
            int resId = 0;
            if (iconName != null && !iconName.isEmpty()) {
                resId = binding.getRoot().getResources().getIdentifier(
                        iconName, "drawable", binding.getRoot().getContext().getPackageName());
                if (resId == 0) {
                    resId = binding.getRoot().getResources().getIdentifier(
                            iconName, "mipmap", binding.getRoot().getContext().getPackageName());
                }
            }
            if (resId != 0) {
                binding.ivBadgeIcon.setImageResource(resId);
            } else {
                binding.ivBadgeIcon.setImageResource(android.R.drawable.btn_star_big_on);
            }

            if (!badge.isUnlocked()) {
                binding.overlayLocked.setVisibility(View.VISIBLE);
            } else {
                binding.overlayLocked.setVisibility(View.GONE);
            }
        }
    }
}
