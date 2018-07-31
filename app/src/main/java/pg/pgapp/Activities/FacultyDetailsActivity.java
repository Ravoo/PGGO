package pg.pgapp.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pg.pgapp.Models.BuildingModel;
import pg.pgapp.Models.FacultyModel;
import pg.pgapp.R;

public class FacultyDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);
        spinner = findViewById(R.id.departmentsSpiner);
        initializeDeatilView();

    }

    private void initializeDeatilView()
    {
        TextView facultyNameTextView = findViewById(R.id.facultyNameTextView);
        Intent intent = getIntent();
        String tag = intent.getStringExtra("TAG");
        Gson gson = new Gson();
        FacultyModel facultyModel = gson.fromJson(readDataFromfile(tag+"faculty.json"),FacultyModel.class);
        facultyNameTextView.setText(facultyModel.name);


        ArrayList<String> departmentsForSpinner = new ArrayList<String>();
        for(int i = 0; i< facultyModel.departments.size();i++)
        {
            departmentsForSpinner.add(facultyModel.departments.get(i).name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,departmentsForSpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public String readDataFromfile(String filename)
    {
        String json = null;
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open(filename);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        TextView selectedTV = findViewById(R.id.selectedTextView);
        selectedTV.setText(spinner.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
