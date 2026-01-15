package org.jscience.arts;

import org.jscience.measure.Identification;

import org.jscience.util.Named;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a group of artworks (usually located at one and
 * only place). For example, a museum is a collection.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Collection extends Object implements Named {
    /** DOCUMENT ME! */
    private String name; //collections sometimes have a name (the owner's usually)

    /** DOCUMENT ME! */
    private Set artworks; //we use a HashSet here but could use a HashTable using identification as key as it should be unique for each member

/**
     * Creates a new Collection object.
     */
    public Collection() {
        this.name = new String();
        artworks = Collections.EMPTY_SET;
    }

/**
     * Creates a new Collection object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Collection(String name) {
        if (name != null) {
            this.name = name;
            artworks = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Collection constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    //mainly in case your name has changed
    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param artwork DOCUMENT ME!
     */
    public void addArtwork(Artwork artwork) {
        artworks.add(artwork);
    }

    /**
     * DOCUMENT ME!
     *
     * @param artwork DOCUMENT ME!
     */
    public void removeArtwork(Artwork artwork) {
        artworks.remove(artwork);
    }

    /**
     * DOCUMENT ME!
     *
     * @param artwork DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Artwork artwork) {
        return artworks.contains(artwork);
    }

    /**
     * DOCUMENT ME!
     *
     * @param identification DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Artwork contains(Identification identification) {
        Iterator iterator;
        Artwork artwork;
        Artwork result;

        result = null;
        iterator = artworks.iterator();

        while (iterator.hasNext() && (result == null)) {
            artwork = (Artwork) iterator.next();

            if (artwork.getIdentification().equals(identification)) {
                result = artwork;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getArtworks() {
        return artworks;
    }

    //checks identifications correspond for all elements of both sets
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        Iterator iterator;
        Collection collection;
        boolean valid;

        if ((o != null) && (o instanceof Collection)) {
            collection = (Collection) o;

            if (this.getArtworks().size() == collection.getArtworks().size()) {
                iterator = this.getArtworks().iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = (collection.contains(((Artwork) iterator.next()).getIdentification()) != null);
                }
            } else {
                valid = false;
            }
        } else {
            valid = false;
        }

        return valid;
    }
}
