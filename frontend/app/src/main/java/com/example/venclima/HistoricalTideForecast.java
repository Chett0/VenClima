package com.example.venclima;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.venclima.databinding.HistoricalTideForecastBinding;
import com.example.venclima.MonthAdapter;
import com.example.venclima.PdfUtils;

import java.util.Arrays;
import java.util.List;

public class HistoricalTideForecast extends Fragment {

    private HistoricalTideForecastBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = HistoricalTideForecastBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler = binding.getRoot().findViewById(R.id.recycler_months);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        List<String> months = Arrays.asList(
                "Gennaio","Febbraio","Marzo","Aprile","Maggio","Giugno",
                "Luglio","Agosto","Settembre","Ottobre","Novembre","Dicembre"
        );
        MonthAdapter adapter = new MonthAdapter(getContext(), months, position -> {

            int monthIndex = position + 1;
            String monthStr = (monthIndex < 10) ? ("0" + monthIndex) : String.valueOf(monthIndex);
            String assetPath = "pdfs/" + monthStr + "_2025ps.pdf";
            openPdfFromAssets(assetPath);
        });
        recycler.setAdapter(adapter);

    }

    private void openPdfFromAssets(String assetPdfPath) {
        try {
            java.io.File pdfFile = PdfUtils.copyAssetPdfToCache(requireContext(), assetPdfPath);
            android.net.Uri uri = PdfUtils.getUriForFile(requireContext(), pdfFile);

            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION | android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(android.content.Intent.createChooser(intent, "Open PDF"));
        } catch (android.content.ActivityNotFoundException e) {
            android.widget.Toast.makeText(requireContext(), "Nessuna app trovata per aprire il PDF. Installa un visualizzatore PDF o usa il visualizzatore integrato.", android.widget.Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            android.widget.Toast.makeText(requireContext(), "Errore nell'aprire il PDF: " + e.getMessage(), android.widget.Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
