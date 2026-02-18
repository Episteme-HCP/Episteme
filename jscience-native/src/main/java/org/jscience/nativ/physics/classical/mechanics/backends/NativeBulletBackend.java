/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.classical.mechanics.backends;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import org.jscience.core.technical.backend.HardwareAccelerator;
import org.jscience.natural.physics.classical.mechanics.CollisionBackend;
import org.jscience.core.technical.backend.Backend;
import org.jscience.core.technical.backend.ComputeBackend;
import com.google.auto.service.AutoService;

/**
 * Implementation of {@link CollisionBackend} using Bullet Physics via Project Panama.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
@AutoService({CollisionBackend.class, ComputeBackend.class, Backend.class})
public class NativeBulletBackend implements CollisionBackend {

    private static final MethodHandle DETECT_SPHERES;
    private static final MethodHandle RESOLVE_COLLISIONS;
    private static final boolean AVAILABLE;

    static {
        Linker linker = Linker.nativeLinker();
        SymbolLookup lookup = null;
        java.lang.foreign.Arena arena = java.lang.foreign.Arena.global();

        // 1. Try standard library path
        try {
            lookup = SymbolLookup.libraryLookup("bullet_capi", arena);
        } catch (Throwable t) {
            // 2. Try explicit path: libs/Bullet3DLL/libbulletc.dll
            java.nio.file.Path libPath = java.nio.file.Path.of("libs", "Bullet3DLL", "libbulletc.dll");
            if (java.nio.file.Files.exists(libPath)) {
                try {
                    lookup = SymbolLookup.libraryLookup(libPath, arena);
                } catch (Throwable t2) {
                     // ignore
                }
            } else {
                 // 3. Try root fallback (just in case)
                 java.nio.file.Path rootPath = java.nio.file.Path.of("libbulletc.dll");
                 if (java.nio.file.Files.exists(rootPath)) {
                     try {
                         lookup = SymbolLookup.libraryLookup(rootPath, arena);
                     } catch (Throwable t3) {}
                 }
            }
        }

        if (lookup != null && lookup.find("bt_detect_sphere_collisions").isPresent()) {
            DETECT_SPHERES = linker.downcallHandle(lookup.find("bt_detect_sphere_collisions").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS));
            RESOLVE_COLLISIONS = linker.downcallHandle(lookup.find("bt_resolve_sphere_collisions").get(),
                FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT));
            AVAILABLE = true;
        } else {
            DETECT_SPHERES = RESOLVE_COLLISIONS = null;
            AVAILABLE = false;
        }
    }

    @Override
    public boolean isAvailable() {
        return AVAILABLE;
    }

    @Override
    public String getName() {
        return "Bullet Physics Panama Backend";
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU; // Bullet is CPU-based
    }

    @Override
    public org.jscience.core.technical.backend.ExecutionContext createContext() {
        return null; // Bullet doesn't use contexts in this simple wrapper
    }

    @Override
    public int detectSphereCollisions(DoubleBuffer positions, DoubleBuffer radii, int n, IntBuffer collisions) {
        if (!AVAILABLE) throw new UnsupportedOperationException("Bullet native library not found");
        try {
            return (int) DETECT_SPHERES.invokeExact(MemorySegment.ofBuffer(positions), MemorySegment.ofBuffer(radii), n, MemorySegment.ofBuffer(collisions));
        } catch (Throwable t) {
            throw new RuntimeException("Bullet collision detection failed", t);
        }
    }

    @Override
    public void resolveCollisions(DoubleBuffer positions, DoubleBuffer velocities, DoubleBuffer masses, int n, IntBuffer collisions, int numCollisions) {
        if (!AVAILABLE) throw new UnsupportedOperationException("Bullet native library not found");
        try {
            RESOLVE_COLLISIONS.invokeExact(MemorySegment.ofBuffer(positions), MemorySegment.ofBuffer(velocities), MemorySegment.ofBuffer(masses), n, MemorySegment.ofBuffer(collisions), numCollisions);
        } catch (Throwable t) {
            throw new RuntimeException("Bullet collision resolution failed", t);
        }
    }
}





