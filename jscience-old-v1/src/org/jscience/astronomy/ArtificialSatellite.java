package org.jscience.astronomy;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.media.j3d.Group;


/**
 * The ArtificialSatellite class provides support for Artificial Satellites
 * (small human made objects).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//an artificial satellite cruising around in a star system, (usually orbitting a natural satellite
public abstract class ArtificialSatellite extends AstralBody {
    /** DOCUMENT ME! */
    private Date launchDate;

    /** DOCUMENT ME! */
    private String country;

    /** DOCUMENT ME! */
    private String mission;

    /** DOCUMENT ME! */
    private Object data;

    /** DOCUMENT ME! */
    private Date endDate; //end of activity

    /** DOCUMENT ME! */
    private boolean geostationnary;

/**
     * Creates a new ArtificialSatellite object.
     *
     * @param name DOCUMENT ME!
     */
    public ArtificialSatellite(String name) {
        super(name);
        this.launchDate = null;
        this.country = new String("");
        this.mission = new String("");
        this.data = null;
        this.endDate = null;
        this.geostationnary = false;
    }

/**
     * Creates a new ArtificialSatellite object.
     *
     * @param name       DOCUMENT ME!
     * @param launchDate DOCUMENT ME!
     * @param country    DOCUMENT ME!
     * @param mission    DOCUMENT ME!
     * @param data       DOCUMENT ME!
     * @param endDate    DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ArtificialSatellite(String name, Date launchDate, String country,
        String mission, Object data, Date endDate) {
        super(name);

        if ((country != null) && (mission != null)) {
            this.launchDate = launchDate;
            this.country = country;
            this.mission = mission;
            this.data = data;
            this.endDate = endDate;
            this.geostationnary = false;
        } else {
            throw new IllegalArgumentException(
                "The ArtificialSatellite constructor can't have null country or mission.");
        }
    }

/**
     * Creates a new ArtificialSatellite object.
     *
     * @param name       DOCUMENT ME!
     * @param mass       DOCUMENT ME!
     * @param launchDate DOCUMENT ME!
     * @param country    DOCUMENT ME!
     * @param mission    DOCUMENT ME!
     * @param data       DOCUMENT ME!
     * @param endDate    DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ArtificialSatellite(String name, double mass, Date launchDate,
        String country, String mission, Object data, Date endDate) {
        super(name, mass);

        if ((country != null) && (mission != null)) {
            this.launchDate = launchDate;
            this.country = country;
            this.mission = mission;
            this.data = data;
            this.endDate = endDate;
            this.geostationnary = false;
        } else {
            throw new IllegalArgumentException(
                "The ArtificialSatellite constructor can't have null country or mission.");
        }
    }

/**
     * Creates a new ArtificialSatellite object.
     *
     * @param name       DOCUMENT ME!
     * @param shape      DOCUMENT ME!
     * @param mass       DOCUMENT ME!
     * @param launchDate DOCUMENT ME!
     * @param country    DOCUMENT ME!
     * @param mission    DOCUMENT ME!
     * @param data       DOCUMENT ME!
     * @param endDate    DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public ArtificialSatellite(String name, Group shape, double mass,
        Date launchDate, String country, String mission, Object data,
        Date endDate) {
        super(name, shape, mass);

        if ((country != null) && (mission != null)) {
            this.launchDate = launchDate;
            this.country = country;
            this.mission = mission;
            this.data = data;
            this.endDate = endDate;
            this.geostationnary = false;
        } else {
            throw new IllegalArgumentException(
                "The ArtificialSatellite constructor can't have null country or mission.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren() {
        return Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     */
    public void setChildren(Set children) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getLaunchDate() {
        return launchDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param launchDate DOCUMENT ME!
     */
    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCountry() {
        return country;
    }

    /**
     * DOCUMENT ME!
     *
     * @param country DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCountry(String country) {
        if (country != null) {
            this.country = country;
        } else {
            throw new IllegalArgumentException("You can't set a null country");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMission() {
        return mission;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mission DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setMission(String mission) {
        if (mission != null) {
            this.mission = mission;
        } else {
            throw new IllegalArgumentException("You can't set a null mission");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param endDate DOCUMENT ME!
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeostationnary() {
        return geostationnary;
    }

    /**
     * DOCUMENT ME!
     *
     * @param geostationnary DOCUMENT ME!
     */
    public void setGeostationnary(boolean geostationnary) {
        this.geostationnary = geostationnary;
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        super.updateShape();
    }
}
