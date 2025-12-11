package com.example.venclima.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.venclima.databinding.TideForecastBinding;
import com.example.venclima.models.RealTimeTide;
import com.example.venclima.utils.CarouselAdapter;
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

    public TideForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = TideForecastBinding.inflate(inflater, container, false);

        //dopo aver fatto l'inflate posso usare tale variabile per prendermi gli "oggetti" del xml
        LineChart lineChart = binding.LineChart;

        //random data for populate chart  --> fix this to get data from backend
        List<Entry> entries = new ArrayList<Entry>();

        entries.add(new Entry(0, 45));
        entries.add(new Entry(1, 48));
        entries.add(new Entry(2, 52));
        entries.add(new Entry(3, 60));
        entries.add(new Entry(4, 72));   // picco alta marea
        entries.add(new Entry(5, 78));
        entries.add(new Entry(6, 70));
        entries.add(new Entry(7, 62));
        entries.add(new Entry(8, 55));
        entries.add(new Entry(9, 47));
        entries.add(new Entry(10, 40));
        entries.add(new Entry(11, 35));
        entries.add(new Entry(12, 30));  // bassa marea
        entries.add(new Entry(13, 32));
        entries.add(new Entry(14, 38));
        entries.add(new Entry(15, 45));
        entries.add(new Entry(16, 53));
        entries.add(new Entry(17, 61));
        entries.add(new Entry(18, 68));
        entries.add(new Entry(19, 74));
        entries.add(new Entry(20, 80));  // picco serale
        entries.add(new Entry(21, 76));
        entries.add(new Entry(22, 65));
        entries.add(new Entry(23, 55));

        LineDataSet lineDataSet = new LineDataSet(entries, "Livello marea");
        LineData lineData = new LineData(lineDataSet);

        lineChart.setData(lineData);
        lineChart.getDescription().setText("Grafico per livello delle maree");
        lineChart.invalidate();

        ViewPager2 viewPager = binding.viewPager;
        WormDotsIndicator dotsIndicator = binding.dotsIndicator;

        List<RealTimeTide> pages = Arrays.asList(
                new RealTimeTide("S. Geremia", 12),
                new RealTimeTide("Piattaforma Acqua Alta Siap", 14),
                new RealTimeTide("Diga nord Malamocco", 16),
                new RealTimeTide("Diga sud Chioggia", 45),
                new RealTimeTide("Diga sud Lido", 78),
                new RealTimeTide("Fusina", 16)
        );

        CarouselAdapter adapter = new CarouselAdapter(pages);
        viewPager.setAdapter(adapter);

        dotsIndicator.attachTo(viewPager);


        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
