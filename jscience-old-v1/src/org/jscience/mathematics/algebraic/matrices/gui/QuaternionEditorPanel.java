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

package org.jscience.mathematics.algebraic.matrices.gui;

import org.jscience.mathematics.algebraic.matrices.Double3Vector;
import org.jscience.mathematics.algebraic.numbers.Quaternion;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * An instance of this class is used by the QuaternionEditor class to
 * display 4 MinMaxPanels representing a Quaternion.
 */
public class QuaternionEditorPanel extends JPanel {
    /** DOCUMENT ME! */
    static final public String QUATERNION_PROPERTY = "quaternion";

    /** DOCUMENT ME! */
    MinMaxPanel rScrol;

    /** DOCUMENT ME! */
    MinMaxPanel iScrol;

    /** DOCUMENT ME! */
    MinMaxPanel jScrol;

    /** DOCUMENT ME! */
    MinMaxPanel kScrol;

    /** DOCUMENT ME! */
    Quaternion value;

    /** DOCUMENT ME! */
    boolean changing;

/**
     * Constructs a new QuaternionEditorPanel initializing 4 MinMaxPanels
     * titled r, i, j and k. The MinMaxPanel r represents the real part of a
     * Quaternion, i, j and k represent the 3 imaginary parts of a Quaternion.
     */
    public QuaternionEditorPanel() {
        value = new Quaternion(0, 0, 0, 0);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1f;
        gbc.insets.top = 10;
        add(rScrol = new MinMaxPanel("r"), gbc);
        rScrol.getModel().addChangeListener(new QuaternionChangeListener(0));
        gbc.insets.top = 0;
        add(iScrol = new MinMaxPanel("i"), gbc);
        iScrol.getModel().addChangeListener(new QuaternionChangeListener(1));
        add(jScrol = new MinMaxPanel("j"), gbc);
        jScrol.getModel().addChangeListener(new QuaternionChangeListener(2));
        gbc.insets.bottom = 10;
        add(kScrol = new MinMaxPanel("k"), gbc);
        kScrol.getModel().addChangeListener(new QuaternionChangeListener(3));
    }

    /**
     * Assigns quat to the Quaternion of this QuaternionEditorPanel.
     * Fires a PropertyChangeEvent with property name {@link
     * #QUATERNION_PROPERTY} and null for oldValue and newValue. Get the
     * updated value by calling one of the getQuaternion methods.
     *
     * @param quat DOCUMENT ME!
     */
    public void setQuaternion(Quaternion quat) {
        if (!value.equals(quat)) {
            try {
                changing = true;

                Double3Vector double3Vector = quat.imag();
                value = new Quaternion(quat.real(), double3Vector.x,
                        double3Vector.y, double3Vector.z);
                rScrol.setValue(quat.real());
                iScrol.setValue(double3Vector.x);
                jScrol.setValue(double3Vector.y);
                kScrol.setValue(double3Vector.z);

                firePropertyChange(QUATERNION_PROPERTY, null, null);
            } finally {
                changing = false;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Quaternion getQuaternion() {
        Double3Vector double3Vector = value.imag();

        return new Quaternion(value.real(), double3Vector.x, double3Vector.y,
            double3Vector.z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void getQuaternion(Quaternion target) {
        Double3Vector double3Vector = value.imag();
        target = new Quaternion(value.real(), double3Vector.x, double3Vector.y,
                double3Vector.z);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class QuaternionChangeListener implements ChangeListener {
        /** DOCUMENT ME! */
        final int index;

        /** DOCUMENT ME! */
        final double[] quaternion = new double[4];

/**
         * Creates a new QuaternionChangeListener object.
         *
         * @param i DOCUMENT ME!
         */
        QuaternionChangeListener(int i) {
            index = i;
        }

        /**
         * DOCUMENT ME!
         *
         * @param ev DOCUMENT ME!
         */
        public void stateChanged(ChangeEvent ev) {
            if (changing) {
                return;
            }

            final DoubleBoundedRangeModel src = (DoubleBoundedRangeModel) ev.getSource();
            quaternion[0] = value.real();
            quaternion[1] = value.imag().x;
            quaternion[2] = value.imag().y;
            quaternion[3] = value.imag().z;
            quaternion[index] = src.getDoubleValue();
            value = new Quaternion(quaternion[0], quaternion[1], quaternion[2],
                    quaternion[3]);
            firePropertyChange(QUATERNION_PROPERTY, null, null);
        }
    }
}
