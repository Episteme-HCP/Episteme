// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
package org.jscience.physics.electricity.circuitry.gui;

import java.applet.Applet;

import java.awt.*;
import java.awt.event.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CircuitApplet extends Applet implements ComponentListener {
    /**
     * DOCUMENT ME!
     */
    CircuitFrame ogf;

    /**
     * DOCUMENT ME!
     */
    boolean started = false;

    /**
     * DOCUMENT ME!
     */
    void destroyFrame() {
        if (ogf != null) {
            ogf.dispose();
        }

        ogf = null;
        repaint();
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        addComponentListener(this);
    }

    /**
     * DOCUMENT ME!
     */
    void showFrame() {
        if (ogf == null) {
            started = true;
            ogf = new CircuitFrame(this);
            ogf.init();
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void toggleSwitch(int x) {
        ogf.toggleSwitch(x);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        String s = "Applet is open in a separate window.";

        if (!started) {
            s = "Applet is starting.";
        } else if (ogf == null) {
            s = "Applet is finished.";
        } else if (ogf.useFrame) {
            ogf.show();
        }

        g.drawString(s, 10, 30);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentHidden(ComponentEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentMoved(ComponentEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentShown(ComponentEvent e) {
        showFrame();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void componentResized(ComponentEvent e) {
        if (ogf != null) {
            ogf.componentResized(e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        if (ogf != null) {
            ogf.dispose();
        }

        ogf = null;
        repaint();
    }
}
