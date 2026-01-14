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

package org.jscience.physics.waves.optics.rays;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class RayPoint implements Cloneable {
    // Point de d�part du raypoint.
    /** DOCUMENT ME! */
    private double x;

    // Point de d�part du raypoint.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double y;

    // Point de d�part du raypoint.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double z;

    // Vecteur d'onde.
    /** DOCUMENT ME! */
    private double kx;

    // Vecteur d'onde.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double ky;

    // Vecteur d'onde.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double kz;

    // Longueur d'onde.
    /** DOCUMENT ME! */
    private double wavelength;

    // Polarisation.
    /** DOCUMENT ME! */
    private double pol_x;

    // Polarisation.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double pol_y;

    // Polarisation.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double pol_z;

    // Amplitude, phase.
    /** DOCUMENT ME! */
    private double amplitude;

    // Amplitude, phase.
    /** DOCUMENT ME! */
    /** DOCUMENT ME! */
    private double phase;

    // Le raypoint existe-t-il ?
    /** DOCUMENT ME! */
    private boolean valid;

/**
     * Creates a new RayPoint object.
     */
    public RayPoint() {
        x = y = z = 0;
        kx = ky = kz = 0;
        wavelength = 0;
        pol_x = pol_y = pol_z = 0;
        amplitude = phase = 0;
        valid = true;
    }

/**
     * Creates a new RayPoint object.
     *
     * @param pos DOCUMENT ME!
     * @param k   DOCUMENT ME!
     * @param w   DOCUMENT ME!
     */
    public RayPoint(Double3Vector pos, Double3Vector k, double w) {
        this();

        Double3Vector k2 = new Double3Vector(k);

        setPosition(pos);
        wavelength = w;
        k2.normalize((2 * Math.PI) / w);
        setKVector(k2);
        valid = true;
    }

/**
     * Creates a new RayPoint object.
     *
     * @param pos DOCUMENT ME!
     * @param k   DOCUMENT ME!
     * @param w   DOCUMENT ME!
     * @param d   DOCUMENT ME!
     */
    public RayPoint(Double3Vector pos, Double3Vector k, double w, double d) {
        this();

        Double3Vector k2 = new Double3Vector(k);

        setPosition(pos);
        wavelength = w;
        k2.normalize((2 * Math.PI * d) / w);
        setKVector(k2);
        valid = true;
    }

    /*public RayPoint( RayPoint r )
    {
        setPosition( new Double3Vector( r.getPosition() ) );
        setKVector( new Double3Vector( r.getKVector() ) );
        wavelength = r.getWavelength();
        setPolarisation( new Double3Vector( r.getPolarisation() ) );
        amplitude = r.getAmplitude();
        phase = r.getPhase();
        valid = r.isValid();
    }*/
    public Object clone() {
        Object o = null;

        try {
            o = super.clone();
        } catch (CloneNotSupportedException e) {
        }

        return o;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x_distance DOCUMENT ME!
     */
    public void goStraight(double x_distance) {
        double mult = x_distance / kx;

        //x += mult * kx; // Erreur d'arrondi;
        x += x_distance;
        y += (mult * ky);
        z += (mult * kz);
    }

    /**
     * DOCUMENT ME!
     */
    public void invalidate() {
        valid = false;
    }

    /*public void validate()
    {
        valid = true;
    }*/
    public void setPosition(Double3Vector pos) {
        x = pos.x;
        y = pos.y;
        z = pos.z;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param z DOCUMENT ME!
     */
    public void translate(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     */
    public void setKVector(Double3Vector k) {
        kx = k.x;
        ky = k.y;
        kz = k.z;
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setWavelength(double w) {
        wavelength = w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setPolarisation(Double3Vector p) {
        pol_x = p.x;
        pol_y = p.y;
        pol_z = p.z;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void setAmplitude(double a) {
        amplitude = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setPhase(double p) {
        phase = p;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double3Vector getPosition() {
        return new Double3Vector(x, y, z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double3Vector getKVector() {
        return new Double3Vector(kx, ky, kz);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWavelength() {
        return wavelength;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Double3Vector getPolarisation() {
        return new Double3Vector(pol_x, pol_y, pol_z);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAmplitude() {
        return amplitude;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPhase() {
        return phase;
    }
}
