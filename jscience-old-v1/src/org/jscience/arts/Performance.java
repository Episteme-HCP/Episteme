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
