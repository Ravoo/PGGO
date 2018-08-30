package pg.pgapp.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FacultyModel extends BaseModel {
    private List<String> departmentsNames;

    public FacultyModel(long id, String name, String tag, List<String> departmentsNames) {
        super(id, name, tag);
        this.departmentsNames = departmentsNames;
    }
}
