/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.instruments;

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
 * @param <Q> the type of quantity this instrument measures
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
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
