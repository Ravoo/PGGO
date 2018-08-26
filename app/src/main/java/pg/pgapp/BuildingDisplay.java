package pg.pgapp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pg.pgapp.Models.BuildingModel;

/**
 * Created by Ravo on 24.07.2018.
 */
@AllArgsConstructor
@Getter
@Setter
public class BuildingDisplay {
    private String tag;
    private BuildingModel building;
    private List<Coordinate> coordinates;
}
