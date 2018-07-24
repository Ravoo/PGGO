package pg.pgapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;


public class BuildingDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_details);

        initializeDeatilView();
    }

    public void initializeDeatilView()
    {
        ImageView imageView = (ImageView)findViewById(R.id.buildingImageView);
        TextView buildingNameTextView = (TextView)findViewById(R.id.buildingName);
        TextView buildingFacultyTextView = (TextView)findViewById(R.id.buildingFacultyName);
        TextView buildingDescription = (TextView)findViewById(R.id.buildingDescription);
        Intent intent = getIntent();
        String tag = intent.getStringExtra("TAG");

        Gson gson = new Gson();
        BuildingModel bm = gson.fromJson(readBuildingData(tag),BuildingModel.class);

        buildingNameTextView.setText(bm.name);
        buildingFacultyTextView.setText(bm.faculty);
        buildingDescription.setText(bm.description);
        imageView.setImageBitmap(getBuildingBitmap(bm.picture));
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

    public Bitmap getBuildingBitmap(String bitmapFileName)
    {
        InputStream istr = null;
        try {
            istr = getAssets().open(bitmapFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
}
