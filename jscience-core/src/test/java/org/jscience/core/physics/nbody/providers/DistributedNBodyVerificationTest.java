package org.jscience.core.physics.nbody.providers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jscience.core.ComputeContext;
import org.jscience.core.distributed.LocalDistributedContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DistributedNBodyVerificationTest {

    private DistributedNBodyProvider provider;

    @BeforeEach
    void setUp() {
        provider = new DistributedNBodyProvider();
        // Ensure we are using LocalDistributedContext for testing logic
        ComputeContext.setCurrent(new ComputeContext());
        ComputeContext.current().setDistributedContext(new LocalDistributedContext(1)); // 1 node
    }

    @Test
    void testIsAvailable() {
        assertTrue(provider.isAvailable(), "DistributedNBodyProvider should be available (via Local/MPI fallback)");
    }

    @Test
    void testComputeForcesLocalSimulation() {
        // Run a small N-Body simulation (2 bodies) and check physics locally
        // 2 bodies at distance 1.0, mass 1.0. 
        // Force F = G * m1 * m2 / r^2 = 1.0 * 1.0 * 1.0 / 1.0 = 1.0
        
        double G = 1.0;
        double softening = 0.0;
        
        // Body 0 at (0,0,0), Body 1 at (1,0,0)
        double[] positions = {
            0.0, 0.0, 0.0,
            1.0, 0.0, 0.0
        };
        
        double[] masses = { 1.0, 1.0 };
        double[] forces = new double[positions.length];
        
        provider.computeForces(positions, masses, forces, G, softening);
        
        // Expected Forces:
        // Body 0: Attracted to Body 1 (+x direction). Fx = 1.0
        // Body 1: Attracted to Body 0 (-x direction). Fx = -1.0
        
        assertEquals(1.0, forces[0], 1e-6, "Body 0 Force X");
        assertEquals(0.0, forces[1], 1e-6, "Body 0 Force Y");
        assertEquals(0.0, forces[2], 1e-6, "Body 0 Force Z");
        
        assertEquals(-1.0, forces[3], 1e-6, "Body 1 Force X");
        assertEquals(0.0, forces[4], 1e-6, "Body 1 Force Y");
        assertEquals(0.0, forces[5], 1e-6, "Body 1 Force Z");
    }
}
