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

/*
 * TargteDefinitionTable.java
 *
 * Created on December 17, 2001, 2:30 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables;

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.Target;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale
 */
public class TargetDefinitionTable extends javax.swing.JTable {
/**
     * Creates new TargteDefinitionTable
     */
    public TargetDefinitionTable(Target target) {
        super(new TargetDefinitionTableModel(target));
    }

    /**
     * Creates a new TargetDefinitionTable object.
     */
    public TargetDefinitionTable() {
        super(new TargetDefinitionTableModel());
    }

    /**
     * Returns a Target object constructed from entries in the table.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public Target makeTarget(String name) throws NuclearException {
        return ((TargetDefinitionTableModel) getModel()).makeTarget(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void addRow() {
        ((TargetDefinitionTableModel) getModel()).addRow();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void removeRow(int row) {
        ((TargetDefinitionTableModel) getModel()).removeLayer(row);
    }
}
