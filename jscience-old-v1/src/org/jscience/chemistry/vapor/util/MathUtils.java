/*
 * Math Utilities for VLE.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util;

/**
 * Math Utilities for VLE.
 */
public class MathUtils {
    /**
     * DOCUMENT ME!
     *
     * @param num DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double square(double num) {
        return (num * num);
    }

    /**
     * DOCUMENT ME!
     *
     * @param num DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double cubeRoot(double num) {
        if (num < 0) {
            return -Math.exp(Math.log(Math.abs(num)) / 3);
        } else {
            return Math.exp(Math.log(num) / 3);
        }
    }
}
