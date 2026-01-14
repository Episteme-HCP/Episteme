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
