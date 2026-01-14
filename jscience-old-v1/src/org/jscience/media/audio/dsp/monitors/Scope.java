/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

// This code is repackaged after the code from Craig A. Lindley, from Digital Audio with Java
// Site ftp://ftp.prenhall.com/pub/ptr/professional_computer_science.w-022/digital_audio/
// Email
package org.jscience.media.audio.dsp.monitors;

import org.jscience.media.audio.dsp.AbstractAudio;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Scope extends AbstractAudio {
    // Private class data
    /** DOCUMENT ME! */
    private TriggerFlag tf;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private int sampleRate;

    /** DOCUMENT ME! */
    private int numberOfChannels;

/**
     * Creates a new Scope object.
     */
    public Scope() {
        super("Scope", AbstractAudio.MONITOR);

        this.name = name;

        // Flag object for controlling scope triggering
        tf = new TriggerFlag();
    }

    // Grab samples from previous stage and store for scope
    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     * @param length DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSamples(short[] buffer, int length) {
        if (length == 0) {
            return 0;
        }

        // Get samples from the previous stage
        int samples = previous.getSamples(buffer, length);

        // See if scope should trigger
        if (!tf.trigger()) {
            // Indicate scope has triggered
            tf.triggered();

            // Clone the array for the scope
            short[] newBuffer = new short[samples];

            // Copy the data
            System.arraycopy(buffer, 0, newBuffer, 0, length);
        }

        // Return samples read
        return samples;
    }

    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxSamplingRate(int min, int max, int preferred) {
        super.minMaxSamplingRate(min, max, preferred);
        sampleRate = preferred;
    }

    // Negotiate the number of channels
    /**
     * DOCUMENT ME!
     *
     * @param min DOCUMENT ME!
     * @param max DOCUMENT ME!
     * @param preferred DOCUMENT ME!
     */
    public void minMaxChannels(int min, int max, int preferred) {
        super.minMaxChannels(min, max, preferred);
        numberOfChannels = preferred;
    }
}
