package com.example.venclima.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.R;
import com.example.venclima.databinding.HistoricalTideForecastBinding;
import com.example.venclima.adapters.MonthAdapter;
import com.example.venclima.utils.PdfUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HistoricalTideForecast extends BaseFragment {

    private HistoricalTideForecastBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = HistoricalTideForecastBinding.inflate(inflater, container, false);

        binding.headerBefore.setOnClickListener(v -> toggleSection(binding.contentAfter, binding.chevBefore));

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = binding.getRoot().findViewById(R.id.recycler_months);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        List<String> months = Arrays.asList(
                getString(R.string.month_december),
                getString(R.string.month_november),
                getString(R.string.month_october),
                getString(R.string.month_september),
                getString(R.string.month_august),
                getString(R.string.month_july),
                getString(R.string.month_june),
                getString(R.string.month_may),
                getString(R.string.month_april),
                getString(R.string.month_march),
                getString(R.string.month_february),
                getString(R.string.month_january)
        );
        MonthAdapter adapter = new MonthAdapter(getContext(), months, position -> {

            int monthIndex = 12 - position;
            String monthStr = (monthIndex < 10) ? ("0" + monthIndex) : String.valueOf(monthIndex);
            String assetPath = "pdfs/" + monthStr + "_2025ps.pdf";
            openPdfFromAssets(assetPath);
        });
        recycler.setAdapter(adapter);

    }

    private void openPdfFromAssets(String assetPdfPath) {
        try {
            File pdfFile = PdfUtils.copyAssetPdfToCache(requireContext(), assetPdfPath);
            Uri uri = PdfUtils.getUriForFile(requireContext(), pdfFile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION | android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(Intent.createChooser(intent, "Open PDF"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "Nessuna app trovata per aprire il PDF. Installa un visualizzatore PDF o usa il visualizzatore integrato.", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Errore nell'aprire il PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
