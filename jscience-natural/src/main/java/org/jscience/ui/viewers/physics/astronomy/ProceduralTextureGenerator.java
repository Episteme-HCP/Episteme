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

package org.jscience.ui.viewers.physics.astronomy;

import java.util.Random;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ProceduralTextureGenerator {

    private static final int PERMUTATION_SIZE = 256;
    private static final int[] p = new int[PERMUTATION_SIZE * 2];
    private static final Random random = new Random();

    static {
        // Initialize permutation table
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            p[i] = i;
        }
        // Shuffle
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            int j = random.nextInt(PERMUTATION_SIZE);
            int temp = p[i];
            p[i] = p[j];
            p[j] = temp;
        }
        // Duplicate
        for (int i = 0; i < PERMUTATION_SIZE; i++) {
            p[i + PERMUTATION_SIZE] = p[i];
        }
    }

    /**
     * Generates a moon-like texture.
     *
     * @param width  Width of the texture
     * @param height Height of the texture
     * @param seed   Random seed
     * @return BufferedImage containing the texture
     */
    public static BufferedImage generateMoonTexture(int width, int height, long seed) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random r = new Random(seed);

        // Base color variation (Grey/White)
        double scale = 0.02;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Cylindrical mapping wrap-around for X (unused for now)
                // double nx = (double) x / width;
                // double ny = (double) y / height;

                // Noise calculation (octaves)
                double noise = fbm(x * scale, y * scale, 4);

                // Map noise to color (Greyscale for Moon)
                int val = (int) ((noise + 1.0) * 0.5 * 255.0);
                val = Math.max(0, Math.min(255, val));

                // Add some craters (simple circular diffs? - maybe just noise for now is
                // enough)

                int rgb = (val << 16) | (val << 8) | val;
                image.setRGB(x, y, rgb);
            }
        }

        // Crude crater simulation
        for (int i = 0; i < 50; i++) {
            int cx = r.nextInt(width);
            int cy = r.nextInt(height);
            int radius = 5 + r.nextInt(20);
            drawCrater(image, cx, cy, radius, width, height);
        }

        return image;
    }

    private static void drawCrater(BufferedImage img, int cx, int cy, int radius, int w, int h) {
        // Simple circle darkening
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    int px = (cx + x + w) % w;
                    int py = Math.max(0, Math.min(h - 1, cy + y));

                    int rgb = img.getRGB(px, py);
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF; // Correction: was (rgb & 0xFF)

                    // Darken
                    red = Math.max(0, red - 30);
                    green = Math.max(0, green - 30);
                    blue = Math.max(0, blue - 30);

                    img.setRGB(px, py, (red << 16) | (green << 8) | blue);
                }
            }
        }
    }

    // Fractional Brownian Motion
    private static double fbm(double x, double y, int octaves) {
        double total = 0;
        double frequency = 1;
        double amplitude = 1;
        double maxValue = 0; // Used for normalizing result to 0.0 - 1.0
        for (int i = 0; i < octaves; i++) {
            total += perlin(x * frequency, y * frequency) * amplitude;
            maxValue += amplitude;
            amplitude *= 0.5;
            frequency *= 2;
        }
        return total / maxValue;
    }

    private static double perlin(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
        x -= Math.floor(x);
        y -= Math.floor(y);
        double u = fade(x);
        double v = fade(y);
        int A = p[X] + Y, AA = p[A], AB = p[A + 1], B = p[X + 1] + Y, BA = p[B], BB = p[B + 1];
        return lerp(v, lerp(u, grad(p[AA], x, y), grad(p[BA], x - 1, y)),
                lerp(u, grad(p[AB], x, y - 1), grad(p[BB], x - 1, y - 1)));
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y,
                v = h < 4 ? y : h == 12 || h == 14 ? x : 0;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
