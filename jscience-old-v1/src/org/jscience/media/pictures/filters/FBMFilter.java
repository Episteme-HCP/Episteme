/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters;

import org.jscience.media.pictures.filters.math.*;

import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class FBMFilter extends RGBImageFilter implements MutatableFilter,
    Cloneable, java.io.Serializable {
    /** DOCUMENT ME! */
    public final static int NOISE = 0;

    /** DOCUMENT ME! */
    public final static int RIDGED = 1;

    /** DOCUMENT ME! */
    public final static int VLNOISE = 2;

    /** DOCUMENT ME! */
    public final static int SCNOISE = 3;

    /** DOCUMENT ME! */
    public final static int CELLULAR = 4;

    /** DOCUMENT ME! */
    private float scale = 32;

    /** DOCUMENT ME! */
    private float stretch = 1.0f;

    /** DOCUMENT ME! */
    private float angle = 0.0f;

    /** DOCUMENT ME! */
    private float amount = 1.0f;

    /** DOCUMENT ME! */
    private float H = 1.0f;

    /** DOCUMENT ME! */
    private float octaves = 4.0f;

    /** DOCUMENT ME! */
    private float lacunarity = 2.5f;

    /** DOCUMENT ME! */
    private float gain = 0.5f;

    /** DOCUMENT ME! */
    private float bias = 0.5f;

    /** DOCUMENT ME! */
    private int operation;

    /** DOCUMENT ME! */
    private float m00 = 1.0f;

    /** DOCUMENT ME! */
    private float m01 = 0.0f;

    /** DOCUMENT ME! */
    private float m10 = 0.0f;

    /** DOCUMENT ME! */
    private float m11 = 1.0f;

    /** DOCUMENT ME! */
    private float min;

    /** DOCUMENT ME! */
    private float max;

    /** DOCUMENT ME! */
    private Colormap colormap = new Gradient();

    /** DOCUMENT ME! */
    private boolean ridged;

    /** DOCUMENT ME! */
    private FBM fBm;

    /** DOCUMENT ME! */
    protected Random random = new Random();

    /** DOCUMENT ME! */
    private int basisType = NOISE;

    /** DOCUMENT ME! */
    private Function2D basis;

/**
     * Creates a new FBMFilter object.
     */
    public FBMFilter() {
        setBasisType(NOISE);
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

    /**
     * DOCUMENT ME!
     *
     * @param operation DOCUMENT ME!
     */
    public void setOperation(int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOperation() {
        return operation;
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

        float cos = (float) Math.cos(this.angle);
        float sin = (float) Math.sin(this.angle);
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
     * @param octaves DOCUMENT ME!
     */
    public void setOctaves(float octaves) {
        this.octaves = octaves;
        fBm = makeFBM(H, octaves, lacunarity);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getOctaves() {
        return octaves;
    }

    /**
     * DOCUMENT ME!
     *
     * @param H DOCUMENT ME!
     */
    public void setH(float H) {
        this.H = H;
        fBm = makeFBM(H, octaves, lacunarity);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getH() {
        return H;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lacunarity DOCUMENT ME!
     */
    public void setLacunarity(float lacunarity) {
        this.lacunarity = lacunarity;
        fBm = makeFBM(H, octaves, lacunarity);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getLacunarity() {
        return lacunarity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gain DOCUMENT ME!
     */
    public void setGain(float gain) {
        this.gain = gain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getGain() {
        return gain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bias DOCUMENT ME!
     */
    public void setBias(float bias) {
        this.bias = bias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getBias() {
        return bias;
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
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setDimensions(int width, int height) {
        super.setDimensions(width, height);
        fBm = makeFBM(H, octaves, lacunarity);
    }

    /**
     * DOCUMENT ME!
     *
     * @param basisType DOCUMENT ME!
     */
    public void setBasisType(int basisType) {
        this.basisType = basisType;

        switch (basisType) {
        default:
        case NOISE:
            basis = new Noise();

            break;

        case RIDGED:
            basis = new RidgedFBM();

            break;

        case VLNOISE:
            basis = new VLNoise();

            break;

        case SCNOISE:
            basis = new SCNoise();

            break;

        case CELLULAR:
            basis = new CellularFunction2D();

            break;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBasisType() {
        return basisType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param basis DOCUMENT ME!
     */
    public void setBasis(Function2D basis) {
        this.basis = basis;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Function2D getBasis() {
        return basis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param H DOCUMENT ME!
     * @param lacunarity DOCUMENT ME!
     * @param octaves DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected FBM makeFBM(float H, float lacunarity, float octaves) {
        FBM fbm = new FBM(H, lacunarity, octaves, basis);
        float[] minmax = Noise.findRange(fbm, null);
        min = minmax[0];
        max = minmax[1];

        return fbm;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param rgb DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int filterRGB(int x, int y, int rgb) {
        float nx = (m00 * x) + (m01 * y);
        float ny = (m10 * x) + (m11 * y);
        nx /= scale;
        ny /= (scale * stretch);

        float f = fBm.evaluate(nx, ny);

        // Normalize to 0..1
        f = (f - min) / (max - min);

        //		f = (f + 0.5) * 0.5;
        f = ImageMath.gain(f, gain);
        f = ImageMath.bias(f, bias);
        f *= amount;

        int a = rgb & 0xff000000;
        int v;

        if (colormap != null) {
            v = colormap.getColor(f);
        } else {
            v = PixelUtils.clamp((int) (f * 255));

            int r = v << 16;
            int g = v << 8;
            int b = v;
            v = a | r | g | b;
        }

        if (operation != PixelUtils.REPLACE) {
            v = PixelUtils.combinePixels(rgb, v, operation);
        }

        return v;
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
        FBMFilter dst = (FBMFilter) d;

        if (keepShape || (amount == 0)) {
            dst.setScale(getScale());
            dst.setAngle(getAngle());
            dst.setStretch(getStretch());
            dst.setAmount(getAmount());
            dst.setLacunarity(getLacunarity());
            dst.setOctaves(getOctaves());
            dst.setH(getH());
            dst.setGain(getGain());
            dst.setBias(getBias());
            dst.setColormap(getColormap());
        } else {
            dst.scale = mutate(scale, 0.6f, 4, 3, 64);
            dst.setAngle(mutate(angle, 0.6f, ImageMath.PI / 2));
            dst.stretch = mutate(stretch, 0.6f, 5, 1, 10);
            dst.amount = mutate(amount, 0.6f, 0.2f, 0, 1);
            dst.lacunarity = mutate(lacunarity, 0.5f, 0.5f, 0, 3);
            dst.octaves = mutate(octaves, 0.9f, 0.2f, 0, 12);
            dst.H = mutate(H, 0.7f, 0.2f, 0, 1);
            dst.gain = mutate(gain, 0.2f, 0.2f, 0, 1);
            dst.bias = mutate(bias, 0.2f, 0.2f, 0, 1);
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
        if (random.nextFloat() >= probability) {
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
        if (random.nextFloat() >= probability) {
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
        FBMFilter f = (FBMFilter) super.clone();
        f.fBm = makeFBM(f.H, f.octaves, f.lacunarity);

        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Fractal Brownian Motion...";
    }
}
