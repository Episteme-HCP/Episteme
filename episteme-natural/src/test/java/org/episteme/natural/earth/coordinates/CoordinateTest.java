/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.natural.earth.coordinates;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.episteme.core.measure.Units;
import org.episteme.core.mathematics.numbers.real.Real;

public class CoordinateTest {

    @Test
    public void testGeodeticToECEFAndBack() {
        // Paris, France
        GeodeticCoordinate paris = new GeodeticCoordinate(48.8566, 2.3522, 35.0);
        
        ECEFCoordinate ecef = paris.toECEF();
        assertNotNull(ecef);
        
        GeodeticCoordinate back = ecef.toGeodetic();
        
        assertEquals(paris.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-4);
        assertEquals(paris.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-4);
        assertEquals(paris.getHeight().to(Units.METER).getValue().doubleValue(), 
                     back.getHeight().to(Units.METER).getValue().doubleValue(), 1.0);
    }

    @Test
    public void testGeodeticToUTMAndBack() {
        // New York City (UTM Zone 18T)
        GeodeticCoordinate nyc = new GeodeticCoordinate(40.7128, -74.0060, 0.0);
        
        UTMCoordinate utm = UTMCoordinate.fromGeodetic(nyc);
        assertEquals(18, utm.getZoneNumber());
        assertEquals('T', utm.getZoneLetter());
        
        GeodeticCoordinate back = utm.toGeodetic();
        assertEquals(nyc.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-4);
        assertEquals(nyc.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-4);
    }

    @Test
    public void testMGRS() {
        GeodeticCoordinate nyc = new GeodeticCoordinate(40.7128, -74.0060, 0.0);
        UTMCoordinate utm = UTMCoordinate.fromGeodetic(nyc);
        MGRSCoordinate mgrs = MGRSCoordinate.fromUTM(utm);
        
        assertNotNull(mgrs);
        assertTrue(mgrs.toString().startsWith("18T"));
    }

    @Test
    public void testLocalENU() {
        GeodeticCoordinate obs = new GeodeticCoordinate(48.8566, 2.3522, 0.0); // Paris
        GeodeticCoordinate target = new GeodeticCoordinate(48.8666, 2.3622, 100.0); // Slightly north-east and high
        
        ENUCoordinate enu = CoordinateConverter.toLocalENU(target, obs);
        
        assertTrue(enu.getEast().getValue().doubleValue() > 0);
        assertTrue(enu.getNorth().getValue().doubleValue() > 0);
        assertTrue(enu.getUp().getValue().doubleValue() > 0);
    }

    @Test
    public void testAlbersUSA() {
        // Washington DC
        GeodeticCoordinate dc = new GeodeticCoordinate(38.8951, -77.0364, 0.0);
        
        AlbersEqualAreaCoordinate albers = CoordinateConverter.toAlbers(dc, HistoricalGeodeticModels.USGS_USA_ALBERS);
        assertNotNull(albers);
        
        GeodeticCoordinate back = albers.toGeodetic();
        assertEquals(dc.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-4);
        assertEquals(dc.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-4);
    }

    @Test
    public void testCassiniUK() {
        // London
        GeodeticCoordinate london = new GeodeticCoordinate(51.5074, -0.1278, 0.0);
        
        CassiniSoldnerCoordinate cassini = CoordinateConverter.toCassini(london, HistoricalGeodeticModels.UK_OS_CASSINI);
        assertNotNull(cassini);
        
        GeodeticCoordinate back = cassini.toGeodetic();
        assertEquals(london.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-3);
        assertEquals(london.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-3);
    }

    @Test
    public void testPolyconic() {
        // Center of USA
        GeodeticCoordinate center = new GeodeticCoordinate(39.8283, -98.5795, 0.0);
        
        PolyconicCoordinate poly = CoordinateConverter.toPolyconic(center, HistoricalGeodeticModels.USGS_USA_POLYCONIC);
        assertNotNull(poly);
        
        GeodeticCoordinate back = poly.toGeodetic();
        assertEquals(center.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLatitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-2); // Iteration approx
        assertEquals(center.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 
                     back.getLongitude().to(Units.DEGREE_ANGLE).getValue().doubleValue(), 1e-2);
    }

    @Test
    public void testHistoricalEllipsoids() {
        ReferenceEllipsoid bessel = HistoricalGeodeticModels.getBessel1841();
        assertNotNull(bessel);
        assertEquals(6377397.155, bessel.getSemiMajorAxis().to(Units.METER).getValue().doubleValue(), 1e-3);
        
        ReferenceEllipsoid clarke = HistoricalGeodeticModels.getClarke1866();
        assertNotNull(clarke);
        assertEquals(6378206.4, clarke.getSemiMajorAxis().to(Units.METER).getValue().doubleValue(), 1e-3);
    }

    @Test
    public void testDatumTransformation() {
        // Test Helmert transformation (e.g. WGS84 to ED50 approx)
        ECEFCoordinate wgs84 = new ECEFCoordinate(Real.of(4000000.0), Real.of(500000.0), Real.of(4900000.0));
        DatumTransformation.HelmertParameters params = DatumTransformation.WGS84_TO_ED50;
        
        ECEFCoordinate ed50 = DatumTransformation.transform(wgs84, params);
        
        // Basic check for translation
        assertEquals(4000000.0 - 87, ed50.getX().to(Units.METER).getValue().doubleValue(), 1.0);
        assertEquals(500000.0 - 98, ed50.getY().to(Units.METER).getValue().doubleValue(), 1.0);
        assertEquals(4900000.0 - 121, ed50.getZ().to(Units.METER).getValue().doubleValue(), 1.0);
    }
}

