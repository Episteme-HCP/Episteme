/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.nativ.physics.classical.mechanics.collision.backends;

import com.google.auto.service.AutoService;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.natural.physics.classical.mechanics.CollisionProvider;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.nativ.technical.backend.nativ.NativeFFMLoader;

/**
 * Native implementation of the CollisionProvider using Project Panama.
 * Renamed and moved as part of the backend standardization.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({CollisionProvider.class, NativeBackend.class, Backend.class})
public class NativeCollisionBackend implements CollisionProvider, NativeBackend, ComputeBackend {

    private static final MethodHandle DETECT_SPHERES;
    private static final MethodHandle RESOLVE_COLLISIONS;
    private static final boolean IS_AVAILABLE_FLAG;

    static {
        Linker linker = Linker.nativeLinker();
        java.lang.foreign.Arena arena = java.lang.foreign.Arena.global();
        Optional<SymbolLookup> lib = NativeFFMLoader.loadLibrary("episteme-native", arena);
        
        if (lib.isPresent()) {
            SymbolLookup lookup = lib.get();
            DETECT_SPHERES = NativeFFMLoader.findSymbol(lookup, "episteme_detect_sphere_collisions")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS))).orElse(null);
            RESOLVE_COLLISIONS = NativeFFMLoader.findSymbol(lookup, "episteme_resolve_sphere_collisions")
                .map(s -> linker.downcallHandle(s, FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT))).orElse(null);
            IS_AVAILABLE_FLAG = DETECT_SPHERES != null && RESOLVE_COLLISIONS != null;
        } else {
            DETECT_SPHERES = null;
            RESOLVE_COLLISIONS = null;
            IS_AVAILABLE_FLAG = false;
        }
    }

    @Override
    public int detectSphereCollisions(MemorySegment positions, MemorySegment radii, int n, MemorySegment collisions) {
        if (!IS_AVAILABLE_FLAG) throw new UnsupportedOperationException("Native collision functions not found in library");
        try {
            return (int) DETECT_SPHERES.invokeExact(positions, radii, n, collisions);
        } catch (Throwable t) {
            throw new RuntimeException("Native collision detection failed", t);
        }
    }

    @Override
    public void resolveCollisions(MemorySegment positions, MemorySegment velocities, MemorySegment masses, int n, MemorySegment collisions, int numCollisions) {
        if (!IS_AVAILABLE_FLAG) throw new UnsupportedOperationException("Native collision functions not found in library");
        try {
            RESOLVE_COLLISIONS.invokeExact(positions, velocities, masses, n, collisions, numCollisions);
        } catch (Throwable t) {
            throw new RuntimeException("Native collision resolution failed", t);
        }
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE_FLAG;
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE_FLAG;
    }

    @Override
    public String getNativeLibraryName() {
        return "episteme-native";
    }

    @Override
    public String getName() {
        return "Native Collision Backend (Standard)";
    }

    @Override
    public String getId() {
        return "native-collision";
    }

    @Override
    public String getDescription() {
        return "High-performance native collision detection using Project Panama.";
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return null; // Managed by native library
    }

    @Override public void shutdown() {}
}
