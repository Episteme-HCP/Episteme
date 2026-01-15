/*
 * AtlasUtils.java
 *
 * Created on February 20, 2005, 10:45 PM
 */
package org.jscience.physics.solids;

import java.util.StringTokenizer;


/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class AtlasUtils {
/**
     * Creates a new instance of AtlasUtils
     */
    public AtlasUtils() {
    }

    /**
     * Utility Method that converts an array of doubles into a comma
     * delimited String.
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String convertDoubles(double[] d) {
        String ret = new String();

        for (int i = 0; i < d.length; i++) {
            if (ret.length() > 0) {
                ret = ret + " , ";
            }

            ret = ret + String.valueOf(d[i]);
        }

        return ret;
    }

    /**
     * Utility Method that converts an array of ints into a comma
     * delimited String.
     *
     * @param ints DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String convertIntegers(int[] ints) {
        String ret = new String();

        for (int i = 0; i < ints.length; i++) {
            if (ret.length() > 0) {
                ret = ret + " , ";
            }

            ret = ret + String.valueOf(ints[i]);
        }

        return ret;
    }

    /**
     * Utility Method that converts an array of AtlasObjects into a
     * comma delimited String of their ids.
     *
     * @param objs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String convertAtlasObjects(AtlasObject[] objs) {
        String ret = new String();

        for (int i = 0; i < objs.length; i++) {
            if (ret.length() > 0) {
                ret = ret + " , ";
            }

            ret = ret + objs[i].getId();
        }

        return ret;
    }

    /**
     * Utility Method that converts a String into an array of doubles.
     *
     * @param vals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double[] convertStringtoDoubles(String vals) {
        StringTokenizer s = new StringTokenizer(vals, ",");

        double[] ret = new double[s.countTokens()];

        int i = 0;

        while (s.hasMoreTokens()) {
            ret[i] = Double.valueOf(s.nextToken().trim()).doubleValue();

            i++;
        }

        return ret;
    }

    /**
     * Utility Method that converts a String into an array of ints.
     *
     * @param vals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] convertStringtoIntegers(String vals) {
        StringTokenizer s = new StringTokenizer(vals, ",");

        int[] ret = new int[s.countTokens()];

        int i = 0;

        while (s.hasMoreTokens()) {
            ret[i] = Integer.valueOf(s.nextToken().trim()).intValue();

            i++;
        }

        return ret;
    }

    /**
     * Utility Method that converts a String into an array of doubles.
     *
     * @param vals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] convertStringtoIds(String vals) {
        StringTokenizer s = new StringTokenizer(vals, ",");

        String[] ret = new String[s.countTokens()];

        int i = 0;

        while (s.hasMoreTokens()) {
            ret[i] = s.nextToken().trim();
            i++;
        }

        return ret;
    }
}
