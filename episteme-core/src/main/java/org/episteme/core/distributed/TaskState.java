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
 * Simplifies synchronization and reduces boilerplate in scientific tasks.
 */
public class TaskState<T extends Serializable> implements Serializable {

    private T realState;
    private double[] doubleBuffer;
    private float[] floatBuffer;
    
    private final Function<T, double[]> toDoubleMapper;
    private final Function<double[], T> fromDoubleMapper;
    private final Function<T, float[]> toFloatMapper;
    private final Function<float[], T> fromFloatMapper;

    public TaskState(T initialState, 
                     Function<T, double[]> toDouble, Function<double[], T> fromDouble,
                     Function<T, float[]> toFloat, Function<float[], T> fromFloat) {
        this.realState = initialState;
        this.toDoubleMapper = toDouble;
        this.fromDoubleMapper = fromDouble;
        this.toFloatMapper = toFloat;
        this.fromFloatMapper = fromFloat;
    }

    public void syncTo(TaskRegistry.PrecisionMode mode) {
        switch (mode) {
            case DOUBLE -> {
                if (doubleBuffer == null) doubleBuffer = toDoubleMapper.apply(realState);
            }
            case FLOAT -> {
                if (floatBuffer == null) floatBuffer = toFloatMapper.apply(realState);
            }
            case REAL -> {
                if (realState == null) {
                    if (doubleBuffer != null) realState = fromDoubleMapper.apply(doubleBuffer);
                    else if (floatBuffer != null) realState = fromFloatMapper.apply(floatBuffer);
                }
            }
        }
    }

    public void syncFrom(TaskRegistry.PrecisionMode mode) {
        switch (mode) {
            case DOUBLE -> realState = fromDoubleMapper.apply(doubleBuffer);
            case FLOAT -> realState = fromFloatMapper.apply(floatBuffer);
            case REAL -> {
                doubleBuffer = null; // Invalidate buffers
                floatBuffer = null;
            }
        }
    }

    public T getReal() { return realState; }
    public double[] getDouble() { return doubleBuffer; }
    public float[] getFloat() { return floatBuffer; }
    
    public void setReal(T state) { this.realState = state; }
}
