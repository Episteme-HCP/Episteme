package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.ArrayMathUtils;

import java.util.Arrays;


/**
 * This class is used to be able to mix the wavelet and other type of
 * functions such as given signals.
 *
 * @author Daniel Lemire
 */
public class DiscreteFunction extends MultiscaleFunction implements Cloneable {
    /** DOCUMENT ME! */
    double[] Data;

/**
     * Creates a new DiscreteFunction object.
     */
    protected DiscreteFunction() {
    }

/**
     * Creates a new DiscreteFunction object.
     *
     * @param v DOCUMENT ME!
     */
    public DiscreteFunction(double[] v) {
        setData(v);
    }

    /**
     * Return a String representation of the object
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (ArrayMathUtils.toString(Data));
    }

    /**
     * Makes the L2 norm of the internal array equal to 1.
     */
    public void normalize() {
        setData(ArrayMathUtils.normalize(Data));
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void setData(double[] v) {
        Data = ArrayMathUtils.copy(v);
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate() {
        return (Data);
    }

    /**
     * Check if another object is equal to this DiscreteFunction object
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if ((a != null) && (a instanceof DiscreteFunction)) {
            DiscreteFunction iv = (DiscreteFunction) a;

            if (Arrays.equals(Data, iv.evaluate(0))) {
                return true;
            }
        }

        return false;
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @param j1 number of iterations (doesn't do anything)
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate(int j1) {
        return (evaluate());
    }

    /**
     * Compute the mass (integral)
     *
     * @param a left boundary of the interval
     * @param b right boundary of the interval
     * @param jfin number of iterations to consider (precision)
     *
     * @return DOCUMENT ME!
     */
    public double mass(double a, double b, int jfin) {
        double somme = 0.0;
        double[] values = evaluate(jfin);

        for (int k = 0; k < values.length; k++) {
            somme += values[k];
        }

        somme = somme / (values.length - 1) * Math.abs(b - a);

        return (somme);
    }

    /**
     * Compute the L2 norm of the signal
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return (ArrayMathUtils.norm(evaluate()));
    }

    /**
     * Compute the L2 norm of the function
     *
     * @param j number of iterations
     *
     * @return DOCUMENT ME!
     */
    public double norm(int j) {
        return (ArrayMathUtils.norm(evaluate(j)));
    }

    /**
     * Return a copy of this object
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        DiscreteFunction df = (DiscreteFunction) super.clone();
        df.Data = ArrayMathUtils.copy(this.Data);

        return (df);
    }

    /**
     * Tells you how many samples you'll get from this function (will
     * not depend on the parameter)
     *
     * @param jfin DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int dimension(int jfin) {
        return (Data.length);
    }

    /**
     * Tells you how many samples you'll get from this function
     *
     * @return DOCUMENT ME!
     */
    public int dimension() {
        return (Data.length);
    }

    /**
     * This method is used to compute how the number of scaling
     * functions changes from on scale to the other. Basically, if you have k
     * scaling function and a Filter of type t, you'll have 2k+t scaling
     * functions at the next scale (dyadic case). Notice that this method
     * assumes that one is working with the dyadic grid while the method
     * "previousDimension" define in the interface "Filter" doesn't.
     *
     * @return DOCUMENT ME!
     */
    public int getFilterType() {
        return (Data.length);
    }
}
