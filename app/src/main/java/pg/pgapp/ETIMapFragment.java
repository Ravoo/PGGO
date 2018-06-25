package pg.pgapp;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MapStyleOptions;

public class ETIMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings mUiSettings;
    private SharedPreferences preferences;

    private final SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    configureUI();
                }
            };

    public static SupportMapFragment newInstance() {
        Bundle args = new Bundle();
        SupportMapFragment fragment = new SupportMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        preferences.registerOnSharedPreferenceChangeListener(listener);
        return inflater.inflate(R.layout.activity_maps, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Initializer initializer = new Initializer();
        initializer.Initialize(mMap, getContext());
        configureUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(listener);
    }

    @SuppressLint("MissingPermission")
    private void configureUI() {
        if (isSet("night_mode_preference")) {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_night));
        }
        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(isSet("UI_settings_zoom"));
        mUiSettings.setCompassEnabled(isSet("UI_settings_compass"));
        mUiSettings.setMyLocationButtonEnabled(isSet("UI_settings_my_location"));
        mMap.setMyLocationEnabled(isSet("UI_settings_my_location"));
        mUiSettings.setScrollGesturesEnabled(isSet("gesture_setting_scroll") && isSet("general_gesture_settings"));
        mUiSettings.setZoomGesturesEnabled(isSet("gesture_setting_zoom") && isSet("general_gesture_settings"));
        mUiSettings.setTiltGesturesEnabled(isSet("gesture_setting_tilt") && isSet("general_gesture_settings"));
        mUiSettings.setRotateGesturesEnabled(isSet("gesture_setting_rotate") && isSet("general_gesture_settings"));
    }

    private boolean isSet(String tag) {
        return this.preferences.getBoolean(tag, true);
    }
}
