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
 * CalibrationPeak.java
 *
 * Created on December 19, 2001, 11:12 AM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import java.util.Collection;
import java.util.List;
import java.util.Vector;


/**
 * Representation of a fitted peak used for calibration of a spectrum.
 *
 * @author <a href="mailto:dale@visser.name">Dale Visser</a>
 */
public class CalibrationPeak implements java.io.Serializable {
    /**
     * DOCUMENT ME!
     */
    static List peaks = new Vector(1, 1);

    /**
     * DOCUMENT ME!
     */
    SpancReaction reaction;

    /**
     * DOCUMENT ME!
     */
    double ExProjectile = 0;

    /**
     * DOCUMENT ME!
     */
    UncertainNumber ExResidual = new UncertainNumber(0);

    /**
     * DOCUMENT ME!
     */
    UncertainNumber channel;

/**
     * Creates new CalibrationPeak
     */
    public CalibrationPeak(SpancReaction reaction, double ExProjectile,
        UncertainNumber ExResidual, UncertainNumber channel) {
        /* the following four lines avoid a call to the overridable setValues()
        in this constructor */
        this.reaction = reaction;
        this.ExProjectile = ExProjectile;
        this.ExResidual = ExResidual;
        this.channel = channel;
        peaks.add(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reaction DOCUMENT ME!
     * @param ExProjectile DOCUMENT ME!
     * @param ExResidual DOCUMENT ME!
     * @param channel DOCUMENT ME!
     */
    protected void setValues(SpancReaction reaction, double ExProjectile,
        UncertainNumber ExResidual, UncertainNumber channel) {
        this.reaction = reaction;
        this.ExProjectile = ExProjectile;
        this.ExResidual = ExResidual;
        this.channel = channel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param which DOCUMENT ME!
     */
    static public void removePeak(int which) {
        peaks.remove(which);
    }

    /**
     * DOCUMENT ME!
     *
     * @param which DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public CalibrationPeak getPeak(int which) {
        return (CalibrationPeak) peaks.get(which);
    }

    /**
     * DOCUMENT ME!
     */
    static public void removeAllPeaks() {
        peaks.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Collection getPeakCollection() {
        return peaks;
    }

    /**
     * DOCUMENT ME!
     *
     * @param retrievedPeaks DOCUMENT ME!
     */
    static public void refreshData(Collection retrievedPeaks) {
        peaks.addAll(retrievedPeaks);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public UncertainNumber getRho()
        throws KinematicsException, NuclearException {
        return reaction.getRho(ExProjectile, ExResidual);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getReactionIndex() {
        return SpancReaction.getReactionIndex(reaction);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UncertainNumber getExResidual() {
        return ExResidual;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getExProjectile() {
        return ExProjectile;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UncertainNumber getChannel() {
        return channel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public UncertainNumber[] getY() {
        UncertainNumber[] rval = new UncertainNumber[peaks.size()];

        for (int index = 0; index < peaks.size(); index++) {
            CalibrationPeak cp = (CalibrationPeak) peaks.get(index);

            try {
                rval[index] = cp.getRho();
            } catch (KinematicsException ke) {
                System.err.println(ke);
            } catch (NuclearException ke) {
                System.err.println(ke);
            }
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public UncertainNumber[] getX() {
        UncertainNumber[] rval = new UncertainNumber[peaks.size()];

        for (int index = 0; index < peaks.size(); index++) {
            CalibrationPeak cp = (CalibrationPeak) peaks.get(index);
            rval[index] = cp.channel;
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rval = "Calibration Peak " + peaks.indexOf(this);

        try {
            rval += (" from reaction #" +
            SpancReaction.getReactionIndex(reaction) + "\n");
            rval += ("Ex of projectile: " + ExProjectile + " MeV\n");
            rval += ("Ex of residual: " + ExResidual + " MeV\n");
            rval += ("rho of projectile: " + getRho() + " cm\n");
            rval += ("Peak Centroid: " + channel + "\n");
        } catch (KinematicsException ke) {
            System.err.println(ke);
        } catch (NuclearException ke) {
            System.err.println(ke);
        }

        return rval;
    }
}
