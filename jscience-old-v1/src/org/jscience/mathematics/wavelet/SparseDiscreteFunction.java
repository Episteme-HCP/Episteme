package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.algebraic.matrices.DoubleSparseVector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SparseDiscreteFunction extends DiscreteFunction
    implements Cloneable {
    /** DOCUMENT ME! */
    public DoubleSparseVector Data;

/**
     * Creates a new SparseDiscreteFunction object.
     *
     * @param v DOCUMENT ME!
     */
    public SparseDiscreteFunction(double[] v) {
        setData(v);
    }

    /**
     * Return a String representation of the object
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (Data.toString());
    }

    /**
     * Makes the L2norm of the internal array=1.
     */
    public void normalize() {
        Data.normalize();
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     */
    public void setData(double[] v) {
        Data = new DoubleSparseVector(v);
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate() {
        return (toDouble((Number[]) Data.toArray()));
    }

    /**
     * Check if another object is equal to this DiscreteFunction object
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if ((a != null) && (a instanceof SparseDiscreteFunction)) {
            SparseDiscreteFunction iv = (SparseDiscreteFunction) a;

            return Data.equals(iv.Data);
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
        return Data.mass() / (Data.getDimension() - 1) * Math.abs(b - a);
    }

    /**
     * Compute the L2 norm of the signal
     *
     * @return DOCUMENT ME!
     */
    public double norm() {
        return (Data.norm());
    }

    /**
     * Compute the L2 norm of the function The parameter doesn't do
     * anything.
     *
     * @param j number of iterations
     *
     * @return DOCUMENT ME!
     */
    public double norm(int j) {
        return (Data.norm());
    }

    /**
     * Return a copy of this object
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        SparseDiscreteFunction sdf = (SparseDiscreteFunction) super.clone();
        sdf.Data = new DoubleSparseVector(toDouble((Number[]) Data.toArray()));

        return (sdf);
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[] toDouble(Number[] v) {
        final double[] ans = new double[v.length];

        for (int k = 0; k < v.length; k++)
            ans[k] = v[k].doubleValue();

        return (ans);
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
        return (Data.getDimension());
    }

    /**
     * Tells you how many samples you'll get from this function
     *
     * @return DOCUMENT ME!
     */
    public int dimension() {
        return (Data.getDimension());
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
        return (Data.getDimension());
    }
}
