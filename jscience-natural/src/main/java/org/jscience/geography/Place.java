package org.jscience.geography;

import org.jscience.util.Named;
import org.jscience.util.Positioned;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;


/**
 * A class representing a geographical spot, namely a feature. It is meant
 * to be used primarily to define places like human settlements, that is
 * places with a name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
@Persistent
public class Place extends Object implements Named, Positioned {

    /**
     * Geographical type of the place.
     */
    public enum Type {
        COUNTRY, REGION, PROVINCE, STATE, COUNTY, CITY, TOWN, VILLAGE, 
        BUILDING, ADDRESS, NATURAL_FEATURE, CELESTIAL_BODY, GLOBAL, OTHER
    }

    @Id
    private Identification identification;
    
    /** DOCUMENT ME! */
    @Attribute
    private String name;

    /** DOCUMENT ME! */
    @Attribute
    private Boundary boundary;

    /** DOCUMENT ME! */
    @Attribute
    private Type type;

    /**
     * Creates a new Place object with a name and boundary.
     *
     * @param name     the name of the place.
     * @param boundary the geographical boundary.
     */
    public Place(String name, Boundary boundary) {
        this(name, boundary, Type.OTHER);
    }

    /**
     * Creates a new Place object with name, boundary and type.
     */
    public Place(String name, Boundary boundary, Type type) {
        if ((name != null) && (name.length() > 0)) {
            this.identification = new SimpleIdentification(name + ":" + System.nanoTime());
            this.name = name;
            this.boundary = boundary;
            this.type = type != null ? type : Type.OTHER;
        } else {
            throw new IllegalArgumentException(
                "The Place constructor can't have null or empty name.");
        }
    }

    /**
     * Creates a new Place object with a name and type (null boundary).
     */
    public Place(String name, Type type) {
        this(name, null, type);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPosition() {
        return boundary != null ? boundary.getPosition() : null;
    }
}
