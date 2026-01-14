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
 * This class represents textfields and textareas, as well as serves as
 * the superclass of PasswordBox class.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see FormElement
 * @see PasswordBox
 * @see FieldValidator
 */
public class TextBox extends FormElement {

    private Vector fieldValidators;

    public TextBox() {
        setValue("");
    }

    /**
     * Adds a <code>FieldValidator</code> object to the list of validators already registered with this TextBox.
     * No adding will take place in case the coming <code>fieldValidator</code> is already registered.
     * <p/>
     * In forms.xml you add a <code>FieldValidator</code> like this:
     * <p/>
     * <p><blockquote><pre>
     * &lt;field name="field name"
     *   type="org.jscience.sociology.forms.TextBox">
     *   ...............
     *   ...............
     *   &lt;field-validator>
     *      FieldValidator object name
     *   &lt;/field-validator>
     * &lt;/field>
     * </pre></blockquote><p>
     *
     * @param fieldValidator FieldValidator object to be registered with this TextBox.
     */
    public void addFieldValidator(FieldValidator fieldValidator) {
        if (fieldValidators == null) fieldValidators = new Vector();
        if (fieldValidators.contains(fieldValidator)) return;
        fieldValidators.addElement(fieldValidator);
    }

    String getError() {
        if (isRequired() && getValue().equals("")) return getErrorMessageForRequired();
        return getErrMess();
    }

    private String getErrMess() {
        if (getValue().equals("") && !isRequired()) return "";
        if (fieldValidators != null) {
            FieldValidator fc = null;
            String err = null;
            for (int i = 0; i < fieldValidators.size(); i++) {
                fc = (FieldValidator) fieldValidators.elementAt(i);
                err = fc.getErrorMessage(getValue());
                if (!err.equals("")) return err;
            }
        }
        return "";
    }

}
