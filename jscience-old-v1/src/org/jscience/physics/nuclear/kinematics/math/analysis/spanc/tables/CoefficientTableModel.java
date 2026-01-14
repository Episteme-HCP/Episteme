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
 * Data model for <code>CoefficientTable</code>.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 * @version 1.0
 */
public class CoefficientTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static String[] headers = { "Coefficient", "Value", "Covar 0", "Covar 1" };

    /**
     * DOCUMENT ME!
     */
    static java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");

    /**
     * DOCUMENT ME!
     */
    static java.text.DecimalFormat df_sci = new java.text.DecimalFormat(
            "0.000E0");

    /**
     * DOCUMENT ME!
     */
    CalibrationFit calFit;

    /**
     * Creates a new CoefficientTableModel object.
     *
     * @param cf DOCUMENT ME!
     */
    public CoefficientTableModel(CalibrationFit cf) {
        super(headers, 0);
        calFit = cf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param number DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String format(double number) {
        if (Math.abs(number) >= 0.01) {
            return df.format(number);
        }

        return df_sci.format(number);
    }

    /**
     * DOCUMENT ME!
     */
    synchronized void updateCoefficients() {
        this.setColumnCount(2);

        while (getRowCount() > 0) {
            removeRow(0);
        }

        if (calFit.hasFit()) {
            int covarDim = calFit.getOrder() + 1;

            for (int i = 0; i < covarDim; i++)
                this.addColumn("Covar " + i);

            for (int i = 0; i < covarDim; i++) {
                Object[] rowData = new Object[2 + covarDim];
                rowData[0] = "a" + i;
                rowData[1] = calFit.getCoefficient(i);

                for (int j = 0; j < covarDim; j++) {
                    rowData[2 + j] = format(calFit.getCovariance(i, j));
                }

                this.addRow(rowData);
            }
        }
    }
}
