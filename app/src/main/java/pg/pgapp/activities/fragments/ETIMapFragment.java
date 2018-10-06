package pg.pgapp.activities.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MapStyleOptions;

import pg.pgapp.Initializer;
import pg.pgapp.R;

public class ETIMapFragment extends Fragment implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

	private GoogleMap mMap;
	private UiSettings mUiSettings;
	private SharedPreferences preferences;
	private DrawerLayout drawer;
	private final SharedPreferences.OnSharedPreferenceChangeListener listener =
			(prefs, key) -> configureUI();

	public static SupportMapFragment newInstance() {
		Bundle args = new Bundle();
		SupportMapFragment fragment = new SupportMapFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
		initializeDrawer();
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
		Initializer initializer = new Initializer(getContext());
		initializer.initialize(mMap);
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

	private void initializeDrawer()
	{
		drawer = getActivity().findViewById(R.id.drawer_layout);
		drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		ImageButton button = getActivity().findViewById(R.id.menuImageButton);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) {
				DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
				drawerLayout.openDrawer(GravityCompat.START);
			}
		});
		NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.nav_search) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_ar) {
			Log.i("Info","Akcja AR");
		}  else if (id == R.id.nav_manage) {
			Log.i("Info","Akcja Ustawienia");
			Intent intent = new Intent(getActivity(), OptionsActivity.class);
			startActivity(intent);
		}else if(id == R.id.nav_search)
		{
			Log.i("Info","Akcj szukaj");
		}

		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	public void OpenMenu(View view) {
		DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		drawerLayout.openDrawer(GravityCompat.START);
	}
}
