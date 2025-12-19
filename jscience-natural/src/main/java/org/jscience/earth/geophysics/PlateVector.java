package org.jscience.earth.geophysics;

import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Angle;
import org.jscience.measure.quantity.Velocity;

/**
 * Represents a point on a tectonic plate with its associated velocity vector.
 */
public class PlateVector {

    private final String siteId;
    private final Quantity<Angle> latitude;
    private final Quantity<Angle> longitude;

    // Velocity components
    private final Quantity<Velocity> northVelocity; // mm/yr usually
    private final Quantity<Velocity> eastVelocity; // mm/yr usually
    // Up component usually ignored for 2D plate motion but could be added

    public PlateVector(String siteId, Quantity<Angle> latitude, Quantity<Angle> longitude,
            Quantity<Velocity> northVelocity, Quantity<Velocity> eastVelocity) {
        this.siteId = siteId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.northVelocity = northVelocity;
        this.eastVelocity = eastVelocity;
    }

    public String getSiteId() {
        return siteId;
    }

    public Quantity<Angle> getLatitude() {
        return latitude;
    }

    public Quantity<Angle> getLongitude() {
        return longitude;
    }

    public Quantity<Velocity> getNorthVelocity() {
        return northVelocity;
    }

    public Quantity<Velocity> getEastVelocity() {
        return eastVelocity;
    }

    @Override
    public String toString() {
        return String.format("Site: %s [%s, %s] Vel: N=%s E=%s", siteId, latitude, longitude, northVelocity,
                eastVelocity);
    }
}
