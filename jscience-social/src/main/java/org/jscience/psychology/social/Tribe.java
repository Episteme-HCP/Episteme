package org.jscience.psychology.social;

import org.jscience.earth.Place;

import org.jscience.sociology.Culture;




import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Relation;

import java.util.Objects;

/**
 * Represents a human tribe, which is an organized human group with a distinct name and culture.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */
@Persistent
public class Tribe extends HumanGroup {
    /** The name of the tribe. */
    @Attribute
    private String name;

    /** The distinct culture associated with the tribe. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Culture culture;

    /**
     * Initializes a new Tribe instance.
     *
     * @param name    the name of the tribe
     * @param culture the culture of the tribe
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if name is empty
     */
    public Tribe(String name, Culture culture) {
        if (Objects.requireNonNull(name, "Name cannot be null").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
    }

    /**
     * Initializes a new Tribe instance with a specific territory.
     *
     * @param name            the name of the tribe
     * @param formalTerritory the territory assigned to or claimed by the tribe
     * @param culture         the culture of the tribe
     * @throws NullPointerException if any argument is null
     * @throws IllegalArgumentException if name is empty
     */
    public Tribe(String name, Place formalTerritory, Culture culture) {
        super(Objects.requireNonNull(formalTerritory, "Territory cannot be null"));
        if (Objects.requireNonNull(name, "Name cannot be null").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
    }

    /**
     * Returns the name of the tribe.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of the tribe.
     *
     * @param name the new name
     * @throws NullPointerException if name is null
     * @throws IllegalArgumentException if name is empty
     */
    public void setName(String name) {
        if (Objects.requireNonNull(name, "Name cannot be null").isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
    }

    /**
     * Returns the culture associated with the tribe.
     *
     * @return the culture
     */
    public Culture getCulture() {
        return culture;
    }

    /**
     * Updates the tribe's culture.
     *
     * @param culture the new culture
     * @throws NullPointerException if culture is null
     */
    public void setCulture(Culture culture) {
        this.culture = Objects.requireNonNull(culture, "Culture cannot be null");
    }
}
