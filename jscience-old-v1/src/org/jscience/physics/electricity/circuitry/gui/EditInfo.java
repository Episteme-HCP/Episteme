// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class EditInfo {
    /**
     * DOCUMENT ME!
     */
    public String name;

    /**
     * DOCUMENT ME!
     */
    public String text;

    /**
     * DOCUMENT ME!
     */
    public double value;

    /**
     * DOCUMENT ME!
     */
    public double minval;

    /**
     * DOCUMENT ME!
     */
    public double maxval;

    /**
     * DOCUMENT ME!
     */
    public TextField textf;

    /**
     * DOCUMENT ME!
     */
    public Scrollbar bar;

    /**
     * DOCUMENT ME!
     */
    public Choice choice;

    /**
     * DOCUMENT ME!
     */
    public Checkbox checkbox;

    /**
     * Creates a new EditInfo object.
     *
     * @param n DOCUMENT ME!
     * @param val DOCUMENT ME!
     * @param mn DOCUMENT ME!
     * @param mx DOCUMENT ME!
     */
    public EditInfo(String n, double val, double mn, double mx) {
        name = n;
        value = val;

        if ((minval == 0) && (maxval == 0) && (val > 0)) {
            minval = 1e10;

            while (minval > (val / 100))
                minval /= 10.;

            maxval = minval * 1000;
        } else {
            minval = mn;
            maxval = mx;
        }
    }
}
