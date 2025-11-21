package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.venclima.databinding.FragmentMoseBinding;

public class MoseFragment extends Fragment {
    private FragmentMoseBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMoseBinding.inflate(inflater, container, false);

        binding.headerHow.setOnClickListener(v -> toggleSection(binding.contentHow, binding.chevHow));
        binding.headerWhen.setOnClickListener(v -> toggleSection(binding.contentWhen, binding.chevWhen));
        binding.headerData.setOnClickListener(v -> toggleSection(binding.contentData, binding.chevData));

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
