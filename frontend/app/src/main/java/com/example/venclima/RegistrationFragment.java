package com.example.venclima;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.venclima.databinding.RegistrationBinding;


public class RegistrationFragment extends Fragment {

    private RegistrationBinding binding;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = RegistrationBinding.inflate(inflater,container,false);

        Button regButton = binding.registrationBtn;
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //una volta premuto il bottono registratio, si ottiene ci che c'è scritto sul form con le seguenti variabili
                //binding.emailInput.getText()
                //binding.passwordInput.getText()
                //aggiungere qui le api(?)
            }
        });

        return binding.getRoot();
    }
}