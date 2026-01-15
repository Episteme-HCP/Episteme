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
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.CalibrationFit;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.CalibrationPeak;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.SpancReaction;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * DOCUMENT ME!
 *
 * @author Dale Visser
 * @version 1.0
 */
public class CalibrationPeakTableModel extends DefaultTableModel {
    /** DOCUMENT ME! */
    static String[] headers = {
            "Reaction", "Ex Projectile [MeV]", "Ex Residual [MeV]", "rho [cm]",
            "Channel"
        };

    /** DOCUMENT ME! */
    static java.text.DecimalFormat df = new java.text.DecimalFormat("0.000#");

/**
     * Creates a new CalibrationPeakTableModel object.
     */
    public CalibrationPeakTableModel() {
        super(headers, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cp DOCUMENT ME!
     */
    public void addRow(CalibrationPeak cp) {
        Vector temp = new Vector(5);
        temp.addElement(new Integer(cp.getReactionIndex()));
        temp.addElement(new Double(cp.getExProjectile()));
        temp.addElement(cp.getExResidual());

        try {
            temp.addElement(cp.getRho());
        } catch (KinematicsException ke) {
            System.err.println(ke);
        } catch (NuclearException ke) {
            System.err.println(ke);
        }

        temp.addElement(cp.getChannel());
        addRow(temp);
    }

    /**
     * DOCUMENT ME!
     */
    void refreshData() {
        while (getRowCount() > 0) {
            removeRow(0);
        }

        Iterator peaks = CalibrationPeak.getPeakCollection().iterator();

        while (peaks.hasNext())
            addRow((CalibrationPeak) peaks.next());
    }

    /**
     * DOCUMENT ME!
     *
     * @param calfit DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExportTableText(CalibrationFit calfit) {
        String rval = "";

        for (int i = 0, n = getRowCount(); i < n; i++) {
            CalibrationPeak cp = CalibrationPeak.getPeak(i);
            int rxnNum = cp.getReactionIndex();
            SpancReaction sr = SpancReaction.getReaction(rxnNum);
            rval += (sr.description() + "\t");
            rval += (sr.getBfield() + "\t");
            rval += (cp.getExProjectile() + "\t");

            UncertainNumber exRes = cp.getExResidual();
            rval += (exRes.value + "\t" + exRes.error + "\t");

            UncertainNumber channel = cp.getChannel();
            rval += (channel.value + "\t" + channel.error + "\t");

            try {
                UncertainNumber rho = cp.getRho(); //input
                rval += (rho.value + "\t" + rho.error + "\t");
                rho = calfit.getRho(channel); //from calibration
                rval += (rho.value + "\t" + rho.error + "\t");
            } catch (Exception e) {
                System.err.println(e);
            }

            rval += (calfit.getResidual(i) + "\n");
        }

        return rval;
    }
}
