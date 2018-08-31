package pg.pgapp.Models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuildingModel extends BaseModel {

    public List<String> faculties;
    public String picture;
    public String description;

    public BuildingModel(String name, String tag, List<String> faculties, String picture, String description) {
        super(name, tag);
        this.faculties = faculties;
        this.picture = picture;
        this.description = description;
    }
}
