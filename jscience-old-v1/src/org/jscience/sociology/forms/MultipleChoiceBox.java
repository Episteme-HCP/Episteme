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
 * This abstract class is the superclass of classes representing the
 * multiple choice input elements of a form, like Checkboxes, Combo Boxes
 * and List Boxes.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see ChoiceBox
 * @see MenuBox
 * @see CheckBox
 */
public abstract class MultipleChoiceBox extends ChoiceBox {

    private Vector chosenValues = new Vector();
    private String checkedOrSelected;

    /**
     * Sets the initially selected value of this field.
     * In forms.xml you set the initial field value(s) like this:
     * <p/>
     * <p><blockquote><pre>
     * <p/>
     *  &lt;field-value state="selected">thevalue&lt;/field-value>
     *  or
     *  &lt;field-value state="checked">thevalue&lt;/field-value>
     * <p/>
     * </pre></blockquote><p>
     *
     * @param value the initially selected value.
     */
    public void setValue(String value) {
        chosenValues.addElement(value);
        super.setValue(value); // getValue() will then return this value
        // showing that a selection was made
    }

    void setCheckedOrSelected(String checkedOrSelected) {
        this.checkedOrSelected = checkedOrSelected;
    }

    String getCheckedOrSelected() {
        return checkedOrSelected;
    }

    Vector getChosenValues() {
        return chosenValues;
    }

    String getError() {
        if (isRequired() && chosenValues.size() == 0) return getErrorMessageForRequired();
        return "";
    }

    /**
     * Will return "selected", "checked" or "".
     * In case the choice represented by <code>value</code> is unselected, the empty string
     * will be returned.
     * <p/>
     * This method is used in the presentation layer of this API.
     * <p/>
     * This method is internally used by <code>generateXML()</code> and
     * <code>generateXmlAsString() methods of class <code>Form</code>.
     *
     * @param value the value of the MultipleChoiceBox item whose status is returned by this method.
     * @return "selected", "checked" or "".
     */
    public String chosen(String value) {
        for (int i = 0; i < chosenValues.size(); i++) { // looping through all selected values
            String checkedValue = (String) chosenValues.elementAt(i);
            if (checkedValue.equals(value)) return getCheckedOrSelected();
        }
        return "";
    }

    void clear() {
        chosenValues = new Vector();
        super.setValue("");
    }

}
