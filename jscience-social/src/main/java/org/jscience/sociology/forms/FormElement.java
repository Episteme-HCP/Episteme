/*
 * @(#)FormElement.java  2.0, 2001/06
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
 * This abstract class is the superclass of all classes that represent
 * form data input elements.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see TextBox
 * @see PasswordBox
 * @see ChoiceBox
 * @see MultipleChoiceBox
 * @see MenuBox
 * @see RadioButton
 * @see CheckBox
 */

public abstract class FormElement implements java.io.Serializable {

    private String name;
    private String value;
    private String errorMessage;
    private boolean required;
    private boolean firstime = true;
    private String messageForRequired = "(required)";
    private String errorMessageForRequired = "(required field)";
    private GroupValidator groupValidator;

    void setGroupValidator(GroupValidator groupValidator) {
        this.groupValidator = groupValidator;
    }

    GroupValidator getGroupValidator() {
        return groupValidator;
    }

    /**
     * Sets the initial value of this element.
     * By default this value is set to an empty string.
     * In forms.xml you set the initial field value like this:
     * <p><blockquote><pre>
     * &lt;field name="field name"
     *  type="org.jscience.sociology.forms.TextBox">
     *  &lt;field-value>thevalue&lt;/field-value>
     *   ...............
     *   ...............
     * &lt;/field>
     * </pre></blockquote><p>
     * The subclass <code>MultipleChoiceBox</code> overrides this method.
     *
     * @param value the initial value for this element.
     */
    public void setValue(String value) {
        this.value = value.trim();
    }

    /**
     * Sets the name of this element. This name is used for bounding
     * this FormElement object to the HttpSession, as well as for naming
     * this field in it's graphical presentation (which is the name of
     * the corresponding request parameter).
     * In forms.xml you set the name of this field as the attribute <code>name</code>
     * of the corresponding <code>field</code> tag, for example:
     * <p><blockquote><pre>
     * <p/>
     *  &lt;field type="org.jscience.sociology.forms.PasswordBox"
     *         name="password">
     *   .......................
     *   .......................
     *  &lt;/field>
     * <p/>
     * </pre></blockquote><p>
     *
     * @param name the name of this element.
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the <code>required</code> boolean state of this element. The value <code>true</code>
     * indicates that user input is required. Otherwise an error message
     * will be generated, and the form will be returned to the client.
     * The default value is <code>false</code>
     *
     * @param required the new required state for this element.
     */
    void setRequired(boolean required) {
        this.required = required;
    }

    void setFirstime(boolean firstime) {
        this.firstime = firstime;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Sets the message which lets the client know that this field is required.
     * The default value is <code>"(required)"</code>
     *
     * @param messageForRequired the message.
     */
    void setMessageForRequired(String messageForRequired) {
        this.messageForRequired = messageForRequired;
    }

    /**
     * Sets the error message which lets the client know that he should have provided
     * some input for this element.
     * By default this message is set to <code>"(required field)"</code>
     *
     * @param errorMessageForRequired the error message.
     */
    void setErrorMessageForRequired(String errorMessageForRequired) {
        this.errorMessageForRequired = errorMessageForRequired;
    }

    /**
     * Gets the name of this element. This is also the name of the
     * corresponding request parameter.
     * The built-in JSP-Bean presentation solution bounds this <code>FormElement</code>
     * object to the HttpSession, using this name as a key.
     * <p/>
     * See the source code of <code>org.jscience.sociology.forms.util.SimpleContentGenerator</code>.
     *
     * @return the name of this <code>FormElement</code> object.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value of this element.
     * Subclasses of <code>MultipleChoiceBox</code> can have more than
     * one value and in this case this method will return the value
     * that was set last.
     *
     * @return the value.
     */
    public String getValue() {
        return value;
    }

    boolean isRequired() {
        return required;
    }

    String getMessageForRequired() {
        return messageForRequired;
    }

    String getErrorMessageForRequired() {
        return errorMessageForRequired;
    }

    /**
     * Gets the error message generated by this object.
     * This error message shows what was wrong with the last client entry.
     *
     * @return the error message.
     */
    public String getErrorMessage() {
        if (firstime) {
            if (isRequired()) return getMessageForRequired();
            else return "";
        }
        return errorMessage;
    }

    abstract String getError();

    void clear() {
        // do nothing
    }
}
