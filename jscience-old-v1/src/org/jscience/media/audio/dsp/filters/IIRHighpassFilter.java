// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.filters;


// Optimized IIR highpass filter used as shelving EQ
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class IIRHighpassFilter extends IIRFilterBase {
    // IIRHighpassFilter class constructor
    /**
     * Creates a new IIRHighpassFilter object.
     *
     * @param alpha DOCUMENT ME!
     * @param beta DOCUMENT ME!
     * @param gamma DOCUMENT ME!
     */
    public IIRHighpassFilter(double alpha, double beta, double gamma) {
        super(alpha, beta, gamma);
    }

    // Filter coefficients can also be extracted by passing in
    /**
     * Creates a new IIRHighpassFilter object.
     *
     * @param fd DOCUMENT ME!
     */
    public IIRHighpassFilter(IIRHighpassFilterDesign fd) {
        super(fd);
    }

    // Run the filter algorithm
    /**
     * DOCUMENT ME!
     *
     * @param inBuffer DOCUMENT ME!
     * @param outBuffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void doFilter(short[] inBuffer, double[] outBuffer, int length) {
        for (int index = 0; index < length; index++) {
            // Fetch sample
            inArray[iIndex] = (double) inBuffer[index];

            // Do indices maintainance
            jIndex = iIndex - 2;

            if (jIndex < 0) {
                jIndex += HISTORYSIZE;
            }

            kIndex = iIndex - 1;

            if (kIndex < 0) {
                kIndex += HISTORYSIZE;
            }

            // Run the highpass difference equation
            double out = outArray[iIndex] = 2.0 * (((alpha * (inArray[iIndex] -
                    (2 * inArray[kIndex]) + inArray[jIndex])) +
                    (gamma * outArray[kIndex])) - (beta * outArray[jIndex]));

            outBuffer[index] += (amplitudeAdj * out);

            iIndex = (iIndex + 1) % HISTORYSIZE;
        }
    }
}
