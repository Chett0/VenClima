package com.example.venclima.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.venclima.databinding.LoginBinding;
import com.example.venclima.viewModels.LoginViewModel;


public class LoginFragment extends Fragment {

    private LoginBinding binding;
    private LoginViewModel viewModel;

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

        binding = LoginBinding.inflate(inflater,container,false);
        viewModel = new LoginViewModel();
        binding.setLoginViewModel(viewModel);
        binding.executePendingBindings();

        return binding.getRoot();
    }
}