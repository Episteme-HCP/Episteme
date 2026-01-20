/*
 * @(#)FormParser.java  2.0, 2001/06
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

import org.jscience.sociology.forms.view.ContentGenerator;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import java.util.Hashtable;


/**
 * This class is used to parse the forms.xml file. In order to have this
 * class work, you must provide a SAX2 compatible parser in the
 * <code>lib</code> directory of your application.
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 */
public class FormParser extends DefaultHandler {
    /**
     * DOCUMENT ME!
     */
    private static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * DOCUMENT ME!
     */
    private XMLReader xmlReader;

    /**
     * DOCUMENT ME!
     */
    private InputStream formsInputStream;

    /**
     * DOCUMENT ME!
     */
    private Hashtable forms; // key ->form name, value ->form itself

    /**
     * DOCUMENT ME!
     */
    private Form form;

    /**
     * DOCUMENT ME!
     */
    private ContentGenerator contentGenerator;

    /**
     * DOCUMENT ME!
     */
    private FormElement formElement;

    /**
     * DOCUMENT ME!
     */
    private GroupValidator groupValidator;

    /**
     * DOCUMENT ME!
     */
    private FieldValidator fieldValidator;

    /**
     * DOCUMENT ME!
     */
    private Hashtable groupValidators = new Hashtable(); // key ->group name, value ->group itself

    /**
     * DOCUMENT ME!
     */
    private Hashtable fieldValidators = new Hashtable();

    /**
     * DOCUMENT ME!
     */
    private boolean is_field_value;

    /**
     * DOCUMENT ME!
     */
    private boolean is_chosen_field_value;

    /**
     * DOCUMENT ME!
     */
    private boolean is_required_message;

    /**
     * DOCUMENT ME!
     */
    private boolean is_required_error_message;

    /**
     * DOCUMENT ME!
     */
    private String fieldType;

    /**
     * DOCUMENT ME!
     */
    private boolean is_field_validator;

    /**
     * DOCUMENT ME!
     */
    private boolean is_field_groupValidator;

    /**
     * DOCUMENT ME!
     */
    private Object o;

