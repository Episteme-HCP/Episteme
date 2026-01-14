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

import java.util.Hashtable;

/**
 * Defines the interface for classes that will play the role of group-validators.
 * <br>
 * The simplest criteria of inter-validation in a group of fields is -
 * fields of one group are not required (to be filled or selected) unless
 * one of them is. This criteria of validation is implemented by <code>org.jscience.sociology.forms.util.SimpleGroup</code> class.<br>
 * Another common criteria is used when submitting a password -
 * fields of one group must have the same value.
 * Class <code>org.jscience.sociology.forms.util.PasswordGroup</code>
 * provides such an implementation for two password fields.
 * <br>
 * In forms.xml you add a <code>GroupValidator</code> like this:
 * <p/>
 * <p><blockquote><pre>
 * &lt;field name="field name"
 *   type="org.jscience.sociology.forms.TextBox">
 *   ...............
 *   ...............
 *   &lt;field-groupValidator>
 *      GroupValidator object name
 *   &lt;/field-groupValidator>
 * &lt;/field>
 * </pre></blockquote><p>
 * <p/>
 * One FormElement object can not be registered in more than one GroupValidator.
 */

public interface GroupValidator extends java.io.Serializable {

    /**
     * <pre>
     * This method takes as argument an Hashtable of :
     *  key   - a string representing the field name.
     *  value - a string or an array of strings representing the value, or the values
     *  of the corresponding field.
     * <p/>
     * This method must return an Hashtable of :
     *  key   - a string representing the name of the field that resulted with
     *  an error message (according to validation).
     *  value - a string representing the error message of the field.
     * or it must return <code>null</code>, in case the validation found no errors.
     * <p/>
     * Five important notes :
     * 1 - With TextBox-es, PasswordBox-es and RadioButton-s, their value
     * will come as a simple string.
     * 2 - With subclasses of MultipleChoiceBox-es, their value(s) will come
     * as an array of strings.
     * 3 - When the client does not enter a value or does not make a selection
     * to a field, the value of that field will come as an empty string, or as
     * an array of only one string, whose value is empty.
     * 4 - The nameValuePairs Hashtable will include all fields of this group.
     * 5 - Only name-errormessage pairs of the fields with error messages must
     * be included in the returned Hashtable, and if there aren't any, <code>null</code>
     * must be returned.
     * </pre>
     *
     * @param nameValuePairs name-value pairs to be tested.
     * @return a Hashtable of name-errormessage pairs.
     */
    Hashtable getErrorMessages(Hashtable nameValuePairs);

    /**
     * This is a utility method that can be used to set or customize
     * an error message that may be used by all fields of this group.
     * <p/>
     * For example, in a group of password fields you might want to set an
     * error message like "(passwords must be the same)" that will apply to
     * both fields.
     * Implementation classes may supply a default error message,
     * and then let this method to customize it.
     * See <code>org.jscience.sociology.forms.util.PasswordGroup</code> source code.
     * <p/>
     * In <code>forms.xml</code> you set this error message through the attribute
     * <code>errormessage</code> of the <code>field-groupValidator</code> tag.
     * <p/>
     * If you are not interested in this method, you can just empty implement it
     * and then ignore the <code>errormessage</code> attribute of
     * <code>field-groupValidator</code> tag.
     *
     * @param groupErrorMessage an error message for all fields of the group.
     */
    void setGroupErrorMessage(String groupErrorMessage);

}
