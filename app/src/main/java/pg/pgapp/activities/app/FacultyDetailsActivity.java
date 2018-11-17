package pg.pgapp.activities.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.Locale;

import pg.pgapp.R;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.Department;
import pg.pgapp.models.Faculty;

public class FacultyDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

	int showcaseItem = 0;
	Faculty faculty;
	Department department;
	Spinner spinner;
    WebView departmentDescriptionTextView;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faculty_details);
		spinner = findViewById(R.id.departmentsSpiner);
		initializeDetailView();

		ShowCase();
	}

	private void initializeDetailView() {
		TextView facultyNameTextView = findViewById(R.id.facultyNameTextView);
		TextView readMoreTextView = findViewById(R.id.readMoreTextView);
		readMoreTextView.setClickable(true);
		readMoreTextView.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='https://eti.pg.edu.pl/katedra-inteligentnych-systemow-interaktywnych/o-katedrze'> Czytaj więcej... </a>";
		readMoreTextView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
		readMoreTextView.setTextColor(Color.WHITE);
		Intent intent = getIntent();
		String tag = intent.getStringExtra("TAG");

		faculty = new DatabaseConnector().getFacultyModel(Long.parseLong(tag));
		facultyNameTextView.setText(faculty.getName());
		facultyNameTextView.setTextColor(Color.WHITE);

		ArrayList<String> departmentsForSpinner = new ArrayList<>(faculty.getDepartmentsNames());
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, departmentsForSpinner);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		departmentDescriptionTextView = findViewById(R.id.departmentDescription);
		departmentDescriptionTextView.setBackgroundColor(getResources().getColor(R.color.colorPgSecondary));
		//TextView selectedTV = findViewById(R.id.selectedTextView);
		//selectedTV.setText(spinner.getItemAtPosition(position).toString());
		department = new DatabaseConnector().getDepartmentModel(faculty.getDepartmentsIds().get(position));
		String data = department.getDescription();
		String justifyTag = "<html><body style='text-align:justify;color:#FFFFFF'>%s</body></html>";
		String dataString = String.format(Locale.US, justifyTag,data);
		departmentDescriptionTextView.loadDataWithBaseURL("", dataString, "text/html", "UTF-8", "");
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

    ShowcaseView showcaseView;
	@Override
	public void onClick(View v) {
        switch (showcaseItem)
        {
            case 0:
                showcaseView.setShowcase(new ViewTarget(spinner),true);
                showcaseView.setContentTitle("");
                showcaseView.setContentText("Wybierz katedrę o której chcesz się dowiedzieć czegoś więcej");
                break;
            case 1:
                showcaseView.setShowcase(new ViewTarget(departmentDescriptionTextView),true);
                showcaseView.setContentText("W tym miejscu wyświetlą się informacje o wybranej katedrze");
                break;
            case 2:
                showcaseView.hide();
                break;
        }
        showcaseItem++;
	}

	private void ShowCase() {
        long singleShot = 20; //Dzięki tej liczbie showcase wykona się tylko raz, na zainstalowanie aplikacji

        showcaseView = new ShowcaseView.Builder(this).singleShot(singleShot)
                .setTarget(Target.NONE)
                .setOnClickListener(this)
                .setContentTitle("Informacje o katedrach")
                .setContentText("Na tej podstronie możesz dowiedzieć się więcej o katedrach wydziału, do którego należy budynek.")
                .setStyle(R.style.CustomShowcaseTheme2)
                .build();

        showcaseView.setButtonText("Ok!");
	}


}
