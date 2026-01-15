// Potentiometer Wrapper Class
// Written by: Craig A. Lindley
// Last Update: 03/18/99
package org.jscience.awt.pots;


// This class extends Pot to provide for real values other than 0..100
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RealValuedPot extends Pot {
    // Private class data
    /** DOCUMENT ME! */
    private double potGranularity;

    /** DOCUMENT ME! */
    private double minValue;

/**
     * Class constructor
     *
     * @param maxValue maxValue is the maximum value the pot should return at
     *                 max rotation.
     * @param minValue minValue is the minimum value the pot should return at
     *                 min rotation.
     */
    public RealValuedPot(double maxValue, double minValue) {
        this.minValue = minValue;
        potGranularity = (maxValue - minValue) / 100.0;
    }

    /**
     * Get the scaled value of the pot at its current position
     *
     * @return double scaled double value
     */
    public double getRealValue() {
        return (getValue() * potGranularity) + minValue;
    }

    /**
     * Set the current position of the pot to the scaled value
     *
     * @param realValue realValue is the scaled double value to set the pot to
     */
    public void setRealValue(double realValue) {
        setValue((int) ((realValue - minValue) / potGranularity));
    }
}
