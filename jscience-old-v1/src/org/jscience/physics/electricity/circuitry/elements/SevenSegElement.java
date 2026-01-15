// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class SevenSegElement extends ChipElement {
    /**
     * DOCUMENT ME!
     */
    public Color darkred;

    /**
     * Creates a new SevenSegElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public SevenSegElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new SevenSegElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public SevenSegElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "7-segment driver/display";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        darkred = new Color(60, 0, 0);
        sizeX = 4;
        sizeY = 4;
        pins = new Pin[7];
        pins[0] = new Pin(0, SIDE_W, "a");
        pins[1] = new Pin(1, SIDE_W, "b");
        pins[2] = new Pin(2, SIDE_W, "c");
        pins[3] = new Pin(3, SIDE_W, "d");
        pins[4] = new Pin(1, SIDE_S, "e");
        pins[5] = new Pin(2, SIDE_S, "f");
        pins[6] = new Pin(3, SIDE_S, "g");
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawChip(g);
        g.setColor(Color.red);

        int xl = x + (cspc * 5);
        int yl = y + cspc;
        setColor(g, 0);
        circuitFrame.drawThickLine(g, xl, yl, xl + cspc, yl);
        setColor(g, 1);
        circuitFrame.drawThickLine(g, xl + cspc, yl, xl + cspc, yl + cspc);
        setColor(g, 2);
        circuitFrame.drawThickLine(g, xl + cspc, yl + cspc, xl + cspc, yl + cspc2);
        setColor(g, 3);
        circuitFrame.drawThickLine(g, xl, yl + cspc2, xl + cspc, yl + cspc2);
        setColor(g, 4);
        circuitFrame.drawThickLine(g, xl, yl + cspc, xl, yl + cspc2);
        setColor(g, 5);
        circuitFrame.drawThickLine(g, xl, yl, xl, yl + cspc);
        setColor(g, 6);
        circuitFrame.drawThickLine(g, xl, yl + cspc, xl + cspc, yl + cspc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param p DOCUMENT ME!
     */
    public void setColor(Graphics g, int p) {
        g.setColor(pins[p].value ? Color.red : darkred);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 7;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 157;
    }
}
