package pg.pgapp;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;

public class OnCircleClickListener implements GoogleMap.OnCircleClickListener {
	private final Context _context;

	public OnCircleClickListener(Context context) {
		_context = context;
	}

	@Override
	public void onCircleClick(Circle circle) {
		Toast.makeText(_context, "Kliknięte POI", Toast.LENGTH_LONG).show();
	}
}
