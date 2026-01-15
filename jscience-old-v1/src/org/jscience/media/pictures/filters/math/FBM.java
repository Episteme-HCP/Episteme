/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters.math;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class FBM implements Function2D {
    /** DOCUMENT ME! */
    protected float[] exponents;

    /** DOCUMENT ME! */
    protected float H;

    /** DOCUMENT ME! */
    protected float lacunarity;

    /** DOCUMENT ME! */
    protected float octaves;

    /** DOCUMENT ME! */
    protected Function2D basis;

/**
     * Creates a new FBM object.
     *
     * @param H          DOCUMENT ME!
     * @param lacunarity DOCUMENT ME!
     * @param octaves    DOCUMENT ME!
     */
    public FBM(float H, float lacunarity, float octaves) {
        this(H, lacunarity, octaves, new Noise());
    }

/**
     * Creates a new FBM object.
     *
     * @param H          DOCUMENT ME!
     * @param lacunarity DOCUMENT ME!
     * @param octaves    DOCUMENT ME!
     * @param basis      DOCUMENT ME!
     */
    public FBM(float H, float lacunarity, float octaves, Function2D basis) {
        this.H = H;
        this.lacunarity = lacunarity;
        this.octaves = octaves;
        this.basis = basis;

        exponents = new float[(int) octaves + 1];

        float frequency = 1.0f;

        for (int i = 0; i <= (int) octaves; i++) {
            exponents[i] = (float) Math.pow(frequency, -H);
            frequency *= lacunarity;
        }
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
    public Function2D getBasisType() {
        return basis;
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
        float value = 0.0f;
        float remainder;
        int i;

        // to prevent "cascading" effects
        x += 371;
        y += 529;

        for (i = 0; i < (int) octaves; i++) {
            value += (basis.evaluate(x, y) * exponents[i]);
            x *= lacunarity;
            y *= lacunarity;
        }

        remainder = octaves - (int) octaves;

        if (remainder != 0) {
            value += (remainder * basis.evaluate(x, y) * exponents[i]);
        }

        return value;
    }
}
