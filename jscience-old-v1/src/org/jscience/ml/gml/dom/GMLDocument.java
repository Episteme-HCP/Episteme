/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.GMLSchema;
import org.jscience.ml.gml.infoset.Feature;
import org.jscience.ml.gml.infoset.FeatureOwner;
import org.jscience.ml.gml.util.FeatureIterator;
import org.jscience.ml.gml.util.GMLConstructIterator;
import org.jscience.ml.gml.xml.XMLException;
import org.jscience.ml.gml.xml.schema.XMLSchema;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.*;


/**
 * Encapsulates a DOM-based GML document, i.e. an XML document, whose root
 * element is a GML feature/feature collection.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class GMLDocument implements FeatureOwner // it may own features or their collections
 {
    // the underlying DOM document
    /** DOCUMENT ME! */
    private Document domDocument_;

    // contains only the root feature
    /** DOCUMENT ME! */
    private Vector features_;

    // the pool of all located schemas in this document
    /** DOCUMENT ME! */
    private Vector locatedSchemasPool_;

    // contains all declared schema locations,keys are the schema namespace URIs
    /** DOCUMENT ME! */
    private Hashtable schemaLocations_;

/**
     * Initializes a GMLDocument object with the given DOM document.
     *
     * @param domDocument DOCUMENT ME!
     * @throws XMLException DOCUMENT ME!
     */
    public GMLDocument(Document domDocument) throws XMLException {
        setDocument(domDocument);
    }

    // FeatureOwner interface implementation
    /**
     * Returns a feature iterator on the root feature of this document.
     * This feature may be a simple feature or a feature collection.
     *
     * @return Null is returned if the root element is not a feature.
     */
    public FeatureIterator getFeatureIterator() {
        FeatureIterator featureIterator = new FeatureIteratorImpl(features_.iterator());

        return featureIterator;
    }

    // GMLConstructOwner interface implementation
    /**
     * Provide access to all GML constructs on the top most level. This
     * method may be used to recursively scan the GML object model.
     *
     * @return DOCUMENT ME!
     */
    public GMLConstructIterator getGMLConstructIterator() {
        // no constructs
        Vector gmlConstructs = new Vector();
        Feature rootFeature = getRootFeature();
        gmlConstructs.addElement(rootFeature);

        GMLConstructIterator gmlConstructIterator = new GMLConstructIteratorImpl(gmlConstructs.iterator());

        return gmlConstructIterator;
    }

    // new methods
    /**
     * Determines if the given element tag refers to a feature
     * collection.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeatureCollection(String namespaceURI, String localName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isFeatureCollection(namespaceURI, localName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given element tag refers to a feature.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeature(String namespaceURI, String localName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isFeature(namespaceURI, localName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given element tag refers to a geometry
     * collection.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeometryCollection(String namespaceURI, String localName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isGeometryCollection(namespaceURI, localName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given element tag refers to a geometry.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeometry(String namespaceURI, String localName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isGeometry(namespaceURI, localName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given element tag refers to a property.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param parentNamespaceURI DOCUMENT ME!
     * @param parentLocalName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isProperty(String namespaceURI, String localName,
        String parentNamespaceURI, String parentLocalName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isProperty(namespaceURI, localName,
                        parentNamespaceURI, parentLocalName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given element tag refers to a "coordinates".
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCoordinates(String namespaceURI, String localName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isCoordinates(namespaceURI, localName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given element tag refers to a "coord".
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCoord(String namespaceURI, String localName) {
        Iterator schemaIterator = locatedSchemasPool_.iterator();

        while (schemaIterator.hasNext()) {
            GMLSchema gmlSchema = (GMLSchema) schemaIterator.next();

            if (gmlSchema.isCoord(namespaceURI, localName)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the root feature or feature collection.
     *
     * @return If the root element is not a feature, null is returned.
     */
    public Feature getRootFeature() {
        Feature rootFeature = null;

        if (features_.size() == 0) // must find the root feature
         {
            Element rootElement = getDOMDocument().getDocumentElement();
            rootFeature = FeatureImpl.newFeature(this, rootElement, this);
        } else // root feature already known, just fetch it
         {
            rootFeature = (Feature) features_.firstElement();
        }

        return rootFeature;
    }

    /**
     * Returns the underlying DOM document.
     *
     * @return DOCUMENT ME!
     */
    public Document getDOMDocument() {
        return domDocument_;
    }

    /**
     * Sets the DOM document and rebuilds the structure.
     *
     * @param document DOCUMENT ME!
     *
     * @throws XMLException DOCUMENT ME!
     */
    public void setDocument(Document document) throws XMLException {
        domDocument_ = document;
        refreshInternals();
    }

    /**
     * Refreshes the internal structure.
     *
     * @throws XMLException DOCUMENT ME!
     */
    protected void refreshInternals() throws XMLException {
        features_ = new Vector();
        schemaLocations_ = new Hashtable();
        locatedSchemasPool_ = new Vector();

        Element rootElement = getDOMDocument().getDocumentElement();

        // the schema URI may change, but let's leave it like this for now
        // also assumes that the schema location is specified
        String schemaLocationAttr = rootElement.getAttributeNS(XMLSchema.XML_SCHEMA_INSTANCE_NAMESPACE,
                XMLSchema.SCHEMA_LOCATION_ATTRIBUTE);

        if (schemaLocationAttr == null) {
            throw new XMLException("Missing schema location attribute " +
                "in the root element.");
        }

        StringTokenizer tokenizer = new StringTokenizer(schemaLocationAttr,
                " \t");

        while (tokenizer.hasMoreTokens()) {
            String schemaURI = tokenizer.nextToken();

            if (!tokenizer.hasMoreTokens()) {
                throw new XMLException("Missing schema location " +
                    "for schema URI: " + schemaURI);
            }

            String schemaLocation = tokenizer.nextToken();

            // insert only if not duplicate
            if (!schemaLocations_.containsKey(schemaURI)) {
                schemaLocations_.put(schemaURI, schemaLocation);
            }
        }

        resolveSchemaLocations();

        // read the document only after the schema locations are resolved
        Feature rootFeature = getRootFeature();
        features_.addElement(rootFeature);
    }

    /**
     * Attempts to read schemas at the specified locations (in the
     * document).
     *
     * @throws XMLException DOCUMENT ME!
     */
    public void resolveSchemaLocations() throws XMLException {
        Set schemaUris = schemaLocations_.keySet();
        Iterator schemaUriIterator = schemaUris.iterator();

        while (schemaUriIterator.hasNext()) {
            String schemaUri = (String) schemaUriIterator.next();
            String schemaLocation = (String) schemaLocations_.get(schemaUri);

            XMLSchema newSchema = new GMLSchema(schemaLocation);
            locatedSchemasPool_.addElement(newSchema);
        }
    }
}
