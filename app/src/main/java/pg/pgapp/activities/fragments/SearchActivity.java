package pg.pgapp.activities.fragments;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

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

public class SearchActivity extends ListActivity {

	ArrayAdapter<SpannableString> adapter;
	ArrayList<Long> buildingsIds = new ArrayList<>();
	ArrayList<Long> facultiesIds = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		adapter = new ArrayAdapter<>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (position < facultiesIds.size()) {
			Context context = getApplicationContext();

			if (position <= buildingsIds.size()) {
				Intent intent = new Intent(context, BuildingDetailsActivity.class);
				intent.putExtra("TAG", buildingsIds.get(position).toString());
				context.startActivity(intent);
			} else {
				Intent intent = new Intent(context, FacultyDetailsActivity.class);
				intent.putExtra("TAG", facultiesIds.get(position - buildingsIds.size()).toString());
				context.startActivity(intent);
			}
		}
	}

	public void getSearchResult(View view) {
		EditText editText = findViewById(R.id.search_input);
		ArrayList<Building> buildings = new DatabaseConnector().getBuildingModels(editText.getText().toString());
		ArrayList<Faculty> faculties = new DatabaseConnector().getFacultyModels(editText.getText().toString());
		ArrayList<Department> departments = new DatabaseConnector().getDepartmentModels(editText.getText().toString());
		adapter.clear();
		buildingsIds.clear();
		facultiesIds.clear();

		buildings.forEach(building -> {
			buildingsIds.add(building.getId());
			adapter.add(getColorfulString(building.getTag(), building.getName()));
		});
		faculties.forEach(faculty -> {
			facultiesIds.add(faculty.getId());
			adapter.add(getColorfulString(faculty.getTag(), faculty.getName()));
		});
		departments.forEach(department -> {
			adapter.add(getColorfulString(department.getTag(), department.getName()));
		});
	}

	private SpannableString getColorfulString(Tag tag, String name) {
		SpannableString str = new SpannableString(tag + " " + name);
		str.setSpan(new ForegroundColorSpan(BaseModel.getModelColor(tag)), 0, tag.toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		str.setSpan(new ForegroundColorSpan(0xFF000000), tag.toString().length() + 1, tag.toString().length() + name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return str;
	}
}
