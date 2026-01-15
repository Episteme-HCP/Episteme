// Circuit.java (c) 2005 by Paul Falstad, www.falstad.com
//permission given by the author to redistribute his code under GPL
package org.jscience.physics.electricity.circuitry.gui;


// info about each row/column of the matrix for simplification purposes
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RowInfo {
    /**
     * DOCUMENT ME!
     */
    public static final int ROW_NORMAL = 0; // ordinary value

    /**
     * DOCUMENT ME!
     */
    public static final int ROW_CONST = 1; // value is constant

    /**
     * DOCUMENT ME!
     */
    public static final int ROW_EQUAL = 2; // value is equal to another value

    /**
     * DOCUMENT ME!
     */
    public int nodeEq;

    /**
     * DOCUMENT ME!
     */
    public int type;

    /**
     * DOCUMENT ME!
     */
    public int mapCol;

    /**
     * DOCUMENT ME!
     */
    public int mapRow;

    /**
     * DOCUMENT ME!
     */
    public double value;

    /**
     * DOCUMENT ME!
     */
    public boolean rsChanges; // row's right side changes

    /**
     * DOCUMENT ME!
     */
    public boolean lsChanges; // row's left side changes

    /**
     * DOCUMENT ME!
     */
    public boolean dropRow; // row is not needed in matrix

    /**
     * Creates a new RowInfo object.
     */
    public RowInfo() {
        type = ROW_NORMAL;
    }
}
