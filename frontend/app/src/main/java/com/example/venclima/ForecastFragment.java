package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * DEPRECATED/LEGACY: The original `ForecastFragment` implementation is
 * preserved below inside a block comment for team reference. Replace or
 * remove this file once the team confirms it's no longer needed.
 */
public class ForecastFragment extends Fragment {

    /*
    private FragmentForecastBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    */

    // Minimal placeholder keeps compilation intact while marking file as removable.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new FrameLayout(inflater.getContext());
    }

}
