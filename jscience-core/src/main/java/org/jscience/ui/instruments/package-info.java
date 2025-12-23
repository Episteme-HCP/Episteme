/**
 * Physical measurement instrument visualization components.
 * 
 * <p>
 * This package provides a hierarchy of reusable instrument visualizations
 * for displaying physical quantities in scientific applications.
 * </p>
 * 
 * <h2>Available Instruments</h2>
 * <ul>
 * <li>{@link org.jscience.ui.instruments.PhysicalMeasureInstrument} - Base
 * class for all instruments</li>
 * <li>{@link org.jscience.ui.instruments.Thermometer} - Temperature display
 * (mercury-style)</li>
 * <li>{@link org.jscience.ui.instruments.PressureGauge} - Analog dial pressure
 * gauge</li>
 * <li>{@link org.jscience.ui.instruments.Oscilloscope} - Waveform/time-series
 * display</li>
 * </ul>
 * 
 * <h2>Usage Example</h2>
 * 
 * <pre>{@code
 * // Create a thermometer
 * Thermometer therm = new Thermometer();
 * therm.setTemperatureRange(-20, 100);
 * 
 * // Set the current temperature (in Kelvin)
 * therm.setValue(Quantities.create(300, Units.KELVIN));
 * 
 * // Add to scene
 * root.getChildren().add(therm.getView());
 * }</pre>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
package org.jscience.ui.instruments;
