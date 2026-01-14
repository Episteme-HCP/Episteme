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

import org.jscience.physics.nuclear.kinematics.math.MathException;
import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.statistics.StatisticsException;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import java.util.Collection;
import java.util.List;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author org.jscience.physics.nuclear.kinematics
 */
public class OutputPeak implements java.io.Serializable {
    /**
     * DOCUMENT ME!
     */
    static CalibrationFit fit;

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
     * DOCUMENT ME!
     */
    UncertainNumber rho;

/**
     * Creates new OutputPeak
     */
    public OutputPeak(SpancReaction reaction, double ExProjectile,
        UncertainNumber channel, CalibrationFit cf)
        throws KinematicsException, StatisticsException, MathException,
            NuclearException {
        fit = cf;
        /* The following 4 statements avoid a call to the overridable
        * setValues() method in this constructor. */
        this.reaction = reaction;
        this.ExProjectile = ExProjectile;
        this.channel = channel;
        calculate();
        peaks.add(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reaction DOCUMENT ME!
     * @param ExProjectile DOCUMENT ME!
     * @param channel DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws MathException DOCUMENT ME!
     * @throws StatisticsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setValues(SpancReaction reaction, double ExProjectile,
        UncertainNumber channel)
        throws KinematicsException, MathException, StatisticsException,
            NuclearException {
        this.reaction = reaction;
        this.ExProjectile = ExProjectile;
        this.channel = channel;
        calculate();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws StatisticsException DOCUMENT ME!
     * @throws MathException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    private void calculate()
        throws KinematicsException, StatisticsException, MathException,
            NuclearException {
        rho = fit.getRho(channel);
        ExResidual = reaction.getExResid(ExProjectile, rho);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cf DOCUMENT ME!
     */
    static public void setCalibration(CalibrationFit cf) {
        fit = cf;
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
    static public OutputPeak getPeak(int which) {
        return (OutputPeak) peaks.get(which);
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
     * @throws KinematicsException DOCUMENT ME!
     * @throws StatisticsException DOCUMENT ME!
     * @throws MathException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    static public void recalculate()
        throws KinematicsException, StatisticsException, MathException,
            NuclearException {
        java.util.Iterator iter = peaks.iterator();

        while (iter.hasNext())
            ((OutputPeak) iter.next()).calculate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param adjustError DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     */
    public UncertainNumber getRho(boolean adjustError)
        throws KinematicsException {
        //UncertainNumber temp = reaction.getRho(ExProjectile,ExResidual);
        if (adjustError) {
            return new UncertainNumber(rho.value,
                rho.error * Math.sqrt(Math.max(1, fit.getReducedChiSq())));
        } else {
            return rho;
        }
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
     * @param adjustError DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UncertainNumber getExResidual(boolean adjustError) {
        if (adjustError) {
            return new UncertainNumber(ExResidual.value,
                ExResidual.error * Math.sqrt(Math.max(1, fit.getReducedChiSq())));
        } else {
            return ExResidual;
        }
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
    public String toString() {
        String rval = "Output Peak for Reaction #" + getReactionIndex() + "\n";
        rval += ("Ex projectile [MeV] = " + ExProjectile + " MeV\n");
        rval += ("Centroid Channel = " + channel + "\n");

        try {
            rval += ("rho from calibration = " + getRho(false) +
            " cm, adjusted error = " + getRho(true).error + " cm\n");
        } catch (KinematicsException ke) {
            System.err.println(ke);
            rval += "ERROR: Problem calculating Brho\n";
        }

        rval += ("Ex residual from calibration = " + getExResidual(false) +
        " MeV, adjusted error = " + (getExResidual(true).error * 1000) +
        " keV\n");

        return rval;
    }
}
