package pg.pgapp.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

import pg.pgapp.Database.DatabaseExtractor;
import pg.pgapp.Models.BuildingModel;
import pg.pgapp.R;


public class BuildingDetailsActivity extends AppCompatActivity {

    BuildingModel buildingModel; //do change activity

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

        // todo remove when database configured
        if (new DatabaseExtractor().isDatabaseReady()) {
            Gson gson = new Gson();
            buildingModel = gson.fromJson(readBuildingData(tag), BuildingModel.class);
        } else {
            buildingModel = new DatabaseExtractor().getBuildingModel(tag);
        }
        buildingNameTextView.setText(buildingModel.getName());
        buildingFacultyTextView.setText(buildingModel.getFaculty());
        buildingDescription.setText(buildingModel.getDescription());
        imageView.setImageBitmap(BitmapFactory.decodeStream(buildingModel.getPicture()));
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(this, FacultyDetailsActivity.class);
        intent.putExtra("TAG", buildingModel.getTag());
        startActivity(intent);
    }

    public String readBuildingData(String tag)
    {
        String json = null;
        String filename = tag + ".json";
        InputStream inputStream = null;
        try {
            inputStream = this.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
