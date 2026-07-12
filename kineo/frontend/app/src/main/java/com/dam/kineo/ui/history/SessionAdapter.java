package com.dam.kineo.ui.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.kineo.databinding.ItemSessionBinding;
import com.dam.kineo.domain.model.StepSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.ViewHolder> {

    private final List<StepSession> items = new ArrayList<>();
    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public void submitList(List<StepSession> sessions) {
        items.clear();
        if (sessions != null) {
            items.addAll(sessions);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSessionBinding binding = ItemSessionBinding.inflate(
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
        private final ItemSessionBinding binding;

        ViewHolder(ItemSessionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(StepSession session) {
            binding.tvSessionDate.setText(formatDate(session.getDate()));
            binding.tvSessionSteps.setText(session.getSteps() + " pasos");
            binding.tvSessionCalories.setText(String.format(Locale.getDefault(), "%.0f kcal", session.getCalories()));
            double km = session.getDistanceMeters() / 1000d;
            binding.tvSessionDistance.setText(String.format(Locale.getDefault(), "%.1f km", km));
        }

        private String formatDate(String raw) {
            if (raw == null) {
                return "";
            }
            try {
                Date d = apiFormat.parse(raw);
                if (d != null) {
                    return displayFormat.format(d);
                }
            } catch (ParseException ignored) {
            }
            return raw;
        }
    }
}
