package org.jscience.media.pictures;

/**
 * Image convolution kernels and processing.
 */
public final class ImageFilterKernels {

    private ImageFilterKernels() {}

    public record Kernel(int size, float[] data) {}

    // Common kernels
    public static final Kernel SHARPEN = new Kernel(3, new float[]{
        0, -1, 0,
        -1, 5, -1,
        0, -1, 0
    });

    public static final Kernel BOX_BLUR = new Kernel(3, new float[]{
        1/9f, 1/9f, 1/9f,
        1/9f, 1/9f, 1/9f,
        1/9f, 1/9f, 1/9f
    });

    public static final Kernel SOBEL_HORIZONTAL = new Kernel(3, new float[]{
        -1, -2, -1,
         0,  0,  0,
         1,  2,  1
    });

    public static final Kernel SOBEL_VERTICAL = new Kernel(3, new float[]{
        -1, 0, 1,
        -2, 0, 2,
        -1, 0, 1
    });

    /**
     * Applies a convolution kernel to a grayscale image.
     */
    public static float[][] convolve(float[][] input, Kernel kernel) {
        int width = input.length;
        int height = input[0].length;
        int kSize = kernel.size();
        int offset = kSize / 2;
        
        float[][] output = new float[width][height];
        
        for (int x = offset; x < width - offset; x++) {
            for (int y = offset; y < height - offset; y++) {
                float sum = 0;
                for (int i = 0; i < kSize; i++) {
                    for (int j = 0; j < kSize; j++) {
                        sum += input[x + i - offset][y + j - offset] * kernel.data()[i * kSize + j];
                    }
                }
                output[x][y] = sum;
            }
        }
        
        return output;
    }

    /**
     * Converts RGB image to Grayscale.
     */
    public static float[][] toGrayscale(int[][] rgb) {
        int width = rgb.length;
        int height = rgb[0].length;
        float[][] gray = new float[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = rgb[x][y];
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                // Luminance formula
                gray[x][y] = (0.299f * r + 0.587f * g + 0.114f * b) / 255.0f;
            }
        }
        
        return gray;
    }

    /**
     * Gaussian Blur kernel generator.
     */
    public static Kernel gaussianKernel(int size, double sigma) {
        float[] data = new float[size * size];
        int center = size / 2;
        float sum = 0;
        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                double dist = Math.pow(x - center, 2) + Math.pow(y - center, 2);
                float val = (float) (Math.exp(-dist / (2 * sigma * sigma)) / (2 * Math.PI * sigma * sigma));
                data[x * size + y] = val;
                sum += val;
            }
        }
        
        // Normalize
        for (int i = 0; i < data.length; i++) data[i] /= sum;
        
        return new Kernel(size, data);
    }
}
