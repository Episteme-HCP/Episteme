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

package org.jscience.computing.ai.util;

import org.jscience.util.Visualizable;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


/**
 * This utility class provides simple helper functions to aid image
 * loading, saving and rendering within the Generation5 classes.
 *
 * @author James Matthews
 */
public class ImageHelper {
/**
     * Creates a new instance of ImageHelper
     */
    public ImageHelper() {
    }

    /**
     * This function writes an image of a given Visualizable object.
     * The <code>render</code> function is called, rendering the image on to a
     * graphics context before being written to an image.
     *
     * @param filename filename of the image to write
     * @param width the width of the image in pixels
     * @param height the height of the image in pixels
     * @param content the visualizable content
     *
     * @throws IOException this exception is thrown by the
     *         <code>javax.imageio</code> classes if there is a problem
     *         writing the image
     *
     * @see org.jscience.util.Visualizable
     */
    public static void writeVisualizedImage(String filename, int width,
        int height, Visualizable content) throws IOException {
        writeVisualizedImage(filename, width, height, content, true);
    }

    /**
     * This function writes an image of a given Visualizable object.
     * The <code>render</code> function is called, rendering the image on to a
     * graphics context before being written to an image.
     *
     * @param filename filename of the image to write
     * @param width the width of the image in pixels
     * @param height the height of the image in pixels
     * @param content the visualizable content
     * @param aa should anti-aliasing be used?
     *
     * @throws IOException this exception is thrown by the
     *         <code>javax.imageio</code> classes if there is a problem
     *         writing the image
     *
     * @see org.jscience.util.Visualizable
     */
    public static void writeVisualizedImage(String filename, int width,
        int height, Visualizable content, boolean aa) throws IOException {
        // TODO: Detect image type from filename.
        BufferedImage buffer = new BufferedImage(width, height, 1);
        Graphics2D graphics = buffer.createGraphics();

        // Anti-alias as default for written images.
        if (aa == true) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        content.render(graphics, width, height);

        File file = new File(filename);
        ImageIO.write(buffer, "png", file);
    }
}
