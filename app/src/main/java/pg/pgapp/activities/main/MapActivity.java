package pg.pgapp.activities.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.Locale;
import java.util.Random;

import pg.pgapp.Initializer;
import pg.pgapp.R;
import pg.pgapp.activities.activities.ARActivity;
import pg.pgapp.activities.activities.OptionsActivity;
import pg.pgapp.activities.activities.SearchActivity;
import pg.pgapp.models.BuildingDisplay;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

	private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;
	private GoogleMap mMap;
	private UiSettings mUiSettings;
	private SharedPreferences preferences;
	private DrawerLayout drawer;
	private boolean locationPermissionGranted = false;
	private final SharedPreferences.OnSharedPreferenceChangeListener listener =
			(prefs, key) -> configureUI();
	private ImageButton drawerMenuButton;

	public static SupportMapFragment newInstance() {
		Bundle args = new Bundle();
		SupportMapFragment fragment = new SupportMapFragment();
		fragment.setArguments(args);
		return fragment;
	}

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
		setContentView(R.layout.activity_maps);
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String newTheme = preferences.getString("text_size", null);
		setTheme(getNewTheme(newTheme));
		String language = preferences.getString("language", null);
		setLanguageForApp(language);

		checkPermissions();

		drawerMenuButton = findViewById(R.id.menuImageButton);
		initializeDrawer();

		ShowCase();
	}

	private void setMapCamera()
	{
		Intent intent = getIntent();
		if(intent.hasExtra("Coordinates"))
		{
			BuildingDisplay.Coordinate coords = (BuildingDisplay.Coordinate) intent.getSerializableExtra("Coordinates");
			mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(coords.getLatitude(),coords.getLongitude())));
			mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
		}
	}

	public void ShowCase()
	{
	    //zeby włączało się zawsze, łatwiej testować
	    Random randomNumber = new Random();
		ShowcaseConfig config = new ShowcaseConfig();
		config.setDelay(500);
		MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this,Integer.toString(5));
		sequence.setConfig(config);

		sequence.addSequenceItem(drawerMenuButton, "Witaj w przewodniku po Politechnice Gdańskiej. Wciśnij podświetlony przycisk" +
                " aby otworzyć menu.", "Ok");

		sequence.start();
	}

	public void setLanguageForApp(String languageToLoad) {
		Locale locale;
		if (languageToLoad == null || languageToLoad.equals("not-set")) {
			locale = Locale.getDefault();
		} else {
			locale = new Locale(languageToLoad);
		}
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.setLocale(locale);

		getBaseContext().getResources().updateConfiguration(config,
				getBaseContext().getResources().getDisplayMetrics());
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		Initializer initializer = new Initializer(this);
		initializer.initialize(mMap);

		mMap.setBuildingsEnabled(false);

		configureUI();
		setMapCamera();
	}

	@Override
	public void onResume() {
		super.onResume();
		String newTheme = preferences.getString("text_size", null);
		setTheme(getNewTheme(newTheme));
		String language = preferences.getString("language", null);
		setLanguageForApp(language);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(listener);
	}

	@Override
	public void onPause() {
		super.onPause();
		PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(listener);
	}

	@SuppressLint("MissingPermission")
	private void configureUI() {
		if (isSet("night_mode_preference")) {
			mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_night));
		}
		mUiSettings = mMap.getUiSettings();
		mUiSettings.setZoomControlsEnabled(isSet("UI_settings_zoom"));
		mUiSettings.setCompassEnabled(isSet("UI_settings_compass"));
		mUiSettings.setMyLocationButtonEnabled(isSet("UI_settings_my_location"));
		mUiSettings.setScrollGesturesEnabled(isSet("gesture_setting_scroll") && isSet("general_gesture_settings"));
		mUiSettings.setZoomGesturesEnabled(isSet("gesture_setting_zoom") && isSet("general_gesture_settings"));
		mUiSettings.setTiltGesturesEnabled(isSet("gesture_setting_tilt") && isSet("general_gesture_settings"));
		mUiSettings.setRotateGesturesEnabled(isSet("gesture_setting_rotate") && isSet("general_gesture_settings"));

		mMap.setMyLocationEnabled(locationPermissionGranted);
	}

	private boolean isSet(String tag) {
		return this.preferences.getBoolean(tag, true);
	}

	private void initializeDrawer() {
		drawer = findViewById(R.id.drawer_layout);
		ImageButton button = findViewById(R.id.menuImageButton);
		button.setOnClickListener(v -> {
			DrawerLayout drawerLayout = drawer;
			drawerLayout.openDrawer(GravityCompat.START);
		});
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
			Log.i("Info", "Akcja AR");
			Intent intent = new Intent(this, ARActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_manage) {
			Log.i("Info", "Akcja Ustawienia");
			Intent intent = new Intent(this, OptionsActivity.class);
			startActivity(intent);
		} else if (id == R.id.nav_search) {
			Log.i("Info", "Akcja szukaj");
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
		}

		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
				// If request is cancelled, the result arrays are empty.
                locationPermissionGranted = grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED;
			}
		}
	}

	public void OpenMenu(View view) {
		DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
		drawerLayout.openDrawer(GravityCompat.START);
	}

	private void checkPermissions() {
		if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
		} else {
			locationPermissionGranted = true;
		}
	}
}
