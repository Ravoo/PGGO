package pg.pgapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class POI extends BaseModel {
	private String description;
	private BuildingDisplay.Coordinate coordinate;
	private Type type;

	public POI(long id, String name, Tag tag, String description, BuildingDisplay.Coordinate coordinate) {
		super(id, name, tag);
		this.description = description;
		this.coordinate = coordinate;
	}

	@Override
	public int getModelColor() {
		switch (this.type) {
			case RESTAURANT:
				return 0xFF0033cc;
			case BIKE_PARK:
				return 0xFF009933;
			case ATM:
				return 0xFFcc00cc;
			case BUS_STOP:
				return 0xFFff9933;
			default:
				return 0xFF000000;
		}
	}
}
