package pg.pgapp;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Ravo on 24.07.2018.
 */
@AllArgsConstructor
@Getter
@Setter
public class BuildingDisplay {
    private String tag;
    private List<Coordinate> coordinates;
}
