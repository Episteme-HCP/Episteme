// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.filters;


// The IIR highpass filter designed here has unity gain
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IIRHighpassFilterDesign extends IIRFilterDesignBase {
/**
     * Creates a new IIRHighpassFilterDesign object.
     *
     * @param cutoffFrequency DOCUMENT ME!
     * @param sampleRate      DOCUMENT ME!
     * @param dampingFactor   DOCUMENT ME!
     */
    public IIRHighpassFilterDesign(int cutoffFrequency, int sampleRate,
        double dampingFactor) {
        super(cutoffFrequency, sampleRate, dampingFactor);
    }

    // Do the design of the filter. Filter response controlled by
    /**
     * DOCUMENT ME!
     */
    public void doFilterDesign() {
        // Get radians per sample at cutoff frequency
        double thetaZero = getThetaZero();

        double theSin = parameter / (2.0 * Math.sin(thetaZero));

        // Beta relates gain to cutoff freq
        beta = 0.5 * ((1.0 - theSin) / (1.0 + theSin));

        // Final filter coefficient
        gamma = (0.5 + beta) * Math.cos(thetaZero);

        // For unity gain
        alpha = (0.5 + beta + gamma) / 4.0;
    }

    // Entry point for testing
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("\nIIR Highpass Filter Design Program");
            System.out.println("\nUsage:");
            System.out.println("\tIIRHighpassFilterDesign " +
                "cutoffFrequency sampleRate dampingFactor");

            System.exit(1);
        }

        // Parse the command line arguments
        int cutoffFrequency = new Integer(args[0]).intValue();
        int sampleRate = new Integer(args[1]).intValue();
        double dampingFactor = new Double(args[2]).doubleValue();

        // Instantiate highpass filter designer
        IIRHighpassFilterDesign d = new IIRHighpassFilterDesign(cutoffFrequency,
                sampleRate, dampingFactor);

        // Do the design
        d.doFilterDesign();

        // Print out the coefficients
        d.printCoefficients();
    }
}
