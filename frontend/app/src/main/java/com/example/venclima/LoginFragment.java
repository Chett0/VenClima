package com.example.venclima;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.venclima.databinding.LoginBinding;


public class LoginFragment extends Fragment {

    private LoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //QUI CONTROLLARE CHE L'UTENTE NON SIA GIA' LOGGATO (?)
        //o forse fare in modo che non si possa accedere a questa pagina se si è loggati
        //in controllo da qualche parte va fatto
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = LoginBinding.inflate(inflater,container,false);

        Button logButton = binding.loginBtn;
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //una volta premuto il bottono login, si ottiene ci che c'è scritto sul form con le seguenti variabili
                //binding.emailInput.getText()
                //binding.passwordInput.getText()
                //aggiungere qui le api(?)
            }
        });



        return binding.getRoot();
    }
}