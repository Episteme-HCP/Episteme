package org.jscience.arts;

import org.jscience.geography.Place;

import org.jscience.sociology.Celebration;

import java.util.Date;


/**
 * A class representing a live show, for example a strip tease, a movie, a
 * flash mob...
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//could also be a projection of a movie, reading a book aloud, etc.
//this could also be the actual shooting of a movie scene or any show
//perhaps we could define some roles as Director, Actor...
public class Performance extends Celebration {
    /** DOCUMENT ME! */
    private Artwork artwork;

/**
     * Creates a new Performance object.
     *
     * @param artwork  DOCUMENT ME!
     * @param place    DOCUMENT ME!
     * @param start    DOCUMENT ME!
     * @param end      DOCUMENT ME!
     * @param comments DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Performance(Artwork artwork, Place place, Date start, Date end,
        String comments) {
        super(artwork.getName(), place, Celebration.EVENT_SPECIFIC, start, end,
            comments);

        if ((artwork != null)) {
            this.artwork = artwork;
        } else {
            throw new IllegalArgumentException(
                "The Performance constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Artwork getArtwork() {
        return artwork;
    }
}
