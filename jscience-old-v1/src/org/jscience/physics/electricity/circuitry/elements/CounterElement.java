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
public class CounterElement extends ChipElement {
    /**
     * Creates a new CounterElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public CounterElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new CounterElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public CounterElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
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
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "Counter";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = 2;
        sizeY = (bits > 2) ? bits : 2;
        pins = new Pin[getPostCount()];
        pins[0] = new Pin(0, SIDE_W, "");
        pins[0].clock = true;
        pins[1] = new Pin(sizeY - 1, SIDE_W, "R");
        pins[1].bubble = true;

        int i;

        for (i = 0; i != bits; i++) {
            int ii = i + 2;
            pins[ii] = new Pin(i, SIDE_E, "Q" + (bits - i - 1));
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
        if (pins[0].value && !lastClock) {
            int i;

            for (i = bits - 1; i >= 0; i--) {
                int ii = i + 2;

                if (!pins[ii].value) {
                    pins[ii].value = true;

                    break;
                }

                pins[ii].value = false;
            }
        }

        if (!pins[1].value) {
            int i;

            for (i = 0; i != bits; i++)
                pins[i + 2].value = false;
        }

        lastClock = pins[0].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 164;
    }
}
