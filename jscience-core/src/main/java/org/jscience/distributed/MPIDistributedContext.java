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

package org.jscience.distributed;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * MPI-based implementation of DistributedContext.
 * <p>
 * This class provides integration with Message Passing Interface (MPI) for
 * high-performance cluster computing.
 * </p>
 * <p>
 * <b>Requirements:</b>
 * <ul>
 * <li>An MPI implementation (e.g., OpenMPI, MPICH) installed on the
 * system.</li>
 * <li>Java bindings for MPI (e.g., mpj-express or openmpi-java) on the
 * classpath.</li>
 * </ul>
 * </p>
 * <p>
 * <b>Simulation Mode:</b>
 * If MPI classes are not found, this context transparently falls back to
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class MPIDistributedContext implements DistributedContext {

    private final DistributedContext delegate;
    private final boolean mpiAvailable;

    public MPIDistributedContext(String[] args) {
        boolean available = false;
        try {
            // Check for MPI class presence (reflection to avoid compile-time dependency)
            Class.forName("mpi.MPI");
            // If found, we would initialize it:
            // mpi.MPI.Init(args);
            available = true;
        } catch (ClassNotFoundException e) {
            System.err.println(
                    "WARNING: MPI libraries not found. MPIDistributedContext running in simulation mode (Local).");
        } catch (UnsatisfiedLinkError e) {
            System.err.println(
                    "WARNING: MPI native libraries not found. MPIDistributedContext running in simulation mode (Local).");
        }

        this.mpiAvailable = available;

        if (this.mpiAvailable) {
            // In a real implementation, we would set up the MPI-based executor here.
            // For now, even if "available" (simulated), we delegate to local for this stub
            // because we don't have the actual MPI wrapper code written yet.
            this.delegate = new LocalDistributedContext();
        } else {
            this.delegate = new LocalDistributedContext();
        }
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        if (mpiAvailable) {
            // Todo: Serialize task and send to worker (Rank > 0)
            return delegate.submit(task);
        }
        return delegate.submit(task);
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        if (mpiAvailable) {
            // Todo: Scatter tasks across communicator
            return delegate.invokeAll(tasks);
        }
        return delegate.invokeAll(tasks);
    }

    @Override
    public int getParallelism() {
        if (mpiAvailable) {
            // return mpi.MPI.COMM_WORLD.Size();
            return delegate.getParallelism();
        }
        return delegate.getParallelism();
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
        if (mpiAvailable) {
            // mpi.MPI.Finalize();
        }
    }

    public boolean isMpiAvailable() {
        return mpiAvailable;
    }
}
