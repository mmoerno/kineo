package com.dam.kineo.ui.history;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dam.kineo.R;
import com.dam.kineo.databinding.FragmentHistoryBinding;
import com.dam.kineo.domain.model.StepSession;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private HistoryViewModel historyViewModel;
    private SessionAdapter sessionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        sessionAdapter = new SessionAdapter();
        binding.rvSessions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSessions.setAdapter(sessionAdapter);

        historyViewModel.getCurrentSessions().observe(getViewLifecycleOwner(), sessions -> {
            List<StepSession> list = sessions != null ? new ArrayList<>(sessions) : new ArrayList<>();
            Collections.reverse(list);
            sessionAdapter.submitList(list);
            updateBarChart(list);
        });

        binding.rgPeriod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_7days) {
                historyViewModel.switchPeriod(7);
            } else if (checkedId == R.id.rb_30days) {
                historyViewModel.switchPeriod(30);
            }
        });
    }

    private void updateBarChart(List<StepSession> sessions) {
        if (sessions == null || sessions.isEmpty()) {
            binding.barChart.clear();
            binding.barChart.setNoDataText("Sin datos de actividad aún");
            binding.barChart.invalidate();
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < sessions.size(); i++) {
            entries.add(new BarEntry(i, (float) sessions.get(i).getSteps()));
        }

        boolean isDark = (requireContext().getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES;

        int textColor = isDark ? 0xFFE6F2E8 : 0xFF1A1A1A;
        int gridColor = isDark ? 0xFF2E3A2F : 0xFFE0E0E0;
        int bgColor   = isDark ? 0xFF111411 : 0xFFF4FBF5;

        int primary = ContextCompat.getColor(requireContext(), R.color.colorPrimary);
        BarDataSet dataSet = new BarDataSet(entries, getString(R.string.label_steps));
        dataSet.setColor(primary);
        dataSet.setValueTextColor(textColor);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);

        binding.barChart.setData(barData);
        binding.barChart.setFitBars(true);
        binding.barChart.setBackgroundColor(bgColor);
        binding.barChart.getDescription().setEnabled(false);

        Legend legend = binding.barChart.getLegend();
        legend.setTextColor(textColor);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        final List<StepSession> snap = sessions;
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(textColor);
        xAxis.setGridColor(gridColor);
        xAxis.setAxisLineColor(gridColor);

        // Ajuste dinámico del rango y etiquetas
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(sessions.size() - 0.5f);
        xAxis.setLabelCount(Math.min(sessions.size(), 7));

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i = (int) value;
                if (i < 0 || i >= snap.size()) return "";
                String date = snap.get(i).getDate();
                if (date == null || date.isEmpty()) return "";
                String[] p = date.split("-");
                return p.length == 3 ? p[2] + "/" + p[1] : date;
            }
        });

        YAxis leftAxis = binding.barChart.getAxisLeft();
        leftAxis.setTextColor(textColor);
        leftAxis.setGridColor(gridColor);
        leftAxis.setAxisLineColor(gridColor);
        leftAxis.setAxisMinimum(0f);

        binding.barChart.getAxisRight().setEnabled(false);

        binding.barChart.fitScreen();
        binding.barChart.notifyDataSetChanged();
        binding.barChart.animateY(800);
        binding.barChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
