package pg.pgapp.activities.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import pg.pgapp.R;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Department;
import pg.pgapp.models.Faculty;

public class FacultyDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
	Faculty faculty;
	Department department;
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
		TextView readMoreTextView = findViewById(R.id.readMoreTextView);
		readMoreTextView.setClickable(true);
		readMoreTextView.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://eti.pg.edu.pl/katedra-inteligentnych-systemow-interaktywnych/o-katedrze'> Czytaj więcej... </a>";
		//String text = "<a href='" + department.getPageUrl() + "'> Czytaj więcej... </a>";
		readMoreTextView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));

		Intent intent = getIntent();
		String tag = intent.getStringExtra("TAG");

		faculty = new DatabaseConnector().getFacultyModel(Long.parseLong(tag));
		facultyNameTextView.setText(faculty.getName());


		ArrayList<String> departmentsForSpinner = new ArrayList<>(faculty.getDepartmentsNames());
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, departmentsForSpinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		TextView departmentDescriptionTextView = findViewById(R.id.departmentDescription);
		//TextView selectedTV = findViewById(R.id.selectedTextView);
		//selectedTV.setText(spinner.getItemAtPosition(position).toString());
		department = new DatabaseConnector().getDepartmentModel(faculty.getDepartmentsIds().get(position));

		departmentDescriptionTextView.setText(department.getDescription());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
