package pg.pgapp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Coordinate {
    private final Double longitude;
    private final Double latitude;
}
