package pg.pgapp.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FacultyModel extends BaseModel {
    private List<DepartmentModel> departments;

    public FacultyModel(long id, String name, String tag, List<DepartmentModel> departments) {
        super(id, name, tag);
        this.departments = departments;
    }
}
