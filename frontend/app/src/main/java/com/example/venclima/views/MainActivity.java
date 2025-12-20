package com.example.venclima.views;

import android.os.Bundle;

import com.example.venclima.R;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.venclima.databinding.ActivityMainBinding;

import android.util.Log;
import android.view.Menu;
import com.example.venclima.network.TokenManager;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //forza la modalità chiara dei colori

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.TideForecastFragment,
                R.id.HistoricalTideForecastFragment,
                R.id.CriticsZoneFragment,
                R.id.MoseFragment,
                R.id.BehaviourFragment,
                R.id.OptionFragment
        )
                .setOpenableLayout(binding.drawerLayout).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        updateDrawerMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void updateDrawerMenu() {
        Menu menu = binding.navView.getMenu();
        boolean loggedIn = false;
        try {
            String token = TokenManager.getInstance().getToken();
            loggedIn = token != null && !TokenManager.getInstance().isTokenExpired();
        } catch (Exception ignored) {}

        if (menu.findItem(R.id.LoginFragment) != null)
            menu.findItem(R.id.LoginFragment).setVisible(!loggedIn);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}