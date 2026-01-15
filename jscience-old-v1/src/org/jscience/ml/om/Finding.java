/* ====================================================================
 * /Finding.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

import org.jscience.ml.om.util.SchemaException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * The abstract class Finding provides some common features that may be
 * used by the subclasses of an org.jscience.ml.om.IFinding.<br>
 * The Finding class stores a description of the findings and provides simple
 * access to this description field. It also implements a basic XML DOM helper
 * method that may be used by all subclasses that have to implement the
 * IFinding interface.
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public abstract class Finding extends SchemaElement implements IFinding {
    // ------------------
    /**
     * DOCUMENT ME!
     */
    private String description = null;

    // -------------------------------------------------------------------
/**
     * Protected constructor used by subclasses construction.
     *
     * @param findingElement The XML Node representing this Finding
     * @throws SchemaException if the XML element is malformed
     */
    protected Finding(Node findingElement) throws SchemaException {
        if (findingElement == null) {
            throw new SchemaException("Given element cannot be NULL. ");
        }

        Element finding = (Element) findingElement;

        // Make sure the element belongs to this class
        String xsiType = finding.getAttribute(IFinding.XML_XSI_TYPE);

        if ((xsiType != null) && (this.getXSIType().equals(xsiType))) {
            Element child = null;
            NodeList children = null;

            // Getting data

            // Get mandatory description
            children = finding.getElementsByTagName(IFinding.XML_ELEMENT_DESCRIPTION);

            if ((children == null) || (children.getLength() != 1)) {
                throw new SchemaException(
                    "Finding must have exact one description. ");
            }

            child = (Element) children.item(0);

            String description = null;

            if (child == null) {
                throw new SchemaException("Finding must have a description. ");
            } else {
                // Check if description has a child 
                if (child.getFirstChild() != null) {
                    description = child.getFirstChild().getNodeValue();
                    this.setDescription(description);
                }
            }
        }
    }

    // -------------------------------------------------------------------
/**
     * Protected constructor used by subclasses construction.
     *
     * @param ID          This elements unique ID
     * @param description The description of the finding
     * @throws IllegalArgumentException if description was <code>null</code>
     */
    protected Finding(String ID, String description)
        throws IllegalArgumentException {
        super(ID);

        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null. ");
        }

        this.description = description;
    }

    // -------------------------------------------------------------------
/**
     * Protected constructor used by subclasses construction.
     *
     * @param description The description of the finding
     * @throws IllegalArgumentException if description was <code>null</code>
     */
    protected Finding(String description) throws IllegalArgumentException {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null. ");
        }

        this.description = description;
    }

    // --------
    // IFinding ----------------------------------------------------------
    // --------

    // -------------------------------------------------------------------
    /**
     * Adds the Finding to an given parent XML DOM Element. This
     * abstract method is derived from org.jscience.ml.om.IFinding.
     *
     * @param parent The parent element for the Finding
     *
     * @return Returns the Element given as parameter with the Finding as child
     *         Element.
     *
     * @see org.w3c.dom.Element
     * @see org.jscience.ml.om.IFinding
     */
    public abstract org.w3c.dom.Element addToXmlElement(
        org.w3c.dom.Element parent);

    // -------------------------------------------------------------------
    /**
     * Returns the description of the Finding. The string describes the
     * impressions the observer had during the observation of an object.
     *
     * @return The description of the finding.
     */
    public String getDescription() {
        return description;
    }

    // -------------------------------------------------------------------
    /**
     * Sets the description of the Finding. The string should describe
     * the impressions the observer had during the observation of an object.
     *
     * @param description A description of the finding.
     *
     * @throws IllegalArgumentException if description was <code>null</code>
     */
    public void setDescription(String description)
        throws IllegalArgumentException {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null. ");
        }

        this.description = description;
    }

    // -----------------
    // Protected methods -------------------------------------------------
    // -----------------    

    // -------------------------------------------------------------------
    /**
     * Creates an XML DOM Element for the Finding. The new Finding
     * element will be added as child element to an given parent element. All
     * specialised subclasses may use this method to create a Finding element
     * under which they may store their addition data.<br>
     * Example:<br>
     * &lt;parentElement&gt;<br>
     * &lt;result&gt;<br>
     * <i>More specialised stuff goes here</i><br>
     * &lt;/result&gt;<br>
     * &lt;/parentElement&gt;
     *
     * @param parent The parent element for the Finding
     *
     * @return Returns a Finding element which is a child of the given parent element.<br>
     *         *  Might return <code>null</code> if parent was
     *         <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    protected Element createXmlFindingElement(Element parent) {
        if (parent == null) {
            return null;
        }

        Document ownerDoc = parent.getOwnerDocument();

        Element e_Finding = ownerDoc.createElement(XML_ELEMENT_FINDING);

        Element e_Description = ownerDoc.createElement(XML_ELEMENT_DESCRIPTION);
        Node n_DescriptionText = ownerDoc.createTextNode(description);
        e_Description.appendChild(n_DescriptionText);
        e_Finding.appendChild(e_Description);

        return e_Finding;
    }
}
