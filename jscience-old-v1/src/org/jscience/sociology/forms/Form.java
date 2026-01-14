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

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.io.StringReader;
import java.util.*;

/**
 * This class represents a set of data input elements that are represented by
 * <code>FormElement</code> objects. The instantiation and initialization
 * of one Form object is done only once when parsing the forms.xml file.
 * The Form object is then serialized for later use in producing instances
 * of class Form (for each new client request).
 *
 * @author Ilirjan Ostrovica
 * @version 2.0, 2001/06
 * @see FormElement
 * @see FieldValidator
 * @see GroupValidator
 * @see FormHandler
 * @see SimpleContentGenerator
 */
public class Form implements Serializable {

    private FormElement[] formElements;
    private String name;
    private Hashtable groups;
    private ArrayList nameFieldList;

    private String formPagePath;
    private String formActionPath;
    private boolean unboundBeforeActionPage = true;
    private boolean firstime = true;

    void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this <code>Form</code> object.
     * In <code>forms.xml</code>, the user sets the name of the form via
     * the <code>name</code> attribute of the <code>form</code> tag.
     *
     * @return the name that identifies this <code>Form</code> object.
     */
    public String getName() {
        return name;
    }

    void setUnboundBeforeActionPage(boolean unboundBeforeActionPage) {
        this.unboundBeforeActionPage = unboundBeforeActionPage;
    }

    /**
     * This method returns a flag that defines one of the two following
     * behaviors :<p>
     * <code>true</code> - once the form validation goes well,
     * the user session must be cleared of the <code>Form</code> object
     * and of any other object related with it.
     * This work has to be done in the <code>forwardToFormAction()</code> method of
     * the <code>ContentGenerator</code>.
     * <p/>
     * <code>false</code> - the user session must not get cleared of the
     * <code>Form</code> object or of any other object related with it, even
     * after the successful form validation.
     * This is specifically useful in the case you use a confirmation page
     * as the "action" of your form. In this case, not clearing the session means that if the
     * user wants to go back from the confirmation page to the form, the last
     * entries will still be preserved.
     * <p/>
     * In <code>forms.xml</code>, the user sets the value of this flag via the <code>unbound</code> attribute
     * of the <code>form</code> tag.
     *
     * @return the flag.
     */
    public boolean isUnboundBeforeActionPage() {
        return unboundBeforeActionPage;
    }

    void setFirstime(boolean firstime) {
        this.firstime = firstime;
    }

    /**
     * This method returns a flag that shows whether this is the first time
     * the form is shown to the current user, or otherwise.
     * <p/>
     * This method can be used by <code>sendFormContent()</code> method of
     * the <code>ContentGenerator</code>.
     * See the source code of <code>SimpleContentGenerator</code> as an
     * example.
     *
     * @return the flag.
     */
    public boolean isFirstime() {
        return firstime;
    }

    void init() {
        Object[] o = nameFieldList.toArray();
        formElements = new FormElement[o.length];
        for (int i = 0; i < o.length; i++)
            formElements[i] = (FormElement) o[i];
        // create groups
        groups = createGroups();
    }

    /**
     * This method returns an array of all <code>FormElement</code>-s
     * of this form.
     * <p/>
     * This method can be used by a <code>ContentGenerator</code> class.
     * See the source code of <code>SimpleContentGenerator</code> for such an
     * example.
     *
     * @return an array of <code>FormElement</code>-s.
     */
    public FormElement[] getFormElements() {
        return formElements;
    }

    void addField(FormElement formElement) {
        if (nameFieldList == null)
            nameFieldList = new ArrayList();

        nameFieldList.add(formElement);
    }

    void setFormPagePath(String formPagePath) {
        this.formPagePath = formPagePath;
    }

    void setFormActionPath(String formActionPath) {
        this.formActionPath = formActionPath;
    }

    /**
     * This method returns the URL path to the presentation page of this form.
     * In forms.xml, the user sets this path through the attribute <code>page</code>
     * of <code>form</code> tag. This path should be relative to application root.
     * <p/>
     * This method has to be used by <code>ContentGenerator</code> classes.
     * See the source code of <code>SimpleContentGenerator</code> as an example.
     *
     * @return a string representing the path to this form presentation page.
     */
    public String getFormPagePath() {
        return formPagePath;
    }

    /**
     * This method returns the form's "action" path that was entered through
     * the <code>form</code> tag's <code>action</code> attribute (in forms.xml).
     * This path should be relative to application root.
     * <p/>
     * This method has to be used by <code>ContentGenerator</code> classes.
     * See the source code of <code>SimpleContentGenerator</code> as an example.
     *
     * @return a string representing the URL to the processing Servlet or JSP.
     */
    public String getFormActionPath() {
        return formActionPath;
    }

    private Hashtable createGroups() {

        // creating the Hashtable (fieldGroup, vector of fields)
        Hashtable toReturn = new Hashtable();
        HashSet allGroups = groupsSet();
        if (allGroups == null) return null;

        GroupValidator fg = null;
        Vector v = null;
        Iterator it = allGroups.iterator();
        while (it.hasNext()) {
            fg = (GroupValidator) it.next();
            v = new Vector();
            toReturn.put(fg, v);
            for (int i = 0; i < formElements.length; i++) {
                if (formElements[i].getGroupValidator() == fg)
                    v.addElement(formElements[i]);
            }
        }
        return toReturn;
    }

