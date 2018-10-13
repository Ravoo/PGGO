package pg.pgapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Department extends BaseModel {
	private String description;
	private String pageUrl;
	private long facultyId;

	public Department(long id, String name, Tag tag, String description, String pageUrl, long facultyId) {
		super(id, name, tag);
		this.description = description;
		this.pageUrl = pageUrl;
		this.facultyId = facultyId;
	}
}
