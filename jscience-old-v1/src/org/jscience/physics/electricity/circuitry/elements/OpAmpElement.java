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
public class OpAmpElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public int opsize;

    /**
     * DOCUMENT ME!
     */
    public int opheight;

    /**
     * DOCUMENT ME!
     */
    public int opwidth;

    /**
     * DOCUMENT ME!
     */
    public int opaddtext;

    /**
     * DOCUMENT ME!
     */
    public double maxOut;

    /**
     * DOCUMENT ME!
     */
    public double minOut;

    /**
     * DOCUMENT ME!
     */
    public final int FLAG_SWAP = 1;

    /**
     * DOCUMENT ME!
     */
    public final int FLAG_SMALL = 2;

    /**
     * DOCUMENT ME!
     */
    public int in1px;

    /**
     * DOCUMENT ME!
     */
    public int in1py;

    /**
     * DOCUMENT ME!
     */
    public int in2px;

    /**
     * DOCUMENT ME!
     */
    public int in2py;

    /**
     * DOCUMENT ME!
     */
    public int outpx;

    /**
     * DOCUMENT ME!
     */
    public int outpy;

    /**
     * DOCUMENT ME!
     */
    public int in1nx;

    /**
     * DOCUMENT ME!
     */
    public int in1ny;

    /**
     * DOCUMENT ME!
     */
    public int in2nx;

    /**
     * DOCUMENT ME!
     */
    public int in2ny;

    /**
     * DOCUMENT ME!
     */
    public int outnx;

    /**
     * DOCUMENT ME!
     */
    public int outny;

    /**
     * DOCUMENT ME!
     */
    public int[] triPointsX;

    /**
     * DOCUMENT ME!
     */
    public int[] triPointsY;

    /**
     * DOCUMENT ME!
     */
    public final double gain = 1000;

    /**
     * DOCUMENT ME!
     */
    public double lastvd;

    /**
     * Creates a new OpAmpElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public OpAmpElement(int xx, int yy) {
        super(xx, yy);
        noDiagonal = true;
        maxOut = 15;
        minOut = -15;
        setSize(circuitFrame.smallGridCheckItem.getState() ? 1 : 2);
    }

    /**
     * Creates a new OpAmpElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public OpAmpElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);

        try {
            maxOut = new Double(st.nextToken()).doubleValue();
            minOut = new Double(st.nextToken()).doubleValue();
        } catch (Exception e) {
            maxOut = 15;
            minOut = -15;
        }

        noDiagonal = true;
        setSize(((f & FLAG_SMALL) != 0) ? 1 : 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "a " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        maxOut + " " + minOut;
    }

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
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, in1px, in1py, in1nx, in1ny);
        setVoltageColor(g, volts[1]);
        circuitFrame.drawThickLine(g, in2px, in2py, in2nx, in2ny);
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);
        setPowerColor(g, true);
        circuitFrame.drawThickPolygon(g, triPointsX, triPointsY, 3);

        Font f = new Font("SansSerif", 0, 10);
        g.setFont(f);
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.lightGray);
        g.drawString("-", in1nx + 5, (in1ny + 3) - opaddtext);
        g.drawString("+", in2nx + 5, in2ny + 5 + opaddtext);
        setVoltageColor(g, volts[2]);
        circuitFrame.drawThickLine(g, outnx, outny, outpx, outpy);
        curcount = updateDotCount(current, curcount);
        circuitFrame.drawDots(g, outpx, outpy, outnx, outny, curcount);
        drawPost(g, in1px, in1py, nodes[0]);
        drawPost(g, in2px, in2py, nodes[1]);
        drawPost(g, outpx, outpy, nodes[2]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPower() {
        return volts[2] * current;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void setSize(int s) {
        opsize = s;
        opheight = 8 * s;
        opwidth = 13 * s;
        flags = (flags & ~FLAG_SMALL) | ((s == 1) ? FLAG_SMALL : 0);
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
        if (x2 == x) {
            y2 = y;
        }

        int dx = x2 - x;
        int dy = y2 - y;
        double dn = Math.sqrt((dx * dx) + (dy * dy));

        if (dn > 150) {
            setSize(2);
        }

        int hs = opheight;
        int dpx = (int) (Math.abs((hs * dy) / dn));
        int dpy = (int) (-Math.abs((hs * dx) / dn));

        if ((flags & FLAG_SWAP) != 0) {
            dpx = -dpx;
            dpy = -dpy;
        }

        opaddtext = (opsize == 1) ? (dpy / 2) : 0;
        in1px = x + dpx;
        in1py = y + dpy;
        in2px = x - dpx;
        in2py = y - dpy;
        outpx = x2;
        outpy = y2;

        int ww = opwidth; // 32;

        if (ww > (dn / 2)) {
            ww = (int) (dn / 2);
        }

        int x1a = (int) (x + ((dx * ((dn / 2) - ww)) / dn));
        int y1a = (int) (y + ((dy * ((dn / 2) - ww)) / dn));
        int x2a = (int) (x + ((dx * ((dn / 2) + ww)) / dn));
        int y2a = (int) (y + ((dy * ((dn / 2) + ww)) / dn));
        in1nx = x1a + dpx;
        in1ny = y1a + dpy;
        in2nx = x1a - dpx;
        in2ny = y1a - dpy;
        outnx = x2a;
        outny = y2a;

        triPointsX = new int[3];
        triPointsY = new int[3];
        triPointsX[0] = x1a + (dpx * 2);
        triPointsY[0] = y1a + (dpy * 2);
        triPointsX[1] = x1a - (dpx * 2);
        triPointsY[1] = y1a - (dpy * 2);
        triPointsX[2] = x2a;
        triPointsY[2] = y2a;
        setBbox(triPointsX[0], triPointsY[0], triPointsX[1], triPointsY[1]);
        adjustBbox(x, y, x2, y2);
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
        return (n == 0) ? new Point(in1px, in1py)
                        : ((n == 1) ? new Point(in2px, in2py)
                                    : new Point(outpx, outpy));
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
        arr[0] = "op-amp";
        arr[1] = "V+ = " + circuitFrame.getVoltageText(volts[1]);
        arr[2] = "V- = " + circuitFrame.getVoltageText(volts[0]);

        // sometimes the voltage goes slightly outside range, to make
        // convergence easier.  so we hide that here.
        double vo = Math.max(Math.min(volts[2], maxOut), minOut);
        arr[3] = "Vout = " + circuitFrame.getVoltageText(vo);
        arr[4] = "Iout = " + circuitFrame.getCurrentText(getCurrent());
        arr[5] = "range = " + circuitFrame.getVoltageText(minOut) + " to " +
            circuitFrame.getVoltageText(maxOut);
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
        int vn = circuitFrame.nodeList.size() + voltSource;
        circuitFrame.stampNonLinear(vn);
        circuitFrame.stampMatrix(nodes[2], vn, 1);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        double vd = volts[1] - volts[0];

        if (Math.abs(lastvd - vd) > .1) {
            circuitFrame.converged = false;
        } else if ((volts[2] > (maxOut + .1)) || (volts[2] < (minOut - .1))) {
            circuitFrame.converged = false;
        }

        double x = 0;
        int vn = circuitFrame.nodeList.size() + voltSource;
        double dx = 0;

        if ((vd >= (maxOut / gain)) && ((lastvd >= 0) || (circuitFrame.getrand(4) == 1))) {
            dx = 1e-4;
            x = maxOut - ((dx * maxOut) / gain);
        } else if ((vd <= (minOut / gain)) &&
                ((lastvd <= 0) || (circuitFrame.getrand(4) == 1))) {
            dx = 1e-4;
            x = minOut - ((dx * minOut) / gain);
        } else {
            dx = gain;
        }

        //System.out.println("opamp " + vd + " " + volts[2] + " " + dx + " "  + x + " " + lastvd + " " + converged);

        // newton-raphson
        circuitFrame.stampMatrix(vn, nodes[0], dx);
        circuitFrame.stampMatrix(vn, nodes[1], -dx);
        circuitFrame.stampMatrix(vn, nodes[2], 1);
        circuitFrame.stampRightSide(vn, x);

        lastvd = vd;

        /*if (converged)
          System.out.println((volts[1]-volts[0]) + " " + volts[2] + " " + initvd);*/
    }

    // there is no current path through the op-amp inputs, but there
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

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasGroundConnection(int n1) {
        return (n1 == 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[2] - volts[1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'a';
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
            return new EditInfo("Max Output (V)", maxOut, 1, 20);
        }

        if (n == 1) {
            return new EditInfo("Min Output (V)", minOut, -20, 0);
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
            maxOut = ei.value;
        }

        if (n == 1) {
            minOut = ei.value;
        }
    }
}
