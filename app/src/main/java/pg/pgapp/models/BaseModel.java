package pg.pgapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class BaseModel {
	private long id;
	private String name;
	private Tag tag;

	public static int getModelColor(Tag tag) {
		switch (tag) {
			case ETI:
				return 0xFF00FF00;
			default:
				return 0xFFFFFF00;
		}
	}
}
