// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RailElement extends VoltageElement {
    /**
     * DOCUMENT ME!
     */
    public final int FLAG_CLOCK = 1;

    /**
     * Creates a new RailElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public RailElement(int xx, int yy) {
        super(xx, yy, WF_DC);
    }

    /**
     * Creates a new RailElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     * @param wf DOCUMENT ME!
     */
    public RailElement(int xx, int yy, int wf) {
        super(xx, yy, wf);
    }

    /**
     * Creates a new RailElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public RailElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 'R';
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String dump() {
        return "R " + x + " " + y + " " + x2 + " " + y2 + " " + flags + " " +
        waveform + " " + frequency + " " + maxVoltage + " " + bias;
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
        double dx = x2 - x;
        double dy = y2 - y;
        double dr = Math.sqrt((dx * dx) + (dy * dy));
        dx /= dr;
        dy /= dr;

        int x3 = x2 - (int) (dx * circleSize);
        int y3 = y2 - (int) (dy * circleSize);
        setBbox(x, y, x2, y2);
        setVoltageColor(g, volts[0]);
        circuitFrame.drawThickLine(g, x, y, x3, y3);

        boolean clock = (waveform == WF_SQUARE) && ((flags & FLAG_CLOCK) != 0);

        if ((waveform == WF_DC) || clock) {
            Font f = new Font("SansSerif", 0, 12);
            g.setFont(f);

            FontMetrics fm = g.getFontMetrics();
            g.setColor((this == circuitFrame.mouseElement) ? Color.cyan : Color.white);
            setPowerColor(g, false);

            String s = circuitFrame.showFormat.format(maxVoltage) + "V";

            if (maxVoltage > 0) {
                s = "+" + s;
            }

            if (this instanceof AntennaElement) {
                s = "Ant";
            }

            if (clock) {
                s = "CLK";
            }

            int w = fm.stringWidth(s) / 2;
            g.drawString(s, x2 - w, (y2 + (fm.getAscent() / 2)) - 1);
            adjustBbox(x2 - w, y2 - w, x2 + w, y2 + w);
        } else {
            drawWaveform(g, x2, y2);
        }

        drawPost(g, x, y, nodes[0]);
        curcount = updateDotCount(-current, curcount);

        if (circuitFrame.dragElement != this) {
            circuitFrame.drawDots(g, x, y, x3, y3, curcount);
        }
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
     */
    public void stamp() {
        if (waveform == WF_DC) {
            circuitFrame.stampVoltageSource(0, nodes[0], voltSource, getVoltage());
        } else {
            circuitFrame.stampVoltageSource(0, nodes[0], voltSource);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        if (waveform != WF_DC) {
            circuitFrame.updateVoltageSource(0, nodes[0], voltSource, getVoltage());
        }
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
