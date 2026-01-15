// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class JohnsonElement extends ChipElement {
    /**
     * Creates a new JohnsonElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public JohnsonElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new JohnsonElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public JohnsonElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "Johnson counter";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean needsBits() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = (bits > 2) ? bits : 2;
        sizeY = 2;
        pins = new Pin[getPostCount()];
        pins[0] = new Pin(1, SIDE_W, "");
        pins[0].clock = true;
        pins[1] = new Pin(sizeX - 1, SIDE_S, "R");
        pins[1].bubble = true;

        int i;

        for (i = 0; i != bits; i++) {
            int ii = i + 2;
            pins[ii] = new Pin(i, SIDE_N, "Q" + i);
            pins[ii].output = pins[ii].state = true;
        }

        allocNodes();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return bits + 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return bits;
    }

    /**
     * DOCUMENT ME!
     */
    public void execute() {
        int i;

        if (pins[0].value && !lastClock) {
            for (i = 0; i != bits; i++)
                if (pins[i + 2].value) {
                    break;
                }

            if (i < bits) {
                pins[i++ + 2].value = false;
            }

            i %= bits;
            pins[i + 2].value = true;
        }

        if (!pins[1].value) {
            for (i = 1; i != bits; i++)
                pins[i + 2].value = false;

            pins[2].value = true;
        }

        lastClock = pins[0].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 163;
    }
}
