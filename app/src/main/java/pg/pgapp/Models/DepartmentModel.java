package pg.pgapp.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DepartmentModel extends BaseModel {
    public String description;

    public long facultyId;

    public DepartmentModel(String name, String tag, String description, long facultyId) {
        super(name, tag);
        this.description = description;
        this.facultyId = facultyId;
    }
}
