package pg.pgapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;

import lombok.NonNull;
import pg.pgapp.Database.DataBaseHelper;
import pg.pgapp.Database.DatabaseExtractor;

public class Initializer {

    private final Context context;

    public Initializer(Context context) {
        this.context = context;
    }

    public void initialize(@NonNull final GoogleMap mMap) {

        setBuildingsOnMap(mMap);

        //pobranie mojej lokalizacji
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        LatLng noweEti = new LatLng(54.371648, 18.612357);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(noweEti)); //TODO: domyślnie będzie move to my location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    private void setBuildingsOnMap(@NonNull final GoogleMap mMap) {
        ArrayList<BuildingDisplay> buildings = loadDataFromDatabase();

        buildings.forEach(
                building -> {
                    PolygonOptions buildingOptions = new PolygonOptions()
                            .clickable(true)
                            .strokeColor(Color.RED)
                            .strokeWidth(2);

                    building.getCoordinates().forEach(coordinate ->
                            buildingOptions.add(new LatLng(coordinate.getLatitude(), coordinate.getLongitude())));

                    Polygon polygon = mMap.addPolygon(buildingOptions);
                    polygon.setClickable(true);
                    polygon.setTag(building.getTag());
                }
        );
        mMap.setOnPolygonClickListener(new OnPolygonClickListener(context));
    }

    private ArrayList<BuildingDisplay> loadDataFromDatabase() {
        DatabaseExtractor dbExtractor = new DatabaseExtractor(getReadableDatabase());
        return dbExtractor.getBuildings();
    }

    private SQLiteDatabase getReadableDatabase() {
        DataBaseHelper myDbHelper = new DataBaseHelper(this.context);

        myDbHelper.createDataBase();

        myDbHelper.openDataBase();

        return myDbHelper.getReadableDatabase();
    }
}
