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

package javax.measure.quantity;

import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 * This interface represents the volume of fluid passing a point in a system
 * per unit of time. The system unit for this quantity is "m³/s" 
 * (cubic meter per second).
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 3.0, March 2, 2006
 * @see <a href="http://en.wikipedia.org/wiki/Rate_of_fluid_flow">
 *      Wikipedia: Volumetric Flow Rate</a>
 */
public interface VolumetricFlowRate extends Quantity {

    /**
     * Holds the SI unit (Système International d'Unités) for this quantity.
     */
    @SuppressWarnings("unchecked")
    public final static Unit<VolumetricFlowRate> UNIT 
       = (Unit<VolumetricFlowRate>) SI.METRE.pow(3).divide(SI.SECOND);
}
