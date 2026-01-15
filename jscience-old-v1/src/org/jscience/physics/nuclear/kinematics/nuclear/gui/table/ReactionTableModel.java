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

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearCollision;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale Visser
 * @version 1.0
 */
public class ReactionTableModel extends AbstractTableModel {
    /**
     * DOCUMENT ME!
     */
    private static final UncertainNumber gs = new UncertainNumber(0.0); //represents g.s. excitation

    //NuclearCollision react;
    /**
     * DOCUMENT ME!
     */
    Nucleus target;

    //NuclearCollision react;
    /**
     * DOCUMENT ME!
     */
    Nucleus beam;

    //NuclearCollision react;
    /**
     * DOCUMENT ME!
     */
    Nucleus projectile;

    /**
     * DOCUMENT ME!
     */
    ReactionTableClient rtc;

    /** Parent component for warning dialogs. */
    private Component parent;

    /**
     * DOCUMENT ME!
     */
    String[] headers = { "", "A,Z", "Mass [Mev]" };

    /**
     * DOCUMENT ME!
     */
    Class[] columnClasses = { String.class, Object.class, UncertainNumber.class };

    //used Object.class instead of Nucleus.class to get editing working
    /**
     * DOCUMENT ME!
     */
    Object[][] data = new Object[5][3];

    /**
     * DOCUMENT ME!
     */
    private Nucleus last_target;

    /**
     * DOCUMENT ME!
     */
    private Nucleus last_beam;

    /**
     * DOCUMENT ME!
     */
    private Nucleus last_projectile;

    /**
     * Creates a new ReactionTableModel object.
     *
     * @param parent DOCUMENT ME!
     */
    public ReactionTableModel(Component parent) {
        super();
        data[0][0] = "Target(1)";
        data[1][0] = "Beam(2)";
        data[2][0] = "Projectile(3)";
        data[3][0] = "Residual(4)";
        data[4][0] = "Q0";

        try {
            data[0][1] = new Nucleus(6, 12);
            data[1][1] = data[0][1];
            data[2][1] = new Nucleus(2, 4);
            data[3][1] = new Nucleus(10, 20);
        } catch (NuclearException e) {
            System.err.println(e);
        }

        for (int i = 0; i < data.length; i++) {
            data[i][2] = new UncertainNumber(0.0);
        }

        this.parent = parent;
        setValueAt(data[0][0], 0, 0); //force calculation of table
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
        //System.out.print("Row "+r+", Col "+c+": ");
        if ((c != 1) || (r > 2)) {
            //System.out.println("not");
            return false;
        }

        //System.out.println("editable");
        return true;
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
        if (c == 1) {
            if (value instanceof String) {
                Object temp = data[r][c];

                try {
                    data[r][c] = Nucleus.parseNucleus((String) value);
                } catch (NuclearException ne) {
                    JOptionPane.showMessageDialog(parent, ne.getMessage(),
                        "Nucleus name error", JOptionPane.ERROR_MESSAGE);
                    data[r][c] = temp;
                }
            } else if (value instanceof Nucleus) {
                data[r][c] = value;
            }
        } else {
            data[r][c] = value;
        }

        target = (Nucleus) data[0][1];
        beam = (Nucleus) data[1][1];
        projectile = (Nucleus) data[2][1];

        try {
            //react=new NuclearCollision(target,beam,projectile);
            if (rtc != null) {
                rtc.setReaction(target, beam, projectile);
            }

            data[3][1] = NuclearCollision.getResidual(target, beam, projectile,
                    gs);
            data[4][2] = NuclearCollision.getQValue(target, beam, projectile, gs);
            /* if successful store thes locally */
            last_target = target;
            last_beam = beam;
            last_projectile = projectile;
        } catch (KinematicsException ke) {
            System.err.println(ke);
            JOptionPane.showMessageDialog(parent, ke.getMessage(),
                "Kinematics Exception", JOptionPane.WARNING_MESSAGE);
            /* revert */
            data[0][1] = last_target;
            data[1][1] = last_beam;
            data[2][1] = last_projectile;
        } catch (NuclearException ke) {
            System.err.println(ke);
            JOptionPane.showMessageDialog(parent, ke.getMessage(),
                "Kinematics Exception", JOptionPane.WARNING_MESSAGE);
            /* revert */
            data[0][1] = last_target;
            data[1][1] = last_beam;
            data[2][1] = last_projectile;
        }

        for (int i = 0; i < 4; i++) {
            data[i][2] = ((Nucleus) data[i][1]).getMass();
        }

        fireTableDataChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param rtc DOCUMENT ME!
     *
     * @throws KinematicsException DOCUMENT ME!
     * @throws NuclearException DOCUMENT ME!
     */
    public void setReactionTableClient(ReactionTableClient rtc)
        throws KinematicsException, NuclearException {
        this.rtc = rtc;

        if (rtc == null) {
            System.err.println("RTM.setRTC():should have rxn at this point");
        } else {
            rtc.setReaction(target, beam, projectile);
        }
    }
}
