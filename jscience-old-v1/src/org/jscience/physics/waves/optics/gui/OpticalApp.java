package org.jscience.physics.waves.optics.gui;

import java.applet.Applet;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpticalApp extends Applet implements RespondToEvents {
/**
     * Creates a new OpticalApp object.
     */
    public OpticalApp() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void scrollbar(String name, int value) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void checkbox(String name, boolean value) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void button(String name) {
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void processEvent(AWTEvent e) {
        if (e.getID() == Event.WINDOW_DESTROY) {
            System.exit(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAppletInfo() {
        // Too funny to destroy
        // return "An interactive test of the Graphics.drawArc and \nGraphics.fillArc routines. Can be run \neither as a standalone application by typing 'java ArcTest' \nor as an applet in the AppletViewer.";
        return "";
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    private void jbInit() throws Exception {
        this.setBackground(new Color(255, 255, 255));
    }
}
