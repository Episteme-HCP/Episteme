/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.vision.providers;

import org.episteme.core.media.vision.ImageOp;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class NativeJavaCVVisionBackendTest {

    @Test
    void testProviderMetadata() {
        org.episteme.nativ.media.backends.NativeJavaCVMediaBackend provider = new org.episteme.nativ.media.backends.NativeJavaCVMediaBackend();
        assertEquals("native-javacv-media", provider.getId());
        assertEquals("vision", provider.getType());
        assertNotNull(provider.getDescription());
    }

    @Test
    void testIsAvailableDefaultsToTrue() {
        // Since JavaCV/OpenCV is on the classpath, it should be true.
        org.episteme.nativ.media.backends.NativeJavaCVMediaBackend provider = new org.episteme.nativ.media.backends.NativeJavaCVMediaBackend();
        assertTrue(provider.isAvailable(), "Provider should be available via JavaCV");
    }

    @Test
    void testApplyDelegates() {
        org.episteme.nativ.media.backends.NativeJavaCVMediaBackend provider = new org.episteme.nativ.media.backends.NativeJavaCVMediaBackend();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        
        ImageOp<BufferedImage> op = input -> input;
        
        BufferedImage result = provider.apply(img, op);
        assertSame(img, result, "Apply should delegate to ImageOp.process");
    }

    @Test
    void testProcessNativeSucceeds() {
        org.episteme.nativ.media.backends.NativeJavaCVMediaBackend provider = new org.episteme.nativ.media.backends.NativeJavaCVMediaBackend();
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        
        if (provider.isAvailable()) {
            BufferedImage result = provider.processNative(img, 1);
            assertNotNull(result);
            assertEquals(10, result.getWidth());
        }
    }
}
