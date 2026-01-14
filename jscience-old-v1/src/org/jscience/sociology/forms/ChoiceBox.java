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

package org.jscience.sociology.forms;

import java.util.Vector;


/**
 * This abstract class is the superclass of classes representing
 * Checkboxes, Combo Boxes, List Boxes and RadioButtons.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 *
 * @see MenuBox
 * @see CheckBox
 * @see RadioButton
 */
public abstract class ChoiceBox extends FormElement {
    /**
     * DOCUMENT ME!
     */
    private Vector values = new Vector();

    /**
     * Adds a new value that can be checked or selected from this
     * ChoiceBox. This method is used by
     * org.jscience.sociology.forms.FormParser class.
     *
     * @param value another value to this ChoiceBox.
     */
    public void addToValues(String value) {
        if (!values.contains(value)) {
            values.addElement(value);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Vector getValues() { // used to generate xml form data

        return values;
    }

    /**
     * This method finds out whether the given value has been
     * checked/selected. If the given value has been checked/selected
     * respectively "checked" or "selected" is returned. Otherwise an empty
     * string is returned. This method gets implemented by subclasses
     * org.jscience.sociology.forms.MultipleChoiceBox and
     * org.jscience.sociology.forms.RadioButton.
     *
     * @param value the value of the ChoiceBox item whose status is returned by
     *        this method.
     *
     * @return "selected", "checked" or "".
     */
    public abstract String chosen(String value);
}
