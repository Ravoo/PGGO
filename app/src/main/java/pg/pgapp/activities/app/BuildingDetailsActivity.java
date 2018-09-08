package pg.pgapp.activities.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pg.pgapp.R;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Building;


public class BuildingDetailsActivity extends AppCompatActivity {

	Building building; //do change activity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_details);

		initializeDetailView();
	}

	public void initializeDetailView() {
		ImageView imageView = findViewById(R.id.buildingImageView);
		TextView buildingNameTextView = findViewById(R.id.buildingName);
		TextView buildingFacultyTextView = findViewById(R.id.buildingFacultyName);
		TextView buildingDescription = findViewById(R.id.buildingDescription);
		Intent intent = getIntent();
		String tag = intent.getStringExtra("TAG");

		building = new DatabaseConnector().getBuildingModel(Long.parseLong(tag));
		buildingNameTextView.setText(building.getName());

		StringBuilder facultiesNames = new StringBuilder();
		for (String facultyName : building.getFacultiesNames()) {
			facultiesNames
					.append(facultyName)
					.append("\n");
		}
		buildingFacultyTextView.setText(facultiesNames.toString());
		buildingDescription.setText(building.getDescription());

		byte[] decodedString = Base64.decode(building.getPicture(), Base64.DEFAULT);
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		imageView.setImageBitmap(decodedByte);
	}

	public void changeActivity(View view) {
		Intent intent = new Intent(this, FacultyDetailsActivity.class);
		intent.putExtra("TAG", building.getFacultiesIds().get(0));
		startActivity(intent);
	}
}
