package pg.pgapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Department extends BaseModel {
	private String description;
	private long facultyId;

	public Department(long id, String name, String tag, String description, long facultyId) {
		super(id, name, tag);
		this.description = description;
		this.facultyId = facultyId;
	}
}
