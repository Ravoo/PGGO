package pg.pgapp.Database;

import android.util.Log;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pg.pgapp.Models.BaseModel;
import pg.pgapp.Models.BuildingModel;
import pg.pgapp.Models.DepartmentModel;
import pg.pgapp.Models.FacultyModel;

public class DatabaseExtractor {

    static final String USER = "root";
    static final String PASSWORD = "root";
    // todo change to server address
    private static String DB_PATH = "jdbc:mysql://192.168.0.103:3306/pggo";

    private Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(DB_PATH, USER, PASSWORD);
        } catch (Exception e) {
            Log.e("DATABASE", "Database could not be reached");
            e.printStackTrace();
        }
        return conn;
    }

    public boolean isDatabaseReady() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Connection> result = executorService.submit(this::connect);

        try {
            return result.get() == null;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    private BuildingModel getDatabaseBuildingModel(String tag) {
        String name = "", faculty = "", description = "";
        InputStream picture = null;
        try {
            String query = String.format("SELECT * FROM building_models WHERE tag='%s';", tag);
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                name = resultSet.getString("name");
                faculty = resultSet.getString("faculty");
                picture = resultSet.getBinaryStream("picture");
                description = resultSet.getString("description");
            }
            statement.close();
        } catch (Exception e) {
            Log.d("MySQLConnection", e.getMessage());
        }
        // empty FacultyModel - next click/database access will get it
        return new BuildingModel(name, tag, new FacultyModel(new ArrayList<>()), faculty, picture, description);
    }

    private FacultyModel getDatabaseFacultyModel(String tag) {
        String name = "";
        ArrayList<DepartmentModel> departmentModels = new ArrayList<>();
        try {
            String query = String.format("SELECT * FROM faculty_models WHERE tag='%s';", tag);
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                name = resultSet.getString("name");
            }
            statement.close();
            departmentModels = getDepartmentModels(tag);
        } catch (Exception e) {
            Log.d("MySQLConnection", e.getMessage());
        }
        // empty FacultyModel - next click/database access will get it
        return new FacultyModel(name, tag, departmentModels);
    }

    private ArrayList<DepartmentModel> getDatabaseDepartmentModels(String tag) {
        String name, description;
        ArrayList<DepartmentModel> departmentModels = new ArrayList<>();

        try {
            String query = String.format("SELECT * FROM department_models WHERE tag='%s';", tag);
            Statement statement = connect().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                name = resultSet.getString("name");
                description = resultSet.getString("description");
                departmentModels.add(new DepartmentModel(name, tag, description));
            }
            statement.close();
        } catch (Exception e) {
            Log.d("MySQLConnection", e.getMessage());
        }
        // empty FacultyModel - next click/database access will get it
        return departmentModels;
    }

    // todo divide class into separate model controllers
    // MODEL

    private BaseModel getModel(String tag, ModelType modelType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<BaseModel> result = executorService.submit(() -> {
            switch (modelType) {
                case BUILDING:
                    return getDatabaseBuildingModel(tag);
                case FACULTY:
                    return getDatabaseFacultyModel(tag);
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

    public BuildingModel getBuildingModel(String tag) {
        return (BuildingModel) getModel(tag, ModelType.BUILDING);
    }

    public FacultyModel getFacultyModel(String tag) {
        return (FacultyModel) getModel(tag, ModelType.FACULTY);
    }

    public DepartmentModel getDepartmentModel(String tag) {
        return (DepartmentModel) getModel(tag, ModelType.DEPARTMENT);
    }

    // MODELS - PLURAL

    private ArrayList<? extends BaseModel> getModels(String tag, ModelType modelType) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<ArrayList<? extends BaseModel>> result = executorService.submit(() -> {
            switch (modelType) {
                case DEPARTMENT:
                    return getDatabaseDepartmentModels(tag);
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

    public ArrayList<DepartmentModel> getDepartmentModels(String tag) {
        return (ArrayList<DepartmentModel>) getModels(tag, ModelType.DEPARTMENT);
    }
}
