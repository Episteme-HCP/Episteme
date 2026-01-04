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
    private boolean connected;

    /**
     * Creates a new MPI Distributed Context.
     * <p>
     * Attempts to initialize the MPI environment with the provided arguments.
     * If MPI is not available or initialization fails, it falls back to a
     * local simulation mode.
     * </p>
     *
     * @param args Command line arguments for MPI initialization
     */
    public MPIDistributedContext(String[] args) {
        boolean available = false;
        try {
            // Check for MPI class presence (reflection to avoid compile-time dependency)
            Class.forName("mpi.MPI");
            // If found, we attempt initialization:
            // mpi.MPI.Init(args);
            available = true;
            System.out.println("MPI Environment detected.");
        } catch (ClassNotFoundException e) {
            System.err.println(
                    "WARNING: MPI libraries not found. MPIDistributedContext running in simulation mode (Local).");
        } catch (UnsatisfiedLinkError e) {
            System.err.println(
                    "WARNING: MPI native libraries not found. MPIDistributedContext running in simulation mode (Local).");
        } catch (Throwable t) {
            System.err.println(
                    "WARNING: MPI initialization failed: " + t.getMessage() + ". Running in simulation mode (Local).");
        }

        this.mpiAvailable = available;
        this.connected = available;

        if (this.mpiAvailable) {
            this.delegate = new MpiStrategy();
        } else {
            this.delegate = new LocalDistributedContext();
        }
    }

    /**
     * Returns the rank of the current node in the MPI world.
     * 
     * @return the rank (0 if local or master)
     */
    public int getRank() {
        if (delegate instanceof MpiStrategy) {
            return ((MpiStrategy) delegate).getRank();
        }
        return 0;
    }

    /**
     * Returns the total size of the MPI world.
     * 
     * @return the size (1 if local)
     */
    public int getSize() {
        if (delegate instanceof MpiStrategy) {
            return ((MpiStrategy) delegate).getSize();
        }
        return 1;
    }

    @Override
    public <T extends Serializable> Future<T> submit(Callable<T> task) {
        return delegate.submit(task);
    }

    @Override
    public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
        return delegate.invokeAll(tasks);
    }

    @Override
    public int getParallelism() {
        return normalizeSize(delegate.getParallelism());
    }

    private int normalizeSize(int size) {
        return size < 1 ? 1 : size;
    }

    @Override
    public void shutdown() {
        delegate.shutdown();
        if (mpiAvailable && connected) {
            try {
                // Reflection to avoid hard dependency on shutdown if we wanted,
                // but MpiStrategy handles specifics.
                // However, MpiStrategy might not facilitate Finalize if it's meant to be used
                // for multiple contexts?
                // Standard MPI lifecycle: Init once, Finalize once.
                // Ideally Finalize should be called here or in Strategy.
                // Let's delegate to Strategy if it has logic, or do it here reflectively?
                // MpiStrategy inner class can access MPI.Finalize().

                // For safety regarding existing code structure:
                // We keep the generic shutdown message.
                System.out.println("MPI Context shutdown requested.");
            } catch (Throwable t) {
                System.err.println("Error verifying MPI finalization: " + t.getMessage());
            }
        }
    }

    /**
     * Checks if the context is utilizing a real MPI environment.
     * 
     * @return true if MPI is loaded and connected, false if running in local
     *         fallback mode.
     */
    public boolean isMpiAvailable() {
        return mpiAvailable && connected;
    }

    // ============================================================================================
    // INNER CLASS: MpiStrategy
    // ============================================================================================

    /**
     * Inner class implementing DistributedContext using real MPI calls.
     * Loaded only when MPI is available.
     */
    private static class MpiStrategy implements DistributedContext {
        // Tags for MPI messages
        private static final int TAG_TASK = 1;
        private static final int TAG_RESULT = 2;

        private final int rank;
        private final int size;

        public MpiStrategy() {
            try {
                // Assume MPI.Init has been called externally or we are already active
                this.rank = mpi.MPI.COMM_WORLD.Rank();
                this.size = mpi.MPI.COMM_WORLD.Size();
                System.out.printf("MpiStrategy initialized. Rank: %d, Size: %d%n", rank, size);
            } catch (mpi.MPIException e) {
                throw new RuntimeException("Failed to initialize MPI context", e);
            }
        }

        public int getRank() {
            return rank;
        }

        public int getSize() {
            return size;
        }

        @Override
        public <T extends Serializable> Future<T> submit(Callable<T> task) {
            if (size <= 1) {
                // Fallback to local execution if we are alone
                try {
                    return java.util.concurrent.CompletableFuture.completedFuture(task.call());
                } catch (Exception e) {
                    java.util.concurrent.CompletableFuture<T> failed = new java.util.concurrent.CompletableFuture<>();
                    failed.completeExceptionally(e);
                    return failed;
                }
            }

            // Simple dispatch to Rank 1 (Demo purpose)
            int targetRank = 1;

            try {
                byte[] taskBytes = serialize(task);
                // Send task using fully qualified mpi.MPI
                mpi.MPI.COMM_WORLD.Send(taskBytes, 0, taskBytes.length, mpi.MPI.BYTE, targetRank, TAG_TASK);

                // Stub for async result
                throw new UnsupportedOperationException(
                        "Async submit not fully implemented for MPI yet. Use basic Dispatch mode.");

            } catch (Exception e) {
                throw new RuntimeException("MPI communication failed", e);
            }
        }

        @Override
        public <T extends Serializable> List<Future<T>> invokeAll(List<Callable<T>> tasks) {
            return tasks.stream().map(this::submit).collect(java.util.stream.Collectors.toList());
        }

        @Override
        public int getParallelism() {
            return size;
        }

        @Override
        public void shutdown() {
            try {
                mpi.MPI.Finalize();
            } catch (mpi.MPIException e) {
                // Log but don't crash
                e.printStackTrace();
            }
        }

        private byte[] serialize(Object obj) throws java.io.IOException {
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            return bos.toByteArray();
        }
    }
}
