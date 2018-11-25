package pg.pgapp;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.GroundOverlay;

public class OnGroundOverlayClickListener implements GoogleMap.OnGroundOverlayClickListener {
	private final Context _context;

	public OnGroundOverlayClickListener(Context context) {
		_context = context;
	}

	@Override
	public void onGroundOverlayClick(GroundOverlay groundOverlay) {
		Toast.makeText(_context, groundOverlay.getTag().toString(), Toast.LENGTH_LONG).show();
	}
}
