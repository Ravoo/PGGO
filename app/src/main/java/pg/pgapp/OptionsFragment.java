package pg.pgapp;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

public class OptionsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.options_fragment, rootKey);

        // Short demo how to use these preferences and set listener to them
        final PreferenceManager preferenceManager = getPreferenceManager();

        final SwitchPreferenceCompat nightMode = (SwitchPreferenceCompat) preferenceManager.findPreference("night_mode_preference");
        final CheckBoxPreference polishLanguage = (CheckBoxPreference) preferenceManager.findPreference("language_pl_key");
        final ListPreference textSize = (ListPreference) preferenceManager.findPreference("text_size");

        polishLanguage.setOnPreferenceChangeListener(printInfoListener);
        nightMode.setOnPreferenceChangeListener(printInfoListener);
        textSize.setOnPreferenceChangeListener(printInfoListener);
    }

    private static Preference.OnPreferenceChangeListener printInfoListener = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.d("PGGO", "Pref " + preference.getKey() + " changed to " + newValue.toString());
            return true;
        }
    };
}
