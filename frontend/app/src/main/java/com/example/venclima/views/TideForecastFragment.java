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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
        viewModel.getDailyTides().observe(getViewLifecycleOwner(), adapter::setTides);
        viewModel.getRealTimeTides().observe(getViewLifecycleOwner(), adapter::setRealTimeTides);

        //dopo aver fatto l'inflate posso usare tale variabile per prendermi gli "oggetti" del xml
        LineChart lineChart = binding.LineChart;

        //random data for populate chart  --> fix this to get data from backend
        List<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 45));

        LineDataSet lineDataSet = new LineDataSet(entries, "Livello marea");
        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getDescription().setText("Grafico per livello delle maree");
        lineChart.invalidate();

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
