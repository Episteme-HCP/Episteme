// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.filters;


// The IIR bandpass filter designed here has unity gain
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IIRBandpassFilterDesign extends IIRFilterDesignBase {
/**
     * Creates a new IIRBandpassFilterDesign object.
     *
     * @param centerFrequency DOCUMENT ME!
     * @param sampleRate      DOCUMENT ME!
     * @param q               DOCUMENT ME!
     */
    public IIRBandpassFilterDesign(int centerFrequency, int sampleRate, double q) {
        super(centerFrequency, sampleRate, q);
    }

    // Do the design of the filter. Filter response controlled by
    /**
     * DOCUMENT ME!
     */
    public void doFilterDesign() {
        // thetaZero = 2 * Pi * Freq * T or (2 * Pi * Freq) / sampleRate
        // where Freq is center frequency of bandpass filter
        double thetaZero = getThetaZero();

        double theTan = Math.tan(thetaZero / (2.0 * parameter));

        // Beta relates gain to bandwidth (and therefore q) at -3 db points
        beta = 0.5 * ((1.0 - theTan) / (1.0 + theTan));

        // For unity gain at center frequency
        alpha = (0.5 - beta) / 2.0;

        // Final filter coefficient
        gamma = (0.5 + beta) * Math.cos(thetaZero);
    }

    // Entry point for testing
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("\nIIR Bandpass Filter Design Program");
            System.out.println("\nUsage:");
            System.out.println("\tIIRBandpassFilterDesign " +
                "centerFrequency sampleRate q");

            System.exit(1);
        }

        // Parse the command line arguments
        int centerFrequency = new Integer(args[0]).intValue();
        int sampleRate = new Integer(args[1]).intValue();
        double q = new Double(args[2]).doubleValue();

        // Instantiate bandpass filter designer
        IIRBandpassFilterDesign d = new IIRBandpassFilterDesign(centerFrequency,
                sampleRate, q);

        // Do the design
        d.doFilterDesign();

        // Print out the coefficients
        d.printCoefficients();
    }
}
