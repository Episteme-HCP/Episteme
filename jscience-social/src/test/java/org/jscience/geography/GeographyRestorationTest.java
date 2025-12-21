/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.geography;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class GeographyRestorationTest {

    @Test
    public void testGeoPath() {
        Coordinate c1 = new Coordinate(0.0, 0.0);
        Coordinate c2 = new Coordinate(0.0, 1.0); // 1 degree longitude difference at equator
        // 1 degree at equator is approx 111.32 km

        GeoPath path = new GeoPath(Arrays.asList(c1, c2));

        assertFalse(path.isClosed());
        assertEquals(2, path.getCoordinates().size());

        // Tolerance for Haversine calculation
        assertEquals(111319.0, path.getLength().doubleValue(), 1000.0); // Within 1km
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
        assertTrue(bottomRight.getLatitude().doubleValue() < topLeft.getLatitude().doubleValue()); // Should be south
        assertTrue(bottomRight.getLongitude().doubleValue() > topLeft.getLongitude().doubleValue()); // Should be east
    }
}
