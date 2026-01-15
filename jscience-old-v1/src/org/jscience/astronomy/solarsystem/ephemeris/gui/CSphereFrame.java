//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class CSphereFrame extends Frame implements WindowListener {
    /** DOCUMENT ME! */
    private CSpherePanel frame_panel;

/**
     * Creates a new CSphereFrame object.
     *
     * @param s               DOCUMENT ME!
     * @param celestialsphere DOCUMENT ME!
     */
    public CSphereFrame(String s, CelestialSphere celestialsphere) {
        super(s);
        frame_panel = null;
        addWindowListener(this);
        setSize(400, 400);
        setLayout(new BorderLayout());
        add("Center", frame_panel = new CSpherePanel(celestialsphere));
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowClosing(WindowEvent windowevent) {
        setVisible(false);
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        setVisible(false);
        removeAll();
        frame_panel.destroy();
        dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowOpened(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowClosed(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowActivated(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     */
    public void tryDraw() {
        if (isVisible()) {
            frame_panel.tryDraw();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowIconified(WindowEvent windowevent) {
    }
}
