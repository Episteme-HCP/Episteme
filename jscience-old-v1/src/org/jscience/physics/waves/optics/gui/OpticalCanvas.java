package org.jscience.physics.waves.optics.gui;

import org.jscience.physics.waves.optics.elements.OpticalDevice;
import org.jscience.physics.waves.optics.rays.RayCaster;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpticalCanvas extends Canvas {
    /** DOCUMENT ME! */
    Image offScreenBuffer;

    /** DOCUMENT ME! */
    OpticalDevice v = null;

    /** DOCUMENT ME! */
    RayCaster raycaster = null;

    /** DOCUMENT ME! */
    private boolean shouldRedraw = true;

/**
     * Creates a new OpticalCanvas object.
     */
    public OpticalCanvas() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void setDevice(OpticalDevice e) {
        v = e;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setRayCaster(RayCaster r) {
        raycaster = r;
    }

    /**
     * DOCUMENT ME!
     */
    public void forceRedraw() {
        shouldRedraw = true;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.clearRect(0, 0, getSize().width, getSize().height);
        v.draw(g);

        if (raycaster != null) {
            raycaster.drawRays(g, v);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void update(Graphics g) {
        Graphics gr;

        // Will hold the graphics context from the offScreenBuffer.
        // We need to make sure we keep our offscreen buffer the same size
        // as the graphics context we're working with.
        if ((offScreenBuffer == null) ||
                (!((offScreenBuffer.getWidth(this) == this.getSize().width) &&
                (offScreenBuffer.getHeight(this) == this.getSize().height)))) {
            offScreenBuffer = this.createImage(getSize().width, getSize().height);
            shouldRedraw = true;
        }

        // We need to use our buffer Image as a Graphics object:
        gr = offScreenBuffer.getGraphics();

        if (shouldRedraw) {
            paint(gr);
            shouldRedraw = false;
        }

        // Passes our off-screen buffer to our paint method, which,
        // unsuspecting, paints on it just as it would on the Graphics
        // passed by the browser or applet viewer.
        g.drawImage(offScreenBuffer, 0, 0, this);

        // And now we transfer the info in the buffer onto the
        // graphics context we got from the browser in one smooth motion.
    }
}
