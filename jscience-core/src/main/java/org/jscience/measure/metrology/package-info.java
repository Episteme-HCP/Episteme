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
 * Provides metrology support for measurements with uncertainty.
 * <p>
 * This package implements the science of measurement following ISO/IEC Guide
 * 98-3
 * (Guide to the Expression of Uncertainty in Measurement - GUM). It provides
 * tools
 * for handling measurement uncertainty, statistical analysis of repeated
 * measurements,
 * and uncertainty budgets.
 * </p>
 * 
 * <h2>Core Concepts</h2>
 * 
 * <h3>Measured Quantities</h3>
 * <p>
 * A {@link org.jscience.measure.metrology.MeasuredQuantity} represents a
 * measurement
 * with its associated uncertainty:
 * </p>
 * 
 * <pre>{@code
 * // Length measurement: 10.0 ± 0.2 m (95% confidence)
 * MeasuredQuantity<Length> length = MeasuredQuantities.create(
 *         Quantities.create(10.0, Units.METER),
 *         Quantities.create(0.2, Units.METER),
 *         0.95 // 95% confidence
 * );
 * }</pre>
 * 
 * <h3>Uncertainty Propagation</h3>
 * <p>
 * Uncertainty propagates automatically through mathematical operations:
 * </p>
 * 
 * <pre>{@code
 * MeasuredQuantity<Length> L = MeasuredQuantities.create(10.0, 0.1, METER);
 * MeasuredQuantity<Length> W = MeasuredQuantities.create(5.0, 0.05, METER);
 * 
 * // Area = L × W, uncertainty propagates automatically!
 * MeasuredQuantity<Area> area = L.multiply(W);
 * // Result: 50.0 ± 0.7 m² (using relative uncertainty formula)
 * }</pre>
 * 
 * <h3>Statistical Analysis</h3>
 * <p>
 * {@link org.jscience.measure.metrology.MeasurementSeries} analyzes repeated
 * measurements:
 * </p>
 * 
 * <pre>{@code
 * MeasurementSeries<Mass> series = new MeasurementSeries<>();
 * series.addMeasurement(Quantities.create(100.2, GRAM));
 * series.addMeasurement(Quantities.create(100.1, GRAM));
 * series.addMeasurement(Quantities.create(100.3, GRAM));
 * 
 * // Get mean with confidence interval
 * MeasuredQuantity<Mass> result = series.getConfidenceInterval(0.95);
 * // Result: 100.2 ± 0.1 g (95%)
 * }</pre>
 * 
 * <h3>Uncertainty Budgets</h3>
 * <p>
 * {@link org.jscience.measure.metrology.UncertaintyBudget} combines multiple
 * uncertainty sources following GUM methodology:
 * </p>
 * 
 * <pre>{@code
 * UncertaintyBudget<Mass> budget = new UncertaintyBudget<>();
 * 
 * // Type A (statistical)
 * budget.addSource("Repeatability",
 *         Quantities.create(0.05, GRAM),
 *         UncertaintyType.TYPE_A);
 * 
 * // Type B (systematic)
 * budget.addSource("Calibration",
 *         Quantities.create(0.1, GRAM),
 *         UncertaintyType.TYPE_B);
 * 
 * // Combined: √(0.05² + 0.1²) = 0.112 g
 * Quantity<Mass> combined = budget.getCombinedUncertainty();
 * 
 * // Expanded (k=2): 0.224 g
 * Quantity<Mass> expanded = budget.getExpandedUncertainty(2.0);
 * }</pre>
 * 
 * <h2>Uncertainty Propagation Formulas</h2>
 * <p>
 * For independent measurements with standard uncertainties σ:
 * </p>
 * <ul>
 * <li><b>Addition/Subtraction</b>: z = x ± y → σ_z = √(σ_x² + σ_y²)</li>
 * <li><b>Multiplication/Division</b>: z = x × y or x / y → σ_z/z = √((σ_x/x)² +
 * (σ_y/y)²)</li>
 * <li><b>Power</b>: z = x^n → σ_z/z = |n| × (σ_x/x)</li>
 * <li><b>General</b>: σ_z² = Σ (∂z/∂x_i)² σ_i² (partial derivatives)</li>
 * </ul>
 * 
 * <h2>Confidence Levels</h2>
 * <p>
 * Common confidence levels for normal distributions:
 * </p>
 * <table>
 * <tr>
 * <th>Confidence</th>
 * <th>Coverage Factor</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>68.3%</td>
 * <td>k = 1</td>
 * <td>±1σ</td>
 * </tr>
 * <tr>
 * <td>95.4%</td>
 * <td>k = 2</td>
 * <td>±2σ (typical)</td>
 * </tr>
 * <tr>
 * <td>99.7%</td>
 * <td>k = 3</td>
 * <td>±3σ</td>
 * </tr>
 * </table>
 * 
 * <h2>Standards Compliance</h2>
 * <p>
 * This package follows international metrology standards:
 * </p>
 * <ul>
 * <li><b>ISO/IEC Guide 98-3:2008</b> - Guide to the Expression of Uncertainty
 * in Measurement (GUM)</li>
 * <li><b>JCGM 100:2008</b> - Evaluation of measurement data</li>
 * <li><b>NIST SP 1297</b> - Guidelines for Evaluating and Expressing
 * Uncertainty</li>
 * </ul>
 * 
 * <h2>Example Workflow</h2>
 * 
 * <pre>{@code
 * // 1. Take repeated measurements
 * MeasurementSeries<Temperature> series = new MeasurementSeries<>();
 * for (int i = 0; i < 10; i++) {
 *     Quantity<Temperature> temp = takeMeasurement();
 *     series.addMeasurement(temp);
 * }
 * 
 * // 2. Analyze statistics
 * Quantity<Temperature> mean = series.getMean();
 * Quantity<Temperature> stdDev = series.getStandardDeviation();
 * 
 * // 3. Build uncertainty budget
 * UncertaintyBudget<Temperature> budget = new UncertaintyBudget<>();
 * budget.addSource("Repeatability", series.getStandardError(), TYPE_A);
 * budget.addSource("Calibration", Quantities.create(0.1, KELVIN), TYPE_B);
 * budget.addSource("Drift", Quantities.create(0.05, KELVIN), TYPE_B);
 * 
 * // 4. Report final result
 * Quantity<Temperature> finalUncertainty = budget.getExpandedUncertainty(2.0);
 * MeasuredQuantity<Temperature> result = MeasuredQuantities.create(
 *         mean, finalUncertainty, 0.95);
 * 
 * System.out.println(result); // e.g., "25.0 ± 0.3 K (95%)"
 * System.out.println(budget.generateReport());
 * }</pre>
 * 
 * @see org.jscience.measure.metrology.MeasuredQuantity
 * @see org.jscience.measure.metrology.MeasurementSeries
 * @see org.jscience.measure.metrology.UncertaintyBudget
 * @see org.jscience.measure
 * 
 * @since 2.0
 */
package org.jscience.measure.metrology;
