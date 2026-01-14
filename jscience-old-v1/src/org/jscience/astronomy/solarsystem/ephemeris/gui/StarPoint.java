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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class StarPoint {
    /** DOCUMENT ME! */
    short x;

    /** DOCUMENT ME! */
    short y;

    /** DOCUMENT ME! */
    short z;

    /** DOCUMENT ME! */
    Color colour;

    /** DOCUMENT ME! */
    byte magnitude;

/**
     * Creates a new StarPoint object.
     */
    StarPoint() {
    }

/**
     * Creates a new StarPoint object.
     *
     * @param starpoint DOCUMENT ME!
     */
    StarPoint(StarPoint starpoint) {
        copy(starpoint);
    }

/**
     * Creates a new StarPoint object.
     *
     * @param word0 DOCUMENT ME!
     * @param word1 DOCUMENT ME!
     * @param word2 DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param byte0 DOCUMENT ME!
     */
    StarPoint(short word0, short word1, short word2, Color color, byte byte0) {
        x = word0;
        y = word1;
        z = word2;
        colour = color;
        magnitude = byte0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param starpoint DOCUMENT ME!
     */
    public void copy(StarPoint starpoint) {
        x = starpoint.x;
        y = starpoint.y;
        z = starpoint.z;
        colour = starpoint.colour;
        magnitude = starpoint.magnitude;
    }
}
