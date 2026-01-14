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

package org.jscience.physics.solids.gui.util;

import java.awt.*;

import javax.swing.*;


/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class DoubleField extends JPanel {
    /**
     * DOCUMENT ME!
     */
    private NamedDouble namedDouble;

    /**
     * DOCUMENT ME!
     */
    private JTextField textField = new JTextField();

/**
     * Creates a new instance of DoubleField
     */
    public DoubleField(NamedDouble nd) {
        namedDouble = nd;
    }

    /**
     * DOCUMENT ME!
     */
    private void initComponents() {
        setLayout(new GridLayout(1, 2, 5, 5));
        add(new JLabel(namedDouble.name));
        add(textField);
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     */
    public void setValue(double val) {
        textField.setText(String.valueOf(val));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NumberFormatException DOCUMENT ME!
     */
    public double getValue() throws NumberFormatException {
        String val = textField.getText();
        double ret = Double.valueOf(val).doubleValue();

        return ret;
    }
}
