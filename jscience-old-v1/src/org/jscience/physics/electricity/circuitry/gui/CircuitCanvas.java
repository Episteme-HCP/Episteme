// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
package org.jscience.physics.electricity.circuitry.gui;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CircuitCanvas extends Canvas {
    /**
     * DOCUMENT ME!
     */
    CircuitFrame pg;

    /**
     * Creates a new CircuitCanvas object.
     *
     * @param p DOCUMENT ME!
     */
    CircuitCanvas(CircuitFrame p) {
        pg = p;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getPreferredSize() {
        return new Dimension(300, 400);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void update(Graphics g) {
        pg.updateCircuit(g);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        pg.updateCircuit(g);
    }
}
