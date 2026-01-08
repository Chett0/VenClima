package com.example.venclima.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.venclima.databinding.RegistrationBinding;
import com.example.venclima.viewModels.RegistrationViewModel;
import com.example.venclima.network.Callbacks.AuthCallback;
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
        setHasOptionsMenu(true);
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

        //to login
        View goLogin = binding.getRoot().findViewById(com.example.venclima.R.id.go_to_login_text);
        if (goLogin != null) {
            goLogin.setOnClickListener(v -> NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.LoginFragment));
        }

        return binding.getRoot();
    }

    //to tides
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavHostFragment.findNavController(RegistrationFragment.this).navigate(R.id.TideForecastFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}