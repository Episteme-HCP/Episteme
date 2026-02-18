package org.jscience.nativ.technical.backend.bullet;

import org.jscience.core.technical.algorithm.AlgorithmProvider;
import com.google.auto.service.AutoService;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.Arena;
import java.lang.foreign.ValueLayout;

/**
 * Native Bullet Physics Provider (via libbulletc).
 */
@AutoService(AlgorithmProvider.class)
public class NativeBulletSimulationProvider implements AlgorithmProvider {

    private MemorySegment world = null;
    // Keep references to prevent GC? Panama is off-heap, so Java GC doesn't matter for the C++ objects themselves,
    // but we need to keep the MemorySegments if we were managing lifetime.
    // For this simple benchmarks, we just create once.

    @Override
    public String getName() {
        return "Native Bullet Physics (libbulletc)";
    }

    @Override
    public String getAlgorithmType() {
        return "simulation";
    }

    @Override
    public boolean isAvailable() {
        return BulletC.isAvailable();
    }
    
    /**
     * Executes a simulation step.
     * Matches JBulletSimulationProvider signature (via reflection usually).
     */
    public void stepSimulation(long iterations) {
        if (!isAvailable()) throw new UnsupportedOperationException("BulletC not available");
        
        try {
            if (world == null) {
                // Initialize World
                MemorySegment config = (MemorySegment) BulletC.btDefaultCollisionConfiguration_new.invokeExact();
                MemorySegment dispatcher = (MemorySegment) BulletC.btCollisionDispatcher_new.invokeExact(config);
                MemorySegment broadphase = (MemorySegment) BulletC.btDbvtBroadphase_new.invokeExact(MemorySegment.NULL);
                MemorySegment solver = (MemorySegment) BulletC.btSequentialImpulseConstraintSolver_new.invokeExact();
                world = (MemorySegment) BulletC.btDiscreteDynamicsWorld_new.invokeExact(dispatcher, broadphase, solver, config);
            }
            
            // Step
            for (long i = 0; i < iterations; i++) {
                // timeStep=1/60, maxSubSteps=10, fixedTimeStep=1/60
                BulletC.btDynamicsWorld_stepSimulation.invokeExact(world, 1.0f/60.0f, 10, 1.0f/60.0f);
            }
            
        } catch (Throwable t) {
            throw new RuntimeException("Bullet simulation failed", t);
        }
    }
}
