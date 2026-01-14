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
 * Multiplet.java
 *
 * Created on February 14, 2001, 1:30 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author jam
 */
public class Multiplet extends Vector {
/**
     * Creates new Multiplet
     */
    public Multiplet() {
    }

    /**
     * Creates a new Multiplet object.
     *
     * @param p DOCUMENT ME!
     */
    public Multiplet(Peak p) {
        this();
        addPeak(p);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Peak[] getAllPeaks() {
        Object[] arr = toArray();
        Peak[] rval = new Peak[arr.length];

        for (int p = 0; p < arr.length; p++) {
            rval[p] = (Peak) arr[p];
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getAllCentroids() {
        Peak[] peaks = getAllPeaks();
        double[] centroids = new double[peaks.length];

        if (peaks.length > 0) {
            for (int i = 0; i < peaks.length; i++) {
                centroids[i] = peaks[i].getPosition();
            }
        }

        return centroids;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getCentroidErrors() {
        Peak[] peaks = getAllPeaks();
        double[] errors = new double[peaks.length];

        for (int i = 0; i < peaks.length; i++) {
            errors[i] = peaks[i].getPositionError();
        }

        return errors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param madd DOCUMENT ME!
     */
    public void addMultiplet(Multiplet madd) {
        addAll(madd);
    }

    /**
     * Removes all peaks with less than the specified area.
     *
     * @param min minimum area to retain the peak
     *
     * @return number of peaks remaining
     */
    public int removeAreaLessThan(double min) {
        Multiplet small = new Multiplet();

        for (int p = 0; p < size(); p++) {
            Peak peak = getPeak(p);

            if (peak.getArea() < min) {
                small.addPeak(peak);
            }
        }

        removeAll(small); //remove any peaks in small from this multiplet

        return size();
    }

    /**
     * Removes all peaks with greater than the specified area.
     *
     * @param max minimum area to retain the peak
     *
     * @return number of peaks remaining
     */
    public int removeAreaGreaterThan(double max) {
        Multiplet large = new Multiplet();

        for (int p = 0; p < size(); p++) {
            Peak peak = getPeak(p);

            if (peak.getArea() > max) {
                large.addPeak(peak);
            }
        }

        removeAll(large); //remove any peaks in small from this multiplet

        return size();
    }

    /**
     * Returns peak nearest given centroid if distance is less than the
     * given tolerance.
     *
     * @param centroid DOCUMENT ME!
     * @param tolerance DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Peak getPeakNear(double centroid, double tolerance) {
        double diff1 = 2 * tolerance;
        double diff2;
        Peak p1;
        Peak p2;
        Iterator it = (new TreeSet(this)).iterator();

        if (it.hasNext()) {
            p1 = (Peak) (it.next());
            diff1 = Math.abs(p1.getPosition() - centroid);
        } else {
            return null;
        }

        while (it.hasNext()) {
            p2 = (Peak) (it.next());
            diff2 = Math.abs(p2.getPosition() - centroid);

            if (diff2 < diff1) {
                p1 = p2;
                diff1 = diff2;
            }
        }

        if (diff1 <= tolerance) {
            return p1;
        } else {
            return null;
        }
    }

    /**
     * Returns a new multiplet containing only the last n peaks of this
     * multiplet.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Multiplet lastPeaks(int n) {
        Multiplet rval = new Multiplet();

        for (int i = size() - n; i < size(); i++)
            rval.addPeak(getPeak(i));

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param marray DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static Multiplet combineMultiplets(Multiplet[] marray) {
        Multiplet rval = new Multiplet();

        for (int i = 0; i < marray.length; i++) {
            rval.addMultiplet(marray[i]);
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m0 DOCUMENT ME!
     * @param m1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static Multiplet combineMultiplets(Multiplet m0, Multiplet m1) {
        Multiplet[] temp = new Multiplet[2];
        temp[0] = m0;
        temp[1] = m1;

        return combineMultiplets(temp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void addPeak(Peak p) {
        if (p != null) {
            addElement(p);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Peak getPeak(int index) {
        return (Peak) elementAt(index);
    }
}
