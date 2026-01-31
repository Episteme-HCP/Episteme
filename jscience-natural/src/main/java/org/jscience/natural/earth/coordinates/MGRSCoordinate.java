/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.natural.earth.coordinates;

import java.io.Serializable;

/**
 * Military Grid Reference System (MGRS) coordinate.
 * Derived from UTM/UPS systems.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class MGRSCoordinate implements EarthCoordinate, Serializable {

    private static final long serialVersionUID = 1L;

    private final String zone; // e.g. "31T"
    private final String gridSquare; // e.g. "EN"
    private final int easting; // Meters
    private final int northing; // Meters
    private final int precision; // number of digits used for easting/northing

    public MGRSCoordinate(String zone, String gridSquare, int easting, int northing, int precision) {
        this.zone = zone;
        this.gridSquare = gridSquare;
        this.easting = easting;
        this.northing = northing;
        this.precision = precision;
    }

    public String getZone() { return zone; }
    public String getGridSquare() { return gridSquare; }
    public int getEasting() { return easting; }
    public int getNorthing() { return northing; }
    public int getPrecision() { return precision; }

    @Override
    public String getCoordinateSystem() { return "MGRS"; }

    @Override
    public ReferenceEllipsoid getEllipsoid() { return ReferenceEllipsoid.WGS84; }

    @Override
    public GeodeticCoordinate toGeodetic() {
        // Convert MGRS back to UTM first
        UTMCoordinate utm = toUTM();
        return utm.toGeodetic();
    }

    @Override
    public ECEFCoordinate toECEF() {
        return toGeodetic().toECEF();
    }

    /**
     * Converts MGRS back to UTM.
     */
    public UTMCoordinate toUTM() {
        int zoneNumber = Integer.parseInt(zone.substring(0, zone.length() - 1));
        char zoneLetter = zone.charAt(zone.length() - 1);
        
        // Decode grid square back to 100km squares
        int col = getColFromGridSquare(zoneNumber);
        int row = getRowFromGridSquare(zoneNumber);
        
        double utmEasting = col * 100000.0 + easting;
        double utmNorthing = row * 100000.0 + northing;
        
        return new UTMCoordinate(zoneNumber, zoneLetter, utmEasting, utmNorthing);
    }

    private int getColFromGridSquare(int zone) {
        String cols135 = "ABCDEFGH";
        String cols246 = "JKLMNPQR";
        String cols = (zone % 3 == 1) ? cols135 : (zone % 3 == 2 ? cols246 : "STUVWXYZ");
        return cols.indexOf(gridSquare.charAt(0)) + 1;
    }

    private int getRowFromGridSquare(int zone) {
        String rowsOdd = "ABCDEFGHJKLMNPQRSTUV";
        String rowsEven = "FGHJKLMNPQRSTUVABCDE";
        String rows = (zone % 2 == 0) ? rowsEven : rowsOdd;
        return rows.indexOf(gridSquare.charAt(1));
    }

    /**
     * Converts UTM to MGRS.
     */
    public static MGRSCoordinate fromUTM(UTMCoordinate utm) {
        int utmZone = utm.getZoneNumber();
        char latBand = utm.getZoneLetter();
        double e = utm.getEasting().getValue().doubleValue();
        double n = utm.getNorthing().getValue().doubleValue();

        String zoneStr = utmZone + String.valueOf(latBand);
        
        // MGRS grid square identification
        int col = (int) (e / 100000);
        int row = (int) (n / 100000) % 20;
        
        String gridSquareStr = getGridSquare(utmZone, col, row);
        
        int precision = 5; // standard 1m precision
        int eInt = (int) (e % 100000);
        int nInt = (int) (n % 100000);

        return new MGRSCoordinate(zoneStr, gridSquareStr, eInt, nInt, precision);
    }

    private static String getGridSquare(int zone, int col, int row) {
        String cols135 = "ABCDEFGH";
        String cols246 = "JKLMNPQR";
        String cols = (zone % 3 == 1) ? cols135 : (zone % 3 == 2 ? cols246 : "STUVWXYZ");
        
        String rowsOdd = "ABCDEFGHJKLMNPQRSTUV";
        String rowsEven = "FGHJKLMNPQRSTUVABCDE";
        String rows = (zone % 2 == 0) ? rowsEven : rowsOdd;
        
        return String.valueOf(cols.charAt(col - 1)) + String.valueOf(rows.charAt(row));
    }

    @Override
    public String toString() {
        String format = "%0" + precision + "d";
        return zone + gridSquare + String.format(format, easting) + String.format(format, northing);
    }
}

