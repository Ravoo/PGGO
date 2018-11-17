package pg.pgapp.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Building extends BaseModel {
	private List<Long> facultiesIds;
	private List<String> facultiesNames;
	private long buildingDisplayId;
	@Setter
	private String picture;
	private String description;

	public Building(long id, String name, Tag tag, List<Long> facultiesIds, List<String> facultiesNames, String picture, String description, long buildingDisplayId) {
		super(id, name, tag);
		this.facultiesIds = facultiesIds;
		this.facultiesNames = facultiesNames;
		this.picture = picture;
		this.description = description;
		this.buildingDisplayId = buildingDisplayId;
	}
}
