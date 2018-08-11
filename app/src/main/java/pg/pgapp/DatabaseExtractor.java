package pg.pgapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseExtractor {
    private final SQLiteDatabase sqLiteDatabase;

    public ArrayList<BuildingDisplay> getBuildings() {
        ArrayList<BuildingDisplay> buildings = new ArrayList<>();
        setTags(buildings);
        setPoints(buildings);
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

    private void setPoints(ArrayList<BuildingDisplay> buildings) {
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from points;", null);
        ArrayList<Point> points = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                final Double longitude = cursor.getDouble(1);
                final Double latitude = cursor.getDouble(2);
                points.add(new Point(longitude, latitude));
                // todo nadmiarowe dodawanie za kazdym razem punktow
                buildings.get(cursor.getInt(3)).setPoints(points);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
