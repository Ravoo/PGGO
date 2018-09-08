package pg.pgapp.activities.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import pg.pgapp.R;
import pg.pgapp.activities.fragments.ARFragment;
import pg.pgapp.activities.fragments.ETIMapFragment;
import pg.pgapp.activities.fragments.MainMenuFragment;

public class MapInPagerActivity extends AppCompatActivity {

	private MyAdapter mAdapter;

	private ViewPager mPager;

	private SharedPreferences preferences;

	public static int getNewTheme(String newTheme) {
		int themeID = R.style.FontSizeMedium;
		if (newTheme != null) {
			if (newTheme.equals("small")) {
				themeID = R.style.FontSizeSmall;
			} else if (newTheme.equals("large")) {
				themeID = R.style.FontSizeLarge;
			}
		}
		return themeID;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String newTheme = preferences.getString("text_size", null);
		setTheme(getNewTheme(newTheme));

		setContentView(R.layout.map_in_pager);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(0);

		// This is required to avoid a black flash when the map is loaded.  The flash is due
		// to the use of a SurfaceView as the underlying view of the map.
		mPager.requestTransparentRegion(mPager);
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return new MainMenuFragment();
				case 1:
					return new ETIMapFragment();
				case 2:
					return new ARFragment();
				default:
					return null;
			}
		}
	}
}
