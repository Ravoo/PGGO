package pg.pgapp.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        buildingModel = new DatabaseConnector().getBuildingModel(Long.parseLong(tag));
        buildingNameTextView.setText(buildingModel.getName());

        StringBuilder facultiesNames = new StringBuilder();
        for (String facultyName : buildingModel.getFacultiesNames()) {
            facultiesNames
                    .append(facultyName)
                    .append("\n");
        }
        buildingFacultyTextView.setText(facultiesNames.toString());
        buildingDescription.setText(buildingModel.getDescription());

        byte[] decodedString = Base64.decode(buildingModel.getPicture(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(decodedByte);
    }

    public void changeActivity(View view) {
        Intent intent = new Intent(this, FacultyDetailsActivity.class);
        intent.putExtra("TAG", buildingModel.getFacultiesIds().get(0));
        startActivity(intent);
    }
}
