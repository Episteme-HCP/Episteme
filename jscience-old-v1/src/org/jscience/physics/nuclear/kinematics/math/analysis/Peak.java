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

/*
 * Peak.java
 *
 * Created on February 14, 2001, 1:21 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis;

/**
 * This class represents a gaussian peak, in terms of it's properties.
 * Fields are also provided for the error bars on these properties.
 *
 * @author Dale
 */
public class Peak extends Object implements Comparable {
    /**
     * DOCUMENT ME!
     */
    private double position;

    /**
     * DOCUMENT ME!
     */
    private double area;

    /**
     * DOCUMENT ME!
     */
    private double width;

    /**
     * DOCUMENT ME!
     */
    private double perr;

    /**
     * DOCUMENT ME!
     */
    private double aerr;

    /**
     * DOCUMENT ME!
     */
    private double werr;

/**
     * Creates new Peak  assuming no uncertainty in values.
     *
     * @param position position of the peak centroid
     * @param area     total peak area
     * @param width    Full width at half max of the peak
     */
    public Peak(double position, double area, double width) {
        this(position, 0.0, area, 0.0, width, 0.0);
    }

/**
     * Generates a peak with error bars on its parameters.
     *
     * @param p  position of peak centroid
     * @param pe error on position
     * @param a  area of peak
     * @param ae uncertainty in area
     * @param w  FWHM of peak
     * @param we uncertainty in FWHM
     */
    public Peak(double p, double pe, double a, double ae, double w, double we) {
        setPosition(p, pe);
        setArea(a, ae);
        setWidth(w, we);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @return
     */
    public double getPosition() {
        return position;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getArea() {
        return area;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPositionError() {
        return perr;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getAreaError() {
        return aerr;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getWidthError() {
        return werr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setPosition(double p) {
        setPosition(p, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public void setPosition(double p, double e) {
        position = p;
        perr = e;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     */
    public void setArea(double a) {
        setArea(a, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public void setArea(double a, double e) {
        area = a;
        aerr = e;
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     */
    public void setWidth(double w) {
        setWidth(w, 0.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public void setWidth(double w, double e) {
        width = w;
        werr = e;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rval = "Peak\n";
        rval += ("  Position = " + position + " +/- " + perr + "\n");
        rval += ("  Area = " + area + " +/- " + aerr + "\n");
        rval += ("  FWHM = " + width + " +/- " + werr + "\n");

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object p1) {
        if (getPosition() < ((Peak) p1).getPosition()) {
            return -1;
        } else if (getPosition() > ((Peak) p1).getPosition()) {
            return 1;
        } else {
            return 0;
        }
    }
}
