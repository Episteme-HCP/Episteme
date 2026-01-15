// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry;

import org.jscience.physics.electricity.circuitry.elements.VoltageElement;
import org.jscience.physics.electricity.circuitry.gui.CircuitFrame;
import org.jscience.physics.electricity.circuitry.gui.EditInfo;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public abstract class CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public int x;

    /**
     * DOCUMENT ME!
     */
    public int y;

    /**
     * DOCUMENT ME!
     */
    public int x2;

    /**
     * DOCUMENT ME!
     */
    public int y2;

    /**
     * DOCUMENT ME!
     */
    public int flags;

    /**
     * DOCUMENT ME!
     */
    public int[] nodes;

    /**
     * DOCUMENT ME!
     */
    public int voltSource;

    /**
     * DOCUMENT ME!
     */
    public double[] volts;

    /**
     * DOCUMENT ME!
     */
    public double current;

    /**
     * DOCUMENT ME!
     */
    public double curcount;

    /**
     * DOCUMENT ME!
     */
    public Rectangle boundingBox;

    /**
     * DOCUMENT ME!
     */
    public boolean noDiagonal;

    public CircuitFrame circuitFrame;

    /**
     * Creates a new CircuitElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public CircuitElement(int xx, int yy) {
        x = x2 = xx;
        y = y2 = yy;
        allocNodes();
        boundingBox = new Rectangle();
    }

    /**
     * Creates a new CircuitElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     */
    public CircuitElement(int xa, int ya, int xb, int yb, int f) {
        x = xa;
        y = ya;
        x2 = xb;
        y2 = yb;
        flags = f;
        allocNodes();
        boundingBox = new Rectangle();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 0;
    }

    public CircuitFrame getCircuitFrame() {
       return circuitFrame;
    }

    public void setCircuitFrame(CircuitFrame circuitFrame) {
      this.circuitFrame = circuitFrame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getDumpClass() {
        return getClass();
    }

    /**
     * DOCUMENT ME!
     */
    public void allocNodes() {
        nodes = new int[getPostCount() + getInternalNodeCount()];
        volts = new double[getPostCount() + getInternalNodeCount()];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        int t = getDumpType();

        return ((t < 127) ? (((char) t) + " ") : (t + " ")) + x + " " + y +
        " " + x2 + " " + y2 + " " + flags;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        int i;

        for (i = 0; i != (getPostCount() + getInternalNodeCount()); i++)
            volts[i] = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public abstract void draw(Graphics g);

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setCurrent(int x, double c) {
        current = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCurrent() {
        return current;
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
    }

    /**
     * DOCUMENT ME!
     */
    public void startIteration() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void setNodeVoltage(int n, double c) {
        volts[n] = c;
        calculateCurrent();
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
    }

    /**
     * DOCUMENT ME!
     */
    public void setPoints() {
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

        if (noDiagonal) {
            if (circuitFrame.abs(x - xx) < circuitFrame.abs(y - yy)) {
                xx = x;
            } else {
                yy = y;
            }
        }

        x2 = xx;
        y2 = yy;
        setPoints();
    }

    /**
     * DOCUMENT ME!
     *
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        x2 += dx;
        y2 += dy;
        setPoints();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    public void movePoint(int n, int dx, int dy) {
        if (n == 0) {
            x += dx;
            y += dy;
        } else {
            x2 += dx;
            y2 += dy;
        }

        setPoints();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void drawPosts(Graphics g) {
        drawPost(g, x, y, nodes[0]);
        drawPost(g, x2, y2, nodes[1]);
    }

    /**
     * DOCUMENT ME!
     */
    public void stamp() {
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
    public int getInternalNodeCount() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    public void setNode(int p, int n) {
        nodes[p] = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param v DOCUMENT ME!
     */
    public void setVoltageSource(int n, int v) {
        voltSource = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSource() {
        return voltSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getVoltageDiff() {
        return volts[0] - volts[1];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean nonLinear() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNode(int n) {
        return nodes[n];
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getPost(int n) {
        return (n == 0) ? new Point(x, y) : ((n == 1) ? new Point(x2, y2) : null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    public void drawPost(Graphics g, int x0, int y0, int n) {
        if ((circuitFrame.dragElement == null) && (circuitFrame.mouseElement != this) &&
                (circuitFrame.getCircuitNode(n).links.size() == 2)) {
            return;
        }

        if ((circuitFrame.mouseMode == circuitFrame.MODE_DRAG_ROW) || (circuitFrame.mouseMode == circuitFrame.MODE_DRAG_COLUMN)) {
            return;
        }

        drawPost(g, x0, y0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     */
    public void drawPost(Graphics g, int x0, int y0) {
        g.setColor(Color.white);
        g.fillOval(x0 - 3, y0 - 3, 7, 7);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     */
    public void setBbox(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            int q = x1;
            x1 = x2;
            x2 = q;
        }

        if (y1 > y2) {
            int q = y1;
            y1 = y2;
            y2 = q;
        }

        boundingBox.setBounds(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     */
    public void adjustBbox(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            int q = x1;
            x1 = x2;
            x2 = q;
        }

        if (y1 > y2) {
            int q = y1;
            y1 = y2;
            y2 = q;
        }

        x1 = circuitFrame.min(boundingBox.x, x1);
        y1 = circuitFrame.min(boundingBox.y, y1);
        x2 = circuitFrame.max((boundingBox.x + boundingBox.width) - 1, x2);
        y2 = circuitFrame.max((boundingBox.y + boundingBox.height) - 1, y2);
        boundingBox.setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param s DOCUMENT ME!
     * @param xc DOCUMENT ME!
     * @param yc DOCUMENT ME!
     * @param dpx DOCUMENT ME!
     * @param dpy DOCUMENT ME!
     */
    public void drawValues(Graphics g, String s, int xc, int yc, int dpx, int dpy) {
        if (s == null) {
            return;
        }

        g.setFont(circuitFrame.unitsFont);

        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(s);
        g.setColor(Color.white);

        int ya = fm.getAscent() / 2;

        if (dpx == 0) {
            g.drawString(s, xc - (w / 2), yc - circuitFrame.abs(dpy) - 2);
        } else {
            int xx = xc + circuitFrame.abs(dpx) + 2;

            if (this instanceof VoltageElement || ((x < x2) && (y > y2))) {
                xx = xc - (w + circuitFrame.abs(dpx) + 2);
            }

            g.drawString(s, xx, yc + dpy + ya);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void updateDotCount() {
        curcount = updateDotCount(current, curcount);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cur DOCUMENT ME!
     * @param cc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double updateDotCount(double cur, double cc) {
        if (circuitFrame.stoppedCheck.getState()) {
            return cc;
        }

        double cadd = cur * circuitFrame.currentMult;

        /*if (cur != 0 && cadd <= .05 && cadd >= -.05)
          cadd = (cadd < 0) ? -.05 : .05;*/
        if (cadd > 8) {
            cadd = 8;
        }

        if (cadd < -8) {
            cadd = -8;
        }

        return cc + cadd;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void doDots(Graphics g) {
        updateDotCount();

        if (circuitFrame.dragElement != this) {
            circuitFrame.drawDots(g, x, y, x2, y2, curcount);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doAdjust() {
    }

    /**
     * DOCUMENT ME!
     */
    public void setupAdjust() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     */
    public void getBasicInfo(String[] arr) {
        arr[1] = "I = " + circuitFrame.getCurrentDText(getCurrent());
        arr[2] = "Vd = " + circuitFrame.getVoltageDText(getVoltageDiff());
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param volts DOCUMENT ME!
     */
    public void setVoltageColor(Graphics g, double volts) {
        if (this == circuitFrame.mouseElement) {
            g.setColor(Color.cyan);

            return;
        }

        if (!circuitFrame.voltsCheckItem.getState()) {
            if (!circuitFrame.powerCheckItem.getState()) {
                g.setColor(Color.white);
            }

            return;
        }

        int c = (int) (((volts + circuitFrame.voltageRange) * (circuitFrame.colorScaleCount - 1)) / (circuitFrame.voltageRange * 2));

        if (c < 0) {
            c = 0;
        }

        if (c >= circuitFrame.colorScaleCount) {
            c = circuitFrame.colorScaleCount - 1;
        }

        g.setColor(circuitFrame.colorScale[c]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param yellow DOCUMENT ME!
     */
    public void setPowerColor(Graphics g, boolean yellow) {
        if (!circuitFrame.powerCheckItem.getState()) {
            return;
        }

        setPowerColor(g, getPower());
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param w0 DOCUMENT ME!
     */
    public void setPowerColor(Graphics g, double w0) {
        w0 *= circuitFrame.powerMult;

        //System.out.println(w);
        double w = (w0 < 0) ? (-w0) : w0;

        if (w > 1) {
            w = 1;
        }

        int rg = 128 + (int) (w * 127);
        int b = (int) (128 * (1 - w));

        /*if (yellow)
            g.setColor(new Color(rg, rg, b));
            else */
        if (w0 > 0) {
            g.setColor(new Color(rg, b, b));
        } else {
            g.setColor(new Color(b, rg, b));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getPower() {
        return getVoltageDiff() * current;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getScopeValue(int x) {
        return (x == 1) ? getPower() : getVoltageDiff();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getScopeUnits(int x) {
        return (x == 1) ? "W" : "V";
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditInfo getEditInfo(int n) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param ei DOCUMENT ME!
     */
    public void setEditValue(int n, EditInfo ei) {
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
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasGroundConnection(int n1) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWire() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canViewInScope() {
        return getPostCount() <= 2;
    }
}
