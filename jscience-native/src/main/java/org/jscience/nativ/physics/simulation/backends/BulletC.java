package org.jscience.nativ.physics.simulation.backends;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
public class BulletC {
    
    private static final Linker linker = Linker.nativeLinker();
    private static SymbolLookup libbullet;
    
    // Handles
    public static MethodHandle btDefaultCollisionConfiguration_new;
    public static MethodHandle btCollisionDispatcher_new;
    public static MethodHandle btDbvtBroadphase_new;
    public static MethodHandle btSequentialImpulseConstraintSolver_new;
    public static MethodHandle btDiscreteDynamicsWorld_new;
    public static MethodHandle btDynamicsWorld_stepSimulation;
    
    // Deletion handles (omitted for now to focus on benchmark running, but good practice to have)
    
    private static boolean available = false;
    
    static {
        try {
            // Load library from absolute path or java.library.path
            String libPath = System.getProperty("user.dir") + "/libs/Bullet3DLL/libbulletc.dll";
            System.load(libPath);
            libbullet = SymbolLookup.loaderLookup();
            
            // Constructors
            // void* btDefaultCollisionConfiguration_new()
            btDefaultCollisionConfiguration_new = linker.downcallHandle(
                libbullet.find("btDefaultCollisionConfiguration_new").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.ADDRESS)
            );

            // void* btCollisionDispatcher_new(void* config)
            btCollisionDispatcher_new = linker.downcallHandle(
                libbullet.find("btCollisionDispatcher_new").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
            );

            // void* btDbvtBroadphase_new(void* paircache) - paircache can be null
            btDbvtBroadphase_new = linker.downcallHandle(
                libbullet.find("btDbvtBroadphase_new").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
            );

            // void* btSequentialImpulseConstraintSolver_new()
            btSequentialImpulseConstraintSolver_new = linker.downcallHandle(
                libbullet.find("btSequentialImpulseConstraintSolver_new").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.ADDRESS)
            );

            // void* btDiscreteDynamicsWorld_new(dispatcher, broadphase, solver, config)
            btDiscreteDynamicsWorld_new = linker.downcallHandle(
                libbullet.find("btDiscreteDynamicsWorld_new").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.ADDRESS, 
                    ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
            );

            // int btDynamicsWorld_stepSimulation(world, timeStep, maxSubSteps, fixedTimeStep)
            // Assuming float (standard Bullet). If generic fails, might crash.
            btDynamicsWorld_stepSimulation = linker.downcallHandle(
                libbullet.find("btDynamicsWorld_stepSimulation").orElseThrow(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, 
                    ValueLayout.ADDRESS, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_INT, ValueLayout.JAVA_FLOAT)
            );
            
            available = true;
        } catch (Throwable t) {
            System.err.println("Failed to load libbulletc symbols: " + t.getMessage());
            available = false;
        }
    }
    
    public static boolean isAvailable() { return available; }
}
