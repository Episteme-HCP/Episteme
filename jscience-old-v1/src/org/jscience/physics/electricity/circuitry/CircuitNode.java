// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class CircuitNode {
    /**
     * DOCUMENT ME!
     */
    public int x;

    /**
     * DOCUMENT ME!
     */
    public int y;

    /**
     * DOCUMENT ME!
     */
    public Vector links;

    /**
     * DOCUMENT ME!
     */
    public boolean internal;

    /**
     * Creates a new CircuitNode object.
     */
    public CircuitNode() {
        links = new Vector();
    }
}
