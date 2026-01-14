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
 * Target.java
 *
 * Created on December 15, 2001, 2:57 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc;

import org.jscience.physics.nuclear.kinematics.nuclear.*;

import java.util.*;

import javax.swing.*;


/**
 * This class represents a target in a splitpole experiment, possibly
 * containing more than one layer. It handles target energy loss calculations.
 * Each target is uniquely identified by a name.
 *
 * @author Dale
 */
public class Target implements java.io.Serializable {
    /**
     * DOCUMENT ME!
     */
    private static Hashtable targets = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    private static DefaultListModel dlm_targets = new DefaultListModel();

    /**
     * DOCUMENT ME!
     */
    private static DefaultComboBoxModel dcbm_targets = new DefaultComboBoxModel();

    /**
     * DOCUMENT ME!
     */
    private List layers = new Vector(1, 1);

    /**
     * DOCUMENT ME!
     */
    private List fullLosses = new Vector(1, 1);

    /**
     * DOCUMENT ME!
     */
    private List halfLosses = new Vector(1, 1);

    /**
     * DOCUMENT ME!
     */
    private String name;

/**
     * Creates new Target
     */
    public Target(String name) {
        this.name = name;
        addTargetToLists();
    }

    /**
     * DOCUMENT ME!
     */
    private void addTargetToLists() {
        targets.put(name, this);
        dlm_targets.addElement(this.name);
        dcbm_targets.addElement(this.name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     */
    static public void removeTarget(Target t) {
        targets.remove(t.getName());
        dlm_targets.removeElement(t.name);
        dcbm_targets.removeElement(t.name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param retrievedTargets DOCUMENT ME!
     */
    static public void refreshData(Collection retrievedTargets) {
        Iterator iter = retrievedTargets.iterator();

        while (iter.hasNext()) {
            Target targ = (Target) iter.next();
            targets.put(targ.getName(), targ);
            dlm_targets.addElement(targ.name);
            dcbm_targets.addElement(targ.name);
        }
    }

    /**
     * DOCUMENT ME!
     */
    static public void removeAllTargets() {
        Iterator it_targ = targets.values().iterator();

        while (it_targ.hasNext()) {
            it_targ.next();
            it_targ.remove();
        }

        dlm_targets.removeAllElements();
        dcbm_targets.removeAllElements();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Target getTarget(String name) {
        return (Target) targets.get(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     */
    public void addLayer(SolidAbsorber layer) {
        layers.add(layer);
        fullLosses.add(new EnergyLoss(layer));

        Absorber half = layer.getNewInstance(0.5);
        halfLosses.add(new EnergyLoss(half));
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    void removeLayer(int index) {
        layers.remove(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param interaction_layer DOCUMENT ME!
     * @param beam DOCUMENT ME!
     * @param Ebeam DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double calculateInteractionEnergy(int interaction_layer, Nucleus beam,
        double Ebeam) {
        double rval = Ebeam;

        for (int i = 0; i < interaction_layer; i++) {
            rval -= (0.001 * getFullLoss(i).getEnergyLoss(beam, rval));
        }

        rval -= (0.001 * getHalfLoss(interaction_layer).getEnergyLoss(beam, rval));

        return rval;
    }

    /*private SolidAbsorber getAbsorber(int layer){
       return (SolidAbsorber)layers.elementAt(layer);
    } */
    private EnergyLoss getFullLoss(int layer) {
        return (EnergyLoss) fullLosses.get(layer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private EnergyLoss getHalfLoss(int layer) {
        return (EnergyLoss) halfLosses.get(layer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNumberOfLayers() {
        return layers.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SolidAbsorber getLayer(int index) {
        return (SolidAbsorber) layers.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public DefaultListModel getTargetList() {
        return dlm_targets;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public DefaultComboBoxModel getComboModel() {
        return dcbm_targets;
    }

    /**
     * DOCUMENT ME!
     *
     * @param interaction_layer DOCUMENT ME!
     * @param projectile DOCUMENT ME!
     * @param Einit DOCUMENT ME!
     * @param thetaRadians DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double calculateProjectileEnergy(int interaction_layer, Nucleus projectile,
        double Einit, double thetaRadians) {
        double rval = Einit;
        rval -= (0.001 * getHalfLoss(interaction_layer)
                             .getEnergyLoss(projectile, Einit, thetaRadians));

        for (int i = interaction_layer + 1; i < layers.size(); i++) {
            rval -= (0.001 * getFullLoss(i)
                                 .getEnergyLoss(projectile, rval, thetaRadians));
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param interaction_layer DOCUMENT ME!
     * @param projectile DOCUMENT ME!
     * @param Efinal DOCUMENT ME!
     * @param thetaRadians DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double calculateInitialProjectileEnergy(int interaction_layer,
        Nucleus projectile, double Efinal, double thetaRadians) {
        double rval = Efinal;

        for (int i = layers.size() - 1; i > interaction_layer; i--) {
            rval = getFullLoss(i)
                       .reverseEnergyLoss(projectile, rval, thetaRadians);
        }

        rval = getHalfLoss(interaction_layer)
                   .reverseEnergyLoss(projectile, rval, thetaRadians);

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DefaultComboBoxModel getLayerNumberComboModel() {
        DefaultComboBoxModel rval = new DefaultComboBoxModel();

        for (int i = 0; i < layers.size(); i++) {
            rval.addElement(new Integer(i));
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layerIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DefaultComboBoxModel getLayerNuclideComboModel(int layerIndex) {
        DefaultComboBoxModel rval = new DefaultComboBoxModel();
        int[] Z = getLayer(layerIndex).getElements();

        for (int i = 0; i < Z.length; i++) {
            List possible = Nucleus.getIsotopes(Z[i]);

            for (int j = 0; j < possible.size(); j++) {
                rval.addElement(possible.get(j));
            }
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public Collection getTargetCollection() {
        return targets.values();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rval = "Target: " + name + "\n";

        for (int i = 0; i < layers.size(); i++) {
            rval += ("Layer " + i + ": Specification '");

            SolidAbsorber l = getLayer(i);
            rval += (l.getText() + "' " + l.getThickness() + " ug/cm^2\n");
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    static public void main(String[] args) {
        Target t = new Target("test");

        try {
            t.addLayer(new SolidAbsorber("C 1", 100));

            //t.addLayer(new SolidAbsorber("Si 1 O 2",170));
            Nucleus proj = new Nucleus(2, 6);
            int layer = 0;
            double Einit = 31.6;
            double thetaRad = Math.toRadians(7.5);
            double Efinal = t.calculateProjectileEnergy(layer, proj, Einit,
                    thetaRad);
            System.out.println("Einit = " + Einit + " -> Efinal = " + Efinal);
            Einit = t.calculateInitialProjectileEnergy(layer, proj, Efinal,
                    thetaRad);
            System.out.println("Efinal = " + Efinal + " -> Einit = " + Einit);
        } catch (NuclearException ne) {
            System.err.println(ne);
        }
    }
}
