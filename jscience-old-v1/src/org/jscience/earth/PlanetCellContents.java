package org.jscience.earth;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;

import java.awt.*;


/**
 * A class representing an infinitesimal volume of a planet. A sample of
 * the soil (soil, minerals, gaz, oil, ice), on land or in the crust
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use this class as the basis for simulations of earth planet (to be used with org.jscience.computing.ai.agents) or when you grab a piece of the soil
//you want to later analyze (bundle this in the org.jscience.Sample class)
//as you may be tempted to use large amount of object from this class, null values are permitted for color, magneticFieldDirection and Composition

////for cells and environments, see "Geodesic Discrete Global Grid Systems" : http://www.sou.edu/cs/sahr/dgg/pubs/gdggs03.pdf

//make this class extend Cell ?
public class PlanetCellContents extends Object {
    //combinations allowed except with unknown
    //we could use the values from ChemistryConstants although the meaning is slightly different here
    /** DOCUMENT ME! */
    public final static int UNKNOWN = 0;

    /** DOCUMENT ME! */
    public final static int GAZ = 1;

    /** DOCUMENT ME! */
    public final static int LIQUID = 2;

    /** DOCUMENT ME! */
    public final static int MUD = 4;

    /** DOCUMENT ME! */
    public final static int SAND = 8;

    /** DOCUMENT ME! */
    public final static int GLASS = 16; //solid

    /** DOCUMENT ME! */
    public final static int CRISTAL = 32; //solid

    /** DOCUMENT ME! */
    public final static int OTHER = 64;

    /** DOCUMENT ME! */
    private double temperature; //celcius degrees

    /** DOCUMENT ME! */
    private double pressure; //pascal

    /** DOCUMENT ME! */
    private double age; //seconds

    /** DOCUMENT ME! */
    private int state;

    /** DOCUMENT ME! */
    private double curiePoint;

    /** DOCUMENT ME! */
    private double radiationLevel;

    /** DOCUMENT ME! */
    private Double3Vector magneticFieldVector;

    /** DOCUMENT ME! */
    private double salinity;

    /** DOCUMENT ME! */
    private double water;

    /** DOCUMENT ME! */
    private double pH;

    /** DOCUMENT ME! */
    private Color color;

    /** DOCUMENT ME! */
    private double size;

    /** DOCUMENT ME! */
    private SoilComposition composition;

    /** DOCUMENT ME! */
    private double volume;

    /** DOCUMENT ME! */
    private double mass;

    //resistivity, etc.
    /**
     * Creates a new PlanetCellContents object.
     *
     * @param temperature DOCUMENT ME!
     * @param pressure DOCUMENT ME!
     * @param age DOCUMENT ME!
     */
    public PlanetCellContents(double temperature, double pressure, double age) {
        this.temperature = temperature;
        this.pressure = pressure;
        this.age = age;
        this.state = 0;
        this.curiePoint = 0;
        this.radiationLevel = 0;
        this.magneticFieldVector = null;
        this.salinity = 0;
        this.water = 0;
        this.pH = 0;
        this.color = null;
        this.size = 0;
        this.composition = null;
        this.volume = 0;
        this.mass = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @param temperature DOCUMENT ME!
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pressure DOCUMENT ME!
     */
    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAge() {
        return age;
    }

    /**
     * DOCUMENT ME!
     *
     * @param age DOCUMENT ME!
     */
    public void setAge(double age) {
        this.age = age;
    }

    //gaz, liquid, solid, mud, lava, granular, cristals
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getState() {
        return state;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setState(int state) {
        this.state = state;
    }

    //rocks
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCuriePoint() {
        return curiePoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param curiePoint DOCUMENT ME!
     */
    public void setCuriePoint(double curiePoint) {
        this.curiePoint = curiePoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getRadiationLevel() {
        return radiationLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param radiationLevel DOCUMENT ME!
     */
    public void setRadiationLevel(double radiationLevel) {
        this.radiationLevel = radiationLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMagneticFieldIntensity() {
        if (magneticFieldVector != null) {
            return magneticFieldVector.norm();
        } else {
            return 0;
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double3Vector getMagneticField() {
        return magneticFieldVector;
    }

    /**
     * DOCUMENT ME!
     *
     * @param magneticFieldVector DOCUMENT ME!
     */
    public void setMagneticField(Double3Vector magneticFieldVector) {
        //may return null
        this.magneticFieldVector = magneticFieldVector;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getSalinity() {
        return salinity;
    }

    //gram per liter
    /**
     * DOCUMENT ME!
     *
     * @param salinity DOCUMENT ME!
     */
    public void setSalinity(double salinity) {
        this.salinity = salinity;
    }

    //humidity, hydropotential, etc... (normally the volume of water that can be extracted from this volume of soil)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWaterConcentration() {
        return water;
    }

    /**
     * DOCUMENT ME!
     *
     * @param water DOCUMENT ME!
     */
    public void setWaterConcentration(double water) {
        this.water = water;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPH() {
        return pH;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pH DOCUMENT ME!
     */
    public void setPH(double pH) {
        this.pH = pH;
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getGrainSize() {
        return size;
    }

    //give actual size in meters (not Wentworth Scale enum)
    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     */
    public void setGrainSize(double size) {
        this.size = size;
    }

    //many othe properties could be included (upon request)
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SoilComposition getSoilComposition() {
        return composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param composition DOCUMENT ME!
     */
    public void setSoilComposition(SoilComposition composition) {
        this.composition = composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVolume() {
        return volume;
    }

    /**
     * DOCUMENT ME!
     *
     * @param volume DOCUMENT ME!
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getMass() {
        return mass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param mass DOCUMENT ME!
     */
    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double computeDensity() {
        return mass / volume;
    }

    //we could also add getGravity()
}
