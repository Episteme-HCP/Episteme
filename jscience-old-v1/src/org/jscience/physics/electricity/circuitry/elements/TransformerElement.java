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
public class TransformerElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public double inductance;

    /**
     * DOCUMENT ME!
     */
    public double ratio;

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
    public int x4;

    /**
     * DOCUMENT ME!
     */
    public int y4;

    /**
     * DOCUMENT ME!
     */
    public int coil1ax;

    /**
     * DOCUMENT ME!
     */
    public int coil1ay;

    /**
     * DOCUMENT ME!
     */
    public int coil1bx;

    /**
     * DOCUMENT ME!
     */
    public int coil1by;

    /**
     * DOCUMENT ME!
     */
    public int coil2ax;

    /**
     * DOCUMENT ME!
     */
    public int coil2ay;

    /**
     * DOCUMENT ME!
     */
    public int coil2bx;

    /**
     * DOCUMENT ME!
     */
    public int coil2by;

    /**
     * DOCUMENT ME!
     */
    public int core1ax;

    /**
     * DOCUMENT ME!
     */
    public int core1ay;

    /**
     * DOCUMENT ME!
     */
    public int core1bx;

    /**
     * DOCUMENT ME!
     */
    public int core1by;

    /**
     * DOCUMENT ME!
     */
    public int core2ax;

    /**
     * DOCUMENT ME!
     */
    public int core2ay;

    /**
     * DOCUMENT ME!
     */
    public int core2bx;

    /**
     * DOCUMENT ME!
     */
    public int core2by;

    /**
     * DOCUMENT ME!
     */
    public double current1;

    /**
     * DOCUMENT ME!
     */
    public double current2;

    /**
     * DOCUMENT ME!
     */
    public double curcount1;

    /**
     * DOCUMENT ME!
     */
    public double curcount2;

    /**
     * DOCUMENT ME!
     */
    public double a1;

    /**
     * DOCUMENT ME!
     */
    public double a2;

    /**
     * DOCUMENT ME!
     */
    public double a3;

    /**
     * DOCUMENT ME!
     */
    public double a4;

    /**
     * DOCUMENT ME!
     */
    public double curSourceValue1;

    /**
     * DOCUMENT ME!
     */
    public double curSourceValue2;

    /**
     * Creates a new TransformerElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public TransformerElement(int xx, int yy) {
        super(xx, yy);
        inductance = 4;
        ratio = 1;
        noDiagonal = true;
    }

    /**
     * Creates a new TransformerElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public TransformerElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        inductance = new Double(st.nextToken()).doubleValue();
        ratio = new Double(st.nextToken()).doubleValue();
        current1 = new Double(st.nextToken()).doubleValue();
        current2 = new Double(st.nextToken()).doubleValue();
        noDiagonal = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'T';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "T " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        inductance + " " + ratio + " " + current1 + " " + current2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, coil1ax, coil1ay);
        setVoltageColor(g, volts[2]);
        circuitFrame.drawThickLine(g, x3, y3, coil1bx, coil1by);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, x2, y2, coil2ax, coil2ay);
        setVoltageColor(g, volts[3]);
        circuitFrame.drawThickLine(g, x4, y4, coil2bx, coil2by);
        setPowerColor(g, current1 * (volts[0] - volts[2]));
        drawCoil(g, coil1ax, coil1ay, coil1bx, coil1by, core1ax, core1ay,
            volts[0], volts[2]);
        setPowerColor(g, current2 * (volts[1] - volts[3]));
        drawCoil(g, coil2ax, coil2ay, coil2bx, coil2by, core2ax, core2ay,
            volts[1], volts[3]);
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);
        circuitFrame.drawThickLine(g, core1ax, core1ay, core1bx, core1by);
        circuitFrame.drawThickLine(g, core2ax, core2ay, core2bx, core2by);
        curcount1 = updateDotCount(current1, curcount1);
        curcount2 = updateDotCount(current2, curcount2);
        setBbox(x, y, x4, y4);
        circuitFrame.drawDots(g, x, y, coil1ax, coil1ay, curcount1);
        circuitFrame.drawDots(g, coil1ax, coil1ay, coil1bx, coil1by, curcount1);
        circuitFrame.drawDots(g, coil1bx, coil1by, x3, y3, curcount1);
        circuitFrame.drawDots(g, x2, y2, coil2ax, coil2ay, curcount2);
        circuitFrame.drawDots(g, coil2ax, coil2ay, coil2bx, coil2by, curcount2);
        circuitFrame.drawDots(g, coil2bx, coil2by, x4, y4, curcount2);
        drawPosts(g);
        drawPost(g, x3, y3, nodes[2]);
        drawPost(g, x4, y4, nodes[3]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     * @param cox DOCUMENT ME!
     * @param coy DOCUMENT ME!
     * @param v1 DOCUMENT ME!
     * @param v2 DOCUMENT ME!
     */
    public void drawCoil(Graphics g, int x1, int y1, int x2, int y2, int cox, int coy,
        double v1, double v2) {
        int segments = 30;
        int xa = x1;
        int ya = y1;
        int i;
        int dx = x2 - x1;
        int dy = y2 - y1;
        double dn = Math.sqrt((dx * dx) + (dy * dy));

        for (i = 0; i != segments; i++) {
            double cx = ((((i + 1) * 6.) / segments) % 2) - 1;
            double hs = .5 * Math.sqrt(1 - (cx * cx));

            if (hs < 0) {
                hs = -hs;
            }

            int dpx = (int) ((cox - x1) * hs);
            int dpy = (int) ((coy - y1) * hs);
            double v = v1 + (((v2 - v1) * i) / segments);
            setVoltageColor(g, v);

            int xb = x1 + ((dx * (i + 1)) / segments) + dpx;
            int yb = y1 + ((dy * (i + 1)) / segments) + dpy;
            circuitFrame.drawThickLine(g, xa, ya, xb, yb);
            xa = xb;
            ya = yb;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        int hs = 32;
        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));
        int dpx = (int) (Math.abs((hs * dy) / dn));
        int dpy = (int) (Math.abs((hs * dx) / dn));
        x3 = x + dpx;
        y3 = y + dpy;
        x4 = x2 + dpx;
        y4 = y2 + dpy;

        int ce = 12;
        int cd = 2;
        coil1ax = (int) (x + ((dx * ((dn / 2) - ce)) / dn));
        coil1ay = (int) (y + ((dy * ((dn / 2) - ce)) / dn));
        coil1bx = (int) (x3 + ((dx * ((dn / 2) - ce)) / dn));
        coil1by = (int) (y3 + ((dy * ((dn / 2) - ce)) / dn));
        coil2ax = (int) (x + ((dx * ((dn / 2) + ce)) / dn));
        coil2ay = (int) (y + ((dy * ((dn / 2) + ce)) / dn));
        coil2bx = (int) (x3 + ((dx * ((dn / 2) + ce)) / dn));
        coil2by = (int) (y3 + ((dy * ((dn / 2) + ce)) / dn));
        core1ax = (int) (x + ((dx * ((dn / 2) - cd)) / dn));
        core1ay = (int) (y + ((dy * ((dn / 2) - cd)) / dn));
        core1bx = (int) (x3 + ((dx * ((dn / 2) - cd)) / dn));
        core1by = (int) (y3 + ((dy * ((dn / 2) - cd)) / dn));
        core2ax = (int) (x + ((dx * ((dn / 2) + cd)) / dn));
        core2ay = (int) (y + ((dy * ((dn / 2) + cd)) / dn));
        core2bx = (int) (x3 + ((dx * ((dn / 2) + cd)) / dn));
        core2by = (int) (y3 + ((dy * ((dn / 2) + cd)) / dn));
        setBbox(x, y, x2, y2);
        adjustBbox(x3, y3, x4, y4);
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
                        : ((n == 1) ? new Point(x2, y2)
                                    : ((n == 2) ? new Point(x3, y3)
                                                : new Point(x4, y4)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 4;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        current1 = current2 = volts[0] = volts[1] = volts[2] = volts[3] = 0;
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        // equations for transformer:
        //   v1 = L1 di1/dt + M  di2/dt
        //   v2 = M  di1/dt + L2 di2/dt
        // we invert that to get:
        //   di1/dt = a1 v1 + a2 v2
        //   di2/dt = a3 v1 + a4 v2
        // integrate di1/dt using trapezoidal approx and we get:
        //   i1(t2) = i1(t1) + dt/2 (i1(t1) + i1(t2))
        //          = i1(t1) + a1 dt/2 v1(t1) + a2 dt/2 v2(t1) +
        //                     a1 dt/2 v1(t2) + a2 dt/2 v2(t2)
        // the norton equivalent of this for i1 is:
        //  a. current source, I = i1(t1) + a1 dt/2 v1(t1) + a2 dt/2 v2(t1)
        //  b. resistor, G = a1 dt/2
        //  c. current source controlled by voltage v2, G = a2 dt/2
        // and for i2:
        //  a. current source, I = i2(t1) + a3 dt/2 v1(t1) + a4 dt/2 v2(t1)
        //  b. resistor, G = a3 dt/2
        //  c. current source controlled by voltage v2, G = a4 dt/2
        // 
        // first winding goes from node 0 to 2, second is from 1 to 3
        double l1 = inductance;
        double l2 = inductance * ratio * ratio;
        double m = .999 * Math.sqrt(l1 * l2);
        double deti = 1 / ((l1 * l2) - (m * m));
        a1 = (l2 * deti * circuitFrame.timeStep) / 2; // we multiply dt/2 into a1..a4 here
        a2 = (-m * deti * circuitFrame.timeStep) / 2;
        a3 = (-m * deti * circuitFrame.timeStep) / 2;
        a4 = (l1 * deti * circuitFrame.timeStep) / 2;
        circuitFrame.stampConductance(nodes[0], nodes[2], a1);
        circuitFrame.stampVCCurrentSource(nodes[0], nodes[2], nodes[1], nodes[3], a2);
        circuitFrame.stampVCCurrentSource(nodes[1], nodes[3], nodes[0], nodes[2], a3);
        circuitFrame.stampConductance(nodes[1], nodes[3], a4);
        circuitFrame.stampRightSide(nodes[0]);
        circuitFrame.stampRightSide(nodes[1]);
        circuitFrame.stampRightSide(nodes[2]);
        circuitFrame.stampRightSide(nodes[3]);
    }

    /**
     * DOCUMENT ME!
     */
    public void startIteration() {
        double voltdiff1 = volts[0] - volts[2];
        double voltdiff2 = volts[1] - volts[3];
        curSourceValue1 = (voltdiff1 * a1) + (voltdiff2 * a2) + current1;
        curSourceValue2 = (voltdiff1 * a3) + (voltdiff2 * a4) + current2;
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        circuitFrame.stampCurrentSource(nodes[0], nodes[2], curSourceValue1);
        circuitFrame.stampCurrentSource(nodes[1], nodes[3], curSourceValue2);
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        double voltdiff1 = volts[0] - volts[2];
        double voltdiff2 = volts[1] - volts[3];
        current1 = (voltdiff1 * a1) + (voltdiff2 * a2) + curSourceValue1;
        current2 = (voltdiff1 * a3) + (voltdiff2 * a4) + curSourceValue2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "transformer";
        arr[1] = "L = " + circuitFrame.getUnitText(inductance, "H");
        arr[2] = "Ratio = " + ratio;
        //arr[3] = "I1 = " + getCurrentText(current1);
        arr[3] = "Vd1 = " + circuitFrame.getVoltageText(volts[0] - volts[2]);
        //arr[5] = "I2 = " + getCurrentText(current2);
        arr[4] = "Vd2 = " + circuitFrame.getVoltageText(volts[1] - volts[3]);
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
        if (circuitFrame.comparePair(n1, n2, 0, 2)) {
            return true;
        }

        if (circuitFrame.comparePair(n1, n2, 1, 3)) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditInfo getEditInfo(int n) {
        if (n == 0) {
            return new EditInfo("Inductance (H)", inductance, .01, 5);
        }

        if (n == 1) {
            return new EditInfo("Ratio", ratio, 1, 10);
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
        if (n == 0) {
            inductance = ei.value;
        }

        if (n == 1) {
            ratio = ei.value;
        }
    }
}
