package pg.pgapp.activities.activities;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;

import java.util.Locale;

import pg.pgapp.R;
import pg.pgapp.activities.main.MapActivity;

// todo replace with activity, not activity with fragment
public class OptionsActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map_in_pager);

		MyAdapter mAdapter = new MyAdapter(getSupportFragmentManager());
		ViewPager mPager = findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);

		mPager.requestTransparentRegion(mPager);
		String newTheme = PreferenceManager.getDefaultSharedPreferences(this).getString("text_size", null);
		setTheme(MapActivity.getNewTheme(newTheme));
	}

	public static class OptionsFragment extends PreferenceFragmentCompat {
		PreferenceManager preferenceManager;
		private Preference.OnPreferenceChangeListener changeTextSizeListener = (preference, newValue) -> {
			reloadActivity();
			return true;
		};
		private Preference.OnPreferenceChangeListener nightModeListener = (preference, newValue) -> {
			UiModeManager uiManager = (UiModeManager) getActivity().getSystemService(Context.UI_MODE_SERVICE);
			if ((boolean) newValue) {
				uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
			} else {
				uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
			}
			return true;
		};
		private Preference.OnPreferenceChangeListener languageListener = (preference, newValue) -> {
			setLanguageForApp(newValue.toString());
			return true;
		};

		private void reloadActivity() {
			if (getActivity() != null) {
				getActivity().finish();
				getActivity().startActivity(getActivity().getIntent());
			}
		}

		public void setLanguageForApp(String languageToLoad) {
			if (getContext() != null) {
				Locale locale = new Locale(languageToLoad);
				Locale.setDefault(locale);
				Configuration config = new Configuration();
				config.setLocale(locale);

				getContext().getResources().updateConfiguration(config,
						getContext().getResources().getDisplayMetrics());
				reloadActivity();
			}
		}

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.options_fragment, rootKey);

			preferenceManager = getPreferenceManager();

			final SwitchPreferenceCompat nightMode = (SwitchPreferenceCompat) preferenceManager.findPreference("night_mode_preference");
			nightMode.setOnPreferenceChangeListener(nightModeListener);

			final ListPreference language = (ListPreference) preferenceManager.findPreference("language");
			language.setOnPreferenceChangeListener(languageListener);

			final ListPreference textSize = (ListPreference) preferenceManager.findPreference("text_size");
			textSize.setOnPreferenceChangeListener(changeTextSizeListener);
		}
	}

	public class MyAdapter extends FragmentPagerAdapter {
		MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return new OptionsFragment();
				default:
					return null;
			}
		}
	}
}
