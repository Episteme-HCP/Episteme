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

import org.jscience.media.pictures.filters.math.*;

import java.awt.image.ImageFilter;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class CellularFilter extends WholeImageFilter implements Function2D,
    MutatableFilter, Cloneable, java.io.Serializable {
    /** DOCUMENT ME! */
    private static byte[] probabilities;

    /** DOCUMENT ME! */
    public final static int RANDOM = 0;

    /** DOCUMENT ME! */
    public final static int SQUARE = 1;

    /** DOCUMENT ME! */
    public final static int HEXAGONAL = 2;

    /** DOCUMENT ME! */
    public final static int OCTAGONAL = 3;

    /** DOCUMENT ME! */
    public final static int TRIANGULAR = 4;

    /** DOCUMENT ME! */
    protected float scale = 32;

    /** DOCUMENT ME! */
    protected float stretch = 1.0f;

    /** DOCUMENT ME! */
    protected float angle = 0.0f;

    /** DOCUMENT ME! */
    public float amount = 1.0f;

    /** DOCUMENT ME! */
    public float turbulence = 1.0f;

    /** DOCUMENT ME! */
    public float gain = 0.5f;

    /** DOCUMENT ME! */
    public float bias = 0.5f;

    /** DOCUMENT ME! */
    public float distancePower = 2;

    /** DOCUMENT ME! */
    public boolean useColor = false;

    /** DOCUMENT ME! */
    protected Colormap colormap = new Gradient();

    /** DOCUMENT ME! */
    protected float[] coefficients = { 1, 0, 0, 0 };

    /** DOCUMENT ME! */
    protected float angleCoefficient;

    /** DOCUMENT ME! */
    protected Random random = new Random();

    /** DOCUMENT ME! */
    protected float m00 = 1.0f;

    /** DOCUMENT ME! */
    protected float m01 = 0.0f;

    /** DOCUMENT ME! */
    protected float m10 = 0.0f;

    /** DOCUMENT ME! */
    protected float m11 = 1.0f;

    /** DOCUMENT ME! */
    protected Point[] results = null;

    /** DOCUMENT ME! */
    protected float randomness = 0;

    /** DOCUMENT ME! */
    protected int gridType = HEXAGONAL;

    /** DOCUMENT ME! */
    private float min;

    /** DOCUMENT ME! */
    private float max;

    /** DOCUMENT ME! */
    private float gradientCoefficient;

/**
     * Creates a new CellularFilter object.
     */
    public CellularFilter() {
        results = new Point[3];

        for (int j = 0; j < results.length; j++)
            results[j] = new Point();

        if (probabilities == null) {
            probabilities = new byte[8192];

            float factorial = 1;
            float total = 0;
            float mean = 2.5f;

            for (int i = 0; i < 10; i++) {
                if (i > 1) {
                    factorial *= i;
                }

                float probability = ((float) Math.pow(mean, i) * (float) Math.exp(-mean)) / factorial;
                int start = (int) (total * 8192);
                total += probability;

                int end = (int) (total * 8192);

                for (int j = start; j < end; j++)
                    probabilities[j] = (byte) i;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param scale DOCUMENT ME!
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stretch DOCUMENT ME!
     */
    public void setStretch(float stretch) {
        this.stretch = stretch;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getStretch() {
        return stretch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param angle DOCUMENT ME!
     */
    public void setAngle(float angle) {
        this.angle = angle;

        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        m00 = cos;
        m01 = sin;
        m10 = -sin;
        m11 = cos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param v DOCUMENT ME!
     */
    public void setCoefficient(int i, float v) {
        coefficients[i] = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCoefficient(int i) {
        return coefficients[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param angleCoefficient DOCUMENT ME!
     */
    public void setAngleCoefficient(float angleCoefficient) {
        this.angleCoefficient = angleCoefficient;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAngleCoefficient() {
        return angleCoefficient;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gradientCoefficient DOCUMENT ME!
     */
    public void setGradientCoefficient(float gradientCoefficient) {
        this.gradientCoefficient = gradientCoefficient;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGradientCoefficient() {
        return gradientCoefficient;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colormap DOCUMENT ME!
     */
    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param randomness DOCUMENT ME!
     */
    public void setRandomness(float randomness) {
        this.randomness = randomness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getRandomness() {
        return randomness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gridType DOCUMENT ME!
     */
    public void setGridType(int gridType) {
        this.gridType = gridType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGridType() {
        return gridType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param distancePower DOCUMENT ME!
     */
    public void setDistancePower(float distancePower) {
        this.distancePower = distancePower;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDistancePower() {
        return distancePower;
    }

    /**
     * DOCUMENT ME!
     *
     * @param turbulence DOCUMENT ME!
     */
    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAmount() {
        return amount;
    }

    /*
            class Grid {
                    public int setup(int x, int y);
                    public int getNumPoints();
                    public int getX();
                    public int getY();
            }
    
            class RandomGrid extends Grid {
                    public int setup(int x, int y) {
                            random.setSeed(571*cubeX + 23*cubeY);
                    }
    
                    public int getNumPoints() {
                            return 3 + random.nextInt() % 4;
                    }
    
                    public int getX() {
                            return random.nextfloat();
                    }
    
                    public int getY() {
                            return random.nextfloat();
                    }
            }
    */
    private float checkCube(float x, float y, int cubeX, int cubeY,
        Point[] results) {
        int numPoints;
        random.setSeed((571 * cubeX) + (23 * cubeY));

        switch (gridType) {
        case RANDOM:default:
            //			numPoints = 3 + random.nextInt() % 4;
            numPoints = probabilities[random.nextInt() & 0x1fff];

            //			numPoints = 4;
            break;

        case SQUARE:
            numPoints = 1;

            break;

        case HEXAGONAL:
            numPoints = 1;

            break;

        case OCTAGONAL:
            numPoints = 2;

            break;

        case TRIANGULAR:
            numPoints = 2;

            break;
        }

        for (int i = 0; i < numPoints; i++) {
            float px = 0;
            float py = 0;
            float weight = 1.0f;

            switch (gridType) {
            case RANDOM:
                px = random.nextFloat();
                py = random.nextFloat();

                break;

            case SQUARE:
                px = py = 0.5f;

                if (randomness != 0) {
                    px += (randomness * (random.nextFloat() - 0.5));
                    py += (randomness * (random.nextFloat() - 0.5));
                }

                break;

            case HEXAGONAL:

                if ((cubeX & 1) == 0) {
                    px = 0.75f;
                    py = 0;
                } else {
                    px = 0.75f;
                    py = 0.5f;
                }

                if (randomness != 0) {
                    px += (randomness * Noise.noise2(271 * (cubeX + px),
                        271 * (cubeY + py)));
                    py += (randomness * Noise.noise2((271 * (cubeX + px)) + 89,
                        (271 * (cubeY + py)) + 137));
                }

                break;

            case OCTAGONAL:

                switch (i) {
                case 0:
                    px = 0.207f;
                    py = 0.207f;

                    break;

                case 1:
                    px = 0.707f;
                    py = 0.707f;
                    weight = 1.6f;

                    break;
                }

                if (randomness != 0) {
                    px += (randomness * Noise.noise2(271 * (cubeX + px),
                        271 * (cubeY + py)));
                    py += (randomness * Noise.noise2((271 * (cubeX + px)) + 89,
                        (271 * (cubeY + py)) + 137));
                }

                break;

            case TRIANGULAR:

                if ((cubeY & 1) == 0) {
                    if (i == 0) {
                        px = 0.25f;
                        py = 0.35f;
                    } else {
                        px = 0.75f;
                        py = 0.65f;
                    }
                } else {
                    if (i == 0) {
                        px = 0.75f;
                        py = 0.35f;
                    } else {
                        px = 0.25f;
                        py = 0.65f;
                    }
                }

                if (randomness != 0) {
                    px += (randomness * Noise.noise2(271 * (cubeX + px),
                        271 * (cubeY + py)));
                    py += (randomness * Noise.noise2((271 * (cubeX + px)) + 89,
                        (271 * (cubeY + py)) + 137));
                }

                break;
            }

            float dx = (float) Math.abs(x - px);
            float dy = (float) Math.abs(y - py);
            float d;
            dx *= weight;
            dy *= weight;

            if (distancePower == 1.0f) {
                d = dx + dy;
            } else if (distancePower == 2.0f) {
                d = (float) Math.sqrt((dx * dx) + (dy * dy));
            } else {
                d = (float) Math.pow((float) Math.pow(dx, distancePower) +
                        (float) Math.pow(dy, distancePower), 1 / distancePower);
            }

            // Insertion sort the long way round to speed it up a bit
            if (d < results[0].distance) {
                Point p = results[2];
                results[2] = results[1];
                results[1] = results[0];
                results[0] = p;
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.x = cubeX + px;
                p.y = cubeY + py;
            } else if (d < results[1].distance) {
                Point p = results[2];
                results[2] = results[1];
                results[1] = p;
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.x = cubeX + px;
                p.y = cubeY + py;
            } else if (d < results[2].distance) {
                Point p = results[2];
                p.distance = d;
                p.dx = dx;
                p.dy = dy;
                p.x = cubeX + px;
                p.y = cubeY + py;
            }
        }

        return results[2].distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float evaluate(float x, float y) {
        for (int j = 0; j < results.length; j++)
            results[j].distance = Float.POSITIVE_INFINITY;

        int ix = (int) x;
        int iy = (int) y;
        float fx = x - ix;
        float fy = y - iy;

        float d = checkCube(fx, fy, ix, iy, results);

        if (d > fy) {
            d = checkCube(fx, fy + 1, ix, iy - 1, results);
        }

        if (d > (1 - fy)) {
            d = checkCube(fx, fy - 1, ix, iy + 1, results);
        }

        if (d > fx) {
            checkCube(fx + 1, fy, ix - 1, iy, results);

            if (d > fy) {
                d = checkCube(fx + 1, fy + 1, ix - 1, iy - 1, results);
            }

            if (d > (1 - fy)) {
                d = checkCube(fx + 1, fy - 1, ix - 1, iy + 1, results);
            }
        }

        if (d > (1 - fx)) {
            d = checkCube(fx - 1, fy, ix + 1, iy, results);

            if (d > fy) {
                d = checkCube(fx - 1, fy + 1, ix + 1, iy - 1, results);
            }

            if (d > (1 - fy)) {
                d = checkCube(fx - 1, fy - 1, ix + 1, iy + 1, results);
            }
        }

        float t = 0;

        for (int i = 0; i < 3; i++)
            t += (coefficients[i] * results[i].distance);

        if (angleCoefficient != 0) {
            float angle = (float) Math.atan2(y - results[0].y, x -
                    results[0].x);

            if (angle < 0) {
                angle += (2 * (float) Math.PI);
            }

            angle /= (4 * (float) Math.PI);
            t += (angleCoefficient * angle);
        }

        if (gradientCoefficient != 0) {
            float a = 1 / (results[0].dy + results[0].dx);
            t += (gradientCoefficient * a);
        }

        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param freq DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float turbulence2(float x, float y, float freq) {
        float t = 0.0f;

        for (float f = 1.0f; f <= freq; f *= 2)
            t += (evaluate(f * x, f * y) / f);

        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param inPixels DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPixel(int x, int y, int[] inPixels, int width, int height) {
        try {
            float nx = (m00 * x) + (m01 * y);
            float ny = (m10 * x) + (m11 * y);
            nx /= scale;
            ny /= (scale * stretch);
            nx += 1000;
            ny += 1000; // Reduce artifacts around 0,0

            float f = (turbulence == 1.0f) ? evaluate(nx, ny)
                                           : turbulence2(nx, ny, turbulence);

            // Normalize to 0..1
            //		f = (f-min)/(max-min);
            f *= 2;
            f *= amount;

            int a = 0xff000000;
            int v;

            if (colormap != null) {
                v = colormap.getColor(f);

                if (useColor) {
                    int srcx = ImageMath.clamp((int) ((results[0].x - 1000) * scale),
                            0, width - 1);
                    int srcy = ImageMath.clamp((int) ((results[0].y - 1000) * scale),
                            0, height - 1);
                    v = inPixels[(srcy * width) + srcx];
                    f = (results[1].distance - results[0].distance) / (results[1].distance +
                        results[0].distance);
                    f = ImageMath.smoothStep(coefficients[1], coefficients[0], f);
                    v = ImageMath.mixColors(f, 0xff000000, v);
                }

                return v;
            } else {
                v = PixelUtils.clamp((int) (f * 255));

                int r = v << 16;
                int g = v << 8;
                int b = v;

                return a | r | g | b;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param status DOCUMENT ME!
     */
    public void imageComplete(int status) {
        if ((status == IMAGEERROR) || (status == IMAGEABORTED)) {
            consumer.imageComplete(status);

            return;
        }

        //		float[] minmax = Noise.findRange(this, null);
        //		min = minmax[0];
        //		max = minmax[1];
        int width = originalSpace.width;
        int height = originalSpace.height;
        int index = 0;
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                outPixels[index++] = getPixel(x, y, inPixels, width, height);
            }
        }

        consumer.setPixels(0, 0, width, height, defaultRGBModel, outPixels, 0,
            width);
        consumer.imageComplete(status);
        inPixels = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param keepShape DOCUMENT ME!
     * @param keepColors DOCUMENT ME!
     */
    public void mutate(int amount, ImageFilter d, boolean keepShape,
        boolean keepColors) {
        CellularFilter dst = (CellularFilter) d;
        random.setSeed((int) System.currentTimeMillis());

        if (keepShape || (amount == 0)) {
            dst.setGridType(getGridType());
            dst.setRandomness(getRandomness());
            dst.setScale(getScale());
            dst.setAngle(getAngle());
            dst.setStretch(getStretch());
            dst.setAmount(getAmount());
            dst.setTurbulence(getTurbulence());
            dst.setColormap(getColormap());
            dst.setDistancePower(getDistancePower());
            dst.setAngleCoefficient(getAngleCoefficient());

            for (int i = 0; i < 4; i++)
                dst.setCoefficient(i, getCoefficient(i));
        } else {
            dst.scale = mutate(scale, 0.9f, 5, 3, 64);
            dst.setAngle(mutate(angle, 0.6f, (float) Math.PI / 2));
            dst.stretch = mutate(stretch, 0.6f, 3, 1, 10);
            dst.amount = mutate(amount, 0.6f, 0.2f, 0, 1);
            dst.turbulence = mutate(turbulence, 0.7f, 0.5f, 1, 8);
            dst.distancePower = mutate(distancePower, 0.5f, 0.5f, 1, 3);
            dst.randomness = mutate(randomness, 0.9f, 0.2f, 0, 1);

            for (int i = 0; i < coefficients.length; i++)
                dst.coefficients[i] = mutate(coefficients[i], 0.7f, 0.2f, -1, 1);

            if (random.nextFloat() >= 0.5) {
                dst.gridType = random.nextInt() % 5;
            }

            if (random.nextFloat() >= 0.5f) {
                dst.angleCoefficient = 0;
            } else {
                dst.angleCoefficient = mutate(angleCoefficient, 0.5f, 0.5f, -1,
                        1);
            }
        }

        if (keepColors || (amount == 0)) {
            dst.setColormap(getColormap());
        } else {
            dst.setColormap(Gradient.randomGradient());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param probability DOCUMENT ME!
     * @param amount DOCUMENT ME!
     * @param lower DOCUMENT ME!
     * @param upper DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float mutate(float n, float probability, float amount, float lower,
        float upper) {
        if (random.nextFloat() <= probability) {
            return n;
        }

        return ImageMath.clamp(n + (amount * (float) random.nextGaussian()),
            lower, upper);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param probability DOCUMENT ME!
     * @param amount DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float mutate(float n, float probability, float amount) {
        if (random.nextFloat() <= probability) {
            return n;
        }

        return n + (amount * (float) random.nextGaussian());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        CellularFilter f = (CellularFilter) super.clone();
        f.coefficients = (float[]) coefficients.clone();

        //		if (colormap != null)
        //			f.colormap = (Colormap)colormap.clone();
        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Cellular...";
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.5 $
     */
    public class Point {
        /** DOCUMENT ME! */
        public int index;

        /** DOCUMENT ME! */
        public float x;

        /** DOCUMENT ME! */
        public float y;

        /** DOCUMENT ME! */
        public float dx;

        /** DOCUMENT ME! */
        public float dy;

        /** DOCUMENT ME! */
        public float cubeX;

        /** DOCUMENT ME! */
        public float cubeY;

        /** DOCUMENT ME! */
        public float distance;
    }
}
