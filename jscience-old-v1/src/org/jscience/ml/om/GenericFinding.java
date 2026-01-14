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

/* ====================================================================
 * /GenericFinding.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;

import org.jscience.ml.om.util.SchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * The class GenericFinding provides a generic implementation of
 * org.jscience.ml.om.IFinding.<br>
 * The GenericFinding class stores a description of the findings
 * and provides simple access to this description field.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.3
 */
public class GenericFinding extends Finding {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    // XSML schema instance value. Enables class/schema loaders to identify this
    // class
    private static final String XML_XSI_TYPE_VALUE = "fgca:findingsType";

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------

    public GenericFinding(Node findingElement) throws SchemaException {

        super(findingElement);

    }

    // -------------------------------------------------------------------
    public GenericFinding(String description) throws SchemaException {

        super(description);

    }

    // -------------------------------------------------------------------
    public GenericFinding(String ID, String description) throws SchemaException {

        super(ID, description);

    }

    // -------------
    // SchemaElement -----------------------------------------------------
    // -------------

    // -------------------------------------------------------------------

    /**
     * Returns a display name for this element.<br>
     * The method differs from the toString() method as toString() shows
     * more technical information about the element. Also the formating of
     * toString() can spread over several lines.<br>
     * This method returns a string (in one line) that can be used as
     * displayname in e.g. a UI dropdown box.
     *
     * @return Returns a String with a one line display name
     * @see java.lang.Object.toString();
     */
    public String getDisplayName() {

        return this.getDescription().substring(0, 7);

    }

    // ------
    // Object ------------------------------------------------------------
    // ------

    // -------------------------------------------------------------------

    /**
     * Overwrittes toString() method from java.lang.Object.<br>
     * Returns the field values of this GenericFinding.
     *
     * @return This GenericFinding field values
     * @see java.lang.Object
     */
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("GenericFinding: Description=");
        buffer.append(super.getDescription());

        return buffer.toString();

    }


    // -------------------------------------------------------------------
    /**
     * Overwrittes equals(Object) method from java.lang.Object.<br>
     * Checks if this GenericFinding and the given Object are equal. Two GenericFinding
     * are equal if both return the same string from their toString() method and
     * both XSI types are equal.<br>
     *
     * @param obj The Object to compare this GenericFinding with.
     * @return <code>true</code> if both Objects are instances from class GenericFinding,
     *         their XSI type is equal and their fields contain the same values.
     *         (Can be checked with calling and comparing both objects toString() method)
     * @see java.lang.Object
     */
    public boolean equals(Object obj) {

        if (obj == null || !(obj instanceof GenericFinding)) {
            return false;
        }

        if ((this.toString().equals(obj.toString()))
                && (this.getXSIType().equals(((IFinding) obj).getXSIType()))
                ) {
            return true;
        }

        return false;

    }

    // ------------------------
    // IExtendableSchemaElement ------------------------------------------
    // ------------------------

    // -------------------------------------------------------------------

    /**
     * Returns the XML schema instance type of the implementation.<br>
     * Example:<br>
     * <target xsi:type="myOwnTarget"><br>
     * </target><br>
     *
     * @return The xsi:type value of this implementation
     */
    public String getXSIType() {

        return GenericFinding.XML_XSI_TYPE_VALUE;

    }

    // -------
    // Finding -----------------------------------------------------------
    // -------

    // -------------------------------------------------------------------

    /**
     * Adds this GenericFinding to an given parent XML DOM Element.
     * The GenericFinding Element will be set as a child element of
     * the passed Element.
     *
     * @param parent The parent element for this GenericFinding
     * @return Returns the Element given as parameter with this
     *         GenericFinding as child Element.<br>
     *         Might return <code>null</code> if parent was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element parent) {

        if (parent == null) {
            return null;
        }

        Document ownerDoc = parent.getOwnerDocument();

        Element e_Finding = super.createXmlFindingElement(parent);

        // Set XSI:Type
        e_Finding.setAttribute(IFinding.XML_XSI_TYPE,
                GenericFinding.XML_XSI_TYPE_VALUE);

        parent.appendChild(e_Finding);

        return parent;

    }

}
