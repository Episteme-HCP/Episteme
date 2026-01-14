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
 * SpancReaction.java
 *
 * Created on December 16, 2001, 3:27 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearCollision;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

import java.util.Collection;
import java.util.List;
import java.util.Vector;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale
 */
public class SpancReaction implements java.io.Serializable {
    /**
     * DOCUMENT ME!
     */
    private static List allReactions = new Vector(1, 1);

    /**
     * DOCUMENT ME!
     */
    private Nucleus beam;

    /**
     * DOCUMENT ME!
     */
    private boolean beamUncertain = false;

    /**
     * DOCUMENT ME!
     */
    private Nucleus targetNuclide;

    /**
     * DOCUMENT ME!
     */
    private boolean targetUncertain = false;

    /**
     * DOCUMENT ME!
     */
    private Nucleus projectile;

    /**
     * DOCUMENT ME!
     */
    private boolean projectileUncertain = false;

    /**
     * DOCUMENT ME!
     */
    private boolean residualUncertain = false;

    /**
     * DOCUMENT ME!
     */
    private double Ebeam;

    /**
     * DOCUMENT ME!
     */
    private double Bfield;

    /**
     * DOCUMENT ME!
     */
    private Target target;

    /**
     * DOCUMENT ME!
     */
    private int interaction_layer;

    /**
     * DOCUMENT ME!
     */
    private int Qprojectile;

