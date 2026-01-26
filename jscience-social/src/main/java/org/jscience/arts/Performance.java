package org.jscience.arts;

import org.jscience.history.Event;
import org.jscience.earth.Place;
import org.jscience.history.time.TimeCoordinate;
// import org.jscience.sociology.Celebration; // Not yet ported

/**
 * A Live Performance of an Artwork.
 * (e.g., play, concert, screening).
 * Represents a specific event where an artwork is "performed".
 */
public class Performance extends Event {

    private final Artwork artwork;
    private final Place place;

    public Performance(Artwork artwork, Place place, TimeCoordinate date, String comments) {
        super(artwork != null ? (artwork.getName() + " Performance") : "Unnamed Performance",
              comments, date, Category.CULTURAL); 
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
