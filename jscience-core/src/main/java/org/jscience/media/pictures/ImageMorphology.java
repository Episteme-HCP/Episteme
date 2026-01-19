package org.jscience.media.pictures;

/**
 * Mathematical morphology for binary and grayscale images.
 */
public final class ImageMorphology {

    private ImageMorphology() {}

    public enum Shape { SQUARE, CROSS, CIRCLE }

    /**
     * Dilation operation (Max filter).
     */
    public static float[][] dilate(float[][] input, int radius, Shape shape) {
        int width = input.length;
        int height = input[0].length;
        float[][] output = new float[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float max = 0;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        if (isValid(x + dx, y + dy, width, height, dx, dy, radius, shape)) {
                            max = Math.max(max, input[x + dx][y + dy]);
                        }
                    }
                }
                output[x][y] = max;
            }
        }
        return output;
    }

    /**
     * Erosion operation (Min filter).
     */
    public static float[][] erode(float[][] input, int radius, Shape shape) {
        int width = input.length;
        int height = input[0].length;
        float[][] output = new float[width][height];
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                float min = 1.0f;
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        if (isValid(x + dx, y + dy, width, height, dx, dy, radius, shape)) {
                            min = Math.min(min, input[x + dx][y + dy]);
                        }
                    }
                }
                output[x][y] = min;
            }
        }
        return output;
    }

    /**
     * Opening operation (Erosion then Dilation).
     * Removes small bright spots.
     */
    public static float[][] open(float[][] input, int radius, Shape shape) {
        return dilate(erode(input, radius, shape), radius, shape);
    }

    /**
     * Closing operation (Dilation then Erosion).
     * Fills small dark holes.
     */
    public static float[][] close(float[][] input, int radius, Shape shape) {
        return erode(dilate(input, radius, shape), radius, shape);
    }

    private static boolean isValid(int x, int y, int w, int h, int dx, int dy, int r, Shape s) {
        if (x < 0 || x >= w || y < 0 || y >= h) return false;
        
        return switch (s) {
            case SQUARE -> true;
            case CROSS -> dx == 0 || dy == 0;
            case CIRCLE -> (dx * dx + dy * dy) <= (r * r);
        };
    }
}
