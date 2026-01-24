package org.jscience.arts;

import org.jscience.history.Event;
import org.jscience.geography.Place;
import org.jscience.history.temporal.TemporalCoordinate;
// import org.jscience.sociology.Celebration; // Not yet ported

/**
 * A Live Performance of an Artwork.
 * (e.g., play, concert, screening).
 * Represents a specific event where an artwork is "performed".
 */
public class Performance extends Event {

    private final Artwork artwork;
    private final Place place;

    public Performance(Artwork artwork, Place place, TemporalCoordinate date, String comments) {
        super(date, comments); // Event constructor
        if (artwork == null || place == null) {
            throw new IllegalArgumentException("Artwork and Place cannot be null.");
        }
        this.artwork = artwork;
        this.place = place;
    }

    public Artwork getArtwork() {
        return artwork;
    }

    public Place getPlace() {
        return place;
    }
}
