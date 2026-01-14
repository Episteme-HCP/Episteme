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

/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.infoset;

import java.math.BigDecimal;


/**
 * Defines the interface every GML coordinate must implement. Coordinates  (1
 * or more) comprise a coordinate tuple. Note that Coordinate doesn't have a
 * GML counterpart, because coordinates may be expressed in two different ways
 * in GML. To circumvent this duality, the Coordinate class is made
 * independent of XML, an abstract entity created and owned by Coord or
 * Coordinates GML constructs.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface Coordinate {
    /**
     * Provides access to the value of this coordinate. The value is
     * BigDecimal because it doesn't lose precision. One may obtain other
     * representations from BigDecimal.
     *
     * @return Returns the value as BigDecimal. Cannot be null.
     */
    public BigDecimal getValue();
}
