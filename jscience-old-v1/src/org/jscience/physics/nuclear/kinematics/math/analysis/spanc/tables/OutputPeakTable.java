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