    private HashSet groupsSet() {
        // finding all groups and putting them in a HashSet
        HashSet toReturn = new HashSet();
        for (int i = 0; i < formElements.length; i++) {
            GroupValidator gv = formElements[i].getGroupValidator();
            if (gv != null) toReturn.add(gv); // will not allow dublicates
        }
        if (toReturn.size() == 0) return null;
        return toReturn;
    }

    boolean validate(HttpServletRequest req) {
        // no request parameter came
        if (!req.getParameterNames().hasMoreElements()) return false;

        boolean toReturn = true;
        String[] val = null;
        String errorMess = null;

        // individual errors
        for (int i = 0; i < formElements.length; i++) {
            FormElement fe = formElements[i];
            fe.setFirstime(false);
            fe.clear();
            val = req.getParameterValues(fe.getName());
            if (val != null) {
                for (int j = 0; j < val.length; j++) fe.setValue(val[j]);
            }
            errorMess = fe.getError();
            if (!errorMess.equals("")) toReturn = false;
            // update the error message
            fe.setErrorMessage(errorMess);
        }

        // group errors
        if (groups != null) { // there are groups
            Enumeration en = groups.keys();
            // looping through groups
            while (en.hasMoreElements()) {
                GroupValidator gv = (GroupValidator) en.nextElement(); // key
                Vector fields = (Vector) groups.get(gv); // value (fields in one group)

                // calling the method from the GroupValidator interface
                Hashtable nameErrorPairs = gv.getErrorMessages(getNameValuePairs(fields));
                if (nameErrorPairs != null) {
                    toReturn = false;
                    // update the error messages (will override the individual errors)
                    registerGroupErrors(fields, nameErrorPairs);
                }
            }
        }
        return toReturn;
    }

    private Hashtable getNameValuePairs(Vector fields) {
        Hashtable toReturn = new Hashtable();
        FormElement fe = null;
        Object value = null;

        for (int i = 0; i < fields.size(); i++) {
            fe = (FormElement) fields.elementAt(i);
            if (fe instanceof MultipleChoiceBox) {
                Vector v = ((MultipleChoiceBox) fe).getChosenValues();
                if (v.size() == 0) value = new String[]{""};
                else value = fromVectorToArrayOfStrings(v);
            } else {
                value = fe.getValue();
                if (value == null) value = ""; // in case of non text fields
            }
            toReturn.put(fe.getName(), value);
        }
        return toReturn;
    }

    private void registerGroupErrors(Vector fields, Hashtable nameErrorPairs) {
        FormElement fe = null;
        String error = null;
        for (int i = 0; i < fields.size(); i++) {
            fe = (FormElement) fields.elementAt(i);
            error = (String) nameErrorPairs.get(fe.getName());

            if (error != null) // there was an error
                fe.setErrorMessage(error);
        }
    }

    /**
     * Returns a StringReader object that contains this form presentation data in
     * XML format.
     * This method can be used by <code>ContentGenerator</code> classes for
     * generating the presentation of this form in any desired format, HTML included.
     *
     * @return a StringReader containing this form presentation data in XML format.
     */
    public StringReader generateXML() {
        return new StringReader(generateXmlAsString());
    }

    /**
     * Returns a String that contains this form presentation data in an
     * XML format.
     *
     * @return a String containing this form presentation data in XML format.
     */
    public String generateXmlAsString() {
        StringBuffer toReturn = new StringBuffer();
        FormElement field = null;
        String NEW_LINE = System.getProperty("line.separator");
        Vector v = null;
        String value = null;
        toReturn.append(" <form name=\"").append(name).append("\" >").append(NEW_LINE)
                .append(NEW_LINE);

        for (int i = 0; i < formElements.length; i++) {
            field = formElements[i];
            toReturn.append("  <form-element type=\"").append(field.getClass().getName()).append("\" >").append(NEW_LINE)
                    .append("   <field-name>").append(field.getName()).append("</field-name>").append(NEW_LINE);
            if (field instanceof org.jscience.sociology.forms.ChoiceBox) {
                org.jscience.sociology.forms.ChoiceBox cb = (org.jscience.sociology.forms.ChoiceBox) field;
                v = cb.getValues();
                for (int j = 0; j < v.size(); j++) {
                    value = (String) v.elementAt(j);
                    toReturn.append("   <field-value state=\"").append(cb.chosen(value)).append("\">").append(value).append("</field-value>").append(NEW_LINE);
                }
            } else {
                toReturn.append("   <field-value>").append(field.getValue()).append("</field-value>").append(NEW_LINE);
            }
            toReturn.append("   <field-error>").append(field.getErrorMessage()).append("</field-error>").append(NEW_LINE)
                    .append("  </form-element>").append(NEW_LINE)
                    .append(NEW_LINE);
        }
        toReturn.append(" </form>").append(NEW_LINE);
        return toReturn.toString();
    }

    private String[] fromVectorToArrayOfStrings(Vector vec) {
        Object[] o = vec.toArray();
        String[] toReturn = new String[o.length];
        for (int i = 0; i < o.length; i++)
            toReturn[i] = (String) o[i];
        return toReturn;
    }

}
