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

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.OutputPeak;


/**
 * Table for displaying reactions in Spanc.
 *
 * @author Dale Visser.
 * @version 1.0
 */
public class OutputPeakTable extends javax.swing.JTable {
/**
     * Constructor.
     */
    public OutputPeakTable() {
        super(new OutputPeakTableModel());
        setOpaque(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param op DOCUMENT ME!
     */
    public void addRow(OutputPeak op) {
        ((OutputPeakTableModel) getModel()).addRow(op);
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
     *
     * @param row DOCUMENT ME!
     */
    public void removeRow(int row) {
        ((javax.swing.table.DefaultTableModel) getModel()).removeRow(row);
    }

    /**
     * DOCUMENT ME!
     */
    public void refreshData() {
        ((OutputPeakTableModel) getModel()).refreshData();
    }

    /**
     * DOCUMENT ME!
     *
     * @param adjust DOCUMENT ME!
     */
    synchronized public void adjustErrors(boolean adjust) {
        ((OutputPeakTableModel) getModel()).adjustErrors(adjust);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ((OutputPeakTableModel) getModel()).toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExportTableText() {
        return ((OutputPeakTableModel) getModel()).getExportTableText();
    }
}
