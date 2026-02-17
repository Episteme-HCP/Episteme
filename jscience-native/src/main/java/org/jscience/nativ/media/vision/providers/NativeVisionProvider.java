/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.media.vision.providers;

import org.jscience.core.media.vision.ImageOp;
import org.jscience.core.media.vision.VisionAlgorithmBackend;
import org.jscience.core.technical.backend.Backend;
import org.jscience.nativ.util.NativeLibraryLoader;
import com.google.auto.service.AutoService;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

/**
 * Vision provider that delegates operations to a native library using Project Panama (FFM API).
 * <p>
 * This provider attempts to load the 'jscience_vision' native library.
 * If accessible, it allows high-performance image processing outside the Java heap.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService(Backend.class)
public class NativeVisionProvider implements VisionAlgorithmBackend<BufferedImage> {

    private static final String LIB_NAME = "jscience_vision";
    private static final SymbolLookup LOOKUP;
    private static final Linker LINKER = Linker.nativeLinker();
    private static final boolean IS_AVAILABLE;

    // Native function handles
    private static final MethodHandle MH_PROCESS_IMAGE;

    static {
        // Load library in global scope
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary(LIB_NAME, Arena.global());
        if (lib.isPresent()) {
            LOOKUP = lib.get();
            // Example: void process_image(int* pixels, int width, int height, int opCode)
            // FunctionDescriptor: (Address, int, int, int) -> void
            FunctionDescriptor desc = FunctionDescriptor.ofVoid(
                    ValueLayout.ADDRESS,
                    ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT,
                    ValueLayout.JAVA_INT
            );
            
            // Look for symbol
            Optional<MemorySegment> symbol = LOOKUP.find("process_image");
            if (symbol.isPresent()) {
                MH_PROCESS_IMAGE = LINKER.downcallHandle(symbol.get(), desc);
                IS_AVAILABLE = true;
            } else {
                MH_PROCESS_IMAGE = null;
                IS_AVAILABLE = false;
                System.err.println("[NativeVisionProvider] Symbol 'process_image' not found in library " + LIB_NAME);
            }
        } else {
            LOOKUP = null;
            MH_PROCESS_IMAGE = null;
            // For now, we don't fail hard, just mark unavailable.
            // In a real scenario, we might want to fallback or log.
            IS_AVAILABLE = false;
        }
    }

    @Override
    public String getType() {
        return "vision";
    }

    @Override
    public String getId() {
        return "native-vision";
    }

    @Override
    public String getDescription() {
        return "Native high-performance vision provider using Project Panama.";
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
        // Since ImageOp is a functional interface in Java, we cannot "native accelerate" arbitrary Java lambdas.
        // This provider assumes that 'op' might be a marker or wrapper for a native operation ID.
        // For demonstration, we simply return the image if native is not used, 
        // or we could define a specific 'NativeImageOp' that has an opcode.
        
        // If we simply want to run the Java op:
        return op.process(image);
    }
    
    /**
     * Executes a native image processing operation identified by an opcode.
     * 
     * @param image Input image (must be INT_ARGB or INT_RGB).
     * @param opCode Operation code defined by the native library.
     * @return Processed image (new instance).
     */
    public BufferedImage processNative(BufferedImage image, int opCode) {
        if (!IS_AVAILABLE) {
            throw new UnsupportedOperationException("Native library not available.");
        }

        int width = image.getWidth();
        int height = image.getHeight();
        
        // 1. Extract pixels
        // Native methods prefer direct memory. We must copy pixels to off-heap memory.
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        try (Arena arena = Arena.ofConfined()) {
            // Allocate off-heap memory for pixels
            MemorySegment pixelSegment = arena.allocateFrom(ValueLayout.JAVA_INT, pixels);
            
            // 2. Call Native Function
            // void process_image(int* pixels, int width, int height, int opCode)
            try {
                MH_PROCESS_IMAGE.invokeExact(pixelSegment, width, height, opCode);
            } catch (Throwable t) {
                throw new RuntimeException("Native invocation failed", t);
            }
            
            // 3. Copy back results
            // The native function modified the buffer in-place (assumed).
            // We copy back to a new image or the same one.
            // Let's create a new one to be safe/functional style.
            BufferedImage result = new BufferedImage(width, height, image.getType());
            int[] resultPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();
            
            // Bulk copy from segment to int array
            // MemorySegment.elements(ValueLayout) returns a stream, need bulk copy.
            // toArray(ValueLayout.JAVA_INT)
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
         throw new IllegalArgumentException("Unsupported data type for NativeVisionProvider");
    }

    @Override
    public String getName() {
        return "Native Vision Provider (FFM)";
    }
}
