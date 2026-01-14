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
 * /Eyepiece.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

import org.jscience.ml.om.util.SchemaException;

import org.w3c.dom.*;


/**
 * Eyepiece implements the class org.jscience.ml.om.IEyepiece. An Eyepiece
 * describes a optical eyepiece. The model name and the focalLength are
 * mandatory fields which have to be set.
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public class Eyepiece extends SchemaElement implements IEyepiece {
    // ------------------
    /**
     * DOCUMENT ME!
     */
    private String model = new String();

    // Vendor name of eyepiece (TeleVue, Meade, Vixen....)
    /**
     * DOCUMENT ME!
     */
    private String vendor = null;

    // Focal length of the eyepiece. 
    /**
     * DOCUMENT ME!
     */
    private float focalLength = Float.NaN;

    // Apparent field of view. Angles values cannot be 0 or negative.
    /**
     * DOCUMENT ME!
     */
    private Angle apparentFOV = null;

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of an Eyepiece from an given XML Schema
     * Node.<br>
     * Normally this constructor is only used by org.jscience.ml.om.util.SchemaLoader
     *
     * @param eyepiece The XML Schema element that represents this eyepiece
     * @throws IllegalArgumentException if parameter is <code>null</code>,
     * @throws SchemaException          if the given Node does not match the XML Schema
     *                                  specifications
     */
    public Eyepiece(Node eyepiece)
        throws SchemaException, IllegalArgumentException {
        if (eyepiece == null) {
            throw new IllegalArgumentException(
                "Parameter eyepiece node cannot be NULL. ");
        }

        // Cast to element as we need some methods from it
        Element eyepieceElement = (Element) eyepiece;

        // Helper classes
        NodeList children = null;
        Element child = null;

        // Getting data
        // First mandatory stuff and down below optional data

        // Get ID from element
        String ID = eyepieceElement.getAttribute(SchemaElement.XML_ELEMENT_ATTRIBUTE_ID);

        if ((ID != null) && ("".equals(ID.trim()))) {
            throw new SchemaException("Eyepiece must have a ID. ");
        }

        super.setID(ID);

        // Get mandatory model
        children = eyepieceElement.getElementsByTagName(IEyepiece.XML_ELEMENT_MODEL);

        if ((children == null) || (children.getLength() != 1)) {
            throw new SchemaException(
                "Eyepiece must have exact one model name. ");
        }

        child = (Element) children.item(0);

        String model = null;

        if (child == null) {
            throw new SchemaException("Eyepiece must have a model name. ");
        } else {
            if (child.getFirstChild() != null) {
                model = child.getFirstChild().getNodeValue();
            } else {
                throw new SchemaException(
                    "Eyepiece cannot have an empty model name. ");
            }
        }

        this.setModel(model);

        // Get mandatory focalLength
        child = null;
        children = eyepieceElement.getElementsByTagName(IEyepiece.XML_ELEMENT_FOCALLENGTH);

        if ((children == null) || (children.getLength() != 1)) {
            throw new SchemaException(
                "Eyepiece must have exact one focal length. ");
        }

        child = (Element) children.item(0);

        String focalLength = null;

        if (child == null) {
            throw new SchemaException("Eyepiece must have a focal length. ");
        } else {
            focalLength = child.getFirstChild().getNodeValue();
        }

        this.setFocalLength(Float.parseFloat(focalLength));

        // Get optional vendor
        child = null;
        children = eyepieceElement.getElementsByTagName(IEyepiece.XML_ELEMENT_VENDOR);

        String vendor = null;

        if (children != null) {
            if (children.getLength() == 1) {
                child = (Element) children.item(0);

                if (child != null) {
                    vendor = child.getFirstChild().getNodeValue();

                    if (vendor != null) {
                        this.setVendor(vendor);
                    }
                } else {
                    throw new SchemaException(
                        "Problem while retrieving vendor from eyepiece. ");
                }
            } else if (children.getLength() > 1) {
                throw new SchemaException("Eyepiece can have only one vendor. ");
            }
        }

        // Get optional apparent field of view
        child = null;
        children = eyepieceElement.getElementsByTagName(IEyepiece.XML_ELEMENT_APPARENTFOV);

        Angle apparentFOV = null;

        if (children != null) {
            if (children.getLength() == 1) {
                child = (Element) children.item(0);

                if (child != null) {
                    String value = child.getFirstChild().getNodeValue();
                    String unit = child.getAttribute(Angle.XML_ATTRIBUTE_UNIT);
                    apparentFOV = new Angle(Double.parseDouble(value), unit);

                    if (apparentFOV != null) {
                        this.setApparentFOV(apparentFOV);
                    }
                } else {
                    throw new SchemaException(
                        "Problem while retrieving apparent field of view from eyepiece. ");
                }
            } else if (children.getLength() > 1) {
                throw new SchemaException(
                    "Eyepiece can have only one apparent field of view. ");
            }
        }
    }

    // -------------------------------------------------------------------
