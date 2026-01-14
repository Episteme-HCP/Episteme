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

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.SpancReaction;
import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.Target;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;


/**
 * Data model for <code>ReactionTable</code>.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 * @version 1.0
 */
public class ReactionTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static String[] headers = {
            "Reaction", "Beam", "Energy [MeV]", "B [kG]", "Target",
            "Interaction Layer", "Target Nuclide", "Projectile", "Residual",
            "Q [e]", "Theta [deg]"
        };

    /*private void iterateTable() throws KinematicsException {
        SpancReaction [] rxns = SpancReaction.getAllReactions();
        for (int i = 0; i< rxns.length; i++){
            addRow(rxns[i]);
        }
    }*/
    static java.text.DecimalFormat df = new java.text.DecimalFormat("0.#");

    /**
     * DOCUMENT ME!
     */
    Class[] columnClasses = {
            Integer.class, String.class, Double.class, Double.class,
            Target.class, Integer.class, String.class, String.class,
            String.class, Boolean.class, Integer.class, Double.class
        };

    /**
     * Creates a new ReactionTableModel object.
     *
     * @throws KinematicsException DOCUMENT ME!
     */
    public ReactionTableModel() throws KinematicsException {
        super(headers, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sr DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public void addRow(SpancReaction sr) throws NuclearException {
        Vector temp = new Vector(headers.length);
        temp.addElement(new Integer(getRowCount()));

        String beamString = sr.getBeam().toString();

        if (sr.getBeamUncertain()) {
            beamString += (" " + (char) 0xb1 + " " +
            df.format(sr.getBeam().getMass().error * 1000) + " keV");
        }

        temp.addElement(beamString);
        temp.addElement(new Double(sr.getEbeam()));
        temp.addElement(new Double(sr.getBfield()));
        temp.addElement(sr.getTarget().getName());
        temp.addElement(new Integer(sr.getInteractionLayer()));

        String targetString = sr.getTargetNuclide().toString();

        if (sr.getTargetUncertain()) {
            targetString += (" " + (char) 0xb1 + " " +
            df.format(sr.getTargetNuclide().getMass().error * 1000) + " keV");
        }

        temp.addElement(targetString);

        String projectileString = sr.getProjectile().toString();

        if (sr.getProjectileUncertain()) {
            projectileString += (" " + (char) 0xb1 + " " +
            df.format(sr.getProjectile().getMass().error * 1000) + " keV");
        }

        temp.addElement(projectileString);

        String residualString = sr.getResidual().toString();

        if (sr.getResidualUncertain()) {
            residualString += (" " + (char) 0xb1 + " " +
            df.format(sr.getResidual().getMass().error * 1000) + " keV");
        }

        temp.addElement(residualString);
        temp.addElement(new Integer(sr.getQ()));
        temp.addElement(new Double(sr.getTheta()));
        addRow(temp);
    }

    /*private void addEmptyRow(){
        Object [] temp = new Object[9];
        addRow(temp);
    }*/
    void refreshData() {
        while (getRowCount() > 0) {
            removeRow(0);
        }

        SpancReaction[] rxns = SpancReaction.getAllReactions();

        try {
            for (int i = 0; i < rxns.length; i++) {
                addRow(rxns[i]);
            }
        } catch (NuclearException ne) {
            System.err.println("Shouldn't be here.");
            ne.printStackTrace();
        }
    }

    /*public void notifyOfChange() throws KinematicsException{
        iterateTable();
    }*/
}
