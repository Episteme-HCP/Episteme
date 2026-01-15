// Slide Pot Wrapper Class
// Written by: Craig A. Lindley
// Last Update: 03/18/99
package org.jscience.awt.pots;


// This class extends SlidePot to provide for real values other than
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RealValuedSlidePot extends SlidePot {
    // Private class data
    /** DOCUMENT ME! */
    private double potGranularity;

    /** DOCUMENT ME! */
    private double minValue;

/**
     * Class constructor
     *
     * @param length   length is the length of the slide pot in pixels
     * @param width    width is the width of the slide pot in pixels
     * @param caption  caption is the label to associate with this pot
     * @param maxValue maxValue is the maximum value the pot should return at
     *                 max rotation.
     * @param minValue minValue is the minimum value the pot should return at
     *                 min rotation.
     */
    public RealValuedSlidePot(int length, int width, String caption,
        double maxValue, double minValue) {
        super(length, width, caption, 0);

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
