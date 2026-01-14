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

/*
 * AtlasPreferences.java
 *
 * Created on December 31, 2004, 1:56 AM
 */

package org.jscience.physics.solids;

/**
 * Reference class of constansts used by ATLAS.
 * <p/>
 * Evenutally, these values will be able to be set by a prefrences file
 * or something...
 *
 * @author Wegge
 */
public class AtlasPreferences {

    private static double zeroTolerance = .00001;
    private static double singularityTolerance = 1e-10;
    private int integrationLevel = 3;
    private int loggingLevel = 1;

    public AtlasPreferences() {
    }

    public AtlasPreferences(int intLevel, int logLevel) {
        integrationLevel = intLevel;
        loggingLevel = logLevel;
    }


    /**
     * Sets the definition of "zero".  This tolerance is used to determine
     * if spatial entities are equivalent.  For example, if the angle between two vectors is
     * less than this value, then they are considered colinear.  Default value is .00001.
     */
    public static void setZeroTolerance(double tolerance) {
        zeroTolerance = tolerance;
    }

    /**
     * Returns the definition of "zero".  This tolerance is used to determine
     * if spatial entities are equivalent.  For example, if the angle between two vectors is
     * less than this value, then they are considered colinear.  Default value is .00001.
     */
    public static double getZeroTolerance() {
        return zeroTolerance;
    }

    /**
     * Sets the definition of a singularity for a singluatory matrix.
     * Default value is 1e-10.
     */
    public static void setSingularityTolerance(double tolerance) {
        singularityTolerance = tolerance;
    }

    /**
     * Returns the definition of a singularity for a singluatory matrix.
     * Default value is 1e-10.
     */
    public static double getSingularityTolerance() {
        return singularityTolerance;
    }

    /**
     * @return Returns the loggingLevel.
     */
    public int getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * @param loggingLevel Sets the desired loggingLevel.
     *                     1 equals info
     *                     2 equals warn
     *                     3 equals error
     *                     4 equals fatal
     */
    public void setLoggingLevel(int loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    /**
     * @return Returns the integrationLevel.
     */
    public int getIntegrationLevel() {
        return integrationLevel;
    }

    /**
     * @param integrationLevel The integrationLevel to set.
     */
    public void setIntegrationLevel(int integrationLevel) {
        this.integrationLevel = integrationLevel;
	}
}
