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
package org.jscience.physics.nuclear.kinematics.nuclear.gui.table;

import org.jscience.physics.nuclear.kinematics.nuclear.*;

import javax.swing.table.AbstractTableModel;


/**
 * Data model for <code>KinematicsOutputTable</code>.
 *
 * @author <a href="mailto:dale@visser.name">Dale W Visser</a>
 * @version 1.0
 */
public class KinematicsOutputTableModel extends AbstractTableModel
    implements ReactionTableClient {
    /**
     * DOCUMENT ME!
     */
    String[] headers = {
            "T(1)", "Ex(4)", "Lab Deg(3)", "CM Deg(3)", "T(3)", "Lab Deg(4)",
            "T(4)", "Jac(3)", "k(3)", "z", "QBrho(3)",
        };

    /**
     * DOCUMENT ME!
     */
    Class[] columnClasses = {
            Double.class, Double.class, Double.class, Double.class, Double.class,
            Double.class, Double.class, Double.class, Double.class, Double.class,
            Double.class
        };

    /**
     * DOCUMENT ME!
     */
    Object[][] data = new Object[1][headers.length];

    /**
     * DOCUMENT ME!
     */
    double[] beamEnergies;

    /**
     * DOCUMENT ME!
     */
    double[] residExcite;

    /**
     * DOCUMENT ME!
     */
    double[] labAngles;

    //NuclearCollision reaction;
    /**
     * DOCUMENT ME!
     */
    Nucleus target;

    //NuclearCollision reaction;
    /**
     * DOCUMENT ME!
     */
    Nucleus beam;

    //NuclearCollision reaction;
    /**
     * DOCUMENT ME!
     */
    Nucleus projectile;

    /**
     * DOCUMENT ME!
     */
    EnergyLoss energyLoss;

    /**
     * Creates a new KinematicsOutputTableModel object.
     *
     * @param rt DOCUMENT ME!
     * @param beamEnergies DOCUMENT ME!
     * @param residExcite DOCUMENT ME!
     * @param labAngles DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public KinematicsOutputTableModel(ReactionTable rt, double[] beamEnergies,
        double[] residExcite, double[] labAngles)
        throws KinematicsException, NuclearException {
        super();
        this.beamEnergies = beamEnergies;
        this.residExcite = residExcite;
        this.labAngles = labAngles;
        //        new EnergyLoss();//called to initialize energy loss routines
        rt.setReactionTableClient(this);
        iterateTable();
        setValueAt(data[0][0], 0, 0);
    }

    /**
     * Sets reaction along with target thickness in ug/cm^2.
     *
     * @param target DOCUMENT ME!
     * @param beam DOCUMENT ME!
     * @param projectile DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setReaction(Nucleus target, Nucleus beam, Nucleus projectile)
        throws KinematicsException, NuclearException {
        this.target = target;
        this.beam = beam;
        this.projectile = projectile;

        if (energyLoss != null) {
            setTargetThickness(energyLoss.getAbsorber().getThickness());
        }

        iterateTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setTargetThickness(double thickness)
        throws KinematicsException, NuclearException {
        try {
            if (thickness != 0.0) {
                energyLoss = new EnergyLoss(new SolidAbsorber(thickness,
                            Absorber.MICROGRAM_CM2, target.getElementSymbol()));
            } else {
                energyLoss = null;
            }
        } catch (NuclearException ne) {
            System.err.println(getClass().getName() + ".setReaction(): " + ne);
        }

        iterateTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param be DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setBeamEnergies(double[] be)
        throws KinematicsException, NuclearException {
        beamEnergies = be;
        iterateTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param re DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setResidualExcitations(double[] re)
        throws KinematicsException, NuclearException {
        residExcite = re;
        iterateTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param la DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setLabAngles(double[] la)
        throws KinematicsException, NuclearException {
        labAngles = la;
        iterateTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return data.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return data[0].length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass(int c) {
        return columnClasses[c];
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int c) {
        return headers[c];
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int r, int c) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int r, int c) {
        return data[r][c];
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setValueAt(Object value, int r, int c) {
        data[r][c] = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    private void iterateTable() throws KinematicsException, NuclearException {
        double[] backBeamEnergy = new double[beamEnergies.length];
        double qbr_front;
        double qbr_back;

        /*reactions[0][i] = front of target; reactions[1][i]=back of target*/
        NuclearCollision[][] reactions = new NuclearCollision[2][beamEnergies.length * residExcite.length * labAngles.length];
        int numRows = 0;
        int counter = 0;

        for (int i = 0; i < beamEnergies.length; i++) {
            if (energyLoss != null) {
                backBeamEnergy[i] = beamEnergies[i] -
                    (0.001 * energyLoss.getThinEnergyLoss(beam, beamEnergies[i]));
            } else {
                backBeamEnergy[i] = beamEnergies[i];
            }

            for (int j = 0; j < residExcite.length; j++) {
                for (int k = 0; k < labAngles.length; k++) {
                    reactions[0][counter] = new NuclearCollision(target, beam,
                            projectile, beamEnergies[i], labAngles[k],
                            residExcite[j]);
                    numRows += reactions[0][counter].getAngleDegeneracy();

                    if (energyLoss != null) {
                        reactions[1][counter] = new NuclearCollision(target,
                                beam, projectile, backBeamEnergy[i],
                                labAngles[k], residExcite[j]);
                        numRows += reactions[0][counter].getAngleDegeneracy();
                    }

                    counter++;
                }
            }
        }

        data = new Object[numRows][headers.length];
        counter = 0;

        int row = 0;

        for (int i = 0; i < beamEnergies.length; i++) {
            for (int j = 0; j < residExcite.length; j++) {
                for (int k = 0; k < labAngles.length; k++) {
                    for (int l = 0;
                            l < reactions[0][counter].getAngleDegeneracy();
                            l++) {
                        setValueAt(new Double(beamEnergies[i]), row, 0);
                        setValueAt(new Double(residExcite[j]), row, 1);
                        setValueAt(new Double(labAngles[k]), row, 2);
                        setValueAt(new Double(reactions[0][counter].getCMAngleProjectile(
                                    l)), row, 3);

                        double Tproj = reactions[0][counter].getLabEnergyProjectile(l);

                        if (energyLoss != null) {
                            Tproj = Tproj -
                                (0.001 * energyLoss.getThinEnergyLoss(projectile,
                                    Tproj, Math.toRadians(labAngles[k])));
                        }

                        setValueAt(new Double(Tproj), row, 4);
                        setValueAt(new Double(reactions[0][counter].getLabAngleResidual(
                                    l)), row, 5);
                        setValueAt(new Double(reactions[0][counter].getLabEnergyResidual(
                                    l)), row, 6);
                        setValueAt(new Double(reactions[0][counter].getJacobianProjectile(
                                    l)), row, 7);

                        double kP = reactions[0][counter].getFocusParameter(l);
                        setValueAt(new Double(kP), row, 8);
                        setValueAt(new Double(50.12 - (57.01 * Math.abs(kP))),
                            row, 9);

                        if (energyLoss != null) {
                            qbr_front = NuclearCollision.getQBrho(projectile,
                                    Tproj);
                        } else {
                            qbr_front = reactions[0][counter].getQBrho(l);
                        }

                        setValueAt(new Double(qbr_front), row, 10);
                        row++;

                        if (energyLoss != null) {
                            setValueAt(new Double(backBeamEnergy[i]), row, 0);
                            setValueAt(new Double(residExcite[j]), row, 1);
                            setValueAt(new Double(labAngles[k]), row, 2);
                            setValueAt(new Double(reactions[1][counter].getCMAngleProjectile(
                                        l)), row, 3);
                            Tproj = reactions[1][counter].getLabEnergyProjectile(l);
                            setValueAt(new Double(Tproj), row, 4);
                            setValueAt(new Double(reactions[1][counter].getLabAngleResidual(
                                        l)), row, 5);
                            setValueAt(new Double(reactions[1][counter].getLabEnergyResidual(
                                        l)), row, 6);
                            setValueAt(new Double(reactions[1][counter].getJacobianProjectile(
                                        l)), row, 7);
                            kP = reactions[1][counter].getFocusParameter(l);
                            setValueAt(new Double(kP), row, 8);
                            setValueAt(new Double(50.12 -
                                    (57.01 * Math.abs(kP))), row, 9);
                            qbr_back = reactions[1][counter].getQBrho(l);
                            setValueAt(new Double(qbr_back), row, 10);
                            row++;
                        }
                    }

                    counter++;
                }
            }
        }

        fireTableDataChanged();
    }
}
