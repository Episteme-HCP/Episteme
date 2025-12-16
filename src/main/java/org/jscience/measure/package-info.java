/**
 * Provides units and measures for physical quantities with dimensional
 * analysis.
 * <p>
 * This package implements a comprehensive units framework enabling type-safe
 * dimensional analysis at compile time. It supports:
 * </p>
 * <ul>
 * <li><b>Type Safety</b> - Quantities are parameterized by their dimension</li>
 * <li><b>Unit Conversions</b> - Automatic conversion between compatible
 * units</li>
 * <li><b>Dimensional Analysis</b> - Compile-time checking of dimensional
 * consistency</li>
 * <li><b>Arbitrary Precision</b> - Uses
 * {@link org.jscience.mathematics.numbers.real.Real} for values</li>
 * </ul>
 * 
 * <h2>Core Concepts</h2>
 * 
 * <h3>Quantities</h3>
 * <p>
 * A {@link Quantity} combines a numerical value with a {@link Unit}:
 * </p>
 * 
 * <pre>{@code
 * Quantity<Length> distance = Quantities.create(100, Units.METER);
 * }</pre>
 * 
 * <h3>Units</h3>
 * <p>
 * {@link Unit}s define how quantities are measured:
 * </p>
 * 
 * <pre>{@code
 * Unit<Length> meter = Units.METER;
 * Unit<Length> kilometer = Units.KILOMETER;
 * 
 * Quantity<Length> km = distance.to(kilometer); // Conversion
 * }</pre>
 * 
 * <h3>Dimensions</h3>
 * <p>
 * {@link Dimension}s ensure dimensional consistency:
 * </p>
 * 
 * <pre>{@code
 * // OK - same dimension
 * Quantity<Length> total = meter1.add(meter2);
 * 
 * // Compile error - incompatible dimensions
 * // Quantity<Length> invalid = length.add(mass); // Won't compile
 * }</pre>
 * 
 * <h2>Example: Velocity Calculation</h2>
 * 
 * <pre>{@code
 * import org.jscience.measure.*;
 * import org.jscience.measure.quantity.*;
 * import static org.jscience.measure.Units.*;
 * 
 * // Create quantities
 * Quantity<Length> distance = Quantities.create(100, METER);
 * Quantity<Time> time = Quantities.create(10, SECOND);
 * 
 * // Dimensional analysis - automatic!
 * Quantity<Velocity> speed = distance.divide(time);  // 10 m/s
 * 
 * // Unit conversion
 * Quantity<Velocity> kmh = speed.to(KILOMETER_PER_HOUR);  // 36 km/h
 * }</pre>
 * 
 * <h2>Comparison with JSR-385</h2>
 * <p>
 * This package is inspired by JSR-385 (Units of Measurement API) but uses
 * JScience's {@link org.jscience.mathematics.numbers.real.Real} for arbitrary
 * precision instead of primitive doubles. API compatibility is maintained
 * where possible for familiarity.
 * </p>
 * 
 * @see Quantity
 * @see Unit
 * @see Dimension
 * @see org.jscience.measure.quantity
 * 
 * @since 2.0
 */
package org.jscience.measure;
