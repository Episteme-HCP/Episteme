package org.jscience.util;

import java.util.NoSuchElementException;


/**
 * A class representing a way to iterate numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use negative stepping
public class BoundedNumberStepper extends NumberStepper {
    /** DOCUMENT ME! */
    private double endStep;

    //step must be different from zero
    //there is no error if endStep<beginStep and step<0 but endStep will be unreachable
    //there is no error if endStep>beginStep and step>0 but endStep will be unreachable
    //there is no error if endStep=beginStep
/**
     * Creates a new BoundedNumberStepper object.
     *
     * @param beginStep DOCUMENT ME!
     * @param endStep DOCUMENT ME!
       * @param step DOCUMENT ME!
     */
    public BoundedNumberStepper(double beginStep, double endStep, double step) {
        super(beginStep, step);
        this.endStep = endStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getEndStep() {
        return endStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param endStep DOCUMENT ME!
     */
    public void setEndStep(double endStep) {
        this.endStep = endStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return !((((getValue() + getStep()) < endStep) && (getStep() > 0)) ||
        (((getValue() + getStep()) > endStep) && (getStep() < 0)));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NoSuchElementException DOCUMENT ME!
     */
    public Object next() {
        if ((((getValue() + getStep()) < endStep) && (getStep() > 0)) ||
                (((getValue() + getStep()) > endStep) && (getStep() < 0))) {
            setValue(getValue() + getStep());

            return new Double(getValue());
        } else {
            throw new NoSuchElementException();
        }
    }
}
