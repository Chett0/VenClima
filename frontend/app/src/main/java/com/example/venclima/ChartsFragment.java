package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.databinding.FragmentChartsBinding;

import java.util.Arrays;
import java.util.List;

public class ChartsFragment extends Fragment {
    private FragmentChartsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChartsBinding.inflate(inflater, container, false);
       //una colonna
        RecyclerView recycler = binding.getRoot().findViewById(R.id.recycler_months);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        List<String> months = Arrays.asList(
        "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
        "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"
    );
    MonthAdapter adapter = new MonthAdapter(getContext(), months);
    recycler.setAdapter(adapter);
    return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
