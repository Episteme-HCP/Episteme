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

import org.jscience.mathematics.chaos.*;
import org.jscience.mathematics.numbers.Complex;

import java.applet.Applet;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;


/**
 * Plot of the Mandelbrot set.
 *
 * @author Mark Hale
 * @version 1.1
 */
public final class MandelbrotPlot extends Applet {
    /**
     * DOCUMENT ME!
     */
    private final int[] colorTable = new int[22];

    /**
     * DOCUMENT ME!
     */
    private final int[] range = {
            1, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 200, 300, 400, 500, 600,
            700, 800, 900, 1000
        };

    /**
     * DOCUMENT ME!
     */
    private final int N = 2000;

    /**
     * DOCUMENT ME!
     */
    private Image mandelbrotImage;

    /**
     * DOCUMENT ME!
     */
    private Image imageBuffer;

    /**
     * Initialise the applet.
     */
    public void init() {
        // create colour palette
        for (int i = 0; i < colorTable.length; i++)
            colorTable[i] = grey(255 - (10 * i));

        // draw Mandelbrot set to an image buffer
        mandelbrotImage = drawMandelbrot();
        // mouse listener
        addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (imageBuffer == mandelbrotImage) {
                        final int width = getSize().width;
                        final int height = getSize().height;
                        final Complex z = new Complex(((evt.getX() * 3.0) / width) -
                                2.0, 1.25 - ((evt.getY() * 2.5) / height));
                        imageBuffer = drawJulia(z);
                        System.err.println(z.toString());
                        showStatus("Click to return to Mandelbrot set");
                    } else {
                        imageBuffer = mandelbrotImage;
                        showStatus("Click to generate a Julia set");
                    }

                    repaint();
                }
            });
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        imageBuffer = mandelbrotImage;
        showStatus("Click to generate a Julia set");
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.drawImage(imageBuffer, 0, 0, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Image drawMandelbrot() {
        final int width = getSize().width;
        final int height = getSize().height;
        final MandelbrotSet set = new MandelbrotSet();
        double x;
        double y;
        int[] pixels = new int[width * height];
        int index = 0;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                x = ((i * 3.0) / width) - 2.0;
                y = 1.25 - ((j * 2.5) / height);
                pixels[index++] = colorLookup(set.isMember(x, y, N));
            }
        }

        return createImage(new MemoryImageSource(width, height, pixels, 0, width));
    }

    /**
     * DOCUMENT ME!
     *
     * @param z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Image drawJulia(Complex z) {
        final int width = getSize().width;
        final int height = getSize().height;
        final JuliaSet set = new JuliaSet(z);
        double x;
        double y;
        int[] pixels = new int[width * height];
        int index = 0;

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                x = ((i * 4.0) / width) - 2.0;
                y = 1.25 - ((j * 2.5) / height);
                pixels[index++] = colorLookup(set.isMember(x, y, N));
            }
        }

        return createImage(new MemoryImageSource(width, height, pixels, 0, width));
    }

    /**
     * Returns a RGBA value.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int colorLookup(int n) {
        if (n == 0) {
            return 0xff000000; // black
        } else {
            for (int i = 0; i < (range.length - 1); i++) {
                if ((n >= range[i]) && (n < range[i + 1])) {
                    return colorTable[i];
                }
            }

            return 0xffffffff; // white
        }
    }

    /**
     * Returns a grey RGBA value.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static int grey(int n) {
        return 0xff000000 | (n << 16) | (n << 8) | n;
    }
}
