// Potentiometer Wrapper Class
// Written by: Craig A. Lindley
// Last Update: 03/18/99
package org.jscience.awt.pots;


// This class extends Pot to provide for integer values other than 0..100
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntValuedPot extends Pot {
    // Private class data
    /** DOCUMENT ME! */
    private double potGranularity;

    /** DOCUMENT ME! */
    private int minValue;

/**
     * Class constructor
     *
     * @param maxValue maxValue is the maximum value the pot should return at
     *                 max rotation.
     * @param minValue minValue is the minimum value the pot should return at
     *                 min rotation.
     */
    public IntValuedPot(int maxValue, int minValue) {
        this.minValue = minValue;
        potGranularity = ((double) maxValue - minValue) / 100.0;
    }

    /**
     * Get the scaled value of the pot at its current position
     *
     * @return int scaled int value
     */
    public int getIntValue() {
        return (int) ((super.getValue() * potGranularity) + minValue);
    }

    /**
     * Set the current position of the pot to the scaled value
     *
     * @param realValue realValue is the scaled int value to set the pot to
     */
    public void setIntValue(int realValue) {
        super.setValue((int) ((realValue - minValue) / potGranularity));
    }
}
