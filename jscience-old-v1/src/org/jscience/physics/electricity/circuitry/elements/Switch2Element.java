// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import java.awt.*;

import java.util.StringTokenizer;


// DPST switch
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class Switch2Element extends SwitchElement {
    /**
     * DOCUMENT ME!
     */
    public int link;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int x3a;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int y3a;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int x3b;

    // posts
    /**
     * DOCUMENT ME!
     */
    public int y3b;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spxa;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spya;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spxb;

    // switch poles
    /**
     * DOCUMENT ME!
     */
    public int spyb;

    /**
     * Creates a new Switch2Element object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public Switch2Element(int xx, int yy) {
        super(xx, yy, false);
        noDiagonal = true;
    }

    /**
     * Creates a new Switch2Element object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param mm DOCUMENT ME!
     */
    public Switch2Element(int xx, int yy, boolean mm) {
        super(xx, yy, mm);
        noDiagonal = true;
    }

    /**
     * Creates a new Switch2Element object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public Switch2Element(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        link = new Integer(st.nextToken()).intValue();
        noDiagonal = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'S';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "S " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        open + " " + momentary + " " + link;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawSwitch(g, x, y, x2, y2, volts[0], volts[1], volts[2], open);
        updateDotCount();
        circuitFrame.drawDots(g, x, y, switchx1, switchy1, curcount);

        if (!open) {
            circuitFrame.drawDots(g, spxa, spya, x3a, y3a, curcount);
        } else {
            circuitFrame.drawDots(g, spxb, spyb, x3b, y3b, curcount);
        }

        drawPost(g, x, y, nodes[0]);
        drawPost(g, x3a, y3a, nodes[1]);
        drawPost(g, x3b, y3b, nodes[2]);
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
                        : ((n == 1) ? new Point(x3a, y3a) : new Point(x3b, y3b));
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
     */
    public void setPoints() {
        int hs = 16;
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        x3a = x2 + dpx;
        y3a = y2 + dpy;
        x3b = x2 - dpx;
        y3b = y2 - dpy;
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        // XXX
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        if (!open) {
            circuitFrame.stampVoltageSource(nodes[0], nodes[1], voltSource, 0);
        } else {
            circuitFrame.stampVoltageSource(nodes[0], nodes[2], voltSource, 0);
        }
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
     * @param g DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     * @param v3 DOCUMENT ME!
     * @param open DOCUMENT ME!
     */
    public void drawSwitch(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2, double v3, boolean open) {
        int i;
        int ox = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;
        int hs = 16;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        setBbox(x1, y1, x2, y2);

        if (dn > 40) {
            int x1a = (int) (x1 + ((dx * ((dn / 2) - 16)) / dn));
            int y1a = (int) (y1 + ((dy * ((dn / 2) - 16)) / dn));
            int x2a = (int) (x1 + ((dx * ((dn / 2) + 16)) / dn));
            int y2a = (int) (y1 + ((dy * ((dn / 2) + 16)) / dn));
            setVoltageColor(g, v1);
            circuitFrame.drawThickLine(g, x1, y1, x1a, y1a);
            setVoltageColor(g, v2);
            spxa = x2a + dpx;
            spya = y2a + dpy;
            spxb = x2a - dpx;
            spyb = y2a - dpy;
            circuitFrame.drawThickLine(g, spxa, spya, x2 + dpx, y2 + dpy);
            setVoltageColor(g, v3);
            circuitFrame.drawThickLine(g, spxb, spyb, x2 - dpx, y2 - dpy);
            dn = 32;
            x1 = x1a;
            y1 = y1a;
            x2 = x2a;
            y2 = y2a;
            dx = x2 - x1;
            dy = y2 - y1;
        }

        switchx1 = x1;
        switchx2 = x2;
        switchy1 = y1;
        switchy2 = y2;
        g.setColor(Color.white);

        int dir = (open) ? (-1) : 1;
        circuitFrame.drawThickLine(g, x1, y1, x2 + (dpx * dir), y2 + (dpy * dir));
        adjustBbox(x1, y1, x2 + dpx, y2 + dpy);
        adjustBbox(x1, y1, x2 - dpx, y2 - dpy);
    }

    /**
     * DOCUMENT ME!
     */
    public void toggle() {
        open = !open;

        if (link != 0) {
            int i;

            for (i = 0; i != circuitFrame.elmList.size(); i++) {
                Object o = circuitFrame.elmList.elementAt(i);

                if (o instanceof Switch2Element) {
                    Switch2Element s2 = (Switch2Element) o;

                    if (s2.link == link) {
                        s2.open = open;
                    }
                }
            }
        }
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
        if (circuitFrame.comparePair(n1, n2, 0, 2) && open) {
            return true;
        }

        if (circuitFrame.comparePair(n1, n2, 0, 1) && !open) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = (link == 0) ? "switch (DPST)" : "switch (DPDT)";
        arr[1] = "I = " + circuitFrame.getCurrentDText(getCurrent());
    }
}
