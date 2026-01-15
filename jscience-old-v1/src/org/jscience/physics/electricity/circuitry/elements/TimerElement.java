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
public class TimerElement extends ChipElement {
    /**
     * DOCUMENT ME!
     */
    public boolean out;

    /**
     * Creates a new TimerElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public TimerElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new TimerElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public TimerElement(int xa, int ya, int xb, int yb, int f, StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "555 Timer";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = 3;
        sizeY = 5;
        pins = new Pin[6];
        pins[0] = new Pin(1, SIDE_W, "dis");
        pins[1] = new Pin(3, SIDE_W, "tr");
        pins[1].lineOver = true;
        pins[2] = new Pin(4, SIDE_W, "th");
        pins[3] = new Pin(1, SIDE_N, "V");
        pins[4] = new Pin(1, SIDE_S, "ctl");
        pins[5] = new Pin(2, SIDE_E, "out");
        pins[5].output = pins[5].state = true;
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
    public void stamp() {
        // stamp voltage divider to put ctl pin at 2/3 V
        circuitFrame.stampResistor(nodes[3], nodes[4], 5000);
        circuitFrame.stampResistor(nodes[4], 0, 10000);
        // output pin
        circuitFrame.stampVoltageSource(0, nodes[5], pins[5].voltSource);
        // discharge pin
        circuitFrame.stampNonLinear(0);
        circuitFrame.stampNonLinear(nodes[0]);
    }

    /**
     * DOCUMENT ME!
     */
    public void calculateCurrent() {
        // need current for V, discharge, control; output current is
        // calculated for us, and other pins have no current
        pins[3].current = (volts[4] - volts[3]) / 5000;
        pins[4].current = (-volts[4] / 10000) - pins[3].current;
        pins[0].current = out ? 0 : (-volts[0] / 10);
    }

    /**
     * DOCUMENT ME!
     */
    public void startIteration() {
        out = volts[5] > (volts[3] / 2);

        // check comparators
        if ((volts[4] / 2) > volts[1]) {
            out = true;
        }

        if (volts[2] > volts[4]) {
            out = false;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        // if output is low, discharge pin 0.  we use a small
        // resistor because it's easier, and sometimes people tie
        // the discharge pin to the trigger and threshold pins.
        if (!out) {
            circuitFrame.stampResistor(nodes[0], 0, 10);
        }

        // output
        circuitFrame.updateVoltageSource(0, nodes[5], pins[5].voltSource, out ? volts[3] : 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPostCount() {
        return 6;
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
     * @return DOCUMENT ME!
     */
    public int getDumpType() {
        return 165;
    }
}
