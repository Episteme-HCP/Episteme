/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         6-Jun-2001    Moved the coordinates to
 * be the first GML constructs to be added to the list in
 * getGMLConstructIterator(). (bug #430473) This was a temporary fix.
 * amilanovic         4-Sep-2001    Fixed bug #430473 so now this GML
 * construct is preserving the information about the ordering of its child
 * elements. amilanovic         29-Mar-2002   Updated for the new package
 * name.
 */
package org.jscience.ml.gml.dom;

import org.jscience.ml.gml.GMLSchema;
import org.jscience.ml.gml.infoset.*;
import org.jscience.ml.gml.util.*;

import org.w3c.dom.Element;

import java.util.Vector;


/**
 * A DOM-based implementation of the Geometry interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class GeometryImpl extends GMLConstructImpl implements Geometry {
/**
     * Initializes this geometry and all GML constructs directly owned by it.
     * Should be called from newGeometry() method only.
     *
     * @param owner      The owner of this geometry.
     * @param domElement The corresponding DOM element.
     * @param document   DOCUMENT ME!
     * @see #newGeometry(GeometryOwner,Element,GMLDocument)
     */
    protected GeometryImpl(GeometryOwner owner, Element domElement,
        GMLDocument document) {
        super(owner, domElement, document);
    }

    // Geometry interface implementation
    /**
     * A convenience method to gain access to inner boundaries if
     * available.
     *
     * @return Iterator to inner boundaries. It is never null.
     */
    public GeometryIterator getInnerBoundaryIterator() {
        // obtain properties and then select inner boundaries among them
        Vector innerBoundaries = new Vector();
        PropertyIterator propertyIterator = getPropertyIterator();

        while (propertyIterator.hasNext()) {
            Property property = propertyIterator.nextProperty();
            String propertyTag = property.getXMLDescriptor().getLocalName();
            String propertyNamespace = property.getXMLDescriptor().getNamespace();

            if (GMLSchema.isInnerBoundary(propertyNamespace, propertyTag)) {
                GeometryIterator geometryIterator = property.getGeometryIterator();

                // assume there is exactly one linear ring !!!
                Geometry linearRing = geometryIterator.nextGeometry();
                innerBoundaries.addElement(linearRing);
            }
        }

        GeometryIterator innerBoundaryIterator = new GeometryIteratorImpl(innerBoundaries.iterator());

        return innerBoundaryIterator;
    }

    /**
     * A convenience method to gain access to geometry coordinates.
     *
     * @return Iterator to coordinate tuples. It is never null.
     */
    public CoordinateTupleIterator getCoordinateTupleIterator() {
        GMLConstructIterator gmlConstructIterator = getGMLConstructIterator();
        Vector coordinatesVector = new Vector();
        Vector coordVector = new Vector();

        // iterating over GML constructs rather than properties...
        // is this necessary
        while (gmlConstructIterator.hasNext()) {
            GMLConstruct gmlConstruct = (GMLConstruct) gmlConstructIterator.next();

            if (gmlConstruct instanceof Coordinates) {
                coordinatesVector.addElement(gmlConstruct);
            } else if (gmlConstruct instanceof Coord) {
                coordVector.addElement(gmlConstruct);
            }
        }

        if (coordinatesVector.size() > 0) // if coordinates available
         {
            // there can be only one coordinates
            Coordinates coordinates = (Coordinates) coordinatesVector.firstElement();

            return coordinates.getCoordinateTupleIterator();
        } else if (coordVector.size() > 0) // if coords available
         {
            CoordinateTupleIterator coordTupleIter = new CoordinateTupleIteratorImpl(coordVector.iterator());

            return coordTupleIter;
        }

        return null;
    }

    /**
     * Returns the Id of this geometry, presumably specified with gid
     * attribute.
     *
     * @return Null if no Id attribute is specified.
     */
    public String getId() {
        for (int ii = 0; ii < getAttributeCount(); ii++) {
            Attribute attr = getAttribute(ii);

            if (attr.getXMLDescriptor().getLocalName().equals(GID_ATTR)) {
                String gid = attr.getValue();

                return gid;
            }
        }

        return null;
    }

    /**
     * Returns the name of the Spatial Reference System (SRS)
     *
     * @return Null if the SRS is unspecified.
     */
    public String getSRSName() {
        for (int ii = 0; ii < getAttributeCount(); ii++) {
            Attribute attr = getAttribute(ii);

            if (attr.getXMLDescriptor().getLocalName().equals(SRS_NAME_ATTR)) {
                String srsName = attr.getValue();

                return srsName;
            }
        }

        return null;
    }

    // UnknownConstructOwner interface implementation
    /**
     * Returns an iterator to all unknown constructs of this geometry.
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

    // PropertyOwner interface implementation
    /**
     * Returns an iterator to all properties of this geometry.
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

    // new methods
    /**
     * Creates a new geometry or geometry collection object.
     *
     * @param owner DOCUMENT ME!
     * @param domElement DOCUMENT ME!
     * @param document DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Geometry newGeometry(GeometryOwner owner, Element domElement,
        GMLDocument document) {
        String geometryName = domElement.getLocalName();
        String geometryNamespace = domElement.getNamespaceURI();
        Geometry newGeometry = null;

        if (document.isGeometryCollection(geometryNamespace, geometryName)) {
            newGeometry = new GeometryCollectionImpl(owner, domElement, document);
        } else if (document.isGeometry(geometryNamespace, geometryName)) {
            // simple geometry (not collection)
            newGeometry = new GeometryImpl(owner, domElement, document);
        } else {
            // todo: throw an exception maybe
        }

        return newGeometry;
    }

    // redefined methods
    /**
     * Returns a one-line string representation of this GML construct.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "Geometry " + getXMLDescriptor().getLocalName() + ' ' +
            getAttributeLine();

        return result;
    }

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
    protected boolean hasCoords() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean hasCoordinates() {
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

        // for now, nothing extra to do
    }
}
