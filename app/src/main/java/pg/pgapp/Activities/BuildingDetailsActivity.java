package pg.pgapp.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import pg.pgapp.Database.DatabaseConnector;
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

        // todo find way to switch tag to id
        buildingModel = new DatabaseConnector().getBuildingModel(Long.parseLong(tag));
        buildingNameTextView.setText(buildingModel.getName());
        buildingFacultyTextView.setText(buildingModel.getFaculties().toString());
        buildingDescription.setText(buildingModel.getDescription());

        // todo check if works
        InputStream stream = new ByteArrayInputStream(buildingModel.getPicture().getBytes(StandardCharsets.UTF_8));
        imageView.setImageBitmap(BitmapFactory.decodeStream(stream));
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(this, FacultyDetailsActivity.class);
        intent.putExtra("TAG", buildingModel.getTag());
        startActivity(intent);
    }
}
