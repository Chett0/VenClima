package com.example.venclima.views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.venclima.adapters.NotificationsAdapter;
import com.example.venclima.databinding.OptionsBinding;
import com.example.venclima.network.RetrofitInstance;
import com.example.venclima.network.TokenManager;
import com.example.venclima.models.UserDTO;
import com.example.venclima.network.services.AuthService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.R;
import com.example.venclima.viewModels.OptionsViewModel;
import com.example.venclima.utils.LocaleHelper;
import androidx.appcompat.app.AlertDialog;

public class OptionsFragment extends Fragment {

    private OptionsBinding binding;
    private OptionsViewModel viewModel;
    private TextView receiveNotification;
    private TextView loginNeeded;
    private Button btnNotifications;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = OptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(OptionsViewModel.class);
        binding.setOptionsViewModel(viewModel);
        binding.executePendingBindings();

        super.onViewCreated(view, savedInstanceState);
        final TextView nameTv = binding.textView3;
        final TextView emailTv = binding.textView7;
        final TextView userTv = binding.textView8;
        final Button btn = binding.btnLogout;
        this.receiveNotification = binding.textView9;
        this.loginNeeded = binding.textView10;
        this.btnNotifications = binding.btnNotifications;
        final TextView changeLangTv = binding.textChangeLanguage;

        btnNotifications.setOnClickListener(v -> NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.NotificationsFragment));

        showProvisionalView(nameTv, emailTv, userTv, btn);

        // Always call /me so the server can return 200 or 401
        AuthService authService = RetrofitInstance.getAuthService();
        authService.me().enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showUserView(response.body(), nameTv, emailTv, userTv, btn);
                    viewModel.enableNotification();
                } else {
                    showProvisionalView(nameTv, emailTv, userTv, btn);
                    viewModel.disableNotification();
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                showProvisionalView(nameTv, emailTv, userTv, btn);
                viewModel.disableNotification();
            }
        });

        viewModel.getIsNotificationEnabled().observe(getViewLifecycleOwner(), isEnabled -> btnNotifications.setEnabled(isEnabled));
        viewModel.getIsNotificationEnabled().observe(getViewLifecycleOwner(), isEnabled -> receiveNotification.setVisibility(isEnabled ? View.VISIBLE : View.GONE));
        viewModel.getIsNotificationEnabled().observe(getViewLifecycleOwner(), isEnabled -> loginNeeded.setVisibility(isEnabled ? View.GONE : View.VISIBLE));

        changeLangTv.setOnClickListener(v -> {
            final String[] languages = new String[]{getString(R.string.lang_italian), getString(R.string.lang_english), getString(R.string.lang_german)};
            final String[] codes = new String[]{"it","en","de"};
            String current = LocaleHelper.getLanguage(requireContext());
            int checked = 0;
            for (int i = 0; i < codes.length; i++) if (codes[i].equals(current)) checked = i;

            new AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.options_change_language))
                    .setSingleChoiceItems(languages, checked, (dialog, which) -> {
                        LocaleHelper.setLocale(requireContext(), codes[which]);
                        requireActivity().recreate();
                        dialog.dismiss();
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });
    }

    private void showProvisionalView(TextView nameTv, TextView emailTv, TextView userTv, Button btn) {
        nameTv.setText(getString(R.string.options_guest_name));
        emailTv.setText(getString(R.string.options_email_unavailable));
        userTv.setText(getString(R.string.options_username_unavailable));
        btn.setText(getString(R.string.option_login));
        btn.setOnClickListener(v -> NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment));
    }

    private void showUserView(UserDTO u, TextView nameTv, TextView emailTv, TextView userTv, Button btn) {
        nameTv.setText(u.getName() + " " + u.getSurname());
        emailTv.setText(getString(R.string.options_email_format, u.getEmail()));
        userTv.setText(getString(R.string.options_username_format, u.getName() + " " + u.getSurname()));
        btn.setText(getString(R.string.option_logout));
        btn.setOnClickListener(v -> {
            String refresh = null;
            try { refresh = TokenManager.getInstance().getRefreshToken(); } catch (Exception ignored) {}
            performLogout(refresh, nameTv, emailTv, userTv, btn);
        });
    }

    private void performLogout(String refresh, TextView nameTv, TextView emailTv, TextView userTv, Button btn) {
        Map<String, String> body = new HashMap<>();
        if (refresh != null) body.put("refreshToken", refresh);

        RetrofitInstance.getAuthService().logout(body).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                Toast.makeText(getActivity(), getString(R.string.options_logout_toast), Toast.LENGTH_SHORT).show();
                showProvisionalView(nameTv, emailTv, userTv, btn);
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).updateDrawerMenu();
                }
                androidx.navigation.NavOptions navOptions = new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.nav_graph, true)
                        .build();
                NavHostFragment.findNavController(OptionsFragment.this).navigate(R.id.LoginFragment, null, navOptions);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                try { TokenManager.getInstance().notifyTokenExpired(); } catch (Exception ignored) {}
                Toast.makeText(getActivity(), getString(R.string.options_logout_toast), Toast.LENGTH_SHORT).show();
                showProvisionalView(nameTv, emailTv, userTv, btn);
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).updateDrawerMenu();
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