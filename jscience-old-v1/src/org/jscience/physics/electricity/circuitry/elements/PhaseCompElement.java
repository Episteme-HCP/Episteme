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
public class PhaseCompElement extends ChipElement {
    /**
     * DOCUMENT ME!
     */
   public boolean ff1;

    /**
     * DOCUMENT ME!
     */
    public boolean ff2;

    /**
     * Creates a new PhaseCompElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public PhaseCompElement(int xx, int yy) {
        super(xx, yy);
    }

    /**
     * Creates a new PhaseCompElement object.
     *
     * @param xa DOCUMENT ME!
     * @param ya DOCUMENT ME!
     * @param xb DOCUMENT ME!
     * @param yb DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param st DOCUMENT ME!
     */
    public PhaseCompElement(int xa, int ya, int xb, int yb, int f,
        StringTokenizer st) {
        super(xa, ya, xb, yb, f, st);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChipName() {
        return "phase comparator";
    }

    /**
     * DOCUMENT ME!
     */
    public void setupPins() {
        sizeX = 2;
        sizeY = 2;
        pins = new Pin[3];
        pins[0] = new Pin(0, SIDE_W, "I1");
        pins[1] = new Pin(1, SIDE_W, "I2");
        pins[2] = new Pin(0, SIDE_E, "O");
        pins[2].output = true;
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
        int vn = circuitFrame.nodeList.size() + pins[2].voltSource;
        circuitFrame.stampNonLinear(vn);
        circuitFrame.stampNonLinear(0);
        circuitFrame.stampNonLinear(nodes[2]);
    }

    /**
     * DOCUMENT ME!
     */
    public void doStep() {
        boolean v1 = volts[0] > 2.5;
        boolean v2 = volts[1] > 2.5;

        if (v1 && !pins[0].value) {
            ff1 = true;
        }

        if (v2 && !pins[1].value) {
            ff2 = true;
        }

        if (ff1 && ff2) {
            ff1 = ff2 = false;
        }

        double out = (ff1) ? 5 : ((ff2) ? 0 : (-1));

        //System.out.println(out + " " + v1 + " " + v2);
        if (out != -1) {
            circuitFrame.stampVoltageSource(0, nodes[2], pins[2].voltSource, out);
        } else {
            // tie current through output pin to 0
            int vn = circuitFrame.nodeList.size() + pins[2].voltSource;
            circuitFrame.stampMatrix(vn, vn, 1);
        }

        pins[0].value = v1;
        pins[1].value = v2;
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
        return 161;
    }
}
