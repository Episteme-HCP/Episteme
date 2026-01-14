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

import org.jscience.mathematics.wavelet.FWTCoef;
import org.jscience.mathematics.wavelet.Signal;
import org.jscience.mathematics.wavelet.daubechies2.Daubechies2;


/**
 * This class illustrates how to do signal processing with daubechies 2
 * wavelets
 *
 * @author Daniel Lemire
 */
public class DougJamesDau2 {
    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        // wavelet
        Daubechies2 ondelette = new Daubechies2();

        // signal data
        double[] data = new double[32];

        for (int k = 0; k < data.length; k++) {
            data[k] = k;
        }

        Signal signal = new Signal(data);

        // transform
        signal.setFilter(ondelette);

        int level = 1;
        FWTCoef sCoef = signal.fwt(level); // for some level int
        ArrayMath.print(sCoef.getCoefs()[0]);
        ArrayMath.print(sCoef.getCoefs()[1]);
/******************************
         * We now have to check if we
         * can get the signal back!
         *******************************/
        ArrayMath.print(sCoef.rebuildSignal(ondelette).evaluate(0));
    }
}
