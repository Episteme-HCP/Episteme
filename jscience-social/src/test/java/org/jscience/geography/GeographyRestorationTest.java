package org.jscience.geography;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class GeographyRestorationTest {

    @Test
    public void testGeoPath() {
        Coordinate c1 = new Coordinate(0, 0);
        Coordinate c2 = new Coordinate(0, 1); // 1 degree longitude difference at equator
        // 1 degree at equator is approx 111.32 km

        GeoPath path = new GeoPath(Arrays.asList(c1, c2));

        assertFalse(path.isClosed());
        assertEquals(2, path.getCoordinates().size());

        // Tolerance for Haversine calculation
        assertEquals(111319.0, path.getLength(), 1000.0); // Within 1km
    }

    @Test
    public void testGeoMap() {
        Coordinate topLeft = new Coordinate(51.5, -0.1); // Near London
        GeoMap map = new GeoMap("London Area", topLeft, 10000, 10000); // 10km x 10km

        Place hydepark = new Place("Hyde Park", Place.Type.PARK);
        map.addPlace(hydepark);

        assertTrue(map.getPlaces().contains(hydepark));

        Coordinate bottomRight = map.getBottomRightCoordinate();
        assertNotNull(bottomRight);
        assertTrue(bottomRight.getLatitude() < topLeft.getLatitude()); // Should be south
        assertTrue(bottomRight.getLongitude() > topLeft.getLongitude()); // Should be east
    }
}
