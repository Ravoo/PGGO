package pg.pgapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Initializer {

    final Context context;

    public Initializer(Context context) {
        this.context = context;
    }

    public void Initialize(GoogleMap mMap) {
        Type listType = new TypeToken<ArrayList<BuildingDisplay>>(){}.getType(); //potrzebne żeby wczytać listę obiektów z jsona
        Gson gson = new Gson();
        ArrayList<BuildingDisplay> buildings = gson.fromJson(readDataFromfile("BuildingsConfiguration.json"),listType);


        for(int i = 0; i< buildings.size(); i++)
        {
            BuildingDisplay buildingDisplay = buildings.get(i);
            LatLng location = new LatLng(buildingDisplay.latitude, buildingDisplay.longitude);
            GroundOverlayOptions gop = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromAsset(buildingDisplay.picture))
                    .position(location,buildingDisplay.width, buildingDisplay.height);

            GroundOverlay groundOverlay = mMap.addGroundOverlay(gop);
            groundOverlay.setClickable(true);
            groundOverlay.setTag(buildingDisplay.tag);
        }

        LatLng noweEti = new LatLng(54.371648, 18.612357);
        mMap.setOnGroundOverlayClickListener(new OnGroundOverlayClickListener(context));

        //pobranie mojej lokalizacji
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


      

        mMap.moveCamera(CameraUpdateFactory.newLatLng(noweEti)); //TODO: domyślnie będzie move to my location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    public String readDataFromfile(String filename)
    {
        String json = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
