package pg.pgapp.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DepartmentModel extends BaseModel {
    private String description;
    private long facultyId;

    public DepartmentModel(long id, String name, String tag, String description, long facultyId) {
        super(id, name, tag);
        this.description = description;
        this.facultyId = facultyId;
    }
}
