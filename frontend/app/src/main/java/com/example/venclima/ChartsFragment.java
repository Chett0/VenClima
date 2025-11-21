
package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * DEPRECATED: This fragment was superseded by `HistoricalTideForecast` and
 * its content was moved. The original implementation is preserved below
 * as a commented reference. You can safely delete this file when you are
 * sure the team no longer needs it.
 */
@Deprecated
public class ChartsFragment extends Fragment {

    // --- Original implementation (commented for reference) ---
    /*
    private FragmentChartsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChartsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    */
    // ---------------------------------------------------------

    // Minimal stub keeps code compiling while signalling deprecation.
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FrameLayout placeholder = new FrameLayout(inflater.getContext());
        return placeholder;
    }

}
