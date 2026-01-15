/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         4-Sep-2001    Fixed bug #430473 so now
 * this GML construct is preserving the information about the ordering of its
 * child elements. amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.GMLConstructIterator;
import org.jscience.ml.gml.util.GeometryIterator;
import org.jscience.ml.gml.util.PropertyIterator;
import org.jscience.ml.gml.util.UnknownConstructIterator;

import org.w3c.dom.Element;

import java.util.Vector;


/**
 * A DOM-based implementation of the Feature interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class FeatureImpl extends GMLConstructImpl implements Feature {
/**
     * Initializes this feature construct and all GML constructs owned by it.
     * This constructor should be called only from the newFeature method.
     *
     * @see #newFeature(FeatureOwner,Element,GMLDocument)
     */
    protected FeatureImpl(FeatureOwner owner, Element domElement,
        GMLDocument document) {
        super(owner, domElement, document);
    }

    // Feature interface implementation
    /**
     * Returns the boundedBy property of this feature. This property
     * defines the bounding box for this feature. In the future, the abstract
     * geometry will be replaced by a Box.
     *
     * @return The Geometry/Box object if specified, otherwise null.
     */
    public Geometry getBoundedBy() {
        Property boundedByProperty = getProperty(BOUNDED_BY_PROP);

        if (boundedByProperty != null) {
            GeometryIterator geomIter = boundedByProperty.getGeometryIterator();

            // check only for one geometry instance!!!
            if (geomIter.hasNext()) {
                Geometry boundedByGeometry = geomIter.nextGeometry();

                return boundedByGeometry;
            }
        }

        return null;
    }

    /**
     * Returns the value of the name property of this feature.
     *
     * @return Null if the name property is not specified.
     */
    public String getName() {
        Property nameProperty = getProperty(NAME_PROP);

        if (nameProperty != null) {
            String name = nameProperty.getValueAsString();

            return name;
        }

        return null;
    }

    /**
     * Returns the value of the description property of this feature.
     *
     * @return Null if the description property is not specified.
     */
    public String getDescription() {
        Property descriptionProperty = getProperty(DESCRIPTION_PROP);

        if (descriptionProperty != null) {
            String description = descriptionProperty.getValueAsString();

            return description;
        }

        return null;
    }

    /**
     * Returns the Id of this feature, presumably stored in the fid
     * attribute.
     *
     * @return Null if an Id is not specified.
     */
    public String getId() {
        for (int ii = 0; ii < getAttributeCount(); ii++) {
            Attribute attr = getAttribute(ii);

            if (attr.getXMLDescriptor().getLocalName().equals(FID_ATTR)) {
                String fid = attr.getValue();

                return fid;
            }
        }

        return null;
    }

    // PropertyOwner interface implementation
    /**
     * Returns an iterator to all properties of this feature.
     *
     * @return DOCUMENT ME!
     */
    public PropertyIterator getPropertyIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector properties = new Vector();

        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof Property) {
                properties.addElement(gmlConstruct);
            }
        }

        PropertyIterator propertyIterator = new PropertyIteratorImpl(properties.iterator());

        return propertyIterator;
    }

    // UnknownConstructOwner interface implementation
    /**
     * Returns an iterator to all unknown constructs of this feature.
     *
     * @return DOCUMENT ME!
     */
    public UnknownConstructIterator getUnknownConstructIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector unknownConstructs = new Vector();

        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof UnknownConstruct) {
                unknownConstructs.addElement(gmlConstruct);
            }
        }

        UnknownConstructIterator unknownConstructIterator = new UnknownConstructIteratorImpl(unknownConstructs.iterator());

        return unknownConstructIterator;
    }

    // new methods
    /**
     * This method must be used to create new features and feature
     * collections.
     *
     * @param owner DOCUMENT ME!
     * @param sourceElement DOCUMENT ME!
     * @param document DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Feature newFeature(FeatureOwner owner, Element sourceElement,
        GMLDocument document) {
        String namespace = sourceElement.getNamespaceURI();
        String elementName = sourceElement.getLocalName();
        Feature newFeature = null;

        // the sequence of the if statements below is not arbitrary
        // FeatureCollection is a specialization of Feature and hence
        // must be checked first.
        if (document.isFeatureCollection(namespace, elementName)) {
            // feature collection
            newFeature = new FeatureCollectionImpl(owner, sourceElement,
                    document);
        } else if (document.isFeature(namespace, elementName)) {
            // feature but not a collection
            newFeature = new FeatureImpl(owner, sourceElement, document);
        } else {
            // TODO: throw exception ??
        }

        return newFeature;
    }

    /**
     * Finds the property with the given tag in this feature.
     * Currently, the namespace is not used!!!
     *
     * @param propertyTag DOCUMENT ME!
     *
     * @return Null if there is no such property.
     */
    private Property getProperty(String propertyTag) {
        PropertyIterator propertyIterator = getPropertyIterator();

        while (propertyIterator.hasNext()) {
            Property nextProperty = propertyIterator.nextProperty();
            XMLDescriptor propXMLDescr = nextProperty.getXMLDescriptor();

            if (propXMLDescr.getLocalName().equals(propertyTag)) {
                return nextProperty;
            }
        }

        return null;
    }

    /**
     * Returns a one-line string representation of this feature.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "Feature " + getXMLDescriptor().getLocalName() +
            getAttributeLine();

        return result;
    }

    // redefined methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasProperties() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasUnknownConstructs() {
        return true;
    }

    /**
     * Refreshes the internal data cache from the DOM source tree. This
     * method should be called each time the underlying DOM structure has
     * changed.
     */
    protected void refreshInternals() {
        // first refresh the general GML construct internals
        super.refreshInternals();

        // nothing to do here
    }
}
