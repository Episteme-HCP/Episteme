/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.server.server.tasks.mathematics.mandelbrot;

import org.episteme.core.distributed.DistributedTask;
import org.episteme.core.distributed.TaskProvider;
import org.episteme.core.distributed.TaskRegistry;
import com.google.auto.service.AutoService;

/**
 * Provider for Mandelbrot tasks.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@AutoService(TaskProvider.class)
public class MandelbrotTaskProvider implements TaskProvider<MandelbrotTask, MandelbrotTask> {

    @Override
    public DistributedTask<MandelbrotTask, MandelbrotTask> createTask() {
        return new MandelbrotTask();
    }

    @Override
    public DistributedTask<MandelbrotTask, MandelbrotTask> createTask(TaskRegistry.PrecisionMode mode) {
        MandelbrotTask task = new MandelbrotTask();
        task.setMode(mode);
        return task;
    }

    @Override
    public String getTaskType() {
        return "MANDELBROT";
    }
    
    @Override
    public boolean supportsGPU() {
        return true;
    }
}
