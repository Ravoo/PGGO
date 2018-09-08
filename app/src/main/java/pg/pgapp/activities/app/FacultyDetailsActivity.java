package pg.pgapp.activities.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import pg.pgapp.R;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Faculty;

public class FacultyDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faculty_details);
		spinner = findViewById(R.id.departmentsSpiner);
		initializeDetailView();

	}

	private void initializeDetailView() {
		TextView facultyNameTextView = findViewById(R.id.facultyNameTextView);
		Intent intent = getIntent();
		Long tag = intent.getLongExtra("TAG", 2);

		Faculty faculty = new DatabaseConnector().getFacultyModel(tag);
		facultyNameTextView.setText(faculty.getName());

		ArrayList<String> departmentsForSpinner = new ArrayList<>(faculty.getDepartmentsNames());
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, departmentsForSpinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
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
