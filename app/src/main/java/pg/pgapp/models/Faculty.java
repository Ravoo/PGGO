package pg.pgapp.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Faculty extends BaseModel {
	private List<String> departmentsNames;

	public Faculty(long id, String name, Tag tag, List<String> departmentsNames) {
		super(id, name, tag);
		this.departmentsNames = departmentsNames;
	}
}
