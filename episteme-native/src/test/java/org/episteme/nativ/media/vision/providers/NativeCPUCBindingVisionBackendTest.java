/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.vision.providers;

import org.episteme.core.media.vision.ImageOp;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class NativeCPUCBindingVisionBackendTest {

    @Test
    void testProviderMetadata() {
        org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend provider = new org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend();
        assertEquals("native-cpu-cbinding-vision", provider.getId());
        assertEquals("vision", provider.getType());
        assertNotNull(provider.getDescription());
    }

    @Test
    void testIsAvailableDefaultsToFalse() {
        // Without compiling episteme_vision.dll via CMake, it should default to false
        org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend provider = new org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend();
        assertFalse(provider.isAvailable(), "Provider should not be available without compiled native library");
    }

    @Test
    void testApplyDelegates() {
        org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend provider = new org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        
        // Define a simple Java Op
        ImageOp<BufferedImage> op = input -> input; // Identity
        
        BufferedImage result = provider.apply(img, op);
        assertSame(img, result, "Apply should delegate to ImageOp.process");
    }

    @Test
    void testProcessNativeThrowsException() {
        org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend provider = new org.episteme.nativ.media.vision.backends.NativeCPUCBindingVisionBackend();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        
        // Should throw because library is not loaded
        assertThrows(UnsupportedOperationException.class, () -> {
            provider.processNative(img, 1);
        });
    }
}
