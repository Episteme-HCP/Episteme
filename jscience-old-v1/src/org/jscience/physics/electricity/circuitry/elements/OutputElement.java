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
public class OutputElement extends CircuitElement {
    /**
     * DOCUMENT ME!
     */
    public final int FLAG_VALUE = 1;

    /**
     * Creates a new OutputElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public OutputElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new OutputElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public OutputElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'O';
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
        Font f = new Font("SansSerif", (this == circuitFrame.mouseElement) ? Font.BOLD : 0, 14);
        g.setFont(f);

        FontMetrics fm = g.getFontMetrics();
        g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.white);

        String s = ((flags & FLAG_VALUE) != 0) ? circuitFrame.getVoltageText(volts[0]) : "out";
        double dx = x2 - x;
        double dy = y2 - y;
        double dr = Math.sqrt((dx * dx) + (dy * dy));
        dx /= dr;
        dy /= dr;
        setBbox(x, y, x2, y2);

        int w = fm.stringWidth(s) / 2;
        int wa = w + 8;
        int x3 = x2 - (int) (dx * wa);
        int y3 = y2 - (int) (dy * wa);
        int yy = (y2 + (fm.getAscent() / 2)) - 1;
        g.drawString(s, x2 - w, yy);
        adjustBbox(x2 - w, yy - fm.getAscent(), x2 + w, yy);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x3, y3);
        drawPost(g, x, y, nodes[0]);
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
     * @param arr DOCUMENT ME!
     */
    public void getInfo(String[] arr) {
        arr[0] = "output";
        arr[1] = "V = " + circuitFrame.getVoltageText(volts[0]);
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
            EditInfo ei = new EditInfo("", 0, -1, -1);
            ei.checkbox = new Checkbox("Show Voltage", (flags & FLAG_VALUE) != 0);

            return ei;
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
            flags = (ei.checkbox.getState()) ? (flags | FLAG_VALUE)
                                             : (flags & ~FLAG_VALUE);
        }
    }
}
