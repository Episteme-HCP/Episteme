package org.jscience.earth.coordinates;

/**
 * Universal Transverse Mercator (UTM) coordinate.
 * 
 * @author Silvere Martin-Michiellot
 * @since 5.0
 */
public class UTMCoordinate {

    private final int zoneNumber;
    private final char zoneLetter;
    private final double easting;
    private final double northing;

    public UTMCoordinate(int zoneNumber, char zoneLetter, double easting, double northing) {
        this.zoneNumber = zoneNumber;
        this.zoneLetter = zoneLetter;
        this.easting = easting;
        this.northing = northing;
    }

    public int getZoneNumber() {
        return zoneNumber;
    }

    public char getZoneLetter() {
        return zoneLetter;
    }

    public double getEasting() {
        return easting;
    }

    public double getNorthing() {
        return northing;
    }
}
