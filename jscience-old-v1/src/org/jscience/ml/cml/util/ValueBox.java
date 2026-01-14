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

package org.jscience.ml.cml.util;

import java.awt.*;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class ValueBox extends JPanel {
    /** DOCUMENT ME! */
    JComboBox comboBox;

    /** DOCUMENT ME! */
    String[] values;

/**
     * Creates a new ValueBox object.
     *
     * @param prompt DOCUMENT ME!
     * @param values DOCUMENT ME!
     */
    public ValueBox(String prompt, String[] values) {
        this.values = values;
        this.setLayout(new BorderLayout());
        this.add(new JLabel(prompt), BorderLayout.WEST);
        comboBox = new JComboBox();

        this.add(comboBox, BorderLayout.CENTER);

        for (int i = 0; i < values.length; i++) {
            comboBox.addItem(values[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void setSelectedIndex(int index) {
        comboBox.setSelectedIndex(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectedIndex() {
        return comboBox.getSelectedIndex();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSelectedValue() {
        return (String) values[comboBox.getSelectedIndex()];
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setSelectedValue(String value) {
        int index = CMLUtils.indexOf(value, values, values.length);
        comboBox.setSelectedIndex(index);
    }
}
