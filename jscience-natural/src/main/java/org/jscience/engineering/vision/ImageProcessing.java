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

package org.jscience.engineering.vision;

/**
 * Computer vision image processing utilities.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ImageProcessing {

    /**
     * Sobel edge detection kernel (3x3).
     */
    public static final double[][] SOBEL_X = {
            { -1, 0, 1 },
            { -2, 0, 2 },
            { -1, 0, 1 }
    };

    public static final double[][] SOBEL_Y = {
            { -1, -2, -1 },
            { 0, 0, 0 },
            { 1, 2, 1 }
    };

    /**
     * Prewitt edge detection kernel.
     */
    public static final double[][] PREWITT_X = {
            { -1, 0, 1 },
            { -1, 0, 1 },
            { -1, 0, 1 }
    };

    public static final double[][] PREWITT_Y = {
            { -1, -1, -1 },
            { 0, 0, 0 },
            { 1, 1, 1 }
    };

    /**
     * Laplacian kernel (second derivative).
     */
    public static final double[][] LAPLACIAN = {
            { 0, 1, 0 },
            { 1, -4, 1 },
            { 0, 1, 0 }
    };

    /**
     * Gaussian blur kernel (3x3, ÃÆ’ Ã¢â€°Ë† 0.85).
     */
    public static final double[][] GAUSSIAN_3x3 = {
            { 1.0 / 16, 2.0 / 16, 1.0 / 16 },
            { 2.0 / 16, 4.0 / 16, 2.0 / 16 },
            { 1.0 / 16, 2.0 / 16, 1.0 / 16 }
    };

    /**
     * Sharpen kernel.
     */
    public static final double[][] SHARPEN = {
            { 0, -1, 0 },
            { -1, 5, -1 },
            { 0, -1, 0 }
    };

    /**
     * Apply convolution kernel to grayscale image.
     * 
     * @param image  2D grayscale image [height][width]
     * @param kernel Convolution kernel
     * @return Filtered image
     */
    public static double[][] convolve(double[][] image, double[][] kernel) {
        int height = image.length;
        int width = image[0].length;
        int kSize = kernel.length;
        int kHalf = kSize / 2;

        double[][] result = new double[height][width];

        for (int y = kHalf; y < height - kHalf; y++) {
            for (int x = kHalf; x < width - kHalf; x++) {
                double sum = 0;
                for (int ky = 0; ky < kSize; ky++) {
                    for (int kx = 0; kx < kSize; kx++) {
                        sum += image[y - kHalf + ky][x - kHalf + kx] * kernel[ky][kx];
                    }
                }
                result[y][x] = sum;
            }
        }

        return result;
    }

    /**
     * Sobel edge detection: gradient magnitude.
     */
    public static double[][] sobelEdgeDetection(double[][] image) {
        double[][] gx = convolve(image, SOBEL_X);
        double[][] gy = convolve(image, SOBEL_Y);

        int height = image.length;
        int width = image[0].length;
        double[][] magnitude = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                magnitude[y][x] = Math.sqrt(gx[y][x] * gx[y][x] + gy[y][x] * gy[y][x]);
            }
        }

        return magnitude;
    }

    /**
     * Gradient direction from Sobel operators.
     */
    public static double[][] gradientDirection(double[][] image) {
        double[][] gx = convolve(image, SOBEL_X);
        double[][] gy = convolve(image, SOBEL_Y);

        int height = image.length;
        int width = image[0].length;
        double[][] direction = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                direction[y][x] = Math.atan2(gy[y][x], gx[y][x]);
            }
        }

        return direction;
    }

    /**
     * Gaussian blur with specified sigma.
     */
    public static double[][] gaussianBlur(double[][] image, double sigma, int kernelSize) {
        double[][] kernel = createGaussianKernel(sigma, kernelSize);
        return convolve(image, kernel);
    }

    /**
     * Creates Gaussian kernel.
     */
    public static double[][] createGaussianKernel(double sigma, int size) {
        double[][] kernel = new double[size][size];
        int half = size / 2;
        double sum = 0;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int dy = y - half;
                int dx = x - half;
                kernel[y][x] = Math.exp(-(dx * dx + dy * dy) / (2 * sigma * sigma));
                sum += kernel[y][x];
            }
        }

        // Normalize
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                kernel[y][x] /= sum;
            }
        }

        return kernel;
    }

    /**
     * Threshold image (binary).
     */
    public static double[][] threshold(double[][] image, double thresh) {
        int height = image.length;
        int width = image[0].length;
        double[][] result = new double[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                result[y][x] = image[y][x] >= thresh ? 1.0 : 0.0;
            }
        }

        return result;
    }

    /**
     * Otsu's threshold (automatic).
     */
    public static double otsuThreshold(double[][] image) {
        // Histogram
        int[] histogram = new int[256];
        int total = 0;

        for (double[] row : image) {
            for (double val : row) {
                int bin = Math.min(255, Math.max(0, (int) (val * 255)));
                histogram[bin]++;
                total++;
            }
        }

        double sum = 0;
        for (int i = 0; i < 256; i++)
            sum += i * histogram[i];

        double sumB = 0, wB = 0;
        double maxVariance = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histogram[t];
            if (wB == 0)
                continue;

            double wF = total - wB;
            if (wF == 0)
                break;

            sumB += t * histogram[t];
            double mB = sumB / wB;
            double mF = (sum - sumB) / wF;

            double variance = wB * wF * (mB - mF) * (mB - mF);
            if (variance > maxVariance) {
                maxVariance = variance;
                threshold = t;
            }
        }

        return threshold / 255.0;
    }

    /**
     * Morphological erosion (3x3 structuring element).
     */
    public static double[][] erode(double[][] image) {
        int height = image.length;
        int width = image[0].length;
        double[][] result = new double[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double min = Double.MAX_VALUE;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        min = Math.min(min, image[y + dy][x + dx]);
                    }
                }
                result[y][x] = min;
            }
        }

        return result;
    }

    /**
     * Morphological dilation (3x3 structuring element).
     */
    public static double[][] dilate(double[][] image) {
        int height = image.length;
        int width = image[0].length;
        double[][] result = new double[height][width];

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                double max = Double.MIN_VALUE;
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        max = Math.max(max, image[y + dy][x + dx]);
                    }
                }
                result[y][x] = max;
            }
        }

        return result;
    }
}


