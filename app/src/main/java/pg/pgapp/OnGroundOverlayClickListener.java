package pg.pgapp;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlay;

/**
 * Created by Ravo on 23.06.2018.
 */

public class OnGroundOverlayClickListener implements GoogleMap.OnGroundOverlayClickListener {
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        Log.i("INFO",groundOverlay.getTag().toString());
    }
}
