package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * DEPRECATED: Legacy Emergency fragment. The original implementation is
 * commented below for reference. Remove this file once the team agrees
 * it is no longer needed.
 */
public class EmergencyFragment extends Fragment {

    /*
    private FragmentEmergencyBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEmergencyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    */

    // Minimal placeholder to keep references valid during transition.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new FrameLayout(inflater.getContext());
    }

}
