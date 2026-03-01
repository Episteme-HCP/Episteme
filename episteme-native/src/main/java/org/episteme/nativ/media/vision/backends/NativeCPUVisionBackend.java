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
import org.episteme.nativ.technical.backend.nativ.NativeLibraryLoader;
import com.google.auto.service.AutoService;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

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
@AutoService({Backend.class, ComputeBackend.class, CPUBackend.class, NativeBackend.class, VisionAlgorithmBackend.class})
public class NativeCPUVisionBackend implements VisionAlgorithmBackend<BufferedImage>, CPUBackend, NativeBackend {

    private static final String LIB_NAME = "episteme_vision";
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
            
            Optional<MemorySegment> symbol = LOOKUP.find("process_image");
            if (symbol.isPresent()) {
                MH_PROCESS_IMAGE = LINKER.downcallHandle(symbol.get(), desc);
                IS_AVAILABLE = true;
            } else {
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
        return "native-cpu-vision";
    }

    @Override
    public String getName() {
        return "Native CPU Vision Backend (FFM)";
    }

    @Override
    public String getDescription() {
        return "Native high-performance vision backend using Project Panama on CPU.";
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
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
            try {
                MH_PROCESS_IMAGE.invokeExact(pixelSegment, width, height, opCode);
            } catch (Throwable t) {
                throw new RuntimeException("Native invocation failed", t);
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
}
