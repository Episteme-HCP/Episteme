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
public class AnalogSwitchElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public int x3;

    /**
     * DOCUMENT ME!
     */
    public int y3;

    /**
     * DOCUMENT ME!
     */
    public int x3a;

    /**
     * DOCUMENT ME!
     */
    public int y3a;

    /**
     * DOCUMENT ME!
     */
    public boolean open;

    /**
     * DOCUMENT ME!
     */
    public final double resistance = 20;

    /**
     * Creates a new AnalogSwitchElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public AnalogSwitchElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new AnalogSwitchElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public AnalogSwitchElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 159;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawSwitch(g, x, y, x2, y2, volts[0], volts[1], open);

        if (!open) {
            doDots(g);
        }

        drawPosts(g);
        drawPost(g, x3, y3, nodes[2]);
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        if (open) {
            current = 0;
        } else {
            current = (volts[0] - volts[1]) / resistance;
        }
    }

    // we need this to be able to change the matrix for each step
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nonLinear() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampNonLinear(nodes[0]);
        circuitFrame.stampNonLinear(nodes[1]);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        open = (volts[2] < 2.5);

        if (!open) {
            circuitFrame.stampResistor(nodes[0], nodes[1], resistance);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     * @param open DOCUMENT ME!
     */
    public void drawSwitch(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2, boolean open) {
        int i;
        int ox = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;
        int openhs = 16;
        int hs = (open) ? openhs : 0;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        setBbox(x, y, x2, y2);

        if (dn > 40) {
            int x1a = (int) (x1 + ((dx * ((dn / 2) - 16)) / dn));
            int y1a = (int) (y1 + ((dy * ((dn / 2) - 16)) / dn));
            int x2a = (int) (x1 + ((dx * ((dn / 2) + 16)) / dn));
            int y2a = (int) (y1 + ((dy * ((dn / 2) + 16)) / dn));
            setVoltageColor(g, v1);
            circuitFrame.drawThickLine(g, x1, y1, x1a, y1a);
            setVoltageColor(g, v2);
            circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);
            dn = 32;
            x1 = x1a;
            y1 = y1a;
            x2 = x2a;
            y2 = y2a;
            dx = x2 - x1;
            dy = y2 - y1;
        }

        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        g.setColor(Color.lightGray);
        circuitFrame.drawThickLine(g, x1, y1, x2 + dpx, y2 + dpy);
        setVoltageColor(g, volts[2]);
        circuitFrame.drawThickLine(g, x3, y3, x3a, y3a);
        adjustBbox(x3, y3, x2, y2);
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        int dx = x2 - x;
        int dy = y2 - y;
        int openhs = 16;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx0 = (int) ((openhs * dy) / dn);
        int dpy0 = (int) ((-openhs * dx) / dn);
        x3 = (x + (dx / 2)) - dpx0;
        y3 = (y + (dy / 2)) - dpy0;
        x3a = (x + (dx / 2)) - (dpx0 / 2);
        y3a = (y + (dy / 2)) - (dpy0 / 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public void drag(int xx, int yy) {
        xx = circuitFrame.snapGrid(xx);
        yy = circuitFrame.snapGrid(yy);

        if (circuitFrame.abs(x - xx) < circuitFrame.abs(y - yy)) {
            xx = x;
        } else {
            yy = y;
        }

        int q1 = circuitFrame.abs(x - xx) + circuitFrame.abs(y - yy);
        int q2 = (q1 / 2) % circuitFrame.gridSize;

        if (q2 != 0) {
            return;
        }

        x2 = xx;
        y2 = yy;
        setPoints();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 3;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPost(int n) {
        return (n == 0) ? new Point(x, y)
                        : ((n == 1) ? new Point(x2, y2) : new Point(x3, y3));
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "analog switch";
        arr[1] = open ? "open" : "closed";
        arr[2] = "Vd = " + circuitFrame.getVoltageDText(getVoltageDiff());
        arr[3] = "I = " + circuitFrame.getCurrentDText(getCurrent());
        arr[4] = "Vc = " + circuitFrame.getVoltageText(volts[2]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     * @param n2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getConnection(int n1, int n2) {
        return false;
    }
}
