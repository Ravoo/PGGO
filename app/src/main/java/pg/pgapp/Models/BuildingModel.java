package pg.pgapp.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuildingModel extends BaseModel {
    // todo change to List<Faculty>
    private List<Long> faculties;
    private String picture;
    private String description;

    public BuildingModel(long id, String name, String tag, List<Long> faculties, String picture, String description) {
        super(id, name, tag);
        this.faculties = faculties;
        this.picture = picture;
        this.description = description;
    }
}