    /**
     * DOCUMENT ME!
     */
    private double thetaDegrees;

/**
     * Creates new SpancReaction
     */
    public SpancReaction(Nucleus beam, Nucleus targetNuclide,
        Nucleus projectile, double Ebeam, double B, Target target,
        int interaction_layer, int Qprojectile, double thetaDegrees) {
        /* The following 9 statements avoid a call to the overridable
        * setValues() method in this constructor. */
        this.beam = beam;
        this.targetNuclide = targetNuclide;
        this.projectile = projectile;
        this.Ebeam = Ebeam;
        this.Bfield = B;
        this.target = target;
        this.interaction_layer = interaction_layer;
        this.Qprojectile = Qprojectile;
        this.thetaDegrees = thetaDegrees;
        allReactions.add(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param beam DOCUMENT ME!
     * @param targetNuclide DOCUMENT ME!
     * @param projectile DOCUMENT ME!
     * @param Ebeam DOCUMENT ME!
     * @param B DOCUMENT ME!
     * @param target DOCUMENT ME!
     * @param interaction_layer DOCUMENT ME!
     * @param Qprojectile DOCUMENT ME!
     * @param thetaDegrees DOCUMENT ME!
     */
    public void setValues(Nucleus beam, Nucleus targetNuclide,
        Nucleus projectile, double Ebeam, double B, Target target,
        int interaction_layer, int Qprojectile, double thetaDegrees) {
        this.beam = beam;
        this.targetNuclide = targetNuclide;
        this.projectile = projectile;
        this.Ebeam = Ebeam;
        this.Bfield = B;
        this.target = target;
        this.interaction_layer = interaction_layer;
        this.Qprojectile = Qprojectile;
        this.thetaDegrees = thetaDegrees;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setBeamUncertain(boolean state) {
        beamUncertain = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getBeamUncertain() {
        return beamUncertain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setTargetUncertain(boolean state) {
        targetUncertain = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getTargetUncertain() {
        return targetUncertain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setProjectileUncertain(boolean state) {
        projectileUncertain = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getProjectileUncertain() {
        return projectileUncertain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void setResidualUncertain(boolean state) {
        residualUncertain = state;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getResidualUncertain() {
        return residualUncertain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.lang.String toString() {
        String rval = "Reaction " + allReactions.indexOf(this) + "\n";
        rval += (description() + "\n");
        rval += ("Target Name: " + target.getName() + ", interaction layer = " +
        interaction_layer + "\n");
        rval += ("Projectile:  Q = +" + Qprojectile + ", Theta = " +
        thetaDegrees + " deg\n");
        rval += ("B-field = " + Bfield + " kG\n");

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String description() {
        return targetNuclide + "(" + Ebeam + " MeV " + beam + "," + projectile +
        ")";
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    static public void removeReaction(int index) {
        allReactions.remove(index);
    }

    /**
     * DOCUMENT ME!
     */
    static public void removeAllReactions() {
        allReactions.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param reaction DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public int getReactionIndex(SpancReaction reaction) {
        return allReactions.indexOf(reaction);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public SpancReaction[] getAllReactions() {
        SpancReaction[] rval = new SpancReaction[allReactions.size()];
        allReactions.toArray(rval);

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Collection getReactionCollection() {
        return allReactions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param retrievedReactions DOCUMENT ME!
     */
    static public void refreshData(Collection retrievedReactions) {
        allReactions.addAll(retrievedReactions);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public SpancReaction getReaction(int index) {
        return (SpancReaction) allReactions.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ExProjectile DOCUMENT ME!
     * @param ExResidual DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    UncertainNumber getRho(double ExProjectile, UncertainNumber ExResidual)
        throws KinematicsException, NuclearException {
        Nucleus tempProjectile = new Nucleus(projectile.Z, projectile.A,
                ExProjectile);
        int calculateOption = 0;
        double[] rho = new double[2];

        for (int i = 0; i < 2; i++) {
            double exResid = ExResidual.value + (i * ExResidual.error);
            NuclearCollision rxn = new NuclearCollision(targetNuclide, beam,
                    tempProjectile,
                    target.calculateInteractionEnergy(interaction_layer, beam,
                        Ebeam), thetaDegrees, exResid);
            double KEinit = rxn.getLabEnergyProjectile(0);
            double KEfinal = target.calculateProjectileEnergy(interaction_layer,
                    tempProjectile, KEinit, Math.toRadians(thetaDegrees));
            rho[i] = NuclearCollision.getQBrho(tempProjectile, KEfinal) / Qprojectile / Bfield;
        }

        double error_from_ex = Math.abs(rho[0] - rho[1]);

        if (beamUncertain) {
            calculateOption |= NuclearCollision.UNCERTAIN_BEAM_MASS_OPTION;
        }

        if (targetUncertain) {
            calculateOption |= NuclearCollision.UNCERTAIN_TARGET_MASS_OPTION;
        }

        if (projectileUncertain) {
            calculateOption |= NuclearCollision.UNCERTAIN_PROJECTILE_MASS_OPTION;
        }

        if (residualUncertain) {
            calculateOption |= NuclearCollision.UNCERTAIN_RESIDUAL_MASS_OPTION;
        }

        NuclearCollision rxn = new NuclearCollision(targetNuclide, beam,
                tempProjectile,
                target.calculateInteractionEnergy(interaction_layer, beam, Ebeam),
                thetaDegrees, ExResidual.value);
        UncertainNumber KEinit = rxn.getLabEnergyProjectile(0, calculateOption);
        double KEfinal = target.calculateProjectileEnergy(interaction_layer,
                tempProjectile, KEinit.value, Math.toRadians(thetaDegrees));
        UncertainNumber qbr_unc_masses = NuclearCollision.getQBrho(tempProjectile,
                new UncertainNumber(KEfinal, KEinit.error), projectileUncertain);
        UncertainNumber rho_unc_masses = qbr_unc_masses.divide(Qprojectile)
                                                       .divide(Bfield);

        return new UncertainNumber(rho[0],
            Math.sqrt((error_from_ex * error_from_ex) +
                (rho_unc_masses.error * rho_unc_masses.error)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ExProjectile DOCUMENT ME!
     * @param rho DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    UncertainNumber getExResid(double ExProjectile, UncertainNumber rho)
        throws KinematicsException, NuclearException {
        Nucleus tempProjectile = new Nucleus(projectile.Z, projectile.A,
                ExProjectile);
        NuclearCollision rxn = new NuclearCollision(targetNuclide, beam,
                tempProjectile,
                target.calculateInteractionEnergy(interaction_layer, beam, Ebeam),
                thetaDegrees, 0.0);
        double[] p = new double[2];

        //need to add target energy loss back to brho for accurate value
        for (int i = 0; i < 2; i++) {
            double qbr = (rho.value + (i * rho.error)) * Qprojectile * Bfield;
            double KE = NuclearCollision.getKE(tempProjectile, qbr);
            KE = target.calculateInitialProjectileEnergy(interaction_layer,
                    tempProjectile, KE, Math.toRadians(thetaDegrees));
            p[i] = NuclearCollision.getQBrho(tempProjectile, KE) * NuclearCollision.QBRHO_TO_P;
        }

        UncertainNumber momentum = new UncertainNumber(p[0],
                Math.abs(p[1] - p[0]));

        return rxn.getEx4(momentum);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getBeam() {
        return beam;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEbeam() {
        return Ebeam;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Target getTarget() {
        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInteractionLayer() {
        return interaction_layer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getTargetNuclide() {
        return targetNuclide;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getProjectile() {
        return projectile;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public Nucleus getResidual() throws NuclearException {
        return new Nucleus((beam.Z + targetNuclide.Z) - projectile.Z,
            (beam.A + targetNuclide.A) - projectile.A);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getQ() {
        return Qprojectile;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getTheta() {
        return thetaDegrees;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getBfield() {
        return Bfield;
    }
}
