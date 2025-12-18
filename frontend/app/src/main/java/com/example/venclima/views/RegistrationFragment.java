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
import com.example.venclima.network.repositories.AuthCallback;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.example.venclima.R;


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
        viewModel.setAuthCallback(new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            android.os.Bundle args = new android.os.Bundle();
                            args.putString("email", viewModel.getEmail());
                            NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.LoginFragment, args);
                    });
                }
            }

            @Override
            public void onError(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Errore: " + message, Toast.LENGTH_LONG).show());
                }
            }
        });
        binding.setRegistrationViewModel(viewModel);
        binding.executePendingBindings();

        return binding.getRoot();
    }
}