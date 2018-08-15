package pg.pgapp.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FacultyModel extends BaseModel {
    public List<DepartmentModel> departments;

    public FacultyModel(String name, String tag, List<DepartmentModel> departments) {
        super(name, tag);
        this.departments = departments;
    }
}
