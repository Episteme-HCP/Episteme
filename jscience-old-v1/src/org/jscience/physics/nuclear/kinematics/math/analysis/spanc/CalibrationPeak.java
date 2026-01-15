/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/

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
