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

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.Locale;
import java.util.Random;

import pg.pgapp.R;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Building;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;


public class BuildingDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    int showcaseItem = 0;
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
        ShowCase();
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
		buildingAddressTextView.setText("Narutowicza 11/12, 80-233 Gdańsk");

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



//		if(savedInstanceState != null) {
//            byte[] pic = savedInstanceState.getByteArray("pic");
//            serializedPicture = pic;
//            Bitmap decodedByte = BitmapFactory.decodeByteArray(pic, 0, pic.length);
//            imageView.setImageBitmap(decodedByte);
//            imageView.setVisibility(View.VISIBLE);
//            ProgressBar progressBar = findViewById(R.id.progressBar1);
//            progressBar.setVisibility(View.GONE);
//        }else
//        {
	       new PictureDownloader().execute();
//        }

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
    ShowcaseView showcaseView;
	private void ShowCase()
    {
        showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setOnClickListener(this)
                .setContentTitle("Detale budynku")
                .setContentText("Oto podstrona z detalami budynku")
				.setStyle(R.style.CustomShowcaseTheme2)
                .build();

        showcaseView.setButtonText("Ok!");
    }

    //Żeby nie trzeba było klikać "OK", teraz wystarczy tapnięcie w ekran i showcase przeskakuje na kolejny element
    @Override
    public void onClick(View v) {

        switch (showcaseItem)
        {
            case 0:
                showcaseView.setShowcase(new ViewTarget(imageView),true);
                showcaseView.setContentTitle("Obrazek budynku");
                showcaseView.setContentText("Wygląd opisywanego budynku");
                break;
            case 1:
                showcaseView.setShowcase(new ViewTarget(buildingNameTextView),true);
                showcaseView.setContentTitle("Nazwa budynku");
                showcaseView.setContentText("Nazwa opisywanego budynku");
                break;
            case 2:
                showcaseView.setShowcase(new ViewTarget(buildingFacultyTextView),true);
                showcaseView.setContentTitle("Nazwa wydziału");
                showcaseView.setContentText("Nazwa wydziału, do którego należy budynek. Kliknij w nią aby dowiedzieć się więcej");
                break;
            case 3:
                showcaseView.setShowcase(new ViewTarget(buildingDescription),true);
                showcaseView.setContentTitle("Opis");
                showcaseView.setContentText("Krótki opis budynku");
                break;
            case 4:
                showcaseView.hide();
                break;
        }
        showcaseItem++;
    }


}
