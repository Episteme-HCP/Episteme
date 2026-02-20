package org.jscience.server.physics.classical.mechanics.providers;

import org.jscience.natural.physics.classical.mechanics.nbody.NBodyProvider;
import org.jscience.core.technical.algorithm.AlgorithmProvider;
import org.jscience.core.mathematics.numbers.real.Real;
import com.google.auto.service.AutoService;
import mpi.MPI;
import mpi.Intracomm;
import mpi.MPIException;

/**
 * Distributed N-Body simulation provider using MPJ Express (MPI for Java).
 * <p>
 * This provider distributes the N-Body force computation across multiple nodes
 * using Message Passing Interface. It requires the application to be launched
 * via an MPI launcher (e.g., mpjrun) or initialized correctly.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.2
 */
@AutoService(AlgorithmProvider.class)
public class DistributedNBodyProvider implements NBodyProvider {

    @Override
    public String getName() {
        return "MPJ Distributed N-Body";
    }

    @Override
    public int getPriority() {
        return 200; // Highest priority if available
    }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("mpi.MPI");
            // Check if we are in an MPI environment (initialized)
            // Note: calling Initialized might throw if library not loaded, but forName checks classpath
            return true; 
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void computeForces(Real[] positions, Real[] masses, Real[] forces, Real G, Real softening) {
        // Convert to primitive doubles for MPI transmission
        double[] d_pos = new double[positions.length];
        double[] d_mass = new double[masses.length];
        double[] d_forces = new double[forces.length];
        
        for (int i = 0; i < positions.length; i++) d_pos[i] = positions[i].doubleValue();
        for (int i = 0; i < masses.length; i++) d_mass[i] = masses[i].doubleValue();
        
        computeForces(d_pos, d_mass, d_forces, G.doubleValue(), softening.doubleValue());
        
        for (int i = 0; i < forces.length; i++) forces[i] = Real.of(d_forces[i]);
    }

    @Override
    public void computeForces(double[] positions, double[] masses, double[] forces, double G, double softening) {
        if (!isAvailable()) throw new UnsupportedOperationException("MPI not available");

        try {
            if (!MPI.Initialized()) {
                // Attempt auto-initialization (might fail if not launched by mpjrun)
                MPI.Init(new String[0]);
            }

            Intracomm comm = MPI.COMM_WORLD;
            int rank = comm.Rank();
            int size = comm.Size();
            int n = masses.length;

            // Broadcast positions and masses to all nodes (naive approach for simplicity)
            // In a real optimized version, we might distribute particles cyclically.
            // Here: All-to-All replication of data, distributed computation of forces.
            
            // Broadcast positions and masses
            comm.Bcast(positions, 0, n * 3, MPI.DOUBLE, 0); // positions is 3*n
            comm.Bcast(masses, 0, n, MPI.DOUBLE, 0);

            // Divide work: Each rank computes forces for a subset of particles
            int chunkSize = n / size;
            int start = rank * chunkSize;
            int end = (rank == size - 1) ? n : start + chunkSize;

            // Local force array (partial)
            double[] localForces = new double[n * 3]; // We could optimize to only store chunk

            // Compute forces for assigned chunk
            for (int i = start; i < end; i++) {
                int idx_i = i * 3;
                double xi = positions[idx_i], yi = positions[idx_i + 1], zi = positions[idx_i + 2];
                double mi = masses[i];
                
                for (int j = 0; j < n; j++) {
                    if (i == j) continue;
                    int idx_j = j * 3;
                    double dx = positions[idx_j] - xi;
                    double dy = positions[idx_j + 1] - yi;
                    double dz = positions[idx_j + 2] - zi;
                    double distSq = dx*dx + dy*dy + dz*dz + softening*softening;
                    double invDist = 1.0 / Math.sqrt(distSq);
                    double f = (G * mi * masses[j]) / distSq * invDist;
                    
                    localForces[idx_i] += f * dx;
                    localForces[idx_i + 1] += f * dy;
                    localForces[idx_i + 2] += f * dz;
                }
            }
            
            // Reduce forces: Sum all localForces arrays to root (or Allreduce to everyone)
            // Using Allreduce so everyone gets the result (consistent state)
            comm.Allreduce(localForces, 0, forces, 0, n * 3, MPI.DOUBLE, MPI.SUM);

        } catch (MPIException e) {
            throw new RuntimeException("MPI Computation Failed", e);
        }
    }
}