/**
     * Constructs a new instance of an Eyepiece.<br>
     *
     * @param model       The eyepieces model name
     * @param focalLength The focal length of the eyepiece
     * @throws IllegalArgumentException if model is <code>null</code> or
     *                                  focalLength is Float.NaN
     */
    public Eyepiece(String model, float focalLength)
        throws IllegalArgumentException {
        this.setModel(model);
        this.setFocalLength(focalLength);
    }

    // -------------
    // SchemaElement -----------------------------------------------------
    // -------------

    // -------------------------------------------------------------------
    /**
     * Returns a display name for this element.<br>
     * The method differs from the toString() method as toString() shows more
     * technical information about the element. Also the formating of
     * toString() can spread over several lines.<br>
     * This method returns a string (in one line) that can be used as
     * displayname in e.g. a UI dropdown box.
     *
     * @return Returns a String with a one line display name
     *
     * @see java.lang.Object.toString();
     */
    public String getDisplayName() {
        String dn = this.getModel() + " " + this.getFocalLength();

        if ((this.vendor != null) && !("".equals(this.vendor.trim()))) {
            dn = this.getVendor() + " " + dn;
        }

        return dn;
    }

    // ------
    // Object ------------------------------------------------------------
    // ------

    // -------------------------------------------------------------------
    /**
     * Overwrittes toString() method from java.lang.Object.<br>
     * Returns all fields of the class Eyepiece (unset field will be ignored).
     * The result string will look like this:<br>
     * Example:<br>
     * <code> Eyepiece Model: Ultra Wide Angle 8.8mm<br>
     * *  Vendor: Meade Focal length: 8.8 Apparent field of view: 84 DEG
     * </code>
     *
     * @return A string representing the eyepiece
     *
     * @see java.lang.Object
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("Eyepiece Model: ");
        buffer.append(this.getModel());

        if (vendor != null) {
            buffer.append("Vendor: ");
            buffer.append(this.getVendor());
        }

        buffer.append("Focal length: ");
        buffer.append(this.getFocalLength());

        if (apparentFOV != null) {
            buffer.append("Apparent field of view: ");
            buffer.append(this.getApparentFOV());
        }

        return buffer.toString();
    }

    // -------------------------------------------------------------------
    /**
     * Overwrittes equals(Object) method from java.lang.Object.<br>
     * Checks if this Eyepiece and the given Object are equal. The given
     * object is equal with this Eyepiece, if its implementing IEyepiece and
     * its modelname, vendorname, focalLength and apparent field of view
     * matches the values of this Eyepiece.
     *
     * @param obj The Object to compare this Eyepiece with.
     *
     * @return <code>true</code> if the given Object is an instance of
     *         IEyepiece and its modelname, vendorname, focalLength and
     *         apparent field of view matches with this Eyepiece.<br>
     *         *
     *
     * @see java.lang.Object
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof IEyepiece)) {
            return false;
        }

        IEyepiece eyepiece = (IEyepiece) obj;

        if (!(model.equals(eyepiece.getModel()))) {
            return false;
        }

        if (!(focalLength == eyepiece.getFocalLength())) {
            return false;
        }

        if (vendor != null) {
            if (!vendor.equals(eyepiece.getVendor())) {
                return false;
            }
        } else if (eyepiece.getVendor() != null) {
            return false;
        }

        if (apparentFOV != null) {
            if (!apparentFOV.equals(eyepiece.getApparentFOV())) {
                return false;
            }
        } else if (eyepiece.getApparentFOV() != null) {
            return false;
        }

        return true;
    }

    // ---------
    // IEyepiece ---------------------------------------------------------
    // ---------

    // -------------------------------------------------------------------
    /**
     * Adds this Eyepiece to a given parent XML DOM Element. The
     * Eyepiece element will be set as a child element of the passed element.
     *
     * @param element The parent element for this Eyepiece
     *
     * @return Returns the element given as parameter with this Eyepiece as
     *         child element.<br>
     *         *  Might return <code>null</code> if parent was
     *         <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element element) {
        if (element == null) {
            return null;
        }

        Document ownerDoc = element.getOwnerDocument();

        // Check if this element doesn't exist so far
        NodeList nodeList = element.getElementsByTagName(IEyepiece.XML_ELEMENT_EYEPIECE);

        if (nodeList.getLength() > 0) {
            Node currentNode = null;
            NamedNodeMap attributes = null;

            for (int i = 0; i < nodeList.getLength(); i++) { // iterate over all found nodes
                currentNode = nodeList.item(i);
                attributes = currentNode.getAttributes();

                Node idAttribute = attributes.getNamedItem(SchemaElement.XML_ELEMENT_ATTRIBUTE_ID);

                if ((idAttribute != null) // if ID attribute is set and equals this objects ID, return existing element
                         &&(idAttribute.getNodeValue().trim()
                                           .equals(super.getID().trim()))) {
                    return element;
                }
            }
        }

        // Create the new eyepiece element
        Element e_Eyepiece = ownerDoc.createElement(XML_ELEMENT_EYEPIECE);
        e_Eyepiece.setAttribute(XML_ELEMENT_ATTRIBUTE_ID, super.getID());

        element.appendChild(e_Eyepiece);

        Element e_Model = ownerDoc.createElement(XML_ELEMENT_MODEL);
        Node n_ModelText = ownerDoc.createTextNode(this.model);
        e_Model.appendChild(n_ModelText);
        e_Eyepiece.appendChild(e_Model);

        if (vendor != null) {
            Element e_Vendor = ownerDoc.createElement(XML_ELEMENT_VENDOR);
            Node n_VendorText = ownerDoc.createTextNode(this.vendor);
            e_Vendor.appendChild(n_VendorText);
            e_Eyepiece.appendChild(e_Vendor);
        }

        Element e_FocalLength = ownerDoc.createElement(XML_ELEMENT_FOCALLENGTH);
        Node n_FocalLengthText = ownerDoc.createTextNode(Float.toString(
                    this.focalLength));
        e_FocalLength.appendChild(n_FocalLengthText);
        e_Eyepiece.appendChild(e_FocalLength);

        if (apparentFOV != null) {
            Element e_ApparentFOV = ownerDoc.createElement(XML_ELEMENT_APPARENTFOV);
            e_ApparentFOV = apparentFOV.setToXmlElement(e_ApparentFOV);
            e_Eyepiece.appendChild(e_ApparentFOV);
        }

        return element;
    }

    // -------------------------------------------------------------------
    /**
     * Adds the eyepiece link to an given XML DOM Element The eyepiece
     * element itself will be attached to given elements ownerDocument. If the
     * ownerDocument has no eyepiece container, it will be created.<br>
     * Example:<br>
     * &lt;parameterElement&gt;<br>
     * <b>&lt;eyepieceLink&gt;123&lt;/eyepieceLink&gt;</b><br>
     * &lt;/parameterElement&gt;<br>
     * <i>More stuff of the xml document goes here</i><br>
     * <b>&lt;eyepieceContainer&gt;</b><br>
     * <b>&lt;eyepiece id="123"&gt;</b><br>
     * <i>eyepiece description goes
     * here</i><br><b>&lt;/eyepiece&gt;</b><br><b>&lt;/eyepieceContainer&gt;</b><br><br>
     *
     * @param element The element under which the the eyepiece link is created
     *
     * @return Returns the Element given as parameter with a additional
     *         eyepiece link, and the eyepiece element under the eyepiece
     *         container of the ownerDocument Might return <code>null</code>
     *         if element was <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    public Element addAsLinkToXmlElement(Element element) {
        if (element == null) {
            return null;
        }

        Document ownerDoc = element.getOwnerDocument();

        // Create the link element
        Element e_Link = ownerDoc.createElement(XML_ELEMENT_EYEPIECE);
        Node n_LinkText = ownerDoc.createTextNode(super.getID());
        e_Link.appendChild(n_LinkText);

        element.appendChild(e_Link);

        // Get or create the container element        
        Element e_Eyepieces = null;
        NodeList nodeList = ownerDoc.getElementsByTagName(RootElement.XML_EYEPIECE_CONTAINER);

        if (nodeList.getLength() == 0) { // we're the first element. Create container element
            e_Eyepieces = ownerDoc.createElement(RootElement.XML_EYEPIECE_CONTAINER);
            ownerDoc.getDocumentElement().appendChild(e_Eyepieces);
        } else {
            e_Eyepieces = (Element) nodeList.item(0); // there should be only one container element
        }

        this.addToXmlElement(e_Eyepieces);

        return element;
    }

    // -------------------------------------------------------------------
    /**
     * Returns the apparent field of view of this eyepiece.
     *
     * @return Returns the apparent field of view of this eyepiece. The Angles
     *         value cannot be negative or 0.<br>
     *         *  If <code>null</code> is returned the apparent field of
     *         view value was never set.
     *
     * @see org.jscience.ml.om.Angle
     */
    public Angle getApparentFOV() {
        return apparentFOV;
    }

    // -------------------------------------------------------------------
    /**
     * Returns the focal length of this eyepiece. The focal length of
     * the telescope divided by the focal length of the eyepiece equals the
     * amplification.
     *
     * @return Returns the focal length of the eyepiece.
     */
    public float getFocalLength() {
        return focalLength;
    }

    // -------------------------------------------------------------------
    /**
     * Returns the model name of the eyepiece.<br>
     *
     * @return Returns a String representing the eyepieces model name.<br>
 *
     */
    public String getModel() {
        return model;
    }

    // -------------------------------------------------------------------
    /**
     * Returns the vendor name of the eyepiece.<br>
     *
     * @return Returns a String representing the eyepieces vendor name.<br>
     *         *  If <code>null</code> is returned the vendor name was never
     *         set.
     */
    public String getVendor() {
        return vendor;
    }

    // -------------------------------------------------------------------
    /**
     * Sets the apparent field of view of this eyepiece.<br>
     * The field of view Angle cannot be negative or 0.
     *
     * @param apparentFOV The new apparent field of view to be set.
     *
     * @return Returns <code>true</code> if the new apparent field of view
     *         could be set. If <code>false</code> is returned the parameter
     *         was null, or the Angles value was negative or 0 (apparent field
     *         of view will not be changed if <code>false</code> is returned).
     */
    public boolean setApparentFOV(Angle apparentFOV) {
        if ((apparentFOV == null) || (apparentFOV.getValue() <= 0.0)) {
            return false;
        }

        this.apparentFOV = apparentFOV;

        return true;
    }

    // -------------------------------------------------------------------
    /**
     * Sets the focal length of the eyepiece.<br>
     *
     * @param focalLength The new focal length to be set.
     *
     * @throws IllegalArgumentException if focalLength was
     *         <code>Float.NaN</code>
     */
    public void setFocalLength(float focalLength)
        throws IllegalArgumentException {
        if (Float.isNaN(focalLength)) {
            throw new IllegalArgumentException(
                "Focal length cannot be Float.NaN. ");
        }

        this.focalLength = focalLength;
    }

    // -------------------------------------------------------------------
    /**
     * Sets the model name for the eyepiece.<br>
     *
     * @param modelname The new model name to be set.
     *
     * @throws IllegalArgumentException if modelname was <code>null</code>
     */
    public void setModel(String modelname) throws IllegalArgumentException {
        if (modelname == null) {
            throw new IllegalArgumentException("Modelname cannot be null. ");
        }

        this.model = modelname;
    }

    // -------------------------------------------------------------------
    /**
     * Sets the vendor name of the eyepiece.<br>
     *
     * @param vendorname The new vendor name to be set.
     */
    public void setVendor(String vendorname) {
        if ((vendorname != null) && ("".equals(vendorname.trim()))) {
            this.vendor = null;

            return;
        }

        this.vendor = vendorname;
    }
}
