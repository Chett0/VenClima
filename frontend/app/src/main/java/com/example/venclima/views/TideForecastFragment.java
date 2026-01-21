package com.example.venclima.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.venclima.databinding.TideForecastBinding;
import com.example.venclima.models.RealTimeTide;
import com.example.venclima.adapters.TidesAdapter;
import com.example.venclima.models.Tide;
import com.example.venclima.viewModels.TideForecastViewModel;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TideForecastFragment extends Fragment {

    private TideForecastBinding binding;

    private TideForecastViewModel viewModel;

    public TideForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = TideForecastBinding.inflate(inflater, container, false);

        ViewPager2 viewPager = binding.viewPager;
        WormDotsIndicator dotsIndicator = binding.dotsIndicator;
        TidesAdapter adapter = new TidesAdapter();

        viewPager.setAdapter(adapter);
        dotsIndicator.attachTo(viewPager);

        viewModel = new ViewModelProvider(this).get(TideForecastViewModel.class);
        binding.setTideForecastViewModel(viewModel);
        binding.executePendingBindings();

        viewModel.getStations().observe(getViewLifecycleOwner(), adapter::setStations);
        viewModel.getStationTides().observe(getViewLifecycleOwner(), map -> adapter.setStationTides(map));

        viewModel.getIsError().observe(getViewLifecycleOwner(), isError -> {
            updateVisibility(isError, viewModel.getIsLoading().getValue());
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            updateVisibility(viewModel.getIsError().getValue(), isLoading);
        });

        return binding.getRoot();
    }


    private void updateVisibility(boolean isError, boolean isLoading){
        boolean isTidesVisible = !isError && !isLoading;
        boolean isErrorVisible = isError && !isLoading;

        binding.viewPager.setVisibility(isTidesVisible ? View.VISIBLE : View.GONE);
        binding.dotsIndicator.setVisibility(isTidesVisible ? View.VISIBLE : View.GONE);
        binding.errorLayout.setVisibility(isErrorVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
