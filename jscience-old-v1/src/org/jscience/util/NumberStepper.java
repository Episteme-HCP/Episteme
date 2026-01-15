package org.jscience.util;

import java.util.Iterator;


/**
 * A class representing a way to iterate numbers.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//you can use negative stepping
public class NumberStepper implements Iterator {
    /** DOCUMENT ME! */
    private double beginStep;

    /** DOCUMENT ME! */
    private double currentValue;

    /** DOCUMENT ME! */
    private double step;

    //step must be different from zero
/**
     * Creates a new NumberStepper object.
     *
     * @param beginStep DOCUMENT ME!
     * @param step DOCUMENT ME!
     */
    public NumberStepper(double beginStep, double step) {
        this.beginStep = beginStep;
        currentValue = beginStep;
        this.step = step;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getBeginStep() {
        return beginStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beginStep DOCUMENT ME!
     */
    protected void setBeginStep(double beginStep) {
        this.beginStep = beginStep;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getValue() {
        return currentValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    protected void setValue(double value) {
        currentValue = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getStep() {
        return step;
    }

    /**
     * DOCUMENT ME!
     *
     * @param step DOCUMENT ME!
     */

    //step must be different from zero
    protected void setStep(double step) {
        this.step = step;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        currentValue = beginStep;
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
        return new Double(currentValue);
    }

    //unsupported
    /**
     * DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
