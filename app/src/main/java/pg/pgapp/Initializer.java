package pg.pgapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class Initializer {

    public void Initialize(GoogleMap mMap, final Context context) {
        //nowe budynki
        LatLng noweEti = new LatLng(54.371648, 18.612357);
        GroundOverlayOptions noweEtiGroundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.neti))
                .position(noweEti, 100f, 88f);

        LatLng stareEti = new LatLng(54.370840, 18.613310);
        GroundOverlayOptions stareEtiGroundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.seti))
                .position(stareEti, 125f, 63f);

        LatLng mechaniczny = new LatLng(54.371612, 18.614723);
        GroundOverlayOptions mechanicznyGroundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mech))
                .position(mechaniczny, 85f, 150f);


        //przypisywanie ich do map | potrzebny zwrot z addGroundOverlay aby ustawić TAG oraz setClickable
        GroundOverlay noweEtiGroundOverlay = mMap.addGroundOverlay(noweEtiGroundOverlayOptions);
        noweEtiGroundOverlay.setClickable(true);
        noweEtiGroundOverlay.setTag(Building.ETI);

        GroundOverlay stareEtiGroundOverlay = mMap.addGroundOverlay(stareEtiGroundOverlayOptions);
        stareEtiGroundOverlay.setClickable(false);
        stareEtiGroundOverlay.setTag(Building.ETI);

        GroundOverlay mechanicznyGroundOverlay = mMap.addGroundOverlay(mechanicznyGroundOverlayOptions);
        mechanicznyGroundOverlay.setClickable(true);
        mechanicznyGroundOverlay.setTag(Building.MECHANICZNY);

        mMap.setOnGroundOverlayClickListener(new OnGroundOverlayClickListener(context));


        //pobranie mojej lokalizacji
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }


      

        mMap.moveCamera(CameraUpdateFactory.newLatLng(noweEti)); //TODO: domyślnie będzie move to my location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }
}
