/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pg.pgapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * This shows how to add a map to a ViewPager. Note the use of
 * {@link ViewGroup#requestTransparentRegion(View)} to reduce jankiness.
 */
public class MapInPagerActivity extends AppCompatActivity {

    private MyAdapter mAdapter;

    private ViewPager mPager;

    private SharedPreferences preferences;

    /**
     * Called when the activity is first created.
     */
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

        // This is required to avoid a black flash when the map is loaded.  The flash is due
        // to the use of a SurfaceView as the underlying view of the map.
        mPager.requestTransparentRegion(mPager);
    }

    public static class ARPlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
            return inflater.inflate(R.layout.ar_placeholder, container, false);
        }
    }

    /**
     * A simple FragmentPagerAdapter that returns two TextFragment and a SupportMapFragment.
     */
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
                    return new OptionsFragment();
                case 1:
                    return new ETIMapFragment();
                case 2:
                    return new ARPlaceholderFragment();
                default:
                    return null;
            }
        }
    }

    public static int getNewTheme(String newTheme) {
        int themeID = R.style.FontSizeMedium;
        if (newTheme.equals("small")) {
            themeID = R.style.FontSizeSmall;
        } else if (newTheme.equals("large")) {
            themeID = R.style.FontSizeLarge;
        }
        return themeID;
    }
}
