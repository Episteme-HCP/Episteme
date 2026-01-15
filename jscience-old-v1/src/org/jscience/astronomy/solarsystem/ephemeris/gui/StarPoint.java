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
