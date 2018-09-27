package pg.pgapp.activities.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
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
		//buildingFacultyTextView.setText(facultiesNames.toString());
		buildingFacultyTextView.setText("Wydział elektroniki, telekomunikacji i informatyki (ETI)");
		//buildingDescription.setText(building.getDescription());
		buildingDescription.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum" +
				"Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?\"");
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