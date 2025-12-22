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
import java.util.HashMap;
import java.util.Map;
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
        final TextView nameTv = binding.textView3;
        final TextView emailTv = binding.textView7;
        final TextView userTv = binding.textView8;
        final Button btn = binding.btnLogout;

        showProvisionalView(nameTv, emailTv, userTv, btn);

        // Always call /me so the server can return 200 or 401
        AuthService authService = RetrofitInstance.getAuthService();
        authService.me().enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showUserView(response.body(), nameTv, emailTv, userTv, btn);
                } else {
                    showProvisionalView(nameTv, emailTv, userTv, btn);
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                showProvisionalView(nameTv, emailTv, userTv, btn);
            }
        });
    }

    private void showProvisionalView(TextView nameTv, TextView emailTv, TextView userTv, Button btn) {
        nameTv.setText("Ospite");
        emailTv.setText("Email: non disponibile");
        userTv.setText("Nome utente: non disponibile");
        btn.setText("Accedi");
        btn.setOnClickListener(v -> NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment));
    }

    private void showUserView(UserDTO u, TextView nameTv, TextView emailTv, TextView userTv, Button btn) {
        nameTv.setText(u.getName() + " " + u.getSurname());
        emailTv.setText("Email: " + u.getEmail());
        userTv.setText("Nome utente: " + u.getName() + " " + u.getSurname());
        btn.setText("Effettua logout");
        btn.setOnClickListener(v -> {
            String refresh = null;
            try { refresh = TokenManager.getInstance().getRefreshToken(); } catch (Exception ignored) {}
            performLogout(refresh, nameTv, emailTv, userTv, btn);
        });
    }

    private void performLogout(String refresh, TextView nameTv, TextView emailTv, TextView userTv, Button btn) {
        Map<String, String> body = new HashMap<>();
        if (refresh != null) body.put("refreshToken", refresh);

        RetrofitInstance.getAuthService().logout(body).enqueue(new Callback<java.util.Map<String, String>>() {
            @Override
            public void onResponse(Call<java.util.Map<String, String>> call, Response<java.util.Map<String, String>> response) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                Toast.makeText(getActivity(), "Logout effettuato", Toast.LENGTH_SHORT).show();
                showProvisionalView(nameTv, emailTv, userTv, btn);
                if (getActivity() instanceof com.example.venclima.views.MainActivity) {
                    ((com.example.venclima.views.MainActivity) getActivity()).updateDrawerMenu();
                }
                androidx.navigation.NavOptions navOptions = new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build();
                NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment, null, navOptions);
            }

            @Override
            public void onFailure(Call<java.util.Map<String, String>> call, Throwable t) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                Toast.makeText(getActivity(), "Logout effettuato", Toast.LENGTH_SHORT).show();
                showProvisionalView(nameTv, emailTv, userTv, btn);
                if (getActivity() instanceof com.example.venclima.views.MainActivity) {
                    ((com.example.venclima.views.MainActivity) getActivity()).updateDrawerMenu();
                }
                androidx.navigation.NavOptions navOptions = new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build();
                NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment, null, navOptions);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}