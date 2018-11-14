package pg.pgapp.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BuildingDisplay extends BaseModel {
	private Long buildingId;
	private List<Coordinate> coordinates;
	private Coordinate center;

	@Getter
	@Setter
	@RequiredArgsConstructor
	public class Coordinate {
		private final Double latitude;
		private final Double longitude;
	}
}
