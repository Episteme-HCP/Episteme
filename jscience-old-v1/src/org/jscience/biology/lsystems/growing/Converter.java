/*
---------------------------------------------------------------------------
VIRTUAL PLANTS
==============

This Diploma work is a computer graphics project made at the University
of applied sciences in Biel, Switzerland. http://www.hta-bi.bfh.ch
The taks is to simulate the growth of 3 dimensional plants and show
them in a virtual world.
This work is based on the ideas of Lindenmayer and Prusinkiewicz which
are taken from the book 'The algorithmic beauty of plants'.
The Java and Java3D classes have to be used for this work. This file
is one class of the program. For more information look at the VirtualPlants
homepage at: http://www.hta-bi.bfh.ch/Projects/VirtualPlants

Hosted by Claude Schwab

Developed by Rene Gressly
http://www.gressly.ch/rene

25.Oct.1999 - 17.Dec.1999

Copyright by the University of applied sciences Biel, Switzerland
----------------------------------------------------------------------------
*/
package org.jscience.biology.lsystems.growing;

/**
 * This class holds methods for easier conversion of types.
 *
 * @author <a href="http://www.gressly.ch/rene/" target="_top">Rene Gressly</a>
 */
public class Converter {
    /**
     * Extracts the value from the string and converts it to an integer
     *
     * @param strNumber The the string value.
     *
     * @return The integer value from the string. zero if an error occured.
     */
    public static int readInt(String strNumber) {
        try {
            return (Integer.valueOf(strNumber).intValue());
        } catch (Exception e) {
            //Log.log("ERROR: Could not convert string '" + strNumber +"' to int. Returning zero");
            return 0;
        }
    }

    /**
     * Extracts the float value from a string.
     *
     * @param strNumber The string with the float value.
     *
     * @return The float value. zero of an error occured.
     */
    public static float readFloat(String strNumber) {
        try {
            double dValue = Float.valueOf(strNumber).floatValue();

            return (float) dValue;
        } catch (Exception e) {
            //Log.log("ERROR: Could not convert string '" + strNumber + "' to float. Returning zero");
            return 0f;
        }
    }

    /**
     * Gets the string behind a separator. This is the string from the
     * separator to the end of the line.
     *
     * @param strMain The string containing the separator.
     * @param strSub DOCUMENT ME!
     *
     * @return The string behind the separator.
     *
     * @throws Exception An exception in thrown if no separator can be found.
     */
    public static String getStringBehind(String strMain, String strSub)
        throws Exception {
        return strMain.substring(strMain.indexOf(strSub) + strSub.length())
                      .trim();
    }
}
