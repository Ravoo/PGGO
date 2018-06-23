package pg.pgapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;


public class Initializer {


    public Initializer()
    {
    }

    public void Initialize(GoogleMap mMap, final Context context)
    {
        //nowe budynki
        LatLng ETI = new LatLng(54.371690, 18.612353);
        GroundOverlayOptions etiGroundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.eti_pic))
                .position(ETI, 40f,30f);

        LatLng Mechaniczny = new LatLng(54.371612, 18.614723);
        GroundOverlayOptions mechanicznyGroundOverlayOptions = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.mecha))
                .position(Mechaniczny, 100f,90f);


        //przypisywanie ich do map | potrzebny zwrot z addGroundOverlay aby ustawić TAG oraz setClickable
        GroundOverlay etiGroundOverlay = mMap.addGroundOverlay(etiGroundOverlayOptions);
        etiGroundOverlay.setClickable(true);
        etiGroundOverlay.setTag(Building.ETI);

        GroundOverlay mechanicnzyGroundOverlay = mMap.addGroundOverlay(mechanicznyGroundOverlayOptions);
        mechanicnzyGroundOverlay.setClickable(true);
        mechanicnzyGroundOverlay.setTag(Building.MECHANICZNY);


        mMap.setOnGroundOverlayClickListener(new OnGroundOverlayClickListener(context));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(ETI)); //TODO: domyślnie będzie move to my location
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }
}
