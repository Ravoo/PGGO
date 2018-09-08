package pg.pgapp.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Building extends BaseModel {
	private List<Long> facultiesIds;
	private List<String> facultiesNames;
	private String picture;
	private String description;

	public Building(long id, String name, String tag, List<Long> facultiesIds, List<String> facultiesNames, String picture, String description) {
		super(id, name, tag);
		this.facultiesIds = facultiesIds;
		this.facultiesNames = facultiesNames;
		this.picture = picture;
		this.description = description;
	}
}
