package com.example.venclima;

import android.content.Context;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfUtils {

    public static File copyAssetPdfToCache(Context context, String assetPdfPath) throws IOException {
        File pdfDir = new File(context.getCacheDir(), "pdfs");
        if (!pdfDir.exists()) pdfDir.mkdirs();

        String fileName = new File(assetPdfPath).getName();
        File outFile = new File(pdfDir, fileName);

        if (outFile.exists()) {
            return outFile;
        }

        try (InputStream is = context.getAssets().open(assetPdfPath);
             FileOutputStream fos = new FileOutputStream(outFile)) {

            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.flush();
        }

        return outFile;
    }

    public static Uri getUriForFile(Context context, File file) {
        String authority = context.getPackageName() + ".fileprovider";
        return FileProvider.getUriForFile(context, authority, file);
    }

}
