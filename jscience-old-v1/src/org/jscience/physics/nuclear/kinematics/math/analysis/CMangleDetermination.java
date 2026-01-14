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
 * CMangleDetermination.java
 *
 * Created on June 8, 2001, 3:37 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearCollision;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;


/**
 * A simple code for finding out what the CM angles for various YLSA strips
 * would be for a given branching ratio experiment.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 * @version 1.0
 */
public class CMangleDetermination extends Object {
    /** Phi of detectors in degrees. */
    private static double[] phi = { 198.0, 126.0, 54.0, -18.0, -90.0 };

    /** theta of strips in degrees. */
    private static double[] theta = {
            165.95, 164.2, 162.3, 160.4, 158.4, 156.2, 154.0, 151.7, 149.3,
            146.85, 144.3, 141.6, 138.9, 136.2, 133.4, 130.6
        };

    /** Angle of residual, in radians. */
    double thetaResid;

    /** Relativistic gamma parameter for residual velocity in lab. */
    double gamma;

    /** Mass of decay nucleus from residual. */
    double Mdecay;

    /** Mass of ultimate decay product. */
    double Mfinalstate;

    /** mass parameter for determining decay energy */
    double Msquare;

    /** momentum of residual, MeV/c */
    double Presid;

    /** mass-energy of residual */
    double Eresid;

    /** lab velocity of residual (CM system) */
    double beta;

    /* these angles stored in radians */
    private double[][] labAngles = new double[5][16];

    /**
     * DOCUMENT ME!
     */
    private double[][] CMangles = new double[5][16];

    /**
     * DOCUMENT ME!
     */
    NuclearCollision reaction;

/**
     * Creates new CMangleDetermination
     */
    public CMangleDetermination(Nucleus target, Nucleus beam,
        Nucleus projectile, double beamEnergy, double thetaSpec, Nucleus decay,
        double residualExcitation) {
        double Mresid = 0.0;

        try {
            reaction = new NuclearCollision(target, beam, projectile,
                    beamEnergy, thetaSpec,
                    new UncertainNumber(residualExcitation));
            /* it is assumed that there is a single solution here */
            Mdecay = decay.getMass().value;

            Nucleus ultimate = new Nucleus(reaction.getResidual().Z - decay.Z,
                    reaction.getResidual().A - decay.A, residualExcitation);
            Mfinalstate = ultimate.getMass().value;
            Mresid = reaction.getResidual().getMass().value;
        } catch (Exception ke) {
            System.err.println(ke);
        }

        Msquare = ((Mresid * Mresid) + (Mdecay * Mdecay)) -
            (Mfinalstate * Mfinalstate);
        Presid = reaction.getLabMomentumResidual(0);
        Eresid = reaction.getTotalEnergyResidual(0);
        thetaResid = Math.toRadians(reaction.getLabAngleResidual(0));
        gamma = reaction.getLabGammaResidual(0);
        beta = reaction.getLabBetaResidual(0);
        System.out.println("Gamma: " + gamma + ", Beta: " + beta);
        System.out.println("det\tstrip\tlab\tCM");

        for (int det = 0; det < phi.length; det++) {
            for (int strip = 0; strip < theta.length; strip++) {
                labAngles[det][strip] = getLabAngle(Math.toRadians(theta[strip]),
                        Math.toRadians(phi[det]));
                CMangles[det][strip] = getCMangle(labAngles[det][strip]);
                System.out.println(det + "\t" + strip + "\t" +
                    Math.toDegrees(labAngles[det][strip]) + "\t" +
                    Math.toDegrees(CMangles[det][strip]));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param thetaDet DOCUMENT ME!
     * @param phiDet DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getLabAngle(double thetaDet, double phiDet) {
        double cosThetaRelative = (Math.sin(thetaResid) * Math.sin(thetaDet) * Math.cos(phiDet)) +
            (Math.cos(thetaResid) * Math.cos(thetaDet));

        return Math.acos(cosThetaRelative);
    }

    /**
     * DOCUMENT ME!
     *
     * @param thetaLab DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getCMangle(double thetaLab) {
        double bDecay = Math.sqrt(1 - Math.pow(gamma, -2));
        double bDx = bDecay * Math.cos(thetaLab);
        double bDy = bDecay * Math.sin(thetaLab);

        /* next 2 lines would have to be divided by 1-beta*bDx to be
        * true velocities, but we only care about their ratio */
        double bDxCM = bDx - beta;
        double bDyCM = bDy / gamma;

        return Math.atan2(bDyCM, bDxCM);
    }

    /**
     * 
    DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Nucleus target = new Nucleus(6, 12);
            Nucleus beam = new Nucleus(6, 12);
            Nucleus projectile = new Nucleus(2, 4);
            Nucleus decay = projectile;
            double beamEnergy = 79.821;
            double theta = 5.0; //degrees
            double Ex = 15.0;
            new CMangleDetermination(target, beam, projectile, beamEnergy,
                theta, decay, Ex);
        } catch (NuclearException e) {
            System.err.println(e);
        }
    }
}
