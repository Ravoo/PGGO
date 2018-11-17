package pg.pgapp.activities.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;

import pg.pgapp.R;
import pg.pgapp.activities.app.BuildingDetailsActivity;
import pg.pgapp.activities.app.FacultyDetailsActivity;
import pg.pgapp.database.DatabaseConnector;
import pg.pgapp.models.BaseModel;
import pg.pgapp.models.Building;
import pg.pgapp.models.Department;
import pg.pgapp.models.Faculty;
import pg.pgapp.models.Tag;

public class SearchActivity extends ListActivity implements View.OnClickListener {

	int showcaseItem = 0;
	ShowcaseView showcaseView;
	ArrayAdapter<SpannableString> adapter;
	ArrayList<Long> buildingsIds = new ArrayList<>();
	ArrayList<Long> facultiesIds = new ArrayList<>();
	ArrayList<Long> departmentsIds = new ArrayList<>();
	EditText editText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		editText = findViewById(R.id.search_input);
		adapter = new ArrayAdapter<SpannableString>(this,
				android.R.layout.simple_list_item_1) {

			@NonNull
			@Override
			public View getView(int position, View convertView, @NonNull ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				if (position % 2 == 1) {
					view.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPgSecondary, null));
				} else {
					view.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPgPrimary, null));
				}

				return view;
			}
		};
		setListAdapter(adapter);
		ShowCase();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Context context = getApplicationContext();

		if (position < buildingsIds.size()) {
			Intent intent = new Intent(context, BuildingDetailsActivity.class);
			intent.putExtra("TAG", buildingsIds.get(position).toString());
			context.startActivity(intent);
		} else {
			Intent intent = new Intent(context, FacultyDetailsActivity.class);
			int departmentOffset = facultiesIds.size() + buildingsIds.size();
			if (position < departmentOffset) {
				intent.putExtra("TAG", facultiesIds.get(position - buildingsIds.size()).toString());
			} else {
				Log.v("asd", departmentsIds.get(position - (departmentOffset)).toString());
				intent.putExtra("TAG", departmentsIds.get(position - (departmentOffset)).toString());
			}
			context.startActivity(intent);
		}
	}

	public void getSearchResult(View view) {
		ArrayList<Building> buildings = new DatabaseConnector().getBuildingModels(editText.getText().toString());
		ArrayList<Faculty> faculties = new DatabaseConnector().getFacultyModels(editText.getText().toString());
		ArrayList<Department> departments = new DatabaseConnector().getDepartmentModels(editText.getText().toString());
		adapter.clear();
		buildingsIds.clear();
		facultiesIds.clear();

		buildings.forEach(building -> {
			buildingsIds.add(building.getId());
			adapter.add(getColorfulString(building, building.getTag(), building.getName()));
		});
		faculties.forEach(faculty -> {
			facultiesIds.add(faculty.getId());
			adapter.add(getColorfulString(faculty, faculty.getTag(), faculty.getName()));
		});
		departments.forEach(department -> {
			departmentsIds.add(department.getFacultyId());
			adapter.add(getColorfulString(department, department.getTag(), department.getName()));
		});
	}

	private SpannableString getColorfulString(BaseModel baseModel, Tag tag, String name) {
		SpannableString str = new SpannableString(tag + " " + name);
		str.setSpan(new ForegroundColorSpan(baseModel.getModelColor()), 0, tag.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new ForegroundColorSpan(0xFFFFFFFF), tag.toString().length() + 1, tag.toString().length() + name.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return str;
	}

	private void ShowCase()	{
		long singleShot = 21; //Dzięki tej liczbie showcase wykona się tylko raz, na zainstalowanie aplikacji

		showcaseView = new ShowcaseView.Builder(this).singleShot(singleShot)
				.setTarget(Target.NONE)
				.setOnClickListener(this)
				.setContentTitle("Wyszukiwarka budynków")
				.setContentText("Ta funkcjonalność pozwala na wyszukiwanie budynku po jego nazwie.")
				.setStyle(R.style.CustomShowcaseTheme2)
				.build();

		showcaseView.setButtonText("Ok!");
	}

	@Override
	public void onClick(View v) {
		switch (showcaseItem)
		{
			case 0:
				showcaseView.setShowcase(new ViewTarget(editText),true);
				showcaseView.setContentTitle("");
				showcaseView.setContentText("Wystarczy, że wpiszesz kawałek nazwy szukanego budynku");
				break;
			case 1:
				showcaseView.setShowcase(new ViewTarget(findViewById(R.id.search_button)),true);
				showcaseView.setContentText("I wciśniesz przycisk 'Szukaj'");
				break;
			case 2:
				showcaseView.hide();
				break;
		}
		showcaseItem++;
	}
}
