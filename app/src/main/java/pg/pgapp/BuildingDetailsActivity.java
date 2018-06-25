package pg.pgapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BuildingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        ImageView imageView = (ImageView)findViewById(R.id.buildingImageView);
        TextView buildingNameTextView = (TextView)findViewById(R.id.buildingNameView);
        TextView buildingFacultyTextView = (TextView)findViewById(R.id.buildingFacultyView);
        Intent intent = getIntent();
        String tag = intent.getStringExtra("TAG");
        if(tag.equals("ETI"))
        {
            imageView.setImageResource(R.drawable.eti_building);
            buildingNameTextView.setText("NOWE ETI");
            buildingFacultyTextView.setText("ELEKTRONIKI, TELEKOMUNIKACJI I INFORMATYKI");
        }
        else if(tag.equals("MECHANICZNY"))
        {
            imageView.setImageResource(R.drawable.mechaniczny_building);
            buildingNameTextView.setText("MECHANICZNY");
            buildingFacultyTextView.setText("MECHANICZNY");
        }
    }
}
