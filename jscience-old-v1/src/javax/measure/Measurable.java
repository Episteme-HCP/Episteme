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

package javax.measure;

import javax.measure.quantity.Quantity;
import javax.measure.unit.Unit;

/**
 * <p> This interface represents the measurable, countable, or comparable 
 *     property or aspect of a thing.</p>
 *     
 * <p> Implementing instances are typically the result of a measurement:[code]
 *         Measurable<Mass> weight = Measure.valueOf(180.0, POUND);
 *     [/code]
 *     They can also be created from custom classes:[code]
 *     class Delay implements Measurable<Duration> {
 *          private long nanoSeconds; // Implicit internal unit.
 *          public double doubleValue(Unit<Velocity> unit) { ... }
 *          public long longValue(Unit<Velocity> unit) { ... }
 *     }
 *     Thread.wait(new Delay(24, HOUR)); // Assuming Thread.wait(Measurable<Duration>) method.
 *     [/code]</p>
 *     
 * <p> Although measurable instances are for the most part scalar quantities; 
 *     more complex implementations (e.g. vectors, data set) are allowed as 
 *     long as an aggregate magnitude can be determined. For example:[code]
 *     class Velocity3D implements Measurable<Velocity> {
 *          private double x, y, z; // Meter per seconds.
 *          public double doubleValue(Unit<Velocity> unit) { ... } // Returns vector norm.
 *          ... 
 *     }
 *     class Sensors<Q extends Quantity> extends Measure<double[], Q> {
 *          public doubleValue(Unit<Q> unit) { ... } // Returns median value. 
 *          ...
 *     } [/code]</p>
 *     
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 4.1, June 8, 2007
 */
public interface Measurable<Q extends Quantity> extends Comparable<Measurable<Q>> {
    
    /**
     * Returns the value of this measurable stated in the specified unit as 
     * a <code>double</code>. If the measurable has too great a magnitude to 
     * be represented as a <code>double</code>, it will be converted to 
     * <code>Double.NEGATIVE_INFINITY</code> or
     * <code>Double.POSITIVE_INFINITY</code> as appropriate.
     * 
     * @param unit the unit in which this measurable value is stated.
     * @return the numeric value after conversion to type <code>double</code>.
     */
    double doubleValue(Unit<Q> unit);

    /**
     * Returns the estimated integral value of this measurable stated in 
     * the specified unit as a <code>long</code>. 
     * 
     * <p> Note: This method differs from the <code>Number.longValue()</code>
     *           in the sense that the closest integer value is returned 
     *           and an ArithmeticException is raised instead
     *           of a bit truncation in case of overflow (safety critical).</p> 
     * 
     * @param unit the unit in which the measurable value is stated.
     * @return the numeric value after conversion to type <code>long</code>.
     * @throws ArithmeticException if this quantity cannot be represented 
     *         as a <code>long</code> number in the specified unit.
     */
    long longValue(Unit<Q> unit) throws ArithmeticException;
    
}
