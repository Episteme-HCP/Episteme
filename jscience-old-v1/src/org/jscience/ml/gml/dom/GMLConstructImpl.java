/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         4-Sep-2001    Fixed bug #430473 so now
 * this GML construct is preserving the information about the ordering of its
 * child elements. This generic GML construct now parses its XML element's
 * children and classifies them. Subclasses access this generic collection to
 * extract specific GML constructs. amilanovic         29-Mar-2002   Updated
 * for the new package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.GMLConstructIterator;

import org.w3c.dom.*;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;


/**
 * DOM-based implementation of the GMLConstruct interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public abstract class GMLConstructImpl implements GMLConstruct {
    // the DOM element representing this GML construct
    /** DOCUMENT ME! */
    private Element domElement_;

    // the owner of this GML construct
    /** DOCUMENT ME! */
    private GMLConstructOwner owner_;

    // the XMLDescriptor object that describes this GML construct in XML
    /** DOCUMENT ME! */
    private XMLDescriptor xmlDescriptor_;

    // stores all attributes
    /** DOCUMENT ME! */
    private Vector attributes_;

    // all GML constructs
    /** DOCUMENT ME! */
    private Vector gmlConstructs_;

    // the GML document this construct belongs to
    /** DOCUMENT ME! */
    private GMLDocument gmlDocument_;

/**
     * Creates a new GMLConstructImpl object.
     *
     * @param owner      DOCUMENT ME!
     * @param domElement DOCUMENT ME!
     * @param document   DOCUMENT ME!
     */
    protected GMLConstructImpl(GMLConstructOwner owner, Element domElement,
        GMLDocument document) {
        owner_ = owner;
        domElement_ = domElement;
        gmlDocument_ = document;
        xmlDescriptor_ = new XMLDescriptorImpl(domElement);
        refreshInternals();
    }

    /**
     * DOCUMENT ME!
     *
     * @param gmlDocument DOCUMENT ME!
     */
    public void setGMLDocument(GMLDocument gmlDocument) {
        gmlDocument_ = gmlDocument;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GMLDocument getGMLDocument() {
        return gmlDocument_;
    }

    // GMLConstruct interface implementation
    /**
     * Returns the direct owner of this GML construct.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstructOwner getOwner() {
        return owner_;
    }

    // GMLConstructOwner interface implementation
    /**
     * Provide access to all GML constructs. This method may be used to
     * recursively scan the GML object model.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstructIterator getGMLConstructIterator() {
        GMLConstructIterator gmlConstructIterator = new GMLConstructIteratorImpl(gmlConstructs_.iterator());

        return gmlConstructIterator;
    }

    // XMLDescribable interface implementation
    /**
     * Returns the XML descriptor for this GML construct.
     *
     * @return DOCUMENT ME!
     */
    public XMLDescriptor getXMLDescriptor() {
        return xmlDescriptor_;
    }

    // GMLConstruct interface implementation
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAttributeCount() {
        return attributes_.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(int index) {
        return ((index >= 0) && (index < attributes_.size()))
        ? (Attribute) attributes_.elementAt(index) : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param namespace DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(String namespace, String localName) {
        Iterator attrIterator = attributes_.iterator();

        while (attrIterator.hasNext()) {
            Attribute attr = (Attribute) attrIterator.next();

            if (attr.getXMLDescriptor().getNamespace().equals(namespace) &&
                    attr.getXMLDescriptor().getLocalName().equals(localName)) {
                return attr;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attribute getAttribute(String localName) {
        Iterator attrIterator = attributes_.iterator();

        while (attrIterator.hasNext()) {
            Attribute attr = (Attribute) attrIterator.next();

            if (attr.getXMLDescriptor().getLocalName().equals(localName)) {
                return attr;
            }
        }

        return null;
    }

    // redefined methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getAttributeLine();
    }

    // new methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAttributeLine() {
        String result = "";

        if (getAttributeCount() > 0) {
            result = "(";

            for (int ii = 0; ii < getAttributeCount(); ii++) {
                Attribute attr = getAttribute(ii);
                result += (attr.toString() + ' ');
            }

            result += ')';
        }

        return result;
    }

    /**
     * Returns the DOM element that was used as source for this feature
     * object.
     *
     * @return DOCUMENT ME!
     */
    public Element getDOMElement() {
        return domElement_;
    }

    // the following methods can be redefined in subclasses
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasProperties() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasCoords() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasCoordinates() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasGeometries() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasFeatures() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasUnknownConstructs() {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    protected void refreshInternals() {
        // purge all attributes
        attributes_ = new Vector();

        // purge all elements
        gmlConstructs_ = new Vector();

        // read attributes
        NamedNodeMap attrMap = getDOMElement().getAttributes();

        for (int ii = 0; ii < attrMap.getLength(); ii++) {
            Attr currentAttr = (Attr) attrMap.item(ii);
            Attribute newGMLAttr = new AttributeImpl(this, currentAttr);
            attributes_.addElement(newGMLAttr);
        }

        // rebuild the cache of GML constructs
        NodeList childNodeList = getDOMElement().getChildNodes();
        Vector childElements = new Vector();

        for (int ii = 0; ii < childNodeList.getLength(); ii++) {
            Node currentNode = childNodeList.item(ii);

            if (currentNode instanceof Element) {
                childElements.addElement(currentNode);
            }
        }

        Enumeration childElementEnum = childElements.elements();

        while (childElementEnum.hasMoreElements()) {
            Element currentChild = (Element) childElementEnum.nextElement();
            String currentChildLocalName = currentChild.getLocalName();
            String currentChildNamespaceURI = currentChild.getNamespaceURI();
            GMLConstruct newGMLConstruct = null;

            if (hasFeatures() &&
                    getGMLDocument()
                            .isFeature(currentChild.getNamespaceURI(),
                        currentChildLocalName)) {
                newGMLConstruct = FeatureImpl.newFeature((FeatureOwner) this,
                        currentChild, getGMLDocument());
            } else if (hasGeometries() &&
                    getGMLDocument()
                            .isGeometry(currentChildNamespaceURI,
                        currentChildLocalName)) {
                newGMLConstruct = GeometryImpl.newGeometry((GeometryOwner) this,
                        currentChild, getGMLDocument());
            } else if (hasCoords() &&
                    getGMLDocument()
                            .isCoord(currentChildNamespaceURI,
                        currentChildLocalName)) {
                newGMLConstruct = new CoordImpl((Geometry) this, currentChild,
                        getGMLDocument());
            } else if (hasCoordinates() &&
                    getGMLDocument()
                            .isCoordinates(currentChildNamespaceURI,
                        currentChildLocalName)) {
                newGMLConstruct = new CoordinatesImpl((Geometry) this,
                        currentChild, getGMLDocument());
            } else if (hasProperties() &&
                    getGMLDocument()
                            .isProperty(currentChildNamespaceURI,
                        currentChildLocalName,
                        getXMLDescriptor().getNamespace(),
                        getXMLDescriptor().getLocalName())) {
                newGMLConstruct = new PropertyImpl((PropertyOwner) this,
                        currentChild, getGMLDocument());
            } else if (hasUnknownConstructs()) // not a known GML construct
             {
                newGMLConstruct = new UnknownConstructImpl((UnknownConstructOwner) this,
                        currentChild, getGMLDocument());
            } else {
                // do nothing
            }

            gmlConstructs_.addElement(newGMLConstruct);
        } // end of while loop
    }
}
