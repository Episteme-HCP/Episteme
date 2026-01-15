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
public class DFlipFlopElement extends ChipElement {
    /**
     * DOCUMENT ME!
     */
    public final int FLAG_RESET = 2;

    /**
     * Creates a new DFlipFlopElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public DFlipFlopElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new DFlipFlopElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public DFlipFlopElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        pins[2].value = !pins[1].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "D flip-flop";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = 2;
        sizeY = 3;
        pins = new Pin[getPostCount()];
        pins[0] = new Pin(0, SIDE_W, "D");
        pins[1] = new Pin(0, SIDE_E, "Q");
        pins[1].output = pins[1].state = true;
        pins[2] = new Pin(2, SIDE_E, "Q");
        pins[2].output = true;
        pins[2].lineOver = true;
        pins[3] = new Pin(1, SIDE_W, "");
        pins[3].clock = true;

        if ((flags & FLAG_RESET) != 0) {
            pins[4] = new Pin(2, SIDE_W, "R");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return ((flags & FLAG_RESET) != 0) ? 5 : 4;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVoltageSourceCount() {
        return 2;
    }

    /**
     * DOCUMENT ME!
     */
    public void execute() {
        if (pins[3].value && !lastClock) {
            pins[1].value = pins[0].value;
            pins[2].value = !pins[0].value;
        }

        if ((pins.length > 4) && pins[4].value) {
            pins[1].value = false;
            pins[2].value = true;
        }

        lastClock = pins[3].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 155;
    }
}
