/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.distributed;

import org.episteme.core.mathematics.numbers.real.Real;
import java.io.Serializable;
import java.util.function.Function;

/**
 * Manages data state across different precision modes (FLOAT, DOUBLE, REAL).
 * 
 * <p>
 * This class ensures that modifications in one precision mode are correctly
 * propagated to others when needed, while minimizing expensive conversions.
 * </p>
 * 
 * @param <T> the type of the high-precision (REAL) state
 */
public class TaskState<T extends Serializable> implements Serializable {

    private T realState;
    private double[] doubleBuffer;
    private float[] floatBuffer;
    
    private final Function<T, double[]> toDoubleMapper;
    private final Function<double[], T> fromDoubleMapper;
    private final Function<T, float[]> toFloatMapper;
    private final Function<float[], T> fromFloatMapper;

    private TaskRegistry.PrecisionMode authority;

    public TaskState(T initialState, 
                     Function<T, double[]> toDouble, Function<double[], T> fromDouble,
                     Function<T, float[]> toFloat, Function<float[], T> fromFloat) {
        this.realState = initialState;
        this.toDoubleMapper = toDouble;
        this.fromDoubleMapper = fromDouble;
        this.toFloatMapper = toFloat;
        this.fromFloatMapper = fromFloat;
        this.authority = TaskRegistry.PrecisionMode.REAL;
    }

    /**
     * Synchronizes all buffers to the target mode.
     * 
     * @param target the mode that needs to be up-to-date
     */
    public void syncTo(TaskRegistry.PrecisionMode target) {
        if (target == authority) return;

        // Ensure authority is REAL before switching to another primitive
        if (authority != TaskRegistry.PrecisionMode.REAL) {
            syncFrom(authority);
        }

        switch (target) {
            case DOUBLE -> {
                doubleBuffer = toDoubleMapper.apply(realState);
                authority = TaskRegistry.PrecisionMode.DOUBLE;
            }
            case FLOAT -> {
                floatBuffer = toFloatMapper.apply(realState);
                authority = TaskRegistry.PrecisionMode.FLOAT;
            }
            case REAL -> {
                // Already synced via syncFrom(authority) above
            }
        }
    }

    /**
     * Marks the current buffer as the new authority and syncs back to Real.
     * Call this after manual modifications to a primitive buffer.
     * 
     * @param mode the mode that was modified
     */
    public void syncFrom(TaskRegistry.PrecisionMode mode) {
        switch (mode) {
            case DOUBLE -> {
                realState = fromDoubleMapper.apply(doubleBuffer);
                floatBuffer = null; // Invalidate float
                authority = TaskRegistry.PrecisionMode.REAL;
            }
            case FLOAT -> {
                realState = fromFloatMapper.apply(floatBuffer);
                doubleBuffer = null; // Invalidate double
                authority = TaskRegistry.PrecisionMode.REAL;
            }
            case REAL -> {
                doubleBuffer = null;
                floatBuffer = null;
                authority = TaskRegistry.PrecisionMode.REAL;
            }
        }
    }

    public T getReal() { 
        if (authority != TaskRegistry.PrecisionMode.REAL) syncFrom(authority);
        return realState; 
    }

    public double[] getDouble() { 
        if (authority != TaskRegistry.PrecisionMode.DOUBLE) syncTo(TaskRegistry.PrecisionMode.DOUBLE);
        return doubleBuffer; 
    }

    public float[] getFloat() { 
        if (authority != TaskRegistry.PrecisionMode.FLOAT) syncTo(TaskRegistry.PrecisionMode.FLOAT);
        return floatBuffer; 
    }
    
    public void setReal(T state) { 
        this.realState = state; 
        this.doubleBuffer = null;
        this.floatBuffer = null;
        this.authority = TaskRegistry.PrecisionMode.REAL;
    }

    public void setDouble(double[] buffer) {
        this.doubleBuffer = buffer;
        this.floatBuffer = null;
        this.realState = null;
        this.authority = TaskRegistry.PrecisionMode.DOUBLE;
    }

    public void setFloat(float[] buffer) {
        this.floatBuffer = buffer;
        this.doubleBuffer = null;
        this.realState = null;
        this.authority = TaskRegistry.PrecisionMode.FLOAT;
    }
}
