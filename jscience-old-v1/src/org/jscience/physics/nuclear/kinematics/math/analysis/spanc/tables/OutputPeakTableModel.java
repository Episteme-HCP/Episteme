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

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.OutputPeak;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.SpancReaction;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale Visser
 * @version 1.0
 */
public class OutputPeakTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static String[] headers = {
            "Reaction", "Ex Projectile [MeV]", "Channel", "rho [cm]",
            "Ex Residual [MeV]"
        };

    /**
     * DOCUMENT ME!
     */
    static java.text.DecimalFormat df = new java.text.DecimalFormat("0.000#");

    /**
     * DOCUMENT ME!
     */
    private boolean adjustError = false;

    /**
     * Creates a new OutputPeakTableModel object.
     */
    public OutputPeakTableModel() {
        super(headers, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param op DOCUMENT ME!
     */
    public void addRow(OutputPeak op) {
        Vector temp = new Vector(5);
        temp.addElement(new Integer(op.getReactionIndex()));
        temp.addElement(new Double(op.getExProjectile()));
        temp.addElement(op.getChannel());

        try {
            temp.addElement(op.getRho(adjustError));
        } catch (KinematicsException ke) {
            System.err.println(ke);
        }

        temp.addElement(op.getExResidual(adjustError));
        addRow(temp);
    }

    /**
     * DOCUMENT ME!
     */
    void refreshData() {
        while (getRowCount() > 0) {
            removeRow(0);
        }

        Iterator peaks = OutputPeak.getPeakCollection().iterator();

        while (peaks.hasNext())
            addRow((OutputPeak) peaks.next());
    }

    /**
     * DOCUMENT ME!
     *
     * @param adjust DOCUMENT ME!
     */
    synchronized void adjustErrors(boolean adjust) {
        this.adjustError = adjust;
        refreshData();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String rval = "";

        for (int i = 0; i < headers.length; i++) {
            rval += headers[i];

            if (i < (headers.length - 1)) {
                rval += "\t";
            } else {
                rval += "\n";
            }
        }

        for (int i = 0, n = getRowCount(); i < n; i++) {
            OutputPeak op = OutputPeak.getPeak(i);
            rval += (op.getReactionIndex() + "\t");
            rval += (op.getExProjectile() + "\t");
            rval += (op.getChannel().plusMinusString() + "\t");

            try {
                rval += (op.getRho(false).plusMinusString() + "\t");
            } catch (KinematicsException ke) {
                System.err.println(ke);
            }

            rval += (op.getExResidual(false).plusMinusString() + "\n");
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExportTableText() {
        String rval = "";

        for (int i = 0, n = getRowCount(); i < n; i++) {
            OutputPeak op = OutputPeak.getPeak(i);
            int rxnNum = op.getReactionIndex();
            SpancReaction sr = SpancReaction.getReaction(rxnNum);
            rval += (sr.description() + "\t");
            rval += (sr.getBfield() + "\t");
            rval += (op.getExProjectile() + "\t");

            UncertainNumber exRes = op.getExResidual(false);
            rval += (exRes.value + "\t" + exRes.error + "\t");
            rval += (op.getChannel().value + "\t" + op.getChannel().error +
            "\t");
            rval += "\t\t";

            try {
                UncertainNumber rho = op.getRho(false);
                rval += (rho.value + "\t" + rho.error + "\t");
            } catch (KinematicsException ke) {
                System.err.println(ke);
                rval += "Problem calculating calibration rho";
            }

            rval += "\n";
        }

        return rval;
    }
}
