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

import javax.swing.table.DefaultTableModel;


/**
 * Data model for <code>ResidualTable</code>.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 * @version 1.0
 */
public class ResidualTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static String[] headers = {
            "Peak", "fitted rho [cm]", "Residual [cm]", "Resid./Sigma"
        };

    /**
     * DOCUMENT ME!
     */
    static java.text.DecimalFormat df = new java.text.DecimalFormat("0.000#");

    /**
     * DOCUMENT ME!
     */
    CalibrationFit calFit;

    /**
     * Creates a new ResidualTableModel object.
     *
     * @param cf DOCUMENT ME!
     */
    public ResidualTableModel(CalibrationFit cf) {
        super(headers, 0);
        calFit = cf;
    }

    /**
     * DOCUMENT ME!
     */
    synchronized void updateResiduals() {
        while (getRowCount() > 0) {
            removeRow(0);
        }

        if (calFit.hasFit()) {
            for (int i = 0; i < calFit.getDataSize(); i++) {
                Object[] rowData = new Object[4];
                rowData[0] = new Integer(i);
                rowData[1] = df.format(calFit.calculateFit(i));
                rowData[2] = df.format(calFit.getResidual(i));
                rowData[3] = df.format(calFit.getNormalizedResidual(i));
                this.addRow(rowData);
            }
        }
    }
}
