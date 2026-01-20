/*
 * @(#)MultipleChoiceBox.java  2.0, 2001/06
 *
 * Copyright (C) 2001 Ilirjan Ostrovica. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 * 
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
 * OF SUCH DAMAGE.
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
