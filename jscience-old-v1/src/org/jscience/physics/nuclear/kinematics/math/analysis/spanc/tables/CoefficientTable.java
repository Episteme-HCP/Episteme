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

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.CalibrationFit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


/**
 * Table for displaying calibration coefficients in Spanc.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 * @version 1.0
 */
public class CoefficientTable extends JTable {
/**
     * Constructor.
     *
     * @param cf the object containing a calibration fit
     */
    public CoefficientTable(CalibrationFit cf) {
        super(new CoefficientTableModel(cf));
        setOpaque(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    synchronized public void updateCoefficients() {
        //if (getColumnCount() > 2) removeColumnSelectionInterval(2, getColumnCount()-1);
        ((CoefficientTableModel) getModel()).updateCoefficients();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void removeRow(int row) {
        ((DefaultTableModel) getModel()).removeRow(row);
    }
}
