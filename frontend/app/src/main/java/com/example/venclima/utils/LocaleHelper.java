package com.example.venclima.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LocaleHelper {

    private static final String PREF_NAME = "venclima_prefs";
    private static final String KEY_LANG = "app_lang";

    public static Context onAttach(Context context) {
        // it default
        if (!context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).contains(KEY_LANG)) {
            persistLanguage(context, "it");
        }
        String lang = getLanguage(context);
        return setLocale(context, lang);
    }

    public static Context setLocale(Context context, String language) {
        if (language == null || language.isEmpty()) language = "it";
        persistLanguage(context, language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            config.setLayoutDirection(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLayoutDirection(locale);
            }
            res.updateConfiguration(config, res.getDisplayMetrics());
            return context;
        }
    }

    public static void persistLanguage(Context context, String language) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putString(KEY_LANG, language).apply();
    }

    public static String getLanguage(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getString(KEY_LANG, "it");
    }
}
