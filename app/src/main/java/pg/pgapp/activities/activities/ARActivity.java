package pg.pgapp.activities.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import pg.pgapp.R;
import pg.pgapp.ar.DemoUtils;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Building;
import pg.pgapp.models.BuildingDisplay;
import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.LocationScene;
import uk.co.appoly.arcorelocation.rendering.LocationNode;
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper;

public class ARActivity extends AppCompatActivity {
	private static final int DISPLAYED_BUILDINGS = 3;

	private boolean installRequested;
	private boolean hasFinishedLoading = false;

	private Snackbar goToSnackbar = null;

	private ArSceneView arSceneView;

	// Renderables for this example
	private List<ViewRenderable> layoutsRenderable = new ArrayList<>();

	// Our ARCore-Location scene
	private LocationScene locationScene;

	private Map<Long, BuildingDisplay> allBuildingsDisplays;
    private Map<Long, Building> buildingsNearby = new HashMap<>(8);
    private List<Long> buildingDisplayIdsToDisplay;
	private BuildingDisplay.Coordinate destination;
	private String destinationName;

	private boolean isGoToEnabled = false;

	private float lastFrame = 0;


	@Override
	@SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
	// CompletableFuture requires api level 24
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sceneform);
		arSceneView = findViewById(R.id.ar_scene_view);

		Intent intent = getIntent();
		if (intent.hasExtra("Coordinates") && intent.hasExtra("BuildingName"))
		{
			isGoToEnabled = true;
			destination = (BuildingDisplay.Coordinate) intent.getSerializableExtra("Coordinates");
			destinationName = intent.getStringExtra("BuildingName");
		} else {
			isGoToEnabled = false;
		}

		List<CompletableFuture<ViewRenderable>> layouts = new ArrayList<>();
		for (int i = 0; i < DISPLAYED_BUILDINGS; i++) {
		    layouts.add(ViewRenderable.builder()
                .setView(this, R.layout.example_layout)
                .build()
            );
        }

		// When you build a Renderable, Sceneform loads its resources in the background while returning
		// a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
		CompletableFuture<ModelRenderable> andy = ModelRenderable.builder()
				.setSource(this, R.raw.andy)
				.build();

		CompletableFuture.allOf(
                layouts.get(0),
				layouts.get(1),
				layouts.get(2))
				.handle(
						(notUsed, throwable) -> {
							// When you build a Renderable, Sceneform loads its resources in the background while
							// returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
							// before calling get().

							if (throwable != null) {
								DemoUtils.displayError(this, "Unable to load renderables", throwable);
								return null;
							}

							try {
							    layoutsRenderable.add(layouts.get(0).get());
                                layoutsRenderable.add(layouts.get(1).get());
                                layoutsRenderable.add(layouts.get(2).get());
								hasFinishedLoading = true;

							} catch (InterruptedException | ExecutionException ex) {
								DemoUtils.displayError(this, "Unable to load renderables", ex);
							}

							return null;
						});

		// Set an update listener on the Scene that will hide the loading message once a Plane is
		// detected.
		arSceneView
				.getScene()
				.setOnUpdateListener(
						frameTime -> {
							if (!hasFinishedLoading) {
								return;
							}

							if (lastFrame == 0)
							    lastFrame = frameTime.getStartSeconds();

							if (locationScene == null) {
								// If our locationScene object hasn't been setup yet, this is a good time to do it
								// We know that here, the AR components have been initiated.
								locationScene = new LocationScene(this, this, arSceneView);
                                locationScene.setAnchorRefreshInterval(10);
//                                locationScene.setMinimalRefreshing(true);
							}

							if (isGoToEnabled && locationScene.mLocationMarkers.isEmpty()) {
                                System.out.println("TRYB: GO TO");
								LocationMarker layoutLocationMarker = new LocationMarker(
										destination.getLongitude(),
										destination.getLatitude(),
										getExampleView(0)
								);

								System.out.println("DESTINATION:");
								System.out.println("LAT: " + layoutLocationMarker.latitude);
								System.out.println("LONG: " + layoutLocationMarker.longitude);
								System.out.println(destinationName);

								layoutLocationMarker.setRenderEvent((LocationNode node) -> {
									View eView = layoutsRenderable.get(0).getView();
									TextView distanceTextView = eView.findViewById(R.id.textView);
									distanceTextView.setText(node.getDistance() + " m\n" + destinationName);

									System.out.println("Prowadzę do: " + destinationName);
								});

                                goToSnackbar(destinationName);

								locationScene.mLocationMarkers.add(layoutLocationMarker);
							} else if (!isGoToEnabled && locationScene.mLocationMarkers.isEmpty()
                                    && frameTime.getStartSeconds() > lastFrame + 10){
							    System.out.println("TRYB: EKSPLORACJA");

							    lastFrame = frameTime.getStartSeconds();

							    hideSnackbar();

								locationScene.mLocationMarkers = new ArrayList<>();

							    if (allBuildingsDisplays == null) {
                                    getBuildingsNearby();
                                }
                                buildingDisplayIdsToDisplay = new ArrayList<>();
							    buildingsNearby.values().stream()
                                        .forEach(b -> buildingDisplayIdsToDisplay.add(b.getBuildingDisplayId()));
								System.out.println("PRZEDFOR");

                                for (Long buildingDisplayId : buildingDisplayIdsToDisplay) {
									System.out.println("FOR123");

									BuildingDisplay buildingDisplay = allBuildingsDisplays.get(buildingDisplayId);
                                    LocationMarker layoutLocationMarker = new LocationMarker(
                                            buildingDisplay.getCenter().getLongitude(),
                                            buildingDisplay.getCenter().getLatitude(),
                                            getExampleView(buildingDisplayIdsToDisplay.indexOf(buildingDisplayId))
                                    );

                                    // An example "onRender" event, called every frame
                                    // Updates the layout with the markers distance
                                    layoutLocationMarker.setRenderEvent((LocationNode node) -> {
                                        View eView = layoutsRenderable.get(buildingDisplayIdsToDisplay.indexOf(buildingDisplayId)).getView();
                                        TextView distanceTextView = eView.findViewById(R.id.textView);
                                        distanceTextView.setText(node.getDistance() + " m\n"
                                                + buildingsNearby.get(buildingDisplay.getBuildingId()).getName());

                                        System.out.println("TEXT: ");
                                        System.out.println(distanceTextView.getText());

                                    });
                                    // Adding the marker
                                    locationScene.mLocationMarkers.add(layoutLocationMarker);
                                }
                            }
/*
							for (Coordinate coords : buildingsCoords) {
								LocationMarker layoutLocationMarker = new LocationMarker(
										coords.longitude,
										coords.latitude,
										getExampleView(buildingsCoords.indexOf(coords))
								);

								// An example "onRender" event, called every frame
								// Updates the layout with the markers distance
								layoutLocationMarker.setRenderEvent((LocationNode node) -> {
									View eView = layoutsRenderable.get(buildingsCoords.indexOf(coords)).getView();
									TextView distanceTextView = eView.findViewById(R.id.textView);
									distanceTextView.setText(node.getDistance() + " m");
								});
								// Adding the marker
								locationScene.mLocationMarkers.add(layoutLocationMarker);
							}*/

							Frame frame = arSceneView.getArFrame();
							if (frame == null) {
								return;
							}

							if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
								return;
							}

							if (locationScene != null) {
								locationScene.processFrame(frame);
							}
						});


		// Lastly request CAMERA & fine location permission which is required by ARCore-Location.
		ARLocationPermissionHelper.requestPermission(this);
	}

    /**
	 * Example node of a layout
	 *
	 * @return
	 */
	private Node getExampleView(int layoutIndex) {
		Node base = new Node();
		base.setRenderable(layoutsRenderable.get(layoutIndex));
		/*Context c = this;
		// Add  listeners etc here
		View eView = layoutsRenderable.get(layoutIndex).getView();
		eView.setOnTouchListener((v, event) -> {
			Toast.makeText(
					c, "Location marker touched.", Toast.LENGTH_LONG)
					.show();
			return false;
		});*/

		return base;
	}

	/**
	 * Make sure we call locationScene.resume();
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (locationScene != null) {
			locationScene.resume();
		}

		if (arSceneView.getSession() == null) {
			// If the session wasn't created yet, don't resume rendering.
			// This can happen if ARCore needs to be updated or permissions are not granted yet.
			try {
				Session session = DemoUtils.createArSession(this, installRequested);
				if (session == null) {
					installRequested = ARLocationPermissionHelper.hasPermission(this);
					return;
				} else {
					arSceneView.setupSession(session);
				}
			} catch (UnavailableException e) {
				DemoUtils.handleSessionException(this, e);
			}
		}

		try {
			arSceneView.resume();
		} catch (CameraNotAvailableException ex) {
			DemoUtils.displayError(this, "Unable to get camera", ex);
			finish();
			return;
		}

		/*if (arSceneView.getSession() != null) {
			showLoadingMessage();
		}*/
	}

	/**
	 * Make sure we call locationScene.pause();
	 */
	@Override
	public void onPause() {
		super.onPause();

		if (locationScene != null) {
			locationScene.pause();
		}

		arSceneView.pause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		arSceneView.destroy();
//		isGoToEnabled = false;
	}

	@Override
	public void onRequestPermissionsResult(
			int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
		if (!ARLocationPermissionHelper.hasPermission(this)) {
			if (!ARLocationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
				// Permission denied with checking "Do not ask again".
				ARLocationPermissionHelper.launchPermissionSettings(this);
			} else {
				Toast.makeText(
						this, "Kamera urządzenia jest wymagana do korzystania z trybu AR!", Toast.LENGTH_LONG)
						.show();
			}
			finish();
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			// Standard Android full-screen functionality.
			getWindow()
					.getDecorView()
					.setSystemUiVisibility(
							View.SYSTEM_UI_FLAG_LAYOUT_STABLE
									| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
									| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
									| View.SYSTEM_UI_FLAG_FULLSCREEN
									| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}

	private void goToSnackbar(String buildingName) {
        if (goToSnackbar != null && goToSnackbar.isShownOrQueued()) {
            return;
        }

        goToSnackbar =
                Snackbar.make(
                        ARActivity.this.findViewById(android.R.id.content),
                        "Prowadzę do: " + buildingName + "\n",
                        Snackbar.LENGTH_INDEFINITE);
        goToSnackbar.getView().setBackgroundColor(0xbf323232);
        goToSnackbar.show();
    }

    private void hideSnackbar() {
        if (goToSnackbar == null) {
            return;
        }

        goToSnackbar.dismiss();
        goToSnackbar = null;
    }

    private void getBuildingsNearby() {
	    if (allBuildingsDisplays == null || allBuildingsDisplays.isEmpty()) {
            getAllBuildingDisplays();
        }

        if (locationScene != null && locationScene.deviceLocation != null && locationScene.deviceLocation.currentBestLocation != null) {
            SortedMap<Long, Double> distanceToBuildingById = new TreeMap<>();

            double deviceLatitude = locationScene.deviceLocation.currentBestLocation.getLatitude();
            double deviceLongitude = locationScene.deviceLocation.currentBestLocation.getLongitude();
            Coordinate deviceLocation = new Coordinate(deviceLatitude, deviceLongitude);

            for (BuildingDisplay buildingDisplay : allBuildingsDisplays.values()) {
                distanceToBuildingById.put(buildingDisplay.getId(), calculateDistance(deviceLocation, buildingDisplay.getCenter()));
            }

            List<Long> buildingDisplaysNearbyIds =
                    distanceToBuildingById.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                            .mapToLong(Map.Entry::getKey)
                            .boxed()
                            .limit(DISPLAYED_BUILDINGS)
                            .collect(Collectors.toList());

            getBuildingData(buildingDisplaysNearbyIds);
        }
    }

    private void getBuildingData(List<Long> buildingDisplaysIds) {
	    Map<Long, Boolean> cachedBuildings = new HashMap<>();
	    buildingsNearby.keySet().forEach(k -> cachedBuildings.put(k, false));

	    for (Long buildingDisplayId : buildingDisplaysIds) {
	        BuildingDisplay buildingDisplay = allBuildingsDisplays.get(buildingDisplayId);
	        Long buildingId = buildingDisplay.getBuildingId();

	        if (cachedBuildings.containsKey(buildingId)) {
	            cachedBuildings.put(buildingId, true);
            } else {
	            Building buildingToCache = new DatabaseConnector().getBuildingModel(buildingId);
	            buildingsNearby.put(buildingToCache.getId(), buildingToCache);
            }
        }

        for (Map.Entry<Long, Boolean> cachedBuilding : cachedBuildings.entrySet()) {
	        if (!cachedBuilding.getValue()) { // jeżeli flaga dla budynku nie została zaktualizowana, budynek nie jest już wyświetlany
	            buildingsNearby.remove(cachedBuilding.getKey());
            }
        }
    }

    private Double calculateDistance(Coordinate deviceLocation, BuildingDisplay.Coordinate buildingLocation) {
	    return Math.pow(deviceLocation.latitude - buildingLocation.getLatitude(), 2) +
                Math.pow(deviceLocation.longitude - buildingLocation.getLongitude(), 2);
    }

    private void getAllBuildingDisplays() {
        List<BuildingDisplay> displays = new DatabaseConnector().getBuildingDisplays();
        allBuildingsDisplays = new HashMap<>();
        displays.stream()
                .forEach(d -> allBuildingsDisplays.put(d.getId(), d));
    }

	private class Coordinate {
		double latitude;
		double longitude;

		Coordinate (double lat, double lon) {
			latitude = lat;
			longitude = lon;
		}
	}
}
