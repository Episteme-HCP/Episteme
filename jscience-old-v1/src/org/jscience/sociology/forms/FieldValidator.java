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
 * Defines the interface for classes that will play the role of value validators
 * for org.jscience.sociology.forms.TextBox and org.jscience.sociology.forms.PasswordBox fields.
 * The criteria of validation will be implemented through
 * <code>getErrorMessage()</code> method of this interface.
 * A class of type TextBox will register one or more FieldValidator objects via
 * <code>addFieldValidator()</code> method.
 * <p/>
 * The following shows how to add a <code>FieldValidator</code> to a field in forms.xml:
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
 * <p/>
 * A FieldValidator object will normally be shared by all client threads of
 * one or even more forms, therefore the implementation has to take care of
 * possible synchronization issues.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see TextBox
 * @see PasswordBox
 */
public interface FieldValidator extends java.io.Serializable {
    /**
     * Gets the error message resulting from the validation. If the
     * supplied value passes the test (or tests) implemented by this method,
     * an empty string must be returned.
     *
     * @param value the value to be tested.
     *
     * @return the error message or an empty string in case the value has
     *         successfully passed the test.
     */
    public String getErrorMessage(String value);
}
