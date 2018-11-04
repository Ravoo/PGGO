package pg.pgapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import lombok.NonNull;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.BuildingDisplay;

public class Initializer {

	private final Context context;

	public Initializer(Context context) {
		this.context = context;
	}

	public void initialize(@NonNull final GoogleMap mMap) {

		setBuildingsOnMap(mMap);

		LatLng noweEti = new LatLng(54.371795, 18.616282);
		mMap.moveCamera(CameraUpdateFactory.newLatLng(noweEti)); //TODO: domyślnie będzie move to my location
		mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
	}

	private void setBuildingsOnMap(@NonNull final GoogleMap mMap) {
        /*        //todo skonfigurowac ewentualne pobieranie danych z pliku lokalnie
        Type listType = new TypeToken<ArrayList<BuildingDisplay>>() {
        }.getType(); //potrzebne żeby wczytać listę obiektów z jsona
        Gson gson = new Gson();
        ArrayList<BuildingDisplay> buildings = gson.fromJson(readDataFromFile("BuildingsConfiguration.json"), listType);
        */
		ArrayList<BuildingDisplay> buildings = new DatabaseConnector().getBuildingDisplays();
		if (buildings != null) {
			buildings.forEach(
					building -> {
						PolygonOptions buildingOptions = new PolygonOptions()
								.clickable(true)
								.strokeColor(building.getModelColor())
								.strokeWidth(2);

						building.getCoordinates().forEach(coordinate ->
								buildingOptions.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude())));

						Polygon polygon = mMap.addPolygon(buildingOptions);
						polygon.setClickable(true);
						polygon.setTag(building.getBuildingId());
					}
			);
			mMap.setOnPolygonClickListener(new OnPolygonClickListener(context));
		}
	}

	private String readDataFromFile(String filename) {
		String json = null;
		try {
			InputStream inputStream = context.getAssets().open(filename);
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
}
