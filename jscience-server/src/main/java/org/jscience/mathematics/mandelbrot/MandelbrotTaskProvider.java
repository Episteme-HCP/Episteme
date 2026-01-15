/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
    public DistributedTask<MandelbrotTask, MandelbrotTask> createTask(TaskRegistry.PrecisionMode mode) {
        if (mode == TaskRegistry.PrecisionMode.REAL) {
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
