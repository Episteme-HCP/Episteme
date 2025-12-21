/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.physics.classical.waves.optics;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an optical system consisting of multiple optical elements.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpticalSystem {

    private List<OpticalElement> elements = new ArrayList<>();

    public void addElement(OpticalElement element) {
        elements.add(element);
    }

    public List<OpticalElement> getElements() {
        return elements;
    }

    /**
     * Traces a ray through the entire optical system sequentially.
     * 
     * @param inputRay The input light ray.
     * @return The final output ray after passing through all elements, or null if
     *         lost.
     */
    public LightRay traceSystem(LightRay inputRay) {
        LightRay currentRay = inputRay;
        for (OpticalElement element : elements) {
            currentRay = element.trace(currentRay);
            if (currentRay == null) {
                return null;
            }
        }
        return currentRay;
    }
}
