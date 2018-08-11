package pg.pgapp.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pg.pgapp.BuildingDisplay;
import pg.pgapp.Coordinate;

@RequiredArgsConstructor
public class DatabaseExtractor {
    private final SQLiteDatabase sqLiteDatabase;

    public ArrayList<BuildingDisplay> getBuildings() {
        ArrayList<BuildingDisplay> buildings = new ArrayList<>();
        setTags(buildings);
        setCoordinates(buildings);
        return buildings;
    }

    private void setTags(@NonNull ArrayList<BuildingDisplay> buildings) {
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from buildings;", null);
        if (cursor.moveToFirst()) {
            do {
                buildings.add(new BuildingDisplay(cursor.getString(1), new ArrayList<>()));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void setCoordinates(ArrayList<BuildingDisplay> buildings) {
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from coordinates;", null);
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                final Double longitude = cursor.getDouble(1);
                final Double latitude = cursor.getDouble(2);
                coordinates.add(new Coordinate(longitude, latitude));
                buildings.get(cursor.getInt(3)).setCoordinates(coordinates);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
