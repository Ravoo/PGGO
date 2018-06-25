package pg.pgapp;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;

public class OptionsFragment extends PreferenceFragmentCompat {
    PreferenceManager preferenceManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.options_fragment, rootKey);

        preferenceManager = getPreferenceManager();

        final SwitchPreferenceCompat nightMode = (SwitchPreferenceCompat) preferenceManager.findPreference("night_mode_preference");
        nightMode.setOnPreferenceChangeListener(nightModeListener);

        final ListPreference textSize = (ListPreference) preferenceManager.findPreference("text_size");
        textSize.setOnPreferenceChangeListener(reloadActivityListener);
    }

    private Preference.OnPreferenceChangeListener reloadActivityListener = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // TODO moze da sie to jakos bardziej elegancko "przeladowac"
            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener printInfoListener = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.d("PGGO", "Pref " + preference.getKey() + " changed to " + newValue.toString());
            return true;
        }
    };

    private Preference.OnPreferenceChangeListener nightModeListener = new Preference.OnPreferenceChangeListener() {
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            UiModeManager uiManager = (UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE);
            if ((boolean) newValue) {
                uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
            } else {
                uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
            }
            return true;
        }
    };
}
