package pg.pgapp.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class BaseModel {
    private long id;
    private String name;
    private String tag;
}
