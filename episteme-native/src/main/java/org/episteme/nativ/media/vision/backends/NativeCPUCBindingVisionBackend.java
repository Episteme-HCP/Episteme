/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.nativ.media.vision.backends;

import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.media.vision.VisionAlgorithmBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.nativ.technical.backend.nativ.NativeBackend;
import org.episteme.core.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.Graphics2D;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

/**
 * Native CPU Vision Backend binding directly to custom C++ via Project Panama (FFM API).
 * <p>
 * This backend delegates operations to the 'episteme_vision' native library built from src/main/cpp.
 * Implements {@link CPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, CPUBackend.class, NativeBackend.class, VisionAlgorithmBackend.class})
public class NativeCPUCBindingVisionBackend implements VisionAlgorithmBackend<BufferedImage>, CPUBackend, NativeBackend {

    private static final String LIB_NAME = "episteme-native";
    private static final SymbolLookup LOOKUP;
    private static final Linker LINKER = Linker.nativeLinker();
    private static final boolean IS_AVAILABLE;

    private static final MethodHandle MH_PROCESS_IMAGE;

    static {
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary(LIB_NAME, Arena.global());
        if (lib.isPresent()) {
            LOOKUP = lib.get();
            FunctionDescriptor desc = FunctionDescriptor.ofVoid(
                    ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT
            );
            
            Optional<MemorySegment> symbol = NativeLibraryLoader.findSymbol(LOOKUP, "process_image");
            if (symbol.isPresent()) {
                MH_PROCESS_IMAGE = LINKER.downcallHandle(symbol.get(), desc);
                IS_AVAILABLE = true;
            } else {
                System.err.println("[DEBUG] NativeCPUCBindingVisionBackend: Symbol 'process_image' not found in library.");
                MH_PROCESS_IMAGE = null;
                IS_AVAILABLE = false;
            }
        } else {
            LOOKUP = null;
            MH_PROCESS_IMAGE = null;
            IS_AVAILABLE = false;
        }
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return LIB_NAME;
    }

    @Override
    public String getType() {
        return "vision";
    }

    @Override
    public String getId() {
        return "native-cpu-cbinding-vision";
    }

    @Override
    public String getName() {
        return "Native CPU Vision Backend (Custom C++ FFM)";
    }

    @Override
    public String getDescription() {
        return "High-performance vision backend using Project Panama to call custom C++ SIMD-optimized routines.";
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public int getPriority() {
        return 100; // Highest priority for Vision
    }

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
        return op.process(image);
    }
    
    public BufferedImage processNative(BufferedImage image, int opCode) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("Native library not available. Compile episteme-native with CMake.");
        int width = image.getWidth();
        int height = image.getHeight();
        
        // ensure we have a TYPE_INT_ARGB or TYPE_INT_RGB for direct buffer access
        if (image.getType() != BufferedImage.TYPE_INT_ARGB && image.getType() != BufferedImage.TYPE_INT_RGB) {
            // Fallback for incompatible types if necessary, but for benchmark we expect TYPE_INT_ARGB
             BufferedImage converted = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
             Graphics2D g = converted.createGraphics();
             g.drawImage(image, 0, 0, null);
             g.dispose();
             image = converted;
        }

        DataBufferInt buffer = (DataBufferInt) image.getRaster().getDataBuffer();
        int[] data = buffer.getData();
        
        try (Arena arena = Arena.ofConfined()) {
            // High-speed copy to native memory
            MemorySegment pixelSegment = arena.allocateFrom(ValueLayout.JAVA_INT, data);
            
            // Call native function
            MH_PROCESS_IMAGE.invokeExact(pixelSegment, width, height, opCode);
            
            // High-speed copy back to heap array
            MemorySegment.copy(pixelSegment, ValueLayout.JAVA_INT, 0, MemorySegment.ofArray(data), ValueLayout.JAVA_INT, 0, data.length);
        } catch (Throwable t) {
            throw new RuntimeException("Native call failed", t);
        }
        
        return image; // Image updated in-place via the DataBufferInt array
    }

    @Override
    public BufferedImage createImage(Object data, int width, int height) {
         if (data instanceof int[]) {
             BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
             img.setRGB(0, 0, width, height, (int[]) data, 0, width);
             return img;
         }
         throw new IllegalArgumentException("Unsupported data type for NativeCPUCBindingVisionBackend");
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }
}
