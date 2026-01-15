// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import org.jscience.physics.electricity.circuitry.CircuitElement;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class GroundElement extends CircuitElement {
    /**
     * Creates a new GroundElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public GroundElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new GroundElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public GroundElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'g';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        setVoltageColor(g, 0);
        circuitFrame.drawThickLine(g, x, y, x2, y2);

        double dx = x2 - x;
        double dy = y2 - y;
        double dr = Math.sqrt((dx * dx) + (dy * dy));
        dx /= dr;
        dy /= dr;

        //drawThickLine(g, x, y, (int) (x2-dx*10), (int) (y2-dy*10));
        int i;

        for (i = 0; i != 3; i++) {
            int a = 10 - (i * 4);
            int b = i * 5; // -10;
            circuitFrame.drawThickLine(g, (int) (x2 + (dx * b) + (dy * a)),
                (int) ((y2 + (dy * b)) - (dx * a)),
                (int) ((x2 + (dx * b)) - (dy * a)),
                (int) (y2 + (dy * b) + (dx * a)));
        }

        doDots(g);
        setBbox(x, y, x2, y2);
        adjustBbox(x2 + (int) ((dx * 10) + (dy * 10)),
            y2 + (int) ((dy * 10) - (dx * 10)),
            x2 + (int) ((dx * 10) - (dy * 10)),
            y2 + (int) ((dy * 10) + (dx * 10)));
        drawPost(g, x, y, nodes[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setCurrent(int x, double c) {
        current = -c;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampVoltageSource(0, nodes[0], voltSource, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "ground";
        arr[1] = "I = " + circuitFrame.getCurrentText(getCurrent());
    }

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasGroundConnection(int n1) {
        return true;
    }
}
