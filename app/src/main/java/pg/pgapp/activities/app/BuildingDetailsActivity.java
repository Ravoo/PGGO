package pg.pgapp.activities.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import pg.pgapp.R;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Building;


public class BuildingDetailsActivity extends AppCompatActivity {

	Building building;
    ImageView imageView;
    TextView buildingNameTextView;
    TextView buildingFacultyTextView;
    TextView buildingAddressTextView;
    WebView buildingDescription;
    byte[] serializedPicture;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_building_details);
		initializeDetailView(savedInstanceState);
	}
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putByteArray("pic",serializedPicture);
    }
	public void initializeDetailView(Bundle savedInstanceState) {
        imageView = findViewById(R.id.buildingImageView);
		imageView.setVisibility(View.GONE);

		buildingNameTextView = findViewById(R.id.buildingName);
		buildingNameTextView.setTextColor(Color.WHITE);

		buildingFacultyTextView = findViewById(R.id.buildingFacultyName);
		buildingFacultyTextView.setTextColor(Color.WHITE);

		buildingAddressTextView = findViewById(R.id.buildingAddress);
		buildingAddressTextView.setTextColor(Color.WHITE);

		buildingDescription = findViewById(R.id.buildingDescription);
		buildingDescription.setBackgroundColor(getResources().getColor(R.color.colorPgSecondary));

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

		String justifyTag = "<html><body style='text-align:justify;color:#FFFFFF'>%s</body></html>";
		String dataString = String.format(Locale.US, justifyTag, building.getDescription());
		buildingDescription.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");



		if(savedInstanceState != null) {
            byte[] pic = savedInstanceState.getByteArray("pic");
            serializedPicture = pic;
            Bitmap decodedByte = BitmapFactory.decodeByteArray(pic, 0, pic.length);
            imageView.setImageBitmap(decodedByte);
            imageView.setVisibility(View.VISIBLE);
            ProgressBar progressBar = findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.GONE);
        }else
        {
            new PictureDownloader().execute();
        }

	}

	public void changeActivity(View view) {
		Intent intent = new Intent(this, FacultyDetailsActivity.class);
		intent.putExtra("TAG", building.getFacultiesIds().get(0).toString());
		startActivity(intent);
	}

	public void setPicture(String picture) {
		imageView = findViewById(R.id.buildingImageView);
		building.setPicture(picture);
		byte[] decodedString = Base64.decode(building.getPicture(), Base64.DEFAULT);
        serializedPicture = decodedString;
		Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
		imageView.setImageBitmap(decodedByte);
		imageView.setVisibility(View.VISIBLE);
		ProgressBar progressBar = findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.GONE);

	}

	private class PictureDownloader extends AsyncTask<Void, Void, String> {
		@Override
		protected void onPostExecute(String result) {
			Log.v("Photo downloaded", result);
			setPicture(result);
		}

		@Override
		protected String doInBackground(Void... voids) {
			return new DatabaseConnector().getBuildingPicture(building.getId());
		}
	}
}
