/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import java.math.BigDecimal;


/**
 * Defines the interface every GML coordinate must implement. Coordinates  (1
 * or more) comprise a coordinate tuple. Note that Coordinate doesn't have a
 * GML counterpart, because coordinates may be expressed in two different ways
 * in GML. To circumvent this duality, the Coordinate class is made
 * independent of XML, an abstract entity created and owned by Coord or
 * Coordinates GML constructs.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Coordinate {
    /**
     * Provides access to the value of this coordinate. The value is
     * BigDecimal because it doesn't lose precision. One may obtain other
     * representations from BigDecimal.
     *
     * @return Returns the value as BigDecimal. Cannot be null.
     */
    public BigDecimal getValue();
}
