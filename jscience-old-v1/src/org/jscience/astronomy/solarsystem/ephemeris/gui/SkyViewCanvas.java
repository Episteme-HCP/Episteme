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
class SkyViewCanvas extends Canvas {
    /** DOCUMENT ME! */
    Image image;

/**
     * Creates a new SkyViewCanvas object.
     *
     * @param image1 DOCUMENT ME!
     */
    public SkyViewCanvas(Image image1) {
        image = image1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.drawString("Loading image from SkyView server.", 20, 100);
        g.drawString("Usually served in 30-60 seconds.", 20, 120);
        g.drawImage(image, 0, 0, this);
    }
}
