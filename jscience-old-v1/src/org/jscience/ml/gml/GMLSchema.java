/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml;

import org.jscience.ml.gml.xml.XMLException;
import org.jscience.ml.gml.xml.schema.XMLSchema;


/**
 * Encapsulates the GML Schema as an XML application schema.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class GMLSchema extends XMLSchema {
    /** DOCUMENT ME! */
    public static final String GML_NS_URI = "http://www.opengis.net/gml";

    /** DOCUMENT ME! */
    public static final String BASE_FEATURE_TYPE = "AbstractFeatureType";

    /** DOCUMENT ME! */
    public static final String BASE_FEATURE_ELEMENT = "_Feature";

    /** DOCUMENT ME! */
    public static final String BASE_FEATURE_COLLECTION_TYPE = "AbstractFeatureCollectionType";

    /** DOCUMENT ME! */
    public static final String BASE_FEATURE_COLLECTION_ELEMENT = "_FeatureCollection";

    /** DOCUMENT ME! */
    public static final String BASE_GEOMETRY_TYPE = "AbstractGeometryType";

    /** DOCUMENT ME! */
    public static final String BASE_GEOMETRY_ELEMENT = "_Geometry";

    /** DOCUMENT ME! */
    public static final String BASE_GEOMETRY_COLLECTION_TYPE = "AbstractGeometryCollectionBaseType";

    /** DOCUMENT ME! */
    public static final String BASE_GEOMETRY_COLLECTION_ELEMENT = "_GeometryCollection";

    /** DOCUMENT ME! */
    public static final String BASE_COORD_TYPE = "CoordType";

    /** DOCUMENT ME! */
    public static final String BASE_COORDINATES_TYPE = "CoordinatesType";

    /** DOCUMENT ME! */
    public static final String INNER_BOUNDARY_IS = "innerBoundaryIs";

    /** DOCUMENT ME! */
    public static final String FEATURE_MEMBER = "featureMember";

    /** DOCUMENT ME! */
    public static final String GEOMETRY_MEMBER = "geometryMember";

/**
     * Instantiates a GML schema object from the given full path or URL.
     *
     * @param location DOCUMENT ME!
     * @throws XMLException DOCUMENT ME!
     */
    public GMLSchema(String location) throws XMLException {
        super(location);
    }

    /**
     * Determines whether an element corresponds to a feature
     * collection.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeatureCollection(String namespaceURI, String localName) {
        boolean result = getParser()
                             .isFeatureCollection(namespaceURI, localName);

        return result;
    }

    /**
     * Determines whether an element corresponds to a feature.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeature(String namespaceURI, String localName) {
        boolean result = getParser().isFeature(namespaceURI, localName);

        return result;
    }

    /**
     * Determines whether an element corresponds to a geometry
     * collection.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeometryCollection(String namespaceURI, String localName) {
        boolean result = getParser()
                             .isGeometryCollection(namespaceURI, localName);

        return result;
    }

    /**
     * Determines whether an element corresponds to a geometry.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeometry(String namespaceURI, String localName) {
        boolean result = getParser().isGeometry(namespaceURI, localName);

        return result;
    }

    /**
     * Determines whether an element corresponds to a property.
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
        boolean result = getParser()
                             .isProperty(namespaceURI, localName,
                parentNamespaceURI, parentLocalName);

        return result;
    }

    /**
     * Determines whether an element is a "coordinates" GML object.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCoordinates(String namespaceURI, String localName) {
        boolean result = getParser().isCoordinates(namespaceURI, localName);

        return result;
    }

    /**
     * Determines whether an element is a "coord" GML object.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCoord(String namespaceURI, String localName) {
        boolean result = getParser().isCoord(namespaceURI, localName);

        return result;
    }

    /**
     * Determines whether an element is a "coord" GML object.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isInnerBoundary(String namespaceURI, String localName) {
        return localName.equals(INNER_BOUNDARY_IS);
    }

    /**
     * Determines whether something is a feature member property
     * element.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isFeatureMemberProperty(String namespaceURI,
        String localName) {
        return localName.equals(FEATURE_MEMBER);
    }

    /**
     * Determines whether something is a geometry member property
     * element.
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isGeometryMemberProperty(String namespaceURI,
        String localName) {
        return localName.equals(GEOMETRY_MEMBER);
    }
}
