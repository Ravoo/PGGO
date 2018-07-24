package pg.pgapp;
/**
 * Created by Ravo on 24.07.2018.
 */

public class BuildingDisplay {
    public String tag;
    public double latitude;
    public double longitude;
    public float height;
    public float width;
    public String picture;

    public BuildingDisplay(String tag, double latitude, double longitude, float height, float width, String picture)
    {
        this.tag = tag;
        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
        this.width = width;
        this.picture = picture;
    }

}
