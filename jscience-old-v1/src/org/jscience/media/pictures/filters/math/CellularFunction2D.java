/*
 * Copyright (C) Jerry Huxtable 1998
 */
package org.jscience.media.pictures.filters.math;

import java.util.Random;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class CellularFunction2D implements Function2D {
    /** DOCUMENT ME! */
    public float distancePower = 2;

    /** DOCUMENT ME! */
    public boolean cells = false;

    /** DOCUMENT ME! */
    public boolean angular = false;

    /** DOCUMENT ME! */
    private float[] coefficients = { 1, 0, 0, 0 };

    /** DOCUMENT ME! */
    private Random random = new Random();

    /** DOCUMENT ME! */
    private Point[] results = null;

/**
     * Creates a new CellularFunction2D object.
     */
    public CellularFunction2D() {
        results = new Point[2];

        for (int j = 0; j < results.length; j++)
            results[j] = new Point();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param v DOCUMENT ME!
     */
    public void setCoefficient(int c, float v) {
        coefficients[c] = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getCoefficient(int c) {
        return coefficients[c];
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param cubeX DOCUMENT ME!
     * @param cubeY DOCUMENT ME!
     * @param results DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private float checkCube(float x, float y, int cubeX, int cubeY,
        Point[] results) {
        random.setSeed((571 * cubeX) + (23 * cubeY));

        int numPoints = 3 + (random.nextInt() % 4);
        numPoints = 4;

        for (int i = 0; i < numPoints; i++) {
            float px = random.nextFloat();
            float py = random.nextFloat();
            float dx = Math.abs(x - px);
            float dy = Math.abs(y - py);
            float d;

            if (distancePower == 1.0f) {
                d = dx + dy;
            } else if (distancePower == 2.0f) {
                d = (float) Math.sqrt((dx * dx) + (dy * dy));
            } else {
                d = (float) Math.pow(Math.pow(dx, distancePower) +
                        Math.pow(dy, distancePower), 1 / distancePower);
            }

            // Insertion sort
            for (int j = 0; j < results.length; j++) {
                if (results[j].distance == Double.POSITIVE_INFINITY) {
                    Point last = results[j];
                    last.distance = d;
                    last.x = px;
                    last.y = py;
                    results[j] = last;

                    break;
                } else if (d < results[j].distance) {
                    Point last = results[results.length - 1];

                    for (int k = results.length - 1; k > j; k--)
                        results[k] = results[k - 1];

                    last.distance = d;
                    last.x = px;
                    last.y = py;
                    results[j] = last;

                    break;
                }
            }
        }

        return results[1].distance;
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

        for (int i = 0; i < 2; i++)
            t += (coefficients[i] * results[i].distance);

        if (angular) {
            t += ((Math.atan2(fy - results[0].y, fx - results[0].x) / (2 * Math.PI)) +
            0.5);
        }

        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    class Point {
        /** DOCUMENT ME! */
        int index;

        /** DOCUMENT ME! */
        float x;

        /** DOCUMENT ME! */
        float y;

        /** DOCUMENT ME! */
        float distance;
    }
}
