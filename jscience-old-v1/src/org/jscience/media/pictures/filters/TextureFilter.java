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

import java.awt.image.RGBImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class TextureFilter extends RGBImageFilter implements java.io.Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -7538331862272404352L;

    /** DOCUMENT ME! */
    private float scale = 32;

    /** DOCUMENT ME! */
    private float stretch = 1.0f;

    /** DOCUMENT ME! */
    private float angle = 0.0f;

    /** DOCUMENT ME! */
    public float amount = 1.0f;

    /** DOCUMENT ME! */
    public float turbulence = 1.0f;

    /** DOCUMENT ME! */
    public float gain = 0.5f;

    /** DOCUMENT ME! */
    public float bias = 0.5f;

    /** DOCUMENT ME! */
    public int operation;

    /** DOCUMENT ME! */
    private float m00 = 1.0f;

    /** DOCUMENT ME! */
    private float m01 = 0.0f;

    /** DOCUMENT ME! */
    private float m10 = 0.0f;

    /** DOCUMENT ME! */
    private float m11 = 1.0f;

    /** DOCUMENT ME! */
    private Colormap colormap = new Gradient();

    /** DOCUMENT ME! */
    private Function2D function = new Noise();

/**
     * Creates a new TextureFilter object.
     */
    public TextureFilter() {
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
     * @param function DOCUMENT ME!
     */
    public void setFunction(Function2D function) {
        this.function = function;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Function2D getFunction() {
        return function;
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

        float f = (turbulence == 1.0) ? Noise.noise2(nx, ny)
                                      : Noise.turbulence2(nx, ny, turbulence);
        f = (f * 0.5f) + 0.5f;
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
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Texture/Noise...";
    }
}
