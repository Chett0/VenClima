package com.example.venclima.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.venclima.models.Tide;
import com.example.venclima.viewModels.TideForecastViewModel;

import java.util.List;
import java.util.Map;

import com.example.venclima.databinding.FragmentMoseBinding;

public class MoseFragment extends Fragment {
    private FragmentMoseBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoseBinding.inflate(inflater, container, false);

        binding.headerHow.setOnClickListener(v -> toggleSection(binding.contentHow, binding.chevHow));
        binding.headerWhen.setOnClickListener(v -> toggleSection(binding.contentWhen, binding.chevWhen));
        binding.headerData.setOnClickListener(v -> toggleSection(binding.contentData, binding.chevData));

        TideForecastViewModel viewModel = new ViewModelProvider(requireActivity()).get(TideForecastViewModel.class);
        viewModel.getStationTides().observe(getViewLifecycleOwner(), map -> {
            String avgText = "N/D";
            if (map == null || map.isEmpty()) {
                binding.statusActive.setText(getString(com.example.venclima.R.string.mose_status_format, getString(com.example.venclima.R.string.mose_active), avgText));
                binding.statusInactive.setText(getString(com.example.venclima.R.string.mose_status_format, getString(com.example.venclima.R.string.mose_non_active), avgText));
                binding.cardMoseActive.setVisibility(View.GONE);
                binding.cardMoseInactive.setVisibility(View.VISIBLE);
                return;
            }

            double sum = 0d;
            int count = 0;
            for (Map.Entry<Integer, List<Tide>> e : map.entrySet()) {
                List<Tide> list = e.getValue();
                if (list == null || list.isEmpty()) continue;
                Tide last = list.get(list.size() - 1);
                if (last != null) {
                    double lvl = last.getLevel();
                    sum += lvl;
                    count++;
                }
            }

            if (count == 0) {
                binding.statusActive.setText(getString(com.example.venclima.R.string.mose_status_format, getString(com.example.venclima.R.string.mose_active), avgText));
                binding.statusInactive.setText(getString(com.example.venclima.R.string.mose_status_format, getString(com.example.venclima.R.string.mose_non_active), avgText));
                binding.cardMoseActive.setVisibility(View.GONE);
                binding.cardMoseInactive.setVisibility(View.VISIBLE);
            } else {
                double avg = sum / (double) count;
                long avgRounded = Math.round(avg);
                avgText = avgRounded + " cm";
                binding.statusActive.setText(getString(com.example.venclima.R.string.mose_status_format, getString(com.example.venclima.R.string.mose_active), avgText));
                binding.statusInactive.setText(getString(com.example.venclima.R.string.mose_status_format, getString(com.example.venclima.R.string.mose_non_active), avgText));
                if (avg >= 110d) {
                    binding.cardMoseActive.setVisibility(View.VISIBLE);
                    binding.cardMoseInactive.setVisibility(View.GONE);
                } else {
                    binding.cardMoseActive.setVisibility(View.GONE);
                    binding.cardMoseInactive.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }

    private void toggleSection(View content, View chevron) {
        if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            chevron.setRotation(180f);
        } else {
            content.setVisibility(View.GONE);
            chevron.setRotation(0f);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