    /**
     * Creates a new FormParser object.
     *
     * @param xmlReader DOCUMENT ME!
     * @param formsInputStream DOCUMENT ME!
     */
    public FormParser(XMLReader xmlReader, InputStream formsInputStream) {
        this.xmlReader = xmlReader;
        this.formsInputStream = formsInputStream;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Hashtable getForms() {
        if (forms == null) {
            generateForms();
        }

        return forms;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ContentGenerator getContentGenerator() {
        if (contentGenerator == null) {
            try {
                contentGenerator = (ContentGenerator) generateObject(
                        "org.jscience.sociology.forms.util.SimpleContentGenerator");
            } catch (SAXException e) {
                System.out.println("----------------------" + NEW_LINE);
                System.out.println(
                    "Exception happend while trying to instantiate SimpleContentGenerator");
                System.out.println("The message is: " + e.getMessage());
                System.out.println("----------------------" + NEW_LINE);
            }
        }

        return contentGenerator;
    }

    /**
     * Overrides the same method of <code>DefaultHandler</code>.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     * @param atts DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void startElement(String namespaceURI, String localName,
        String qName, Attributes atts) throws SAXException {
        String type = atts.getValue("type");
        String name = atts.getValue("name");

        if (type != null) {
            o = generateObject(type);
        }

        if (qName.equals("forms-content-generator")) {
            if (!(o instanceof org.jscience.sociology.forms.view.ContentGenerator)) {
                throw new SAXException(type +
                    " does not implement ContentGenerator");
            }

            contentGenerator = (ContentGenerator) o;

            return;
        }

        if (qName.equals("form")) {
            String page = atts.getValue("page");
            String action = atts.getValue("action");
            String unbound = atts.getValue("unbound");

            form = new Form();
            form.setName(name);

            if (page != null) {
                form.setFormPagePath(page);
            }

            if (action != null) {
                form.setFormActionPath(action);
            }

            if ("false".equals(unbound)) {
                form.setUnboundBeforeActionPage(false);
            }

            // create forms Hashtable if it did not exist before
            if (forms == null) {
                forms = new Hashtable();
            }

            forms.put(name, form);

            return;
        }

        if (qName.equals("field")) {
            if (!(o instanceof org.jscience.sociology.forms.FormElement)) {
                throw new SAXException(type +
                    " does not implement ContentGenerator");
            }

            fieldType = type;
            formElement = (FormElement) o;
            formElement.setName(name);

            String required = atts.getValue("required");

            if (required != null) {
                boolean reqValue = (required.equals("true")) ? true : false;
                formElement.setRequired(reqValue);
            }

            return;
        }

        if (qName.equals("field-validator")) {
            is_field_validator = true;

            return;
        }

        if (qName.equals("forms-field-validator")) {
            if (!(o instanceof org.jscience.sociology.forms.FieldValidator)) {
                throw new SAXException(type +
                    " does not implement FieldValidator");
            }

            fieldValidator = (FieldValidator) o;
            fieldValidators.put(name, fieldValidator);
        }

        if (qName.equals("field-groupValidator")) {
            is_field_groupValidator = true;

            return;
        }

        if (qName.equals("forms-field-groupValidator")) {
            if (!(o instanceof org.jscience.sociology.forms.GroupValidator)) {
                throw new SAXException(type +
                    " does not implement GroupValidator");
            }

            groupValidator = (GroupValidator) o;

            String errormessage = atts.getValue("errormessage");

            if (errormessage != null) {
                groupValidator.setGroupErrorMessage(errormessage);
            }

            groupValidators.put(name, groupValidator);
        }

        if (qName.equals("field-value")) {
            is_field_value = true;

            if ("org.jscience.sociology.forms.TextBox".equals(fieldType) ||
                    "org.jscience.sociology.forms.PasswordBox".equals(fieldType) ||
                    "checked".equals(atts.getValue("state")) ||
                    "selected".equals(atts.getValue("state"))) {
                is_chosen_field_value = true;
            }

            return;
        }

        if (qName.equals("field-required-message")) {
            is_required_message = true;

            return;
        }

        if (qName.equals("field-required-error-message")) {
            is_required_error_message = true;

            return;
        }
    }

    /**
     * Overrides the same method of <code>DefaultHandler</code>.
     *
     * @param chars DOCUMENT ME!
     * @param iStart DOCUMENT ME!
     * @param iLen DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void characters(char[] chars, int iStart, int iLen)
        throws SAXException {
        String s = new String(chars, iStart, iLen).trim();

        if ((s.length() == 0) || NEW_LINE.equals(s)) {
            return;
        }

        if (is_field_value) {
            if (is_chosen_field_value) {
                formElement.setValue(s);
            }

            if ("org.jscience.sociology.forms.CheckBox".equals(fieldType) ||
                    "org.jscience.sociology.forms.MenuBox".equals(fieldType) ||
                    "org.jscience.sociology.forms.RadioButton".equals(fieldType)) {
                ((org.jscience.sociology.forms.ChoiceBox) formElement).addToValues(s);
            }
        }

        if (is_required_message) {
            formElement.setMessageForRequired(s);
        }

        if (is_required_error_message) {
            formElement.setErrorMessageForRequired(s);
        }

        if (is_field_groupValidator) {
            // pick it up from the Hashtable
            o = groupValidators.get(s);

            if (o == null) {
                throw new SAXException("'" + s +
                    "' has not been defined as 'forms-field-groupValidator' in forms.xml");
            }

            formElement.setGroupValidator((GroupValidator) o);
            is_field_groupValidator = false;

            return;
        }

        if (is_field_validator) {
            o = fieldValidators.get(s);

            if (o == null) {
                throw new SAXException("'" + s +
                    "' has not been defined as 'forms-field-validator' in forms.xml");
            }

            ((TextBox) formElement).addFieldValidator((FieldValidator) o);
            is_field_validator = false;

            return;
        }
    }

    /**
     * Overrides the same method of <code>DefaultHandler</code>.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     */
    public void endElement(String namespaceURI, String localName, String qName) {
        if (qName.equals("form")) {
            form.init();

            return;
        }

        if (qName.equals("field")) {
            form.addField(formElement);
            fieldType = null;

            return;
        }

        if (qName.equals("field-value")) {
            is_field_value = false;
            is_chosen_field_value = false;

            return;
        }

        if (qName.equals("field-required-message")) {
            is_required_message = false;

            return;
        }

        if (qName.equals("field-required-error-message")) {
            is_required_error_message = false;

            return;
        }
    }

    /**
     * Overrides the same method of <code>DefaultHandler</code>.
     *
     * @param e DOCUMENT ME!
     */
    public void warning(SAXParseException e) {
        System.out.println("------ WARNING ------");
        System.out.println("\tLine number:\t" + e.getLineNumber());
        System.out.println("\tColumn number:\t" + e.getColumnNumber());
        System.out.println("\tMessage:\t" + e.getMessage());
        System.out.println("----------------------" + NEW_LINE);
    }

    /**
     * Overrides the same method of <code>DefaultHandler</code>.
     *
     * @param e DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void error(SAXParseException e) throws SAXException {
        System.out.println("------ ERROR ------");
        System.out.println("\tLine number:\t" + e.getLineNumber());
        System.out.println("\tColumn number:\t" + e.getColumnNumber());
        System.out.println("\tMessage:\t" + e.getMessage());
        System.out.println("----------------------" + NEW_LINE);
        throw new SAXException(
            "An Error occured, see message above for more details");
    }

    /**
     * Overrides the same method of <code>DefaultHandler</code>.
     *
     * @param e DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void fatalError(SAXParseException e) throws SAXException {
        System.out.println("------ FATAL ERROR ------");
        System.out.println("\tLine number:\t" + e.getLineNumber());
        System.out.println("\tColumn number:\t" + e.getColumnNumber());
        System.out.println("\tMessage:\t" + e.getMessage());
        System.out.println("----------------------" + NEW_LINE);
        throw new SAXException("Fatal Error encountered - parsing terminated.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    private Object generateObject(String type) throws SAXException {
        Object o = null;

        try {
            Class c = Class.forName(type);
            o = c.newInstance();
        } catch (ClassNotFoundException e) {
            throw new SAXException("No class named '" + type + "' was found.");
        } catch (InstantiationException e) {
            throw new SAXException("Class named '" + type +
                "' could not be  instantiated.");
        } catch (IllegalAccessException e) {
            throw new SAXException("No access to class named '" + type + "'.");
        }

        return o;
    }

    /**
     * DOCUMENT ME!
     */
    private void generateForms() {
        try {
            xmlReader.setFeature("http://xml.org/sax/features/validation", true);
            xmlReader.setFeature("http://xml.org/sax/features/namespaces", false);
            xmlReader.setContentHandler(this);
            xmlReader.setErrorHandler(this);
            xmlReader.parse(new InputSource(formsInputStream));
        } catch (SAXNotRecognizedException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("\tOoops.. cannot activate validation." +
                NEW_LINE);
            System.out.println("----------------------" + NEW_LINE);
        } catch (SAXNotSupportedException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("\tOoops.. cannot activate validation." +
                NEW_LINE);
            System.out.println("----------------------" + NEW_LINE);
        } catch (SAXException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("\tOoops.. a SAXException has happend : ");
            System.out.println("\tThe message is : " + e.getMessage() +
                NEW_LINE);
            System.out.println("----------------------" + NEW_LINE);
        } catch (IOException e) {
            System.out.println("----------------------" + NEW_LINE);
            System.out.println("\tOoops.. an IOException has happend : ");
            System.out.println("\tThe message is : " + e.getMessage() +
                NEW_LINE);
            System.out.println("----------------------" + NEW_LINE);
        }
    }
}
