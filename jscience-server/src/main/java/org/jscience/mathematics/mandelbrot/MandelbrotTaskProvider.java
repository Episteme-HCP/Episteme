package org.jscience.mathematics.mandelbrot;

import org.jscience.distributed.DistributedTask;
import org.jscience.distributed.TaskProvider;
import org.jscience.distributed.TaskRegistry.PrecisionMode;

/**
 * Provider for Mandelbrot tasks, supporting both Primitive (double) and Real (arbitrary precision) modes.
 */
public class MandelbrotTaskProvider implements TaskProvider<MandelbrotTask, MandelbrotTask> {

    @Override
    public DistributedTask<MandelbrotTask, MandelbrotTask> createTask() {
         // Default to Primitive for performance unless Real is explicitly requested
        return new MandelbrotTask();
    }

    @Override
    public DistributedTask<MandelbrotTask, MandelbrotTask> createTask(PrecisionMode mode) {
        if (mode == PrecisionMode.REAL) {
            return new RealMandelbrotTask();
        }
        return new MandelbrotTask();
    }

    @Override
    public String getTaskType() {
        return "MANDELBROT";
    }
    
    @Override
    public boolean supportsGPU() {
        return true; // Real implementation supports GPU via Real context
    }
}
