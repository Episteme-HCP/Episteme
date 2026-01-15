//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class ShortPoint {
    /** DOCUMENT ME! */
    short x;

    /** DOCUMENT ME! */
    short y;

    /** DOCUMENT ME! */
    short z;

/**
     * Creates a new ShortPoint object.
     */
    ShortPoint() {
    }

/**
     * Creates a new ShortPoint object.
     *
     * @param word0 DOCUMENT ME!
     * @param word1 DOCUMENT ME!
     * @param word2 DOCUMENT ME!
     */
    ShortPoint(short word0, short word1, short word2) {
        set(word0, word1, word2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param word0 DOCUMENT ME!
     * @param word1 DOCUMENT ME!
     * @param word2 DOCUMENT ME!
     */
    public void set(short word0, short word1, short word2) {
        x = word0;
        y = word1;
        z = word2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param shortpoint DOCUMENT ME!
     */
    public void set(ShortPoint shortpoint) {
        x = shortpoint.x;
        y = shortpoint.y;
        z = shortpoint.z;
    }
}
