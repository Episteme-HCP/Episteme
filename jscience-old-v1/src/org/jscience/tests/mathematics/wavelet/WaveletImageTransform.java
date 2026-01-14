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

import org.jscience.awt.ImageCanvas;

import org.jscience.mathematics.wavelet.cdf2_4.CDF2_4;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Launch the following code on an image having odd dimensions and you'll
 * have a visual example.
 *
 * @author Daniel Lemire
 */
public final class WaveletImageTransform extends Frame {
    /**
     * Creates a new WaveletImageTransform object.
     *
     * @param filename DOCUMENT ME!
     */
    public WaveletImageTransform(String filename) {
        super("Fast Wavelet Transform");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });

        System.err.println("Opening \"" + filename + "\"");

        PixelArray pa1 = new PixelArray(filename);
        ImageCanvas ic1 = new ImageCanvas(pa1.rebuildImage());
        System.err.println("Please wait... This could take a few minutes...");

        PixelArray[][] paa = pa1.greenFWT(new CDF2_4());

        ImageCanvas ic0_0 = new ImageCanvas(paa[0][0].rebuildImage());

        ImageCanvas ic1_0 = new ImageCanvas(paa[1][0].rebuildImage());
        ImageCanvas ic0_1 = new ImageCanvas(paa[0][1].rebuildImage());
        ImageCanvas ic1_1 = new ImageCanvas(paa[1][1].rebuildImage());

        setLayout(new GridLayout(2, 0, 2, 2));
        add(ic1);

        Panel p2 = new Panel();
        p2.setLayout(new GridLayout(0, 2, 1, 1));
        p2.add(ic0_0);
        p2.add(ic1_0);
        p2.add(ic0_1);
        p2.add(ic1_1);
        add(p2);
        setBounds(0, 0, 400, 400);
        show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        if (arg.length == 0) {
            System.err.println("Please specify an image with odd dimensions.");

            return;
        }

        new WaveletImageTransform(arg[0]);
    }
}
