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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pg.pgapp.models.BaseModel;
import pg.pgapp.models.Building;
import pg.pgapp.models.BuildingDisplay;
import pg.pgapp.models.Department;
import pg.pgapp.models.Faculty;
import pg.pgapp.models.ModelType;
import pg.pgapp.models.POI;

public class DatabaseConnector {

	//private static String URL = "http://40.121.44.25:8080/";
	private static String URL = "http://192.168.137.1:8080/";
	
	private static String BUILDING_PATH = "building/";
	private static String BUILDING_DISPLAY_PATH = "building/display/";
	private static String BUILDING_PICTURE_PATH = "building/picture/";
	private static String FACULTY_PATH = "faculty/";
	private static String DEPARTMENT_PATH = "department/";
	private static String POI_PATH = "poi/";

	private String getModel(String path, Long id) {
		URL url;
		HttpURLConnection urlConnection;
		String serverResponse = "";
		try {
			url = new URL(URL + path + id);
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

	private String getModels(String path, String name) {
		URL url;
		HttpURLConnection urlConnection;
		String serverResponse = "";
		try {
			url = new URL(URL + path + "?name=" + name);
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

	private BuildingDisplay getDatabaseBuildingDisplayModel(Long id) {
		return new Gson().fromJson(getModel(BUILDING_DISPLAY_PATH, id), BuildingDisplay.class);
	}

	private Faculty getDatabaseFacultyModel(Long id) {
		return new Gson().fromJson(getModel(FACULTY_PATH, id), Faculty.class);
	}

	private Department getDatabaseDepartmentModel(Long id) {
		return new Gson().fromJson(getModel(DEPARTMENT_PATH, id), Department.class);
	}

	private ArrayList<Building> getDatabaseBuildingDisplaysModels(String name) {
		return new Gson().fromJson(getModels(BUILDING_DISPLAY_PATH, name), new TypeToken<ArrayList<BuildingDisplay>>() {
		}.getType());
	}

	private ArrayList<Building> getDatabaseBuildingModels(String name) {
		return new Gson().fromJson(getModels(BUILDING_PATH, name), new TypeToken<ArrayList<Building>>() {
		}.getType());
	}

	private ArrayList<Faculty> getDatabaseFacultyModels(String name) {
		return new Gson().fromJson(getModels(FACULTY_PATH, name), new TypeToken<ArrayList<Faculty>>() {
		}.getType());
	}

	private ArrayList<Department> getDatabaseDepartmentModels(String name) {
		return new Gson().fromJson(getModels(DEPARTMENT_PATH, name), new TypeToken<ArrayList<Department>>() {
		}.getType());
	}

	private ArrayList<POI> getDatabasePOIModels(String name) {
		return new Gson().fromJson(getModels(POI_PATH, name), new TypeToken<ArrayList<POI>>() {
		}.getType());
	}

	private BaseModel getModel(Long id, ModelType modelType) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<BaseModel> result = executorService.submit(() -> {
			switch (modelType) {
				case BUILDING_DISPLAY:
					return getDatabaseBuildingDisplayModel(id);
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

	public BuildingDisplay getBuildingDisplayModel(Long id) {
		return (BuildingDisplay) getModel(id, ModelType.BUILDING_DISPLAY);
	}

	public String getBuildingPicture(Long id) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<String> result = executorService.submit(() -> getModel(BUILDING_PICTURE_PATH, id));
		try {
			return result.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Faculty getFacultyModel(Long id) {
		return (Faculty) getModel(id, ModelType.FACULTY);
	}

	public Department getDepartmentModel(Long id) {
		return (Department) getModel(id, ModelType.DEPARTMENT);
	}

	private ArrayList<? extends BaseModel> getModels(String name, ModelType modelType) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		Future<ArrayList<? extends BaseModel>> result = executorService.submit(() -> {
			switch (modelType) {
				case BUILDING_DISPLAY:
					return getDatabaseBuildingDisplaysModels(name);
				case BUILDING:
					return getDatabaseBuildingModels(name);
				case FACULTY:
					return getDatabaseFacultyModels(name);
				case DEPARTMENT:
					return getDatabaseDepartmentModels(name);
				case POI:
					return getDatabasePOIModels(name);
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

	public ArrayList<BuildingDisplay> getBuildingDisplays() {
		return (ArrayList<BuildingDisplay>) getModels("", ModelType.BUILDING_DISPLAY);
	}

	public ArrayList<POI> getPOIs() {
		return (ArrayList<POI>) getModels("", ModelType.POI);
	}


	public ArrayList<Building> getBuildingModels(String name) {
		return (ArrayList<Building>) getModels(name, ModelType.BUILDING);
	}

	public ArrayList<Faculty> getFacultyModels(String name) {
		return (ArrayList<Faculty>) getModels(name, ModelType.FACULTY);
	}

	public ArrayList<Department> getDepartmentModels(String name) {
		return (ArrayList<Department>) getModels(name, ModelType.DEPARTMENT);
	}
}
