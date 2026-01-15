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
class TooHighException extends RangeException {
    /** DOCUMENT ME! */
    double limit;

/**
     * Creates a new TooHighException object.
     *
     * @param d  DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param s  DOCUMENT ME!
     */
    TooHighException(double d, double d1, String s) {
        super(s + " value must be less than or equal to " + d1);
        limit = d1;
        super.value = d;
    }

/**
     * Creates a new TooHighException object.
     */
    TooHighException() {
    }
}
