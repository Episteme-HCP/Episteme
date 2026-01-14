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

package org.jscience.media.pictures.filters;

/**
 * The interface for an image quantizer. The addColor method is called
 * (repeatedly if necessary) with all the image pixels. A color table can then
 * be returned by calling the buildColorTable method.
 */
public interface Quantizer {
    /**
     * Initialize the quantizer. This should be called before adding
     * any pixels.
     *
     * @param numColors the number of colors we're quantizing to.
     */
    public void setup(int numColors);

    /**
     * Add pixels to the quantizer.
     *
     * @param pixels the array of ARGB pixels
     * @param offset the offset into the array
     * @param count the count of pixels
     */
    public void addPixels(int[] pixels, int offset, int count);

    /*
     * Build a color table from the added pixels.
     * @param
     * @return an array of ARGB pixels representing a color table
     */
    public int[] buildColorTable();

    /*
     * Using the previously-built color table, return the index into that table for a pixel.
     * This is guaranteed to return a valid index - returning the index of a color closer
     * to that requested if necessary.
     * @param rgb the pixel to find
     * @return the pixel's index in the color table
     */
    public int getIndexForColor(int rgb);
}
