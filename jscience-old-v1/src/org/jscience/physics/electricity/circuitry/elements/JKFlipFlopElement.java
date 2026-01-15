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
public class JKFlipFlopElement extends ChipElement {
    /**
     * Creates a new JKFlipFlopElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public JKFlipFlopElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new JKFlipFlopElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public JKFlipFlopElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
        pins[4].value = !pins[3].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "JK flip-flop";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = 2;
        sizeY = 3;
        pins = new Pin[5];
        pins[0] = new Pin(0, SIDE_W, "J");
        pins[1] = new Pin(1, SIDE_W, "");
        pins[1].clock = true;
        pins[1].bubble = true;
        pins[2] = new Pin(2, SIDE_W, "K");
        pins[3] = new Pin(0, SIDE_E, "Q");
        pins[3].output = pins[3].state = true;
        pins[4] = new Pin(2, SIDE_E, "Q");
        pins[4].output = true;
        pins[4].lineOver = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 5;
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
        if (!pins[1].value && lastClock) {
            boolean q = pins[3].value;

            if (pins[0].value) {
                if (pins[2].value) {
                    q = !q;
                } else {
                    q = true;
                }
            } else if (pins[2].value) {
                q = false;
            }

            pins[3].value = q;
            pins[4].value = !q;
        }

        lastClock = pins[1].value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 156;
    }
}
