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
class RangeException extends Exception {
    /** DOCUMENT ME! */
    double value;

/**
     * Creates a new RangeException object.
     *
     * @param s DOCUMENT ME!
     */
    RangeException(String s) {
        super(s);
    }

/**
     * Creates a new RangeException object.
     */
    RangeException() {
    }
}
