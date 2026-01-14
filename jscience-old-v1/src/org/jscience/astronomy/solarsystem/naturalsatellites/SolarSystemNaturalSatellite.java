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

package org.jscience.astronomy.solarsystem.naturalsatellites;

import org.jscience.astronomy.NaturalSatellite;


/**
 * The SolarSystemNaturalSatellite class provides some data fields for
 * bodies in our star system (primarily).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//name, rotationperiod, radius, temperature, tilt, mass are set using parent class

//TODO we really should convert everything to the standard MKSA unit system here

//type			planet or moon
//name
//parent		parent astral body    (unused)
//diameter			km
//rotationperiod			day
//orbitperiod				year
//meanorbitvelocity			km/s
//orbiteccentricity
//orbitinclinationtoecliptic            �
//inclinationofequatortoorbit           �
//volume				ratio to earth volume
//distance				AU or km
//mass					g
//density				g/cm3   (unused, use mass/volume)
//surfacegravity			m/s2
//escapevelocity			km/s
//meantempatsolidsurface		Kelvin
//majoratmosphericconstitutents 	comma separated string
public class SolarSystemNaturalSatellite extends NaturalSatellite {
    /** The volume for that body. */
    private double volume;

    /** The orbit period for that body. */
    private double orbitperiod;

    /** The mean orbit velocity for that body. */
    private double meanorbitvelocity;

    /** The orbit eccentricity for that body. */
    private double orbiteccentricity;

    /** The orbit inclination to ecliptic for that body. */
    private double orbitinclinationtoecliptic;

    /** The inclination of equator to orbit for that body. */
    private double inclinationofequatortoorbit;

    /** The distance from parent for that body. */
    private double distance;

    /** The surface gravity for that body. */
    private double surfacegravity;

    /** The escape velocity for that body. */
    private double escapevelocity;

    /** The major atmospheric constitutents for that body. */
    private double majoratmosphericconstitutents;

/**
     * Constructs an SolarSystemPlanetaryBody.
     *
     * @param name DOCUMENT ME!
     */
    public SolarSystemNaturalSatellite(String name) {
        super(name);
    }

/**
     * Constructs an SolarSystemPlanetaryBody.
     *
     * @param name           DOCUMENT ME!
     * @param mass           DOCUMENT ME!
     * @param radius         DOCUMENT ME!
     * @param rotationPeriod DOCUMENT ME!
     * @param tilt           DOCUMENT ME!
     * @param age            DOCUMENT ME!
     * @param composition    DOCUMENT ME!
     */
    public SolarSystemNaturalSatellite(String name, double mass, double radius,
        double rotationPeriod, double tilt, double age, String composition) {
        super(name, mass, radius, rotationPeriod, tilt, age, composition);
    }

    /**
     * returns the volume for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getVolume() {
        return volume;
    }

    /**
     * sets the volume for that body.
     *
     * @param volume DOCUMENT ME!
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * returns the orbit period for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getOrbitPeriod() {
        return orbitperiod;
    }

    /**
     * sets the orbit period for that body.
     *
     * @param orbitperiod DOCUMENT ME!
     */
    public void setOrbitPeriod(double orbitperiod) {
        this.orbitperiod = orbitperiod;
    }

    /**
     * returns the mean orbit velocity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getMeanOrbitVelocity() {
        return meanorbitvelocity;
    }

    /**
     * sets the mean orbit velocity for that body.
     *
     * @param meanorbitvelocity DOCUMENT ME!
     */
    public void setMeanOrbitVelocity(double meanorbitvelocity) {
        this.meanorbitvelocity = meanorbitvelocity;
    }

    /**
     * returns the orbit eccentricity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getOrbitEccentricity() {
        return orbiteccentricity;
    }

    /**
     * sets the orbit eccentricity for that body.
     *
     * @param orbiteccentricity DOCUMENT ME!
     */
    public void setOrbitEccentricity(double orbiteccentricity) {
        this.orbiteccentricity = orbiteccentricity;
    }

    /**
     * returns the orbit inclination to ecliptic for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getOrbitInclinationToEcliptic() {
        return orbitinclinationtoecliptic;
    }

    /**
     * sets the orbit inclination to ecliptic for that body.
     *
     * @param orbitinclinationtoecliptic DOCUMENT ME!
     */
    public void setOrbitInclinationToEcliptic(double orbitinclinationtoecliptic) {
        this.orbitinclinationtoecliptic = orbitinclinationtoecliptic;
    }

    /**
     * returns the inclination of equator to orbit for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getInclinationOfEquatorToOrbit() {
        return inclinationofequatortoorbit;
    }

    /**
     * sets the inclination of equator to orbit for that body.
     *
     * @param inclinationofequatortoorbit DOCUMENT ME!
     */
    public void setInclinationOfEquatorToOrbit(
        double inclinationofequatortoorbit) {
        this.inclinationofequatortoorbit = inclinationofequatortoorbit;
    }

    /**
     * returns the distance for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getDistanceToParent() {
        return distance;
    }

    /**
     * sets the distance for that body.
     *
     * @param distance DOCUMENT ME!
     */
    public void setDistanceToParent(double distance) {
        this.distance = distance;
    }

    /**
     * returns the surface gravity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getSurfaceGravity() {
        return surfacegravity;
    }

    /**
     * sets the surface gravity for that body.
     *
     * @param surfacegravity DOCUMENT ME!
     */
    public void setSurfaceGravity(double surfacegravity) {
        this.surfacegravity = surfacegravity;
    }

    /**
     * returns the escape velocity for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getEscapeVelocity() {
        return escapevelocity;
    }

    /**
     * sets the escape velocity for that body.
     *
     * @param escapevelocity DOCUMENT ME!
     */
    public void setEscapeVelocity(double escapevelocity) {
        this.escapevelocity = escapevelocity;
    }

    /**
     * returns the major atmospheric constitutents for that body.
     *
     * @return DOCUMENT ME!
     */
    public double getAtmosphericConstitutents() {
        return majoratmosphericconstitutents;
    }

    /**
     * sets the major atmospheric constitutents for that body.
     *
     * @param majoratmosphericconstitutents DOCUMENT ME!
     */
    public void setAtmosphericConstitutents(
        double majoratmosphericconstitutents) {
        this.majoratmosphericconstitutents = majoratmosphericconstitutents;
    }
}
