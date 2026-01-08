package com.example.venclima.views;

import android.view.View;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected void toggleSection(View content, View chevron) {
        if (content.getVisibility() == View.GONE) {
            content.setVisibility(View.VISIBLE);
            chevron.setRotation(180f);
        } else {
            content.setVisibility(View.GONE);
            chevron.setRotation(0f);
        }
    }

}
