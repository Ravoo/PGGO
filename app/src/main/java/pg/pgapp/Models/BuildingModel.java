package pg.pgapp.Models;

import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BuildingModel extends BaseModel {
    // to pole moze nie jest juz potrzebne, bo i tak osobno potrzebujemy tego, co zawiera. Zawolamy po to, jak bedzie potrzebne
    public FacultyModel owner;
    public String faculty;
    public InputStream picture;
    public String description;

    public BuildingModel(String name, String tag, FacultyModel owner, String faculty, InputStream picture, String description) {
        super(name, tag);
        this.owner = owner;
        this.faculty = faculty;
        this.picture = picture;
        this.description = description;
    }
}
