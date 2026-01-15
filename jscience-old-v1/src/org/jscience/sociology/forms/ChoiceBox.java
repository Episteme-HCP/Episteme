/*
 * @(#)ChoiceBox.java  2.0, 2001/06
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
