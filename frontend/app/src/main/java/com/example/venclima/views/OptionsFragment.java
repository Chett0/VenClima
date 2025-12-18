package com.example.venclima.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.venclima.databinding.OptionsBinding;
import com.example.venclima.network.RetrofitInstance;
import com.example.venclima.network.TokenManager;
import com.example.venclima.models.UserDTO;
import com.example.venclima.network.services.AuthService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.fragment.NavHostFragment;
import com.example.venclima.R;

public class OptionsFragment extends Fragment {

    private OptionsBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = OptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean loggedIn = false;
        try {
            String token = TokenManager.getInstance().getToken();
            loggedIn = token != null && !TokenManager.getInstance().isTokenExpired();
        } catch (Exception ignored) {}

        TextView nameTv = binding.textView3;
        TextView emailTv = binding.textView7;
        TextView userTv = binding.textView8;
        Button btn = binding.btnLogout;

        if (!loggedIn) {
            nameTv.setText("Ospite");
            emailTv.setText("Email: non disponibile");
            userTv.setText("Nome utente: non disponibile");
            btn.setText("Accedi");
            btn.setOnClickListener(v -> {
                NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment);
            });
        } else {
            AuthService authService = RetrofitInstance.getAuthService();
            authService.me().enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDTO u = response.body();
                        nameTv.setText(u.getName() + " " + u.getSurname());
                        emailTv.setText("Email: " + u.getEmail());
                        userTv.setText("Nome utente: " + u.getName() + " " + u.getSurname());
                        btn.setText("Effettua logout");
                        btn.setOnClickListener(v -> {
                            // perform logout then navigate to login
                            try { TokenManager.getInstance().clearToken(); } catch (Exception ignored) {}
                            Toast.makeText(getActivity(), "Logout effettuato", Toast.LENGTH_SHORT).show();
                            nameTv.setText("Ospite");
                            emailTv.setText("Email: non disponibile");
                            userTv.setText("Nome utente: non disponibile");
                            btn.setText("Accedi");
                            // update drawer menu visibility in Activity
                            if (getActivity() instanceof com.example.venclima.views.MainActivity) {
                                ((com.example.venclima.views.MainActivity) getActivity()).updateDrawerMenu();
                            }
                            NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment);
                        });
                    } else {
                        nameTv.setText("Ospite");
                        emailTv.setText("Email: non disponibile");
                        userTv.setText("Nome utente: non disponibile");
                        btn.setText("Accedi");
                        btn.setOnClickListener(v -> NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment));
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    nameTv.setText("Ospite");
                    emailTv.setText("Email: non disponibile");
                    userTv.setText("Nome utente: non disponibile");
                    btn.setText("Accedi");
                    btn.setOnClickListener(v -> NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment));
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}