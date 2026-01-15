// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.elements;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ClockElement extends RailElement {
    /**
     * Creates a new ClockElement object.
     *
     * @param xx DOCUMENT ME!
     * @param yy DOCUMENT ME!
     */
    public ClockElement(int xx, int yy) {
        super(xx, yy, WF_SQUARE);
        maxVoltage = 2.5;
        bias = 2.5;
        frequency = 100;
        flags |= FLAG_CLOCK;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getDumpClass() {
        return RailElement.class;
    }
}
