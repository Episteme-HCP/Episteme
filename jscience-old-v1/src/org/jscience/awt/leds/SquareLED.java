// SquareLED Class
// Written by: Craig A. Lindley
// Last Update: 07/12/98
package org.jscience.awt.leds;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SquareLED extends LEDBase {
    /** DOCUMENT ME! */
    private static final int BORDERPAD = 5;

    // Private class data
    /** DOCUMENT ME! */
    private boolean raised;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private int height;

/**
     * Creates a new SquareLED object.
     *
     * @param width      DOCUMENT ME!
     * @param height     DOCUMENT ME!
     * @param raised     DOCUMENT ME!
     * @param ledColor   DOCUMENT ME!
     * @param panelColor DOCUMENT ME!
     * @param mode       DOCUMENT ME!
     * @param rate       DOCUMENT ME!
     * @param state      DOCUMENT ME!
     */
    public SquareLED(int width, int height, boolean raised, Color ledColor,
        Color panelColor, int mode, boolean rate, boolean state) {
        // Allow the superclass constructor to do its thing
        super(ledColor, panelColor, mode, rate, state);

        // Save incoming
        setRaised(raised);
        setWidth(width);
        setHeight(height);
    }

    // Zero agrument constructor
/**
     * Creates a new SquareLED object.
     */
    public SquareLED() {
        this(18, 12, true, Color.red, Color.lightGray, MODESOLID, false, false);
    }

    // Accessor methods
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getRaised() {
        return raised;
    }

    /**
     * DOCUMENT ME!
     *
     * @param raised DOCUMENT ME!
     */
    public void setRaised(boolean raised) {
        this.raised = raised;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        this.width = width;
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        this.height = height;
        repaint();
    }

    // Other public methods
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        int cwidth = getSize().width;
        int cheight = getSize().height;
        int xOrg = (cwidth - width) / 2;
        int yOrg = (cheight - height) / 2;

        // Paint the background then the LED
        g.setColor(panelColor);
        g.fillRect(0, 0, cwidth, cheight);

        // Paint the ring around the LED
        g.setColor(Color.black);
        g.fill3DRect(xOrg, yOrg, width, height, raised);

        // Determine which color to paint the LED with
        Color newColor = ledState ? ledOnColor : ledOffColor;
        g.setColor(newColor);
        g.fill3DRect(xOrg, yOrg, width, height, raised);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        // Calculate the preferred size
        return new Dimension(width + BORDERPAD, height + BORDERPAD);
    }
}
