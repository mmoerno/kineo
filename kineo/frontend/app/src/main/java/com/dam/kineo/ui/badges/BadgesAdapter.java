package com.dam.kineo.ui.badges;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.kineo.databinding.ItemBadgeBinding;
import com.dam.kineo.domain.model.Badge;

import java.util.ArrayList;
import java.util.List;

public class BadgesAdapter extends RecyclerView.Adapter<BadgesAdapter.ViewHolder> {

    public interface OnBadgeClickListener {
        void onBadgeClick(Badge badge);
    }

    private final OnBadgeClickListener clickListener;
    private final List<Badge> items = new ArrayList<>();

    public BadgesAdapter(OnBadgeClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void submitList(List<Badge> badges) {
        items.clear();
        if (badges != null) {
            items.addAll(badges);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBadgeBinding binding = ItemBadgeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, clickListener);
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
        private final ItemBadgeBinding binding;
        private final OnBadgeClickListener clickListener;

        ViewHolder(ItemBadgeBinding binding, OnBadgeClickListener clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.clickListener = clickListener;
        }

        void bind(Badge badge) {
            binding.tvBadgeName.setText(badge.getName());
            binding.tvBadgeDesc.setText(badge.getDescription());

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
                binding.lockedOverlay.setVisibility(View.VISIBLE);
                binding.ivLock.setVisibility(View.VISIBLE);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0f);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                binding.ivBadgeIcon.setColorFilter(filter);
                binding.getRoot().setOnClickListener(null);
            } else {
                binding.lockedOverlay.setVisibility(View.GONE);
                binding.ivLock.setVisibility(View.GONE);
                binding.ivBadgeIcon.clearColorFilter();
                binding.getRoot().setOnClickListener(v -> clickListener.onBadgeClick(badge));
            }
        }
    }
}
