package com.example.venclima.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.venclima.databinding.RegistrationBinding;
import com.example.venclima.viewModels.RegistrationViewModel;


public class RegistrationFragment extends Fragment {

    private RegistrationBinding binding;
    private RegistrationViewModel viewModel;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = RegistrationBinding.inflate(inflater,container,false);
        viewModel = new RegistrationViewModel();
        binding.setRegistrationViewModel(viewModel);
        binding.executePendingBindings();

        return binding.getRoot();
    }
}