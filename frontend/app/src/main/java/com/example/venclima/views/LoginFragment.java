package com.example.venclima.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.venclima.databinding.LoginBinding;
import com.example.venclima.viewModels.LoginViewModel;
import com.example.venclima.network.repositories.AuthCallback;
import android.widget.Toast;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.example.venclima.R;


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
        //email from signup
        if (getArguments() != null && getArguments().containsKey("email")) {
            String prefill = getArguments().getString("email");
            if (prefill != null && !prefill.isEmpty()) {
                viewModel.setEmail(prefill);
            }
        }
        viewModel.setAuthCallback(new AuthCallback() {
            @Override
            public void onSuccess(String message) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        NavOptions navOptions = new NavOptions.Builder()
                                .setPopUpTo(R.id.LoginFragment, true)
                                .build();
                        NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.TideForecastFragment, null, navOptions);
                        //Hide Login/Reg
                        if (getActivity() instanceof com.example.venclima.views.MainActivity) {
                            ((com.example.venclima.views.MainActivity) getActivity()).updateDrawerMenu();
                        }
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
        binding.setLoginViewModel(viewModel);
        binding.executePendingBindings();

        return binding.getRoot();
    }
}