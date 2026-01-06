package com.example.venclima.views;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.R;
import com.example.venclima.adapters.NotificationsAdapter;
import com.example.venclima.adapters.OnIslandCheckedListener;
import com.example.venclima.databinding.NotificationsBinding;
import com.example.venclima.models.IslandNotification;
import com.example.venclima.network.Callbacks.NotificationUpdateCallback;
import com.example.venclima.viewModels.NotificationsViewModel;


public class NotificationsFragment extends Fragment implements OnIslandCheckedListener {

    private NotificationsBinding binding;
    private NotificationsViewModel viewModel;
    private RecyclerView notifications;
    private EditText searchBox;


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = NotificationsBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding.setNotificationsViewModel(viewModel);
        binding.executePendingBindings();
        binding.setLifecycleOwner(getViewLifecycleOwner());

        this.notifications = binding.checkboxRecyclerView;
        this.searchBox = binding.searchBox;
        this.notifications.setLayoutManager(new LinearLayoutManager(getContext()));

        NotificationsAdapter notificationsAdapter = new NotificationsAdapter(this);
        notifications.setAdapter(notificationsAdapter);
        viewModel.getNotifications().observe(getViewLifecycleOwner(), notificationsAdapter::setIslandNotificationList);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                notificationsAdapter.getFilter().filter(s.toString());
            }
        });


        this.viewModel.setNotificationUpdateCallback(new NotificationUpdateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Salvataggio riuscito!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(NotificationsFragment.this).navigate(R.id.OptionFragment);
            }
            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Errore: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

    }


    public void onIslandChecked(Integer islandId, boolean isChecked) {
        viewModel.onIslandSelectionChanged(islandId, isChecked);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
