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
