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

package org.jscience.swing;

import java.util.Date;

import javax.swing.*;


/**
 * A simple specialized <code>JSpinner</code> using
 * <code>java.util.Date</code> for a value.
 *
 * @author Holger Antelmann
 */
public class JTimeChooser extends JSpinner {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5524090997350592980L;

/**
     * Creates a new JTimeChooser object.
     */
    public JTimeChooser() {
        super(new SpinnerDateModel());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(this);
        setEditor(editor);
    }

/**
     * Creates a new JTimeChooser object.
     *
     * @param time DOCUMENT ME!
     */
    public JTimeChooser(Date time) {
        this();
        setTime(time);
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     */
    public void setTime(Date date) {
        setValue(date);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTime() {
        return (Date) getValue();
    }
}
