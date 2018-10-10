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

	public int getModelColor() {
		switch (this.tag) {
			case ETI:
				return 0xFF0033cc;
			case ARCH:
				return 0xFFcc0066;
			case CHEM:
				return 0xFF009933;
			case EIA:
				return 0xFFcc00cc;
			case FTIMS:
				return 0xFF00cc99;
			case ILIS:
				return 0xFFff9933;
			case MECH:
				return 0xFF666699;
			case OIO:
				return 0xFF3366ff;
			case ZIE:
				return 0xFF66ffff;
			case DS:
				return 0xFF993333;
			case CSA:
				return 0xFF663300;
			case NANO:
				return 0xFF99ff33;
			case OTHER:
				return 0xFF999966;

			default:
				return 0xFF000000;
		}
	}
}
