/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.device.transducers;

import org.episteme.core.device.Actuator;
import org.episteme.core.device.Sensor;
import org.episteme.core.measure.Quantity;
import org.episteme.core.measure.quantity.Angle;

/**
 * Interface for telescopes.
 * A telescope is both a sensor (for observing coordinates or images)
 * and an actuator (for slewing to specific coordinates).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Telescope extends Sensor<Angle>, Actuator<String> {

    /**
     * Slews the telescope to the given Right Ascension and Declination.
     */
    void slewTo(Quantity<Angle> ra, Quantity<Angle> dec);

    /**
     * Synchronizes the telescope to the given Right Ascension and Declination.
     */
    void syncTo(Quantity<Angle> ra, Quantity<Angle> dec);

    /**
     * Returns the current Right Ascension.
     */
    Quantity<Angle> getRightAscension();

    /**
     * Returns the current Declination.
     */
    Quantity<Angle> getDeclination();

    /**
     * Aborts any ongoing slew operation.
     */
    void abort();

    /**
     * Captures a 2D image/intensity map from the current position.
     * @return 2D array of intensity values.
     */
    double[][] getImage();

    /**
     * Returns the top-left coordinate of the current field of view.
     */
    Quantity<Angle>[] getTopLeftFOV();

    /**
     * Returns the bottom-right coordinate of the current field of view.
     */
    Quantity<Angle>[] getBottomRightFOV();
}
