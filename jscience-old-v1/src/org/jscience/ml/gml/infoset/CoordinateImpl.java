/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import java.math.BigDecimal;


/**
 * A convenience class that implements the Coordinate interface.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class CoordinateImpl implements Coordinate {
    // the decimal value of this coordinate
    /** DOCUMENT ME! */
    private BigDecimal value_;

/**
     * Constructor.
     *
     * @param value The value used for the construction of this coordinate in
     *              string representation.
     */
    public CoordinateImpl(String value) {
        value_ = new BigDecimal(value);
    }

    // Coordinate interface implementation
    /**
     * Provides access to the value of this coordinate. The value is
     * BigDecimal because it doesn't lose precision. One may obtain other
     * representations from BigDecimal.
     *
     * @return Returns the value as BigDecimal. Cannot be null.
     */
    public BigDecimal getValue() {
        return value_;
    }

    // redefined methods
    /**
     * Returns the string representation of the coordinate decimal
     * value.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return value_.toString();
    }
}
