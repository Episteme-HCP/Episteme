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
/**
 * Provides quantity types for the units and measures framework.
 * <p>
 * This package defines interfaces for physical quantities, organized by
 * dimension. Each quantity type is a marker interface extending
 * {@link org.jscience.measure.Quantity} that enables type-safe dimensional
 * analysis.
 * </p>
 * 
 * <h2>SI Base Quantities</h2>
 * <table>
 * <tr>
 * <th>Quantity</th>
 * <th>SI Unit</th>
 * <th>Symbol</th>
 * <th>Dimension</th>
 * </tr>
 * <tr>
 * <td>{@link Length}</td>
 * <td>meter</td>
 * <td>m</td>
 * <td>[L]</td>
 * </tr>
 * <tr>
 * <td>{@link Mass}</td>
 * <td>kilogram</td>
 * <td>kg</td>
 * <td>[M]</td>
 * </tr>
 * <tr>
 * <td>{@link Time}</td>
 * <td>second</td>
 * <td>s</td>
 * <td>[T]</td>
 * </tr>
 * </table>
 * 
 * <h2>Derived Quantities</h2>
 * <p>
 * Derived quantities are combinations of base quantities:
 * </p>
 * <ul>
 * <li>{@link Velocity} = Length / Time</li>
 * <li>Acceleration = Velocity / Time = Length / Time²</li>
 * <li>Force = Mass × Acceleration</li>
 * <li>Energy = Force × Length</li>
 * </ul>
 * 
 * <h2>Example Usage</h2>
 * 
 * <pre>{@code
 * import org.jscience.measure.*;
 * import org.jscience.measure.quantity.*;
 * import static org.jscience.measure.Units.*;
 * 
 * Quantity<Length> distance = Quantities.create(100, METER);
 * Quantity<Time> time = Quantities.create(10, SECOND);
 * Quantity<Velocity> speed = distance.divide(time);  // 10 m/s
 * }</pre>
 * 
 * @see org.jscience.measure.Quantity
 * @see org.jscience.measure.Unit
 * @see org.jscience.measure.Units
 * 
 * @since 2.0
 */
package org.jscience.measure.quantity;
