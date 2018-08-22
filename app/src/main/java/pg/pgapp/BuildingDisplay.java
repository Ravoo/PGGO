package pg.pgapp;

import java.util.List;

import lombok.AllArgsConstructor;

/**
 * Created by Ravo on 24.07.2018.
 */
@AllArgsConstructor
public class BuildingDisplay {
    //do wyświetlania na mapie
    public String tag;
    public List<Double> latitudes;
    public List<Double> longitudes;
}
