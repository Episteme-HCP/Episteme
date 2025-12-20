/*
 * @(#)RadioButton.java  2.0, 2001/06
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

/**
 * This class extends ChoiceBox and represents the radiobutton field.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see FormElement
 * @see ChoiceBox
 */
public class RadioButton extends ChoiceBox {

    String getError() {
        if (isRequired() && getValue() == null) return getErrorMessageForRequired();
        return "";
    }

    /**
     * Will return "checked" or "".
     * In case the choice represented by <code>value</code> is unchecked, the empty string
     * will be returned.
     * <p/>
     * This method is used in the presentation layer of this API.
     * <p/>
     * This method is internally used by <code>generateXML()</code> and
     * <code>generateXmlAsString() methods of class <code>Form</code>.
     *
     * @param value the value of the RadioButton item whose status is returned by this method.
     * @return "checked" or "".
     */
    public String chosen(String value) {
        if (value == null) return "";
        if (value.equals(getValue())) return "checked";
        return "";
    }

}
