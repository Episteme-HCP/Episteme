package org.jscience.util;

/**
 * A class representing a way to iterate numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use negative stepping
public class CircularNumberStepper extends BoundedNumberStepper {
    /** DOCUMENT ME! */
    private double endStep;

    //step must be different from zero
/**
     * Creates a new CircularNumberStepper object.
     *
     * @param beginStep DOCUMENT ME!
     * @param endStep DOCUMENT ME!
      * @param step DOCUMENT ME!
     */

    //when currentValue has reached endStep, it is rolled back to begin step
    public CircularNumberStepper(double beginStep, double endStep, double step) {
        super(beginStep, endStep, step);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //always true
    public boolean hasNext() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        if ((((getValue() + getStep()) < endStep) && (getStep() > 0)) ||
                (((getValue() + getStep()) > endStep) && (getStep() < 0))) {
            setValue(getValue() + getStep());
        } else {
            reset();
        }

        return new Double(getValue());
    }
}
