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

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.Target;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;
import org.jscience.physics.nuclear.kinematics.nuclear.SolidAbsorber;

import javax.swing.table.DefaultTableModel;


/**
 * Data model for <code>TargetDefinitionTable</code>.
 *
 * @author <a href="mailto:dale@visser.name">Dale W. Visser</a>
 * @version 1.0
 */
public class TargetDefinitionTableModel extends DefaultTableModel {
    /**
     * DOCUMENT ME!
     */
    static String[] headers = { "Layer", "Components", "Thickness [ug/cm^2]" };

    /**
     * DOCUMENT ME!
     */
    Class[] columnClasses = { Integer.class, String.class, Double.class };

    /**
     * DOCUMENT ME!
     */
    Target _target;

    /**
     * DOCUMENT ME!
     */
    Object[] defaultRowData = { new Integer(0), "C 1", new Double(20.0) };

    /**
     * Creates a new TargetDefinitionTableModel object.
     *
     * @param target DOCUMENT ME!
     */
    public TargetDefinitionTableModel(Target target) {
        super(headers, target.getNumberOfLayers());
        _target = target;
        setLayerNumbers();
        setComponentsColumn();
        setThicknesses();
    }

    /**
     * Creates a new TargetDefinitionTableModel object.
     */
    public TargetDefinitionTableModel() {
        super(headers, 0);
        addRow(defaultRowData);
    }

    /**
     * DOCUMENT ME!
     */
    private void setLayerNumbers() {
        for (int i = 0; i < _target.getNumberOfLayers(); i++) {
            setValueAt(new Integer(i), i, 0);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setComponentsColumn() {
        for (int i = 0; i < _target.getNumberOfLayers(); i++) {
            setValueAt(_target.getLayer(i).getText(), i, 1);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setThicknesses() {
        for (int i = 0; i < _target.getNumberOfLayers(); i++) {
            setValueAt(new Double(_target.getLayer(i).getThickness()), i, 2);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    Target makeTarget(String name) throws NuclearException {
        double thickness = 20;
        Target rval = new Target(name);

        for (int row = 0; row < getRowCount(); row++) {
            Object spec = getValueAt(row, 1);

            //System.out.println(getValueAt(row,2).getClass().getName());
            Object o_thickness = getValueAt(row, 2);

            if (o_thickness instanceof Double) {
                thickness = ((Double) o_thickness).doubleValue();
            } else if (o_thickness instanceof String) {
                thickness = Double.parseDouble((String) o_thickness);
            } else {
                System.err.println("Thickness not String or Double?!:" +
                    o_thickness.getClass().getName() +
                    "; Setting thickness to default: " + thickness);
            }

            SolidAbsorber layer = new SolidAbsorber((String) spec, thickness);
            rval.addLayer(layer);
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     */
    void addRow() {
        addRow(defaultRowData);
        renumberLayers();
    }

    /**
     * DOCUMENT ME!
     */
    private void renumberLayers() {
        for (int row = 0; row < getRowCount(); row++)
            setValueAt(new Integer(row), row, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    void removeLayer(int row) {
        removeRow(row);
        renumberLayers();
    }
}
