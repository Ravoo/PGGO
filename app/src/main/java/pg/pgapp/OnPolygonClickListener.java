package pg.pgapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polygon;

import pg.pgapp.Activities.BuildingDetailsActivity;

/**
 * Created by Ravo on 23.06.2018.
 */

public class OnPolygonClickListener implements GoogleMap.OnPolygonClickListener {
    private final Context _context;

    public OnPolygonClickListener(Context context) {
        _context = context;
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
        Log.i("INFO", polygon.getTag().toString());
        Intent intent = new Intent(_context, BuildingDetailsActivity.class);
        intent.putExtra("TAG", polygon.getTag().toString());
        _context.startActivity(intent);
    }
}
