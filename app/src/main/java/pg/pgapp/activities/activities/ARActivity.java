package pg.pgapp.activities.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import pg.pgapp.R;
import pg.pgapp.ar.DemoUtils;
import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.LocationScene;
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper;

public class ARActivity extends AppCompatActivity {
	private boolean installRequested;
	private boolean hasFinishedLoading = false;

	private Snackbar loadingMessageSnackbar = null;

	private ArSceneView arSceneView;

	// Renderables for this example
	private ModelRenderable andyRenderable;
	private List<ViewRenderable> layoutsRenderable = new ArrayList<>();

	// Our ARCore-Location scene
	private LocationScene locationScene;

	private Coordinate destination;


	@Override
	@SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
	// CompletableFuture requires api level 24
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sceneform);
		arSceneView = findViewById(R.id.ar_scene_view);

        List<Coordinate> buildingsCoords = Arrays.asList(new Coordinate(54.371696, 18.612375), new Coordinate(54.370910, 18.613070), new Coordinate(54.371649, 18.614504));

		// Build a renderable from a 2D View.
		/*CompletableFuture<ViewRenderable> exampleLayout =
				ViewRenderable.builder()
						.setView(this, R.layout.example_layout)
						.build();*/

		/*CompletableFuture[] layouts = new CompletableFuture[buildingsCoords.size()];
		for (int i = 0; i < buildingsCoords.size(); i++) {
		    layouts[i] = ViewRenderable.builder()
                    .setView(this, R.layout.example_layout)
                    .build();
        }*/

		List<CompletableFuture<ViewRenderable>> layouts = new ArrayList<>();
		for (Coordinate coords : buildingsCoords) {
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
				layouts.get(2),
				andy)
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
								andyRenderable = andy.get();
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

							if (locationScene == null) {
								// If our locationScene object hasn't been setup yet, this is a good time to do it
								// We know that here, the AR components have been initiated.
								locationScene = new LocationScene(this, this, arSceneView);

								// Now lets create our location markers.
								// First, a layout

								for (Coordinate coords : buildingsCoords) {
									LocationMarker layoutLocationMarker = new LocationMarker(
											coords.longitude,
											coords.latitude,
											getExampleView(buildingsCoords.indexOf(coords))
									);

									// An example "onRender" event, called every frame
									// Updates the layout with the markers distance
                                    layoutLocationMarker.setRenderEvent(node -> {
                                        View eView = layoutsRenderable.get(buildingsCoords.indexOf(coords)).getView();
                                        TextView distanceTextView = eView.findViewById(R.id.textView);
                                        distanceTextView.setText(node.getDistance() + " m");
                                    });
									// Adding the marker
									locationScene.mLocationMarkers.add(layoutLocationMarker);
								}
							}

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

							if (loadingMessageSnackbar != null) {
								for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
									if (plane.getTrackingState() == TrackingState.TRACKING) {
										hideLoadingMessage();
									}
								}
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
		Context c = this;
		// Add  listeners etc here
		View eView = layoutsRenderable.get(layoutIndex).getView();
		eView.setOnTouchListener((v, event) -> {
			Toast.makeText(
					c, "Location marker touched.", Toast.LENGTH_LONG)
					.show();
			return false;
		});

		return base;
	}

	/***
	 * Example Node of a 3D model
	 *
	 * @return
	 */
	private Node getAndy() {
		Node base = new Node();
		base.setRenderable(andyRenderable);
		Context c = this;
		base.setOnTapListener((v, event) -> {
			Toast.makeText(
					c, "Andy touched.", Toast.LENGTH_LONG)
					.show();
		});
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

		if (arSceneView.getSession() != null) {
			showLoadingMessage();
		}
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

	private void showLoadingMessage() {
		if (loadingMessageSnackbar != null && loadingMessageSnackbar.isShownOrQueued()) {
			return;
		}

		loadingMessageSnackbar =
				Snackbar.make(
						ARActivity.this.findViewById(android.R.id.content),
						R.string.plane_finding,
						Snackbar.LENGTH_INDEFINITE);
		loadingMessageSnackbar.getView().setBackgroundColor(0xbf323232);
		loadingMessageSnackbar.show();
	}

	private void hideLoadingMessage() {
		if (loadingMessageSnackbar == null) {
			return;
		}

		loadingMessageSnackbar.dismiss();
		loadingMessageSnackbar = null;
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
