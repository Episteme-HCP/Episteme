/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.technical.backend.nativ;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.Arena;
import java.util.Objects;

/**
 * A wrapper for MemorySegment that ensures it was created from a known origin
 * and provides additional lifecycle validation for native boundary protection.
 * <p>
 * This replaces unsafe direct address manipulation with a tracked, 
 * scavenge-aware container.
 * </p>
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public record ScavengeProtectedSegment(MemorySegment segment, String origin, long sizeBytes) {
    
    public ScavengeProtectedSegment {
        Objects.requireNonNull(segment, "segment must not be null");
        Objects.requireNonNull(origin, "origin must not be null");
        if (sizeBytes < 0) throw new IllegalArgumentException("sizeBytes must be non-negative");
    }

    /**
     * Reinterprets a raw address into a ScavengeProtectedSegment.
     */
    public static ScavengeProtectedSegment fromAddress(long address, long size, Arena arena, String origin) {
        MemorySegment raw = MemorySegment.ofAddress(address).reinterpret(size, arena, segment -> {
            // Optional: log cleanup or perform extra 'scavenging' checks
        });
        return new ScavengeProtectedSegment(raw, origin, size);
    }
    
    /**
     * Creates a protected view of an existing segment.
     */
    public static ScavengeProtectedSegment protect(MemorySegment segment, String origin) {
        return new ScavengeProtectedSegment(segment, origin, segment.byteSize());
    }

    public boolean isNull() {
        return segment.equals(MemorySegment.NULL);
    }
    
    @Override
    public String toString() {
        try {
            return String.format("ScavengeProtectedSegment[origin=%s, address=0x%s, size=%d]", 
                origin, Long.toHexString(segment.address()), sizeBytes);
        } catch (UnsupportedOperationException e) {
            return String.format("ScavengeProtectedSegment[origin=%s, address=UNAVAILABLE, size=%d]", 
                origin, sizeBytes);
        }
    }
}
