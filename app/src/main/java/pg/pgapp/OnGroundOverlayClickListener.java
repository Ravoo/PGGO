package pg.pgapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.GroundOverlay;

/**
 * Created by Ravo on 23.06.2018.
 */

public class OnGroundOverlayClickListener implements GoogleMap.OnGroundOverlayClickListener {
    private final Context _context;
    public OnGroundOverlayClickListener(Context context)
    {
        _context = context;
    }
    @Override
    public void onGroundOverlayClick(GroundOverlay groundOverlay) {
        Log.i("INFO",groundOverlay.getTag().toString());
        Intent intent = new Intent(_context, BuildingDetailsActivity.class);
        _context.startActivity(intent);
    }
}
