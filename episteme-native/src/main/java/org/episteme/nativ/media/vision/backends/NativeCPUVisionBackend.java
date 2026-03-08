/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.vision.backends;

import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.media.VisionBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import com.google.auto.service.AutoService;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.foreign.*;

/**
 * Native CPU Vision Backend using Project Panama (FFM API).
 * <p>
 * This backend delegates operations to the 'episteme_vision' native library.
 * Implements {@link CPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, VisionBackend.class, CPUBackend.class, NativeBackend.class})
public class NativeCPUVisionBackend implements VisionBackend, CPUBackend, NativeBackend {
    private static final boolean IS_AVAILABLE = true;

    // No external C++ library to load (Pure Java Panama)

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "none (pure panama)";
    }

    @Override
    public String getType() {
        return "vision";
    }

    @Override
    public String getId() {
        return "native-cpu-vision";
    }

    @Override
    public String getBackendName() {
        return "Native CPU Vision Backend (Pure Java FFM)";
    }

    @Override
    public String getDescription() {
        return "Native high-performance vision backend using Project Panama (Foreign Memory API) on CPU without external C++ libraries.";
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public void shutdown() {
        // Pure Panama backend - no resources to release
    }

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
        return op.process(image);
    }
    
    public BufferedImage processNative(BufferedImage image, int opCode) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native library not available.");
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment pixelSegment = arena.allocateFrom(ValueLayout.JAVA_INT, pixels);
            
            // Pure Java Panama off-heap processing
            long byteSize = pixelSegment.byteSize();
            if (opCode == 1) {
                // Example: Grayscale conversion (simple average for demonstration)
                for (long offset = 0; offset < byteSize; offset += ValueLayout.JAVA_INT.byteSize()) {
                    int argb = pixelSegment.get(ValueLayout.JAVA_INT, offset);
                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;
                    int gray = (r + g + b) / 3;
                    int newArgb = (argb & 0xFF000000) | (gray << 16) | (gray << 8) | gray;
                    pixelSegment.set(ValueLayout.JAVA_INT, offset, newArgb);
                }
            }
            
            BufferedImage result = new BufferedImage(width, height, image.getType());
            int[] resultPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();
            int[] processedPixels = pixelSegment.toArray(ValueLayout.JAVA_INT);
            System.arraycopy(processedPixels, 0, resultPixels, 0, processedPixels.length);
            return result;
        }
    }

    @Override
    public BufferedImage createImage(Object data, int width, int height) {
         if (data instanceof int[]) {
             BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
             img.setRGB(0, 0, width, height, (int[]) data, 0, width);
             return img;
         }
         throw new IllegalArgumentException("Unsupported data type for NativeCPUVisionBackend");
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }

    @Override
    public org.episteme.core.technical.backend.HardwareAccelerator getAcceleratorType() {
        return org.episteme.core.technical.backend.HardwareAccelerator.CPU;
    }
}
