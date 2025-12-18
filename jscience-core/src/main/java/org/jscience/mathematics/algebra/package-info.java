/**
 * Core algebraic structures following mathematical hierarchy.
 * <p>
 * This package defines fundamental algebraic concepts:
 * </p>
 * 
 * <pre>
 * Set (membership)
 *   ↓
 * Magma (binary operation)
 *   ↓
 * Group (identity + inverse)
 *   ↓
 * AbelianGroup (commutativity)
 *   ↓
 * Ring (second operation: multiplication)
 *   ↓
 * Field (division)
 * </pre>
 * 
 * <h2>Design Philosophy</h2>
 * <p>
 * These interfaces respect pure mathematical definitions while enabling
 * practical implementations. All structures are generic, allowing:
 * </p>
 * <ul>
 * <li>Double precision for performance (default)</li>
 * <li>BigDecimal for arbitrary precision</li>
 * <li>GPU types for acceleration</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * // Real numbers form a field
 * Field<Double> reals = RealField.getInstance();
 * 
 * Double a = 5.0;
 * Double b = 3.0;
 * 
 * // Field operations
 * Double sum = reals.add(a, b); // 8.0
 * Double product = reals.multiply(a, b); // 15.0
 * Double quotient = reals.divide(a, b); // 1.666...
 * }</pre>
 * 
 * @see org.jscience.mathematics.algebra.Set
 * @see org.jscience.mathematics.algebra.Field
 * @since 1.0
 */
package org.jscience.mathematics.algebra;
