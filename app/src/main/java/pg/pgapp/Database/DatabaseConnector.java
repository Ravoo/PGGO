package pg.pgapp.Database;

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

import pg.pgapp.Models.BaseModel;
import pg.pgapp.Models.BuildingModel;
import pg.pgapp.Models.DepartmentModel;
import pg.pgapp.Models.FacultyModel;

public class DatabaseConnector {

    private static String URL = "http://192.168.0.104:8080/";
    private static String BUILDING_PATH = "building/";
    private static String FACULTY_PATH = "faculty/";
    private static String DEPARTMENT_PATH = "department/";


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

    private BuildingModel getDatabaseBuildingModel(Long id) {
        return new Gson().fromJson(getModel(BUILDING_PATH, id), BuildingModel.class);
    }

    private FacultyModel getDatabaseFacultyModel(Long id) {
        return new Gson().fromJson(getModel(FACULTY_PATH, id), FacultyModel.class);
    }

    private DepartmentModel getDatabaseDepartmentModel(Long id) {
        return new Gson().fromJson(getModel(DEPARTMENT_PATH, id), DepartmentModel.class);
    }

    //todo check if Gson is getting content or trash for all modelS
    private ArrayList<BuildingModel> getDatabaseBuildingModels(Long id) {
        return new Gson().fromJson(getModel(BUILDING_PATH, id), new TypeToken<ArrayList<BuildingModel>>() {
        }.getType());
    }

    private ArrayList<FacultyModel> getDatabaseFacultyModels(Long id) {
        return new Gson().fromJson(getModel(FACULTY_PATH, id), new TypeToken<ArrayList<FacultyModel>>() {
        }.getType());
    }

    private ArrayList<DepartmentModel> getDatabaseDepartmentModels(Long id) {
        return new Gson().fromJson(getModel(DEPARTMENT_PATH, id), new TypeToken<ArrayList<DepartmentModel>>() {
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

    public BuildingModel getBuildingModel(Long id) {
        return (BuildingModel) getModel(id, ModelType.BUILDING);
    }

    public FacultyModel getFacultyModel(Long id) {
        return (FacultyModel) getModel(id, ModelType.FACULTY);
    }

    public DepartmentModel getDepartmentModel(Long id) {
        return (DepartmentModel) getModel(id, ModelType.DEPARTMENT);
    }

    private ArrayList<? extends BaseModel> getModels(Long id, ModelType modelType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<ArrayList<? extends BaseModel>> result = executorService.submit(() -> {
            switch (modelType) {
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

    private ArrayList<BuildingModel> getBuildingModels(Long id) {
        return (ArrayList<BuildingModel>) getModels(id, ModelType.BUILDING);
    }

    private ArrayList<FacultyModel> getFacultyModels(Long id) {
        return (ArrayList<FacultyModel>) getModels(id, ModelType.FACULTY);
    }

    private ArrayList<DepartmentModel> getDepartmentModels(Long id) {
        return (ArrayList<DepartmentModel>) getModels(id, ModelType.DEPARTMENT);
    }
}
