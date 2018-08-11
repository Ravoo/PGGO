package pg.pgapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import lombok.NonNull;

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

    private void setBuildingsOnMap(@NonNull final GoogleMap mMap){
        ArrayList<BuildingDisplay> buildings = loadDataFromDatabase();

        buildings.forEach(
                building -> {
                    PolygonOptions buildingOptions = new PolygonOptions()
                            .clickable(true)
                            .strokeColor(Color.RED)
                            .strokeWidth(20);

                    building.getPoints().forEach(point -> buildingOptions.add(new LatLng(point.getLatitude(), point.getLongitude())));

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
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();

        return myDbHelper.getReadableDatabase();
    }
}
