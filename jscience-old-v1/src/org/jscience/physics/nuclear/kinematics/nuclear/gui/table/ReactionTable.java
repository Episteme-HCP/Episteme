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

package org.jscience.physics.nuclear.kinematics.nuclear.gui.table;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;
import org.jscience.physics.nuclear.kinematics.nuclear.KinematicsException;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.Nucleus;

import javax.swing.*;


/**
 * Table at top of JRelKin window showing the reaction.
 *
 * @author <a href="mailto:dale@visser.name">Dale W Visser</a>
 * @version 1.0
 */
public class ReactionTable extends JTable {
    /** data model */
    ReactionTableModel rtm;

/**
     * Creates the table.
     *
     * @param rtm data model
     */
    public ReactionTable(ReactionTableModel rtm) {
        super(rtm);
        this.rtm = rtm;
        setOpaque(true);

        //setDefaultRenderer(Nucleus.class, new NucleusRenderer());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getBeam() {
        return (Nucleus) rtm.getValueAt(1, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getTarget() {
        return (Nucleus) rtm.getValueAt(0, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getProjectile() {
        return (Nucleus) rtm.getValueAt(2, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Nucleus getResidual() {
        return (Nucleus) rtm.getValueAt(3, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UncertainNumber getQ0() {
        return (UncertainNumber) rtm.getValueAt(4, 2);
    }

    /**
     * Sets the client for data.
     *
     * @param rtc receiver
     *
     * @throws KinematicsException if something goes wrong
     * @throws NuclearException DOCUMENT ME!
     */
    public void setReactionTableClient(ReactionTableClient rtc)
        throws KinematicsException, NuclearException {
        rtm.setReactionTableClient(rtc);
    }
}
