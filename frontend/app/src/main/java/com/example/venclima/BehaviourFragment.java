package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.venclima.databinding.BehaviourBinding;

public class BehaviourFragment extends Fragment {


    private BehaviourBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = BehaviourBinding.inflate(inflater, container, false);

        binding.headerBefore.setOnClickListener(v -> toggleSection(binding.contentBefore, binding.chevBefore));
        binding.headerDuring.setOnClickListener(v -> toggleSection(binding.contentDuring, binding.chevDuring));
        binding.headerAfter.setOnClickListener(v -> toggleSection(binding.contentAfter, binding.chevAfter));

        binding.headerNue.setOnClickListener(v -> toggleSection(binding.contentNue, binding.chevNue));
        binding.headerVigili.setOnClickListener(v -> toggleSection(binding.contentVigili, binding.chevVigili));
        binding.headerAmbulance.setOnClickListener(v -> toggleSection(binding.contentAmbulance, binding.chevAmbulance));
        binding.headerPolizia.setOnClickListener(v -> toggleSection(binding.contentPolizia, binding.chevPolizia));
        binding.headerGuardiaCostiera.setOnClickListener(v -> toggleSection(binding.contentGuardiaCostiera, binding.chevGuardiaCostiera));
        binding.headerGuardiaFinanza.setOnClickListener(v -> toggleSection(binding.contentGuardiaFinanza, binding.chevGuardiaFinanza));
        binding.headerComuneCentralino.setOnClickListener(v -> toggleSection(binding.contentComuneCentralino, binding.chevComuneCentralino));
        binding.headerProtezione.setOnClickListener(v -> toggleSection(binding.contentProtezione, binding.chevProtezione));
        binding.headerPoliziaLocale.setOnClickListener(v -> toggleSection(binding.contentPoliziaLocale, binding.chevPoliziaLocale));
        binding.headerMobilitaTerrestre.setOnClickListener(v -> toggleSection(binding.contentMobilitaTerrestre, binding.chevMobilitaTerrestre));
        binding.headerMobilitaAcquei.setOnClickListener(v -> toggleSection(binding.contentMobilitaAcquei, binding.chevMobilitaAcquei));

        return binding.getRoot();
    }

    private void toggleSection(View content, View chevron) {
        if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            chevron.setRotation(180f);
        } else {
            content.setVisibility(View.GONE);
            chevron.setRotation(0f);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
