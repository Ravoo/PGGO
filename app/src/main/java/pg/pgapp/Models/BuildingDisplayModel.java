package pg.pgapp.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pg.pgapp.Coordinate;

/**
 * Created by Ravo on 24.07.2018.
 */
@AllArgsConstructor
@Getter
@Setter
public class BuildingDisplayModel extends BaseModel {
    private Long buildingId;
    private List<Coordinate> coordinates;
}
