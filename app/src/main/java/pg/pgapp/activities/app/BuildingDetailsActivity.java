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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_details);

		initializeDetailView();
	}

	public void initializeDetailView() {
		ImageView imageView = findViewById(R.id.buildingImageView);
		imageView.setVisibility(View.GONE);
		TextView buildingNameTextView = findViewById(R.id.buildingName);
		buildingNameTextView.setTextColor(Color.WHITE);
		TextView buildingFacultyTextView = findViewById(R.id.buildingFacultyName);
		buildingFacultyTextView.setTextColor(Color.WHITE);
		TextView buildingAddressTextView = findViewById(R.id.buildingAddress);
		buildingAddressTextView.setTextColor(Color.WHITE);
		WebView buildingDescription = findViewById(R.id.buildingDescription);
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
		//buildingFacultyTextView.setText(facultiesNames.toString());
		buildingFacultyTextView.setText("Wydział elektroniki, telekomunikacji i informatyki (ETI)");
		//buildingDescription.setText(building.getDescription());
		String justifyTag = "<html><body style='text-align:justify;color:#FFFFFF'>%s</body></html>";
		String dataString = String.format(Locale.US, justifyTag, "Największy z wydziałów Politechniki Gdańskiej. Składa się z 16 katedr zatrudniających blisko 200 pracowników naukowo-dydaktycznych i naukowych, w tym trzech członków korespondentów Polskiej Akademii Nauk. Na Wydziale kształci się około 4 000 studentów na kierunkach: informatyka, elektronika i telekomunikacja, inżynieria biomedyczna, automatyka i robotyka oraz inżynieria danych, na studiach I i II stopnia oraz studiach doktoranckich." +
		"ydział należy do najlepszych jednostek akademickich w Polsce, od roku 1992 utrzymuje kategorię naukową A. Działalność naukowa Wydziału obejmuje szeroki zakres nowoczesnych technologii informacyjnych i komunikacyjnych. Wydział ma pełne prawa akademickie w dyscyplinach: informatyka, elektronika i telekomunikacja, a ponadto prawa doktoryzowania w dyscyplinach: biocybernetyka i inżynieria biomedyczna oraz automatyka i robotyka. W 2017 uzyskał akredytację w najwyższej kategorii A+, nadaną przez Komitet Ewaluacji Jednostek Naukowych");
		buildingDescription.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");

		new PictureDownloader().execute();
	}

	public void changeActivity(View view) {
		Intent intent = new Intent(this, FacultyDetailsActivity.class);
		intent.putExtra("TAG", building.getFacultiesIds().get(0));
		startActivity(intent);
	}

	public void setPicture(String picture) {
		ImageView imageView = findViewById(R.id.buildingImageView);
		building.setPicture(picture);
		byte[] decodedString = Base64.decode(building.getPicture(), Base64.DEFAULT);
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
