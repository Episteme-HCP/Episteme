package org.jscience.mathematics.wavelet.splines;

import org.jscience.mathematics.ArrayMathUtils;
import org.jscience.mathematics.wavelet.Cascades;
import org.jscience.mathematics.wavelet.Filter;


/**
 * This class is used to generate quadratic splines to be used as wavelets
 * or related functions. It can also be used for basic interpolation.
 *
 * @author Daniel Lemire
 */
public class QuadraticSpline extends Spline implements Filter, Cloneable {
    /** DOCUMENT ME! */
    protected final static int filtretype = 2;

    /** DOCUMENT ME! */
    static final double[] vg = { 1 / 4d, 3 / 4d, 3 / 4d, 1 / 4d };

    /** DOCUMENT ME! */
    static final double[] v0 = { 3 / 4d, 1 / 4d };

    /** DOCUMENT ME! */
    private double[] vecteur;

/**
     * ************************************
     * **************************************
     */
    public QuadraticSpline(double[] v) {
        vecteur = v;
    }

/**
     * ************************************
     * **************************************
     */
    public QuadraticSpline() {
    }

    /**
     * Return a String representation of the object
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (ArrayMathUtils.toString(vecteur));
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
        return (filtretype);
    }

    /**
     * Check if another object is equal to this QuadraticSpline object
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object a) {
        if ((a != null) && (a instanceof QuadraticSpline) &&
                (vecteur.length == ((QuadraticSpline) a).dimension())) {
            QuadraticSpline iv = (QuadraticSpline) a;

            for (int i = 0; i < vecteur.length; i++) {
                if (vecteur[i] != iv.getValue(i)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * This method return the number of "scaling" functions at the
     * previous scale given a number of scaling functions. The answer is
     * always smaller than the provided value (about half since this is a
     * dyadic implementation). This relates to the same idea as the "Filter
     * type". It is used by the interface "Filter".
     *
     * @param k DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int previousDimension(int k) {
        return (Cascades.previousDimension(filtretype, k));
    }

    /**
     * This is the implementation of the lowpass Filter. It is used by
     * the interface "Filter". Lowpass filters are normalized so that they
     * preserve constants away from the boundaries.
     *
     * @param v DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] lowpass(double[] v, double[] param) {
        return (lowpass(v));
    }

    /**
     * This is the implementation of the highpass Filter. It is used by
     * the interface "Filter". This class doesn't have a highpass Filter so
     * that this method is only provided to be compatible with the interface.
     *
     * @param v DOCUMENT ME!
     * @param param DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] highpass(double[] v, double[] param) {
        return (highpass(v));
    }

    /**
     * This is the implementation of the lowpass Filter. It is used by
     * the interface "Filter". Lowpass filters are normalized so that they
     * preserve constants away from the boundaries.
     *
     * @param gete DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] lowpass(double[] gete) {
        if (gete.length < 3) {
            throw new IllegalArgumentException(
                "The array is not long enough : " + gete.length + " < 3");
        }

        double[] sortie = new double[(2 * gete.length) - 2];

        for (int k = 1; k < (gete.length - 1); k++) {
            sortie[(2 * k) - 2] += (gete[k] * vg[0]);
            sortie[(2 * k) - 1] += (gete[k] * vg[1]);
            sortie[2 * k] += (gete[k] * vg[2]);
            sortie[(2 * k) + 1] += (gete[k] * vg[3]);
        }

        sortie[0] += (v0[0] * gete[0]);
        sortie[1] += (v0[1] * gete[0]);
        sortie[sortie.length - 1] += (v0[0] * gete[gete.length - 1]);
        sortie[sortie.length - 2] += (v0[1] * gete[gete.length - 1]);

        return (sortie);
    }

    /**
     * This is the implementation of the highpass Filter. It is used by
     * the interface "Filter". This class doesn't have a highpass Filter so
     * that this method is only provided to be compatible with the interface.
     *
     * @param gete DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] highpass(double[] gete) {
        return (lowpass(gete));
    }

    /**
     * Return a copy of this object
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        QuadraticSpline sod = (QuadraticSpline) super.clone();

        if (vecteur != null) {
            sod.vecteur = ArrayMathUtils.copy(this.vecteur);
        }

        return (sod);
    }

    /**
     * Get the i th sampled value of the function.
     *
     * @param i position (knot)
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException if i is not a within 0 and the last
     *         knot (dimension()-1)
     */
    public double getValue(int i) {
        if ((i < 0) || (i > (vecteur.length - 1))) {
            throw new IllegalArgumentException("Parameter incorrect : " + i +
                ", " + vecteur.length);
        }

        return (vecteur[i]);
    }

    /**
     * Set a particular value
     *
     * @param i position (knot)
     * @param d value
     *
     * @throws IllegalArgumentException if the parameter i is negative
     */
    public void setValue(int i, double d) {
        if (i < 0) {
            throw new IllegalArgumentException(
                "The parameter must be positive : " + i);
        }

        vecteur[i] = d;
    }

    /**
     * Set the sampled values of the function to the specified array
     *
     * @param v DOCUMENT ME!
     */
    public void setValues(double[] v) {
        vecteur = v;
    }

    /**
     * compute the derivative of the function - useful for numerical
     * analysis
     *
     * @return DOCUMENT ME!
     */
    public LinearSpline derive() {
        return (this.derive(0, 1));
    }

    /**
     * compute the derivative of the function - useful for numerical
     * analysis
     *
     * @param a left boundary of the interval
     * @param b right boundary of the interval
     *
     * @return DOCUMENT ME!
     */
    public LinearSpline derive(double a, double b) {
        double[] v = new double[vecteur.length - 1];

        for (int i = 0; i < (vecteur.length - 1); i++) {
            v[i] = (2 * (vecteur[i + 1] - vecteur[i]) * vecteur.length) / Math.abs(b -
                    a);
        }

        return (new LinearSpline(v));
    }

    /**
     * Number of knots
     *
     * @return DOCUMENT ME!
     */
    public int dimension() {
        return (vecteur.length);
    }

    /**
     * Number of knots after j iterations
     *
     * @param jfin number of iterations
     *
     * @return DOCUMENT ME!
     */
    public int dimension(int jfin) {
        return (Cascades.dimension(vecteur.length, jfin, filtretype));
    }

    /**
     * Return as an array the interpolated values of the function.
     * WARNING: Will return the same values as the evaluate method while it
     * should really proceed by interpolation. Postprocessing should be added
     * to this method in order that it returns the correct values.
     *
     * @param j number of iterations
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public double[] interpolate(int j) {
        if (j < 0) {
            throw new IllegalArgumentException(
                "This parameter must be postive : " + j);
        }

        return (Cascades.evaluation(this, j, vecteur));
    }

    /**
     * Return as an array the sampled values of the function
     *
     * @param jfin number of iterations
     *
     * @return DOCUMENT ME!
     */
    public double[] evaluate(int jfin) {
        return (interpolate(jfin));
    }
}
