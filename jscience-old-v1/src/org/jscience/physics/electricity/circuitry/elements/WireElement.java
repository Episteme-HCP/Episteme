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
public class WireElement extends CircuitElement {
    /**
     * Creates a new WireElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public WireElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new WireElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public WireElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    /*void setCurrent(double c) {
        current = c;
        System.out.print("wire current set to " + c + "\n");
        }*/
    public void draw(Graphics g) {
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x2, y2);
        doDots(g);

        if (y == y2) {
            setBbox(x, y - 3, x2, y + 3);
        } else if (x == x2) {
            setBbox(x - 3, y, x + 3, y2);
        } else {
            setBbox(x, y, x2, y2);
        }

        drawPosts(g);
    }

    //static final double resistance = .001;
    /*void calculateCurrent() {
        current = (volts[0]-volts[1])/resistance;
        }*/
    public void stamp() {
        circuitFrame.stampVoltageSource(nodes[0], nodes[1], voltSource, 0);
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
        arr[0] = "wire";
        arr[1] = "I = " + circuitFrame.getCurrentDText(getCurrent());
        arr[2] = "V = " + circuitFrame.getVoltageText(volts[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'w';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPower() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWire() {
        return true;
    }
}
