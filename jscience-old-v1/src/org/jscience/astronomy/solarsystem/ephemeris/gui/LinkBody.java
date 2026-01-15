//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import org.jscience.astronomy.solarsystem.ephemeris.Vector3;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class LinkBody {
    /** DOCUMENT ME! */
    public String link_base;

    /** DOCUMENT ME! */
    public String link_extension;

    /** DOCUMENT ME! */
    public Vector3 coordinate;

/**
     * Creates a new LinkBody object.
     *
     * @param s  DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     */
    LinkBody(String s, String s1) {
        link_base = s;
        link_extension = s1;
        coordinate = new Vector3();
    }
}
