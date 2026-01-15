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
public class VLNoise implements Function2D {
    /** DOCUMENT ME! */
    private float distortion = 10.0f;

    /**
     * DOCUMENT ME!
     *
     * @param distortion DOCUMENT ME!
     */
    public void setDistortion(float distortion) {
        this.distortion = distortion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getDistortion() {
        return distortion;
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
        float ox = Noise.noise2(x + 0.5f, y) * distortion;
        float oy = Noise.noise2(x, y + 0.5f) * distortion;

        return Noise.noise2(x + ox, y + oy);
    }
}
