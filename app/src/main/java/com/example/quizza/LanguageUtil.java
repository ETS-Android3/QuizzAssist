package com.example.quizza;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.lang.reflect.Field;
import java.util.Locale;



public class LanguageUtil {

    public static final String SYSTEM_LANGUAGE_TGA = "systemLanguageTag";


    public static void updateLanguage(final Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        Locale contextLocale = config.locale;
        if (isSameLocale(contextLocale, locale)) {
            return;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        config.setLocale(locale);
        if (context instanceof Application) {
            Context newContext = context.createConfigurationContext(config);
            try {
                //noinspection JavaReflectionMemberAccess
                Field mBaseField = ContextWrapper.class.getDeclaredField("mBase");
                mBaseField.setAccessible(true);
                mBaseField.set(context, newContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resources.updateConfiguration(config, dm);
    }


    private static boolean isSameLocale(Locale l0, Locale l1) {
        return equals(l1.getLanguage(), l0.getLanguage())
                && equals(l1.getCountry(), l0.getCountry());
    }

    /**
     * Return whether string1 is equals to string2.
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

}
