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
