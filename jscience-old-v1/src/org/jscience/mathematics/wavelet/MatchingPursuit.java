package org.jscience.mathematics.wavelet;

import org.jscience.mathematics.ArrayMathUtils;

import org.jscience.util.MaximumIterationsExceededException;


/**
 * A Wavelet (and more) matching pursuit class Uses adaptative Morse coding
 * for better performance. The MatchingPursuit is used to obtain a
 * Time-Frequency representation (TF) through a fast algorithm.
 *
 * @author Daniel Lemire
 */
public class MatchingPursuit extends BasisFunctionLibrary implements Cloneable {
    /** DOCUMENT ME! */
    private int[] Record;

    /** DOCUMENT ME! */
    private double[] Coefs;

    /** DOCUMENT ME! */
    private double[][][] TF = new double[0][][];

/**
     * Constructor
     *
     * @param f DOCUMENT ME!
     */
    public MatchingPursuit(DiscreteFunction f) {
        super(f);
    }

/**
     * Creates a new MatchingPursuit object.
     */
    private MatchingPursuit() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param fprimary DOCUMENT ME!
     * @param fdual DOCUMENT ME!
     */
    public void add(MultiscaleFunction fprimary, MultiscaleFunction fdual) {
        super.add(fprimary, fdual);

        double[][][] TFback = TF;
        TF = new double[getSize()][][];
        System.arraycopy(TFback, 0, TF, 0, TFback.length);
        TF[getSize() - 1] = getTF(super.Fprimary[getSize() - 1]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        MatchingPursuit c = (MatchingPursuit) super.clone();

        if (this.Record != null) {
            c.Record = ArrayMathUtils.copy(this.Record);
        }

        if (this.Coefs != null) {
            c.Coefs = ArrayMathUtils.copy(this.Coefs);
        }

        if (this.TF != null) {
            TF = new double[this.TF.length][][];
            System.arraycopy(TF, 0, c.TF, 0, TF.length);
        }

        return (c);
    }

    /**
     * Allows one to trace back the matches
     *
     * @param K DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    private void addToRecord(int K, double d) {
        if (Record == null) {
            Record = new int[1];
            Record[0] = K;
            Coefs = new double[1];
            Coefs[0] = d;
        } else {
            int[] back = Record;
            double[] backednorm = Coefs;
            Record = new int[Record.length + 1];
            Coefs = new double[Record.length + 1];
            System.arraycopy(back, 0, Record, 0, Record.length - 1);
            System.arraycopy(backednorm, 0, Coefs, 0, Record.length - 1);

            //for(int k=0;k<Record.length-1;k++) {
            //Record[k]=back[k];
            //Coefs[k]=backednorm[k];
            // }
            Record[Record.length - 1] = K;
            Coefs[Record.length - 1] = d;
        }
    }

    /**
     * all matches are recorded so one can trace them back
     *
     * @return DOCUMENT ME!
     */
    public int[] getRecord() {
        return (Record);
    }

    /**
     * Trace back how much of the norm was taken out at each match.
     *
     * @return DOCUMENT ME!
     */
    public double[] getRecordedNorms() {
        double[] ans = new double[Record.length];

        for (int k = 0; k < Record.length; k++) {
            ans[k] = Math.abs(Coefs[k]) * Fprimary[k].norm();
        }

        return (ans);
    }

    /**
     * Recover the matching coefficients.
     *
     * @return DOCUMENT ME!
     */
    public double[] getCoefs() {
        return (Coefs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setData(DiscreteFunction f) {
        if (DFunction != null) {
            if (f.dimension() != DFunction.dimension()) {
                throw new IllegalArgumentException(
                    "You cannot change the dimension of the data object. Please create a new object.");
            }
        }

        DFunction = (DiscreteFunction) f.clone();
        Record = null;
        Coefs = null;
    }

    /**
     * Check the validity of the current matching algorithm. Will
     * return an exception if the diagnostic fails. This makes sense: the
     * software should stop if the algorithm isn't safely within the given
     * tolerance.
     *
     * @param tol DOCUMENT ME!
     *
     * @throws MaximumIterationsExceededException if it can't match one of the
     *         elements of the dictionnary
     * @throws IllegalArgumentException if the matching fails
     */
    public void diagnostic(double tol)
        throws MaximumIterationsExceededException {
        if (tol < 0) {
            throw new IllegalArgumentException(
                "A tolerance cannot be negative :" + tol + " < 0");
        }

        MatchingPursuit testmatch = (MatchingPursuit) this.clone();
        double ener1;
        double ener2;

        for (int k = 0; k < getSize(); k++) {
            testmatch.DFunction = (DiscreteFunction) super.Fprimary[k].clone();
            ener1 = testmatch.DFunction.norm();
            testmatch.match();
            ener2 = testmatch.DFunction.norm();

            if (ener2 > (ener1 * tol)) {
                throw new IllegalArgumentException(
                    "Fail to match dictionnary element number " + k +
                    " got a 'de facto' tolerance of " + (ener2 / ener1));
            }
        }
    }

    /**
     * Convert the an element to a time-frequency representation using
     * Fourier as a basis multiply everything by a coefficient b.
     *
     * @param pos DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[][] getTF(int pos, double b) {
        double[][] ans = ArrayMathUtils.copy(TF[pos]);

        for (int k = 0; k < ans.length; k++) {
            for (int l = 0; l < ans[k].length; l++) {
                ans[k][l] *= b;
            }
        }

        return (ans);
    }

    /**
     * Convert the an element to a time-frequency representation using
     * Fourier as a basis.
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double[][] getTF(DiscreteFunction a) {
        int freqlength = (int) Math.ceil(DFunction.dimension() / 2.0);
        double freq;
        double[][] TF = new double[freqlength][DFunction.dimension()];
        double[] time = a.evaluate(0);

        for (int k = 0; k < freqlength; k++) {
            freq = norm(DiscreteHilbertSpace.integrate(a,
                        new Cosine(DFunction.dimension(), k)),
                    DiscreteHilbertSpace.integrate(a,
                        new Sine(DFunction.dimension(), k)));

            for (int l = 0; l < DFunction.dimension(); l++) {
                TF[k][l] = time[l] * freq;
            }
        }

        return (TF);
    }

    /**
     * Does the matching 1 time and return the TF representation. The
     * TF representation may contain negative values and so, taking the
     * absolute value of the result will often be useful.
     *
     * @return DOCUMENT ME!
     */
    public double[][] match() {
        double coef = getWeigth(0);
        int pos = 0;
        double min = getResidue(0);
        double resitemp;
        double weigthtemp;
        double[] soltemp;

        for (int k = 1; k < getSize(); k++) {
            weigthtemp = DiscreteHilbertSpace.integrate(DFunction, Fdual[k]);
            soltemp = DiscreteHilbertSpace.add(DFunction, -weigthtemp,
                    Fprimary[k]);
            resitemp = ArrayMathUtils.norm(soltemp);

            if (resitemp < min) {
                min = resitemp;
                coef = weigthtemp;
                pos = k;
            }
        }

        addToRecord(pos, coef);
        DFunction = new DiscreteFunction(DiscreteHilbertSpace.add(DFunction,
                    -coef, Fprimary[pos]));

        return (getTF(pos, coef));
    }

    /**
     * Repeatly match until it remains less than tol  100 percent of
     * the original L2 norm; no matter what, at least one match will be done.
     * It will return the TF representaiton. The TF representation may contain
     * negative values and so, taking the absolute value of the result will
     * often be useful.
     *
     * @param tol percentile of energy
     *
     * @return DOCUMENT ME!
     *
     * @throws MaximumIterationsExceededException if the number of required
     *         match exceeds 5 times the size of the dictionnary (it should be
     *         a more than confortable margin unless the problem is ill-posed,
     *         change the dictionnary if it doesn't work)
     * @throws IllegalArgumentException if tol is not within the interval [0,1]
     */
    public double[][] matchAll(double tol)
        throws MaximumIterationsExceededException {
        if ((tol > 1) || (tol < 0)) {
            throw new IllegalArgumentException(
                "The percentile should be between 0 and 1: " + tol);
        }

        int TimesAround = 0;
        double ener0 = DFunction.norm();
        double ts = ener0 * tol;
        int maxTimesAround = 5 * getSize();
        double[][] ans = new double[0][0];

        while (DFunction.norm() > ts) {
            TimesAround++;

            if (TimesAround > maxTimesAround) {
                throw new MaximumIterationsExceededException(
                    "Impossible to match to the desired precision (" + tol +
                    ") with this dictionnary of size " + getSize() + " after " +
                    TimesAround +
                    " iterations. You might want to expand the dictionnary.");
            }

            if (ans.length != 0) {
                ans = add(ans, match());
            } else {
                ans = match();
            }
        }

        return (ans);
    }

    /**
     * Force the system to select the given element as the best match.
     * It will return the TF representaiton. The TF representation may contain
     * negative values and so, taking the absolute value of the result will
     * often be useful.
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] forcedMatch(int pos) {
        double coef = DiscreteHilbertSpace.integrate(DFunction, Fdual[pos]);
        addToRecord(pos, coef);
        DFunction = new DiscreteFunction(DiscreteHilbertSpace.add(DFunction,
                    -coef, Fprimary[pos]));

        return (getTF(pos, coef));
    }

    /**
     * Does the matching j times and return the TF representation. The
     * TF representation may contain negative values and so, taking the
     * absolute value of the result will often be useful.
     *
     * @param j of iterations
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException if j is not positive
     */
    public double[][] match(int j) {
        if (j <= 0) {
            throw new IllegalArgumentException("Can't do " + j +
                " matching... Must be a positive number.");
        }

        double[][] ans = match();

        for (int k = 1; k < j; k++) {
            ans = add(ans, match());
        }

        return (ans);
    }
}
