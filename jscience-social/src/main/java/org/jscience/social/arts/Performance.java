/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.social.arts;

import org.jscience.social.history.Event;
import org.jscience.social.history.EventCategory;
import org.jscience.natural.earth.Place;
import org.jscience.social.history.time.TimeCoordinate;
// import org.jscience.social.sociology.Celebration; // Not yet ported

/**
 * A Live Performance of an Artwork.
 * (e.g., play, concert, screening).
 * Represents a specific event where an artwork is "performed".
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Performance extends Event {

    private final Artwork artwork;
    private final Place place;

    public Performance(Artwork artwork, Place place, TimeCoordinate date, String comments) {
        super(artwork != null ? (artwork.getName() + " Performance") : "Unnamed Performance",
              comments, date, EventCategory.CULTURAL); 
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

