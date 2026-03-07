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
import com.google.auto.service.AutoService;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.Frame;

import java.awt.image.BufferedImage;

/**
 * Native CPU Vision Backend using JavaCV and OpenCV.
 * <p>
 * This backend delegates operations to the Bytedeco OpenCV native library.
 * It serves as an available alternative to the pure C++ "NativeCPUVisionBackend"
 * which requires a custom compiled DLL.
 * Implements {@link CPUBackend} and {@link NativeBackend}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@AutoService({Backend.class, ComputeBackend.class, CPUBackend.class, NativeBackend.class, VisionAlgorithmBackend.class})
public class NativeJavaCVVisionBackend implements VisionAlgorithmBackend<BufferedImage>, CPUBackend, NativeBackend {

    private static final boolean IS_AVAILABLE;

    static {
        boolean available = false;
        try {
            Loader.load(org.bytedeco.opencv.global.opencv_core.class);
            available = true;
        } catch (Throwable t) {
            System.err.println("Warning: NativeJavaCVVisionBackend initialization failed: " + t.getMessage());
        }
        IS_AVAILABLE = available;
    }

    @Override
    public boolean isLoaded() {
        return IS_AVAILABLE;
    }

    @Override
    public String getNativeLibraryName() {
        return "opencv_core";
    }

    @Override
    public String getType() {
        return "vision";
    }

    @Override
    public String getId() {
        return "native-javacv-vision";
    }

    @Override
    public String getName() {
        return "Native JavaCV Vision Backend";
    }

    @Override
    public String getDescription() {
        return "Native high-performance vision backend using OpenCV (via JavaCV) on CPU.";
    }

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE;
    }

    @Override
    public void shutdown() {
        // JavaCV/OpenCV handles its own lifecycle.
    }

    @Override
    public int getPriority() {
        return 50; // Medium priority, fallback from pure C binding
    }

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
        // Here we could unroll or translate ImageOp directly to OpenCV primitives,
        // but for general ops we fallback to the default processing if we don't have a direct cv:: mapping.
        return op.process(image);
    }
    
    public BufferedImage processNative(BufferedImage image, int opCode) {
        if (!IS_AVAILABLE) throw new UnsupportedOperationException("OpenCV Native library not available.");
        
        try (Java2DFrameConverter javaConverter = new Java2DFrameConverter();
             OpenCVFrameConverter.ToMat cvConverter = new OpenCVFrameConverter.ToMat()) {
            
            Frame frame = javaConverter.convert(image);
            Mat src = cvConverter.convert(frame);
            Mat dst = new Mat();

            // Demonstration: Example Op mappings. In a real scenario, ImageOp would encapsulate these intent codes.
            switch (opCode) {
                case 1: // Grayscale
                    opencv_imgproc.cvtColor(src, dst, opencv_imgproc.COLOR_BGR2GRAY);
                    break;
                case 2: // Blur
                    opencv_imgproc.blur(src, dst, new org.bytedeco.opencv.opencv_core.Size(5, 5));
                    break;
                default:
                    src.copyTo(dst);
            }
            
            Frame dstFrame = cvConverter.convert(dst);
            return javaConverter.convert(dstFrame);
        }
    }

    @Override
    public BufferedImage createImage(Object data, int width, int height) {
         if (data instanceof int[]) {
             BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
             img.setRGB(0, 0, width, height, (int[]) data, 0, width);
             return img;
         }
         throw new IllegalArgumentException("Unsupported data type for NativeJavaCVVisionBackend");
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.cpu.CPUExecutionContext();
    }
}
