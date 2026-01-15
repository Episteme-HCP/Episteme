// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import org.jscience.physics.electricity.circuitry.CircuitElement;
import org.jscience.physics.electricity.circuitry.gui.EditInfo;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ResistorElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double resistance;

    /**
     * Creates a new ResistorElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public ResistorElement(int xx, int yy) {
        super(xx, yy);
        resistance = 100;
    }

    /**
     * Creates a new ResistorElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public ResistorElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        resistance = new Double(st.nextToken()).doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'r';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "r " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        resistance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawResistor(g, x, y, x2, y2, volts[0], volts[1]);
        doDots(g);
        drawPosts(g);
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
     */
    public void drawResistor(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2) {
        int segments = 16;
        int i;
        int ox = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;
        int hs = circuitFrame.euroResistorCheckItem.getState() ? 6 : 8;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        setBbox(x1, y1, x2, y2);

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
        adjustBbox(x1 - dpx, y1 - dpy, x2 + dpx, y2 + dpy);
        setPowerColor(g, true);

        if (!circuitFrame.euroResistorCheckItem.getState()) {
            for (i = 0; i != segments; i++) {
                int nx = 0;

                switch (i & 3) {
                case 0:
                    nx = 1;

                    break;

                case 2:
                    nx = -1;

                    break;

                default:
                    nx = 0;

                    break;
                }

                double v = v1 + (((v2 - v1) * i) / segments);
                setVoltageColor(g, v);

                int xa = x1 + ((dx * i) / segments) + (dpx * ox);
                int ya = y1 + ((dy * i) / segments) + (dpy * ox);
                int xb = x1 + ((dx * (i + 1)) / segments) + (dpx * nx);
                int yb = y1 + ((dy * (i + 1)) / segments) + (dpy * nx);
                circuitFrame.drawThickLine(g, xa, ya, xb, yb);
                ox = nx;
            }
        } else {
            setVoltageColor(g, v1);
            circuitFrame.drawThickLine(g, x1 + dpx, y1 + dpy, x1 - dpx, y1 - dpy);

            for (i = 0; i != segments; i++) {
                double v = v1 + (((v2 - v1) * i) / segments);
                setVoltageColor(g, v);

                int xa = x1 + ((dx * i) / segments);
                int ya = y1 + ((dy * i) / segments);
                int xb = x1 + ((dx * (i + 1)) / segments);
                int yb = y1 + ((dy * (i + 1)) / segments);
                circuitFrame.drawThickLine(g, xa + dpx, ya + dpy, xb + dpx, yb + dpy);
                circuitFrame.drawThickLine(g, xa - dpx, ya - dpy, xb - dpx, yb - dpy);
            }

            circuitFrame.drawThickLine(g, x2 + dpx, y2 + dpy, x2 - dpx, y2 - dpy);
        }

        if (circuitFrame.showValuesCheckItem.getState()) {
            String s = circuitFrame.getShortUnitText(resistance, "");
            drawValues(g, s, (x2 + x1) / 2, (y2 + y1) / 2, dpx, dpy);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        current = (volts[0] - volts[1]) / resistance;

        //System.out.print(this + " res current set to " + current + "\n");
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        circuitFrame.stampResistor(nodes[0], nodes[1], resistance);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "resistor";
        getBasicInfo(arr);
        arr[3] = "R = " + circuitFrame.getUnitText(resistance, circuitFrame.ohmString);
        arr[4] = "P = " + circuitFrame.getUnitText(getPower(), "W");
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditInfo getEditInfo(int n) {
        // ohmString doesn't work here on linux
        if (n == 0) {
            return new EditInfo("Resistance (ohms)", resistance, 0, 0);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param ei DOCUMENT ME!
     */
    public void setEditValue(int n, EditInfo ei) {
        resistance = ei.value;
    }
}
