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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class provides a method of creating a smooth gradient between up to 10
 * evenly distributed colour points. To create a gradient, add up to 10 colours
 * and call <code>createGradient</code>. <code>getColour</code> or
 * <code>getGradient</code> can be used to retrieve the resultant colours.
 *
 * @author James Matthews
 */
public class Gradient {
    private Color[] crGradientPoints = new Color[10];
    private Color[] crGradient;
    private int intGradientPoints = 0;

    // TODO: toWhite(), toBlack() using luminosity

    /**
     * Creates a new instance of Gradient.
     */
    public Gradient() {
        reset();
    }

    /**
     * Add a colour to the gradient list. A maximum of 10 colour points are
     * allowed, any more will throw an exception.
     *
     * @param gradientColour the next colour in the gradient.
     */
    public void addPoint(Color gradientColour) {
        if (intGradientPoints > 10) {
            throw new java.lang.IllegalArgumentException("Only 10 points allowed");
        }

        crGradientPoints[intGradientPoints++] = gradientColour;
    }

    /**
     * Reset the gradient.
     */
    public void reset() {
        intGradientPoints = 0;
    }

    /**
     * Calculate the RGB deltas between two different colour values and over a
     * given number of timesteps. <code>createGradient</code> uses this function
     * to connect the gradient points together.
     *
     * @param start the starting colour.
     * @param end   the ending colour.
     * @param steps the number of steps required to get from <code>start</code>
     *              to <code>end</code.
     * @return a <code>double[3]</code> array returning the red, green and blue
     *         delta values.
     */
    protected double[] getRGBDeltas(Color start, Color end, int steps) {
        double[] delta = new double[3];

        delta[0] = (end.getRed() - start.getRed()) / (double) steps;
        delta[1] = (end.getGreen() - start.getGreen()) / (double) steps;
        delta[2] = (end.getBlue() - start.getBlue()) / (double) steps;

        return delta;
    }

    /**
     * Create the gradient using the current gradient list. This method
     * defaults to using 256 steps.
     */
    public void createGradient() {
        createGradient(256);
    }

    /**
     * Create the gradient using the current gradient list.
     *
     * @param numSteps the number of steps to take from the first colour to
     *                 the last.
     */
    public void createGradient(int numSteps) {
        int steps = numSteps / (intGradientPoints - 1);
        int grad = 0;
        double[] crColours = new double[3];
        crColours[0] = crGradientPoints[0].getRed();
        crColours[1] = crGradientPoints[0].getGreen();
        crColours[2] = crGradientPoints[0].getBlue();

        crGradient = new Color[numSteps];

        // For each of the gradient points
        for (int i = 0; i < (intGradientPoints - 1); i++) {
            double[] delta = getRGBDeltas(crGradientPoints[i],
                    crGradientPoints[i + 1], steps);

            for (int s = 0; s < steps; s++) {
                crColours[0] += delta[0];
                crColours[1] += delta[1];
                crColours[2] += delta[2];

                if (crColours[0] > 255) {
                    crColours[0] = 255;
                }

                if (crColours[1] > 255) {
                    crColours[1] = 255;
                }

                if (crColours[2] > 255) {
                    crColours[2] = 255;
                }

                crGradient[grad] = new Color((int) Math.round(crColours[0]),
                        (int) Math.round(crColours[1]),
                        (int) Math.round(crColours[2]));
                grad++;
            }
        }
    }

    /**
     * Return the <i>ith</i> colour in the gradient.
     *
     * @param i the index of gradient array to return.
     * @return the <code>Color</code> value in the gradient.
     */
    public Color getColour(int i) {
        return crGradient[i];
    }

    /**
     * Return the entire gradient as a Color array.
     *
     * @return <code>Color[256]</code> containing the gradient colours.
     */
    public Color[] getGradient() {
        return crGradient;
    }

    /**
     * Test function.
     *
     * @param args command-line arguments (ignored).
     */
    public static void main(String[] args) {
        Gradient gradient = new Gradient();

        gradient.addPoint(new Color(64, 0, 128));
        gradient.addPoint(new Color(255, 0, 128));
        gradient.addPoint(new Color(255, 255, 128));
        gradient.addPoint(Color.WHITE);
        gradient.createGradient();

        BufferedImage buffer = new BufferedImage(512, 100, 1);
        Graphics graphics = buffer.createGraphics();

        for (int i = 0; i < 512; i += 2) {
            graphics.setColor(gradient.getColour(i / 2));
            graphics.fillRect(i, 0, 2, 100);
        }

        java.awt.image.RenderedImage rendered = buffer;

        try {
            File file = new File("gradtest.png");
            ImageIO.write(rendered, "png", file);
        } catch (IOException e) {
            System.err.println("An error occurred.");
        }
    }
}
