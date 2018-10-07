package pg.pgapp.database;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pg.pgapp.models.BaseModel;
import pg.pgapp.models.Building;
import pg.pgapp.models.BuildingDisplay;
import pg.pgapp.models.Department;
import pg.pgapp.models.Faculty;

public class DatabaseConnector {

	// todo change to server ip:port
	private static String URL = "http://192.168.137.1:8080/";
	private static String BUILDING_PATH = "building/";
	private static String BUILDING_DISPLAY_PATH = "building/display/";
	private static String FACULTY_PATH = "faculty/";
	private static String DEPARTMENT_PATH = "department/";


	private String getModel(String path, Long id) {
		URL url;
		HttpURLConnection urlConnection;
		String serverResponse = "";
		try {
			if (id == 0) {
				url = new URL(URL + path);
			} else {
				url = new URL(URL + path + id);
			}
			urlConnection = (HttpURLConnection) url.openConnection();

			int responseCode = urlConnection.getResponseCode();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				serverResponse = readStream(urlConnection.getInputStream());
				Log.v("GET returned", serverResponse);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return serverResponse;
	}

	private String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuilder response = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response.toString();
	}

	private Building getDatabaseBuildingModel(Long id) {
		return new Gson().fromJson(getModel(BUILDING_PATH, id), Building.class);
	}

	private Faculty getDatabaseFacultyModel(Long id) {
		return new Gson().fromJson(getModel(FACULTY_PATH, id), Faculty.class);
	}

	private Department getDatabaseDepartmentModel(Long id) {
		return new Gson().fromJson(getModel(DEPARTMENT_PATH, id), Department.class);
	}

	private ArrayList<Building> getDatabaseBuildingDisplaysModels(Long id) {
		return new Gson().fromJson(getModel(BUILDING_DISPLAY_PATH, id), new TypeToken<ArrayList<BuildingDisplay>>() {
		}.getType());
	}

	private ArrayList<Building> getDatabaseBuildingModels(Long id) {
		return new Gson().fromJson(getModel(BUILDING_PATH, id), new TypeToken<ArrayList<Building>>() {
		}.getType());
	}

	private ArrayList<Faculty> getDatabaseFacultyModels(Long id) {
		return new Gson().fromJson(getModel(FACULTY_PATH, id), new TypeToken<ArrayList<Faculty>>() {
		}.getType());
	}

	private ArrayList<Department> getDatabaseDepartmentModels(Long id) {
		return new Gson().fromJson(getModel(DEPARTMENT_PATH, id), new TypeToken<ArrayList<Department>>() {
		}.getType());
	}

	private BaseModel getModel(Long id, ModelType modelType) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<BaseModel> result = executorService.submit(() -> {
			switch (modelType) {
				case BUILDING:
					return getDatabaseBuildingModel(id);
				case FACULTY:
					return getDatabaseFacultyModel(id);
				case DEPARTMENT:
					return getDatabaseDepartmentModel(id);
				default:
					return null;
			}
		});

		BaseModel model = null;
		try {
			model = result.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		executorService.shutdown();

		return model;
	}

	public Building getBuildingModel(Long id) {
		return (Building) getModel(id, ModelType.BUILDING);
	}

	public Faculty getFacultyModel(Long id) {
		return (Faculty) getModel(id, ModelType.FACULTY);
	}

	public Department getDepartmentModel(Long id) {
		return (Department) getModel(id, ModelType.DEPARTMENT);
	}

	private ArrayList<? extends BaseModel> getModels(Long id, ModelType modelType) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<ArrayList<? extends BaseModel>> result = executorService.submit(() -> {
			switch (modelType) {
				case BUILDING_DISPLAY:
					return getDatabaseBuildingDisplaysModels(id);
				case BUILDING:
					return getDatabaseBuildingModels(id);
				case FACULTY:
					return getDatabaseFacultyModels(id);
				case DEPARTMENT:
					return getDatabaseDepartmentModels(id);
				default:
					return null;
			}
		});

		ArrayList<? extends BaseModel> model = null;
		try {
			model = result.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		executorService.shutdown();

		return model;
	}

	public ArrayList<BuildingDisplay> getBuildingDisplays(Long id) {
		return (ArrayList<BuildingDisplay>) getModels(id, ModelType.BUILDING_DISPLAY);
	}

	public ArrayList<Building> getBuildingModels(Long id) {
		return (ArrayList<Building>) getModels(id, ModelType.BUILDING);
	}

	public ArrayList<Faculty> getFacultyModels(Long id) {
		return (ArrayList<Faculty>) getModels(id, ModelType.FACULTY);
	}

	public ArrayList<Department> getDepartmentModels(Long id) {
		return (ArrayList<Department>) getModels(id, ModelType.DEPARTMENT);
	}
}
