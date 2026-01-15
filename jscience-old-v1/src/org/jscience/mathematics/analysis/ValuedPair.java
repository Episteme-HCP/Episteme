package org.jscience.mathematics.analysis;

import java.io.Serializable;

/**
 * This class represents an (x, f(x)) pair for vectorial functions.
 * <p/>
 * <p>A vectorial function is a function of one vectorial parameter x whose
 * value is a vector. This class is used has a simple placeholder to
 * contain both an abscissa and the value of the function at this
 * abscissa.</p>
 *
 * @version Silvere Martin-Michiellot
 * @see SampledMapping
 */
//we could provide many other constructors, for example ValuedPair(double, double[]),  ValuedPair(long, double)...
public class ValuedPair implements Serializable {
    /**
     * Abscissa of the point.
     */
    private Number[] x;

    /**
     * Vectorial ordinate of the point, y = f (x).
     */
    private Number[] y;

    /**
     * Simple constructor, values are wrapped into their corresponding type and into an array.
     * Build an instance from its coordinates
     *
     * @param x abscissa
     * @param y ordinate (value of the function)
     */
    public ValuedPair(double x, double y) {
        this.x = new Number[1];
        this.x[0] = new Double(x);
        this.y = new Number[1];
        this.y[0] = new Double(y);
    }

    /**
     * Simple constructor, values are wrapped into their corresponding type and into an array.
     * Build an instance from its coordinates
     *
     * @param x abscissa
     * @param y ordinate (value of the function)
     */
    public ValuedPair(float x, float y) {
        this.x = new Number[1];
        this.x[0] = new Float(x);
        this.y = new Number[1];
        this.y[0] = new Float(y);
    }

    /**
     * Simple constructor, values are wrapped into their corresponding type and into an array.
     * Build an instance from its coordinates
     *
     * @param x abscissa
     * @param y ordinate (value of the function)
     */
    public ValuedPair(long x, long y) {
        this.x = new Number[1];
        this.x[0] = new Long(x);
        this.y = new Number[1];
        this.y[0] = new Long(y);
    }

    /**
     * Simple constructor, values are wrapped into their corresponding type and into an array.
     * Build an instance from its coordinates
     *
     * @param x abscissa
     * @param y ordinate (value of the function)
     */
    public ValuedPair(int x, int y) {
        this.x = new Number[1];
        this.x[0] = new Integer(x);
        this.y = new Number[1];
        this.y[0] = new Integer(y);
    }

    /**
     * Simple constructor, values are wrapped into an array.
     * Build an instance from its coordinates
     *
     * @param x abscissa
     * @param y ordinate (value of the function)
     */
    public ValuedPair(Number x, Number y) {
        //we should test null arguments here
        this.x = new Number[1];
        this.x[0] = x;
        this.y = new Number[1];
        this.y[0] = y;
    }

    /**
     * Simple constructor.
     * Build an instance from its coordinates
     *
     * @param x abscissa
     * @param y ordinate (value of the function)
     */
    public ValuedPair(Number[] x, Number[] y) {
        //we should test null arguments here
        this.x = x;
        this.y = y;
    }

    /**
     * Copy-constructor.
     *
     * @param p point to copy
     */
    public ValuedPair(ValuedPair p) {
        x = p.x;
        y = p.y;
    }

    /**
     * Getter for the abscissa, values are wrapped into an array.
     *
     * @return value of the abscissa
     */
    public Number[] getX() {
        return x;
    }

    /**
     * Getter for the ordinate, values are wrapped into an array.
     *
     * @return value of the ordinate
     */
    public Number[] getY() {
        return y;
    }

    /**
     * Setter for the abscissa, value is wrapped into its corresponding type and into an array.
     *
     * @param x new value for the abscissa
     */
    public void setX(double x) {
        this.x = new Number[1];
        this.x[0] = new Double(x);
    }

    /**
     * Setter for the ordinate, value is wrapped into its corresponding type and into an array.
     *
     * @param y new value for the ordinate
     */
    public void setY(double y) {
        this.y = new Number[1];
        this.y[0] = new Double(y);
    }

    /**
     * Setter for the abscissa, value is wrapped into its corresponding type and into an array.
     *
     * @param x new value for the abscissa
     */
    public void setX(float x) {
        this.x = new Number[1];
        this.x[0] = new Float(x);
    }

    /**
     * Setter for the ordinate, value is wrapped into its corresponding type and into an array.
     *
     * @param y new value for the ordinate
     */
    public void setY(float y) {
        this.y = new Number[1];
        this.y[0] = new Float(y);
    }

    /**
     * Setter for the abscissa, value is wrapped into its corresponding type and into an array.
     *
     * @param x new value for the abscissa
     */
    public void setX(long x) {
        this.x = new Number[1];
        this.x[0] = new Long(x);
    }

    /**
     * Setter for the ordinate, value is wrapped into its corresponding type and into an array.
     *
     * @param y new value for the ordinate
     */
    public void setY(long y) {
        this.y = new Number[1];
        this.y[0] = new Long(y);
    }

    /**
     * Setter for the abscissa, value is wrapped into its corresponding type and into an array.
     *
     * @param x new value for the abscissa
     */
    public void setX(int x) {
        this.x = new Number[1];
        this.x[0] = new Integer(x);
    }

    /**
     * Setter for the ordinate, value is wrapped into its corresponding type and into an array.
     *
     * @param y new value for the ordinate
     */
    public void setY(int y) {
        this.y = new Number[1];
        this.y[0] = new Integer(y);
    }

    /**
     * Setter for the abscissa, value is wrapped into an array.
     *
     * @param x new value for the abscissa
     */
    public void setX(Number x) {
        this.x = new Number[1];
        this.x[0] = x;
    }

    /**
     * Setter for the ordinate, value is wrapped into an array.
     *
     * @param y new value for the ordinate
     */
    public void setY(Number y) {
        this.y = new Number[1];
        this.y[0] = y;
    }

    /**
     * Setter for the abscissa.
     *
     * @param x new value for the abscissa
     */
    public void setX(Number[] x) {
        this.x = x;
    }

    /**
     * Setter for the ordinate.
     *
     * @param y new value for the ordinate
     */
    public void setY(Number[] y) {
        this.y = y;
    }

}
