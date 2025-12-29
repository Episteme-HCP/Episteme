/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.devices;

import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import javafx.scene.Node;

/**
 * Base class for physical measurement instruments.
 * Provides a common interface for all instrument visualizations.
 *
 * <p>
 * Instruments display real-time physical quantities with appropriate
 * units and visual representations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class PhysicalMeasureInstrument<Q extends Quantity<Q>> {

    protected String name;
    protected Unit<Q> displayUnit;
    protected Quantity<Q> currentValue;
    protected Quantity<Q> minValue;
    protected Quantity<Q> maxValue;
    protected boolean enabled = true;

    /**
     * Creates a new instrument with the specified name.
     * 
     * @param name the display name of this instrument
     */
    public PhysicalMeasureInstrument(String name) {
        this.name = name;
    }

    /**
     * Gets the display name of this instrument.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the unit used for display.
     */
    public void setDisplayUnit(Unit<Q> unit) {
        this.displayUnit = unit;
    }

    /**
     * Gets the current measured value.
     */
    public Quantity<Q> getValue() {
        return currentValue;
    }

    /**
     * Sets the current value displayed by this instrument.
     */
    public void setValue(Quantity<Q> value) {
        this.currentValue = value;
        onValueChanged(value);
    }

    /**
     * Sets the valid range for this instrument.
     */
    public void setRange(Quantity<Q> min, Quantity<Q> max) {
        this.minValue = min;
        this.maxValue = max;
    }

    /**
     * Called when the value changes. Override to update visualization.
     */
    protected abstract void onValueChanged(Quantity<Q> newValue);

    /**
     * Returns the JavaFX node representing this instrument for display.
     */
    public abstract Node getView();

    /**
     * Enables or disables this instrument.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks if this instrument is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Resets the instrument to its default state.
     */
    public abstract void reset();
}
