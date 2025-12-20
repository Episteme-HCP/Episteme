package org.jscience.earth.geophysics;

/**
 * Represents a point on a tectonic plate with its associated velocity vector.
 */
public class PlateVector {

    private final String siteId;
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> latitude;
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> longitude;

    // Velocity components
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> northVelocity; // mm/yr usually
    private final org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> eastVelocity; // mm/yr usually
    // Up component usually ignored for 2D plate motion but could be added

    public PlateVector(String siteId,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> latitude,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> longitude,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> northVelocity,
            org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> eastVelocity) {
        this.siteId = siteId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.northVelocity = northVelocity;
        this.eastVelocity = eastVelocity;
    }

    public String getSiteId() {
        return siteId;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> getLatitude() {
        return latitude;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Angle> getLongitude() {
        return longitude;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> getNorthVelocity() {
        return northVelocity;
    }

    public org.jscience.measure.Quantity<org.jscience.measure.quantity.Velocity> getEastVelocity() {
        return eastVelocity;
    }

    @Override
    public String toString() {
        return String.format("Site: %s [%s, %s] Vel: N=%s E=%s", siteId, latitude, longitude, northVelocity,
                eastVelocity);
    }
}
