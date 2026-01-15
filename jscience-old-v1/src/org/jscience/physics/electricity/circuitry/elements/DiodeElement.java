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
public class DiodeElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public static final double vt = .025;

    /**
     * DOCUMENT ME!
     */
    public static final double vdcoef = 1 / vt; // *2?

    /**
     * DOCUMENT ME!
     */
    public double leakage = 1e-14; // was 1e-9;

    /**
     * DOCUMENT ME!
     */
    public double lastvoltdiff;

    /**
     * DOCUMENT ME!
     */
    public double vcrit;

    /**
     * Creates a new DiodeElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public DiodeElement(int xx, int yy) {
        super(xx, yy);
        setup();
    }

    /**
     * Creates a new DiodeElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public DiodeElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
        setup();
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
     */
    public void setup() {
        vcrit = vt * Math.log(vt / (Math.sqrt(2) * leakage));

        //System.out.println("vcrit " + vcrit); // fix
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'd';
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void draw(Graphics g) {
        drawDiode(g, x, y, x2, y2, volts[0], volts[1]);
        doDots(g);
        drawPosts(g);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        lastvoltdiff = volts[0] = volts[1] = 0;
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
    public void drawDiode(Graphics g, int x1, int y1, int x2, int y2, double v1,
        double v2) {
        int i;
        int ox = 0;
        int dx = x2 - x1;
        int dy = y2 - y1;
        int hs = 8;
        setBbox(x1, y1, x2, y2);

        double dn = Math.sqrt((dx * dx) + (dy * dy));

        if (dn > 20) {
            int x1a = (int) (x1 + ((dx * ((dn / 2) - 8)) / dn));
            int y1a = (int) (y1 + ((dy * ((dn / 2) - 8)) / dn));
            int x2a = (int) (x1 + ((dx * ((dn / 2) + 8)) / dn));
            int y2a = (int) (y1 + ((dy * ((dn / 2) + 8)) / dn));
            setVoltageColor(g, v1);
            circuitFrame.drawThickLine(g, x1, y1, x1a, y1a);
            setVoltageColor(g, v2);
            circuitFrame.drawThickLine(g, x2a, y2a, x2, y2);
            dn = 16;
            x1 = x1a;
            y1 = y1a;
            x2 = x2a;
            y2 = y2a;
            dx = x2 - x1;
            dy = y2 - y1;
        }

        setPowerColor(g, true);
        setVoltageColor(g, v1);

        int dpx = (int) ((hs * dy) / dn);
        int dpy = (int) ((-hs * dx) / dn);
        circuitFrame.xpoints[0] = x1 + dpx;
        circuitFrame.ypoints[0] = y1 + dpy;
        circuitFrame.xpoints[1] = x1 - dpx;
        circuitFrame.ypoints[1] = y1 - dpy;
        circuitFrame.xpoints[2] = x2;
        circuitFrame.ypoints[2] = y2;
        g.fillPolygon(circuitFrame.xpoints, circuitFrame.ypoints, 3);
        setVoltageColor(g, v2);
        circuitFrame.drawThickLine(g, x2 + dpx, y2 + dpy, x2, y2);
        circuitFrame.drawThickLine(g, x2 - dpx, y2 - dpy, x2, y2);
        adjustBbox(x2 + dpx, y2 + dpy, x2 - dpx, y2 - dpy);
    }

    /**
     * DOCUMENT ME!
     *
     * @param vnew DOCUMENT ME!
     * @param vold DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double limitStep(double vnew, double vold) {
        // this is what spice does for limiting
        double arg;
        double oo = vnew;

        if ((vnew > vcrit) && (Math.abs(vnew - vold) > (vt + vt))) {
            if (vold > 0) {
                arg = 1 + ((vnew - vold) / vt);

                if (arg > 0) {
                    vnew = vold + (vt * Math.log(arg));

                    double v0 = Math.log(1e-6 / leakage) * vt;
                    vnew = Math.max(v0, vnew);

                    //System.out.println(oo + " " + vnew);
                } else {
                    vnew = vcrit;
                }
            } else {
                vnew = vt * Math.log(vnew / vt);
            }

            circuitFrame.converged = false;

            //System.out.println(vnew + " " + oo + " " + vold);
        }

        return (vnew);
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
        double voltdiff = volts[0] - volts[1];

        // tried .01 here, didn't work w/ filtered full-wave rect
        if (Math.abs(voltdiff - lastvoltdiff) > .1) {
            circuitFrame.converged = false;
        }

        voltdiff = limitStep(voltdiff, lastvoltdiff);
        lastvoltdiff = voltdiff;

        double eval = Math.exp(voltdiff * vdcoef);

        // make diode linear with negative voltages; aids convergence
        if (voltdiff < 0) {
            eval = 1;
        }

        double geq = vdcoef * leakage * eval;
        double nc = ((eval - 1) * leakage) - (geq * voltdiff);
        //System.out.println("D " + voltdiff + " " + geq + " " + current + " " + lastvoltdiff);
        circuitFrame.stampConductance(nodes[0], nodes[1], geq);
        circuitFrame.stampCurrentSource(nodes[0], nodes[1], nc);
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        double voltdiff = volts[0] - volts[1];
        current = leakage * (Math.exp(voltdiff * vdcoef) - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "diode";
        arr[1] = "I = " + circuitFrame.getCurrentText(getCurrent());
        arr[2] = "Vd = " + circuitFrame.getVoltageText(getVoltageDiff());
        arr[3] = "P = " + circuitFrame.getUnitText(getPower(), "W");
    }
}
