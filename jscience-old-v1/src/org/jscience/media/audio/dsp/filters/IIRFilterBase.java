// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.filters;


// Base class for all IIR filters.
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public abstract class IIRFilterBase {
    /** DOCUMENT ME! */
    protected static final int HISTORYSIZE = 3;

    // Private class data
    /** DOCUMENT ME! */
    protected double alpha;

    /** DOCUMENT ME! */
    protected double beta;

    /** DOCUMENT ME! */
    protected double gamma;

    /** DOCUMENT ME! */
    protected double amplitudeAdj;

    /** DOCUMENT ME! */
    protected double[] inArray;

    /** DOCUMENT ME! */
    protected double[] outArray;

    /** DOCUMENT ME! */
    protected int iIndex;

    /** DOCUMENT ME! */
    protected int jIndex;

    /** DOCUMENT ME! */
    protected int kIndex;

    // IIRFilterBase class constructor
    /**
     * Creates a new IIRFilterBase object.
     *
     * @param alpha DOCUMENT ME!
     * @param beta DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     */
    public IIRFilterBase(double alpha, double beta, double gamma) {
        // Save incoming
        this.alpha = alpha;
        this.beta = beta;
        this.gamma = gamma;

        amplitudeAdj = 1.0;

        // Allocate history buffers
        inArray = new double[HISTORYSIZE];
        outArray = new double[HISTORYSIZE];
    }

    // Filter coefficients can also be extracted by passing in
    /**
     * Creates a new IIRFilterBase object.
     *
     * @param fdb DOCUMENT ME!
     */
    public IIRFilterBase(IIRFilterDesignBase fdb) {
        this(fdb.getAlpha(), fdb.getBeta(), fdb.getGamma());
    }

    /**
     * DOCUMENT ME!
     *
     * @param fdb DOCUMENT ME!
     */
    public void updateFilterCoefficients(IIRFilterDesignBase fdb) {
        this.alpha = fdb.getAlpha();
        this.beta = fdb.getBeta();
        this.gamma = fdb.getGamma();
    }

    /**
     * DOCUMENT ME!
     *
     * @param alpha DOCUMENT ME!
     */
    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beta DOCUMENT ME!
     */
    public void setBeta(double beta) {
        this.beta = beta;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gamma DOCUMENT ME!
     */
    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    // Abstract method that runs the filter algorithm
    /**
     * DOCUMENT ME!
     *
     * @param inBuffer DOCUMENT ME!
     * @param outBuffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public abstract void doFilter(short[] inBuffer, double[] outBuffer,
        int length);

    // Set the amplitude adjustment to be applied to filtered data
    /**
     * DOCUMENT ME!
     *
     * @param amplitudeAdj DOCUMENT ME!
     */
    public void setAmplitudeAdj(double amplitudeAdj) {
        this.amplitudeAdj = amplitudeAdj;
    }
}
