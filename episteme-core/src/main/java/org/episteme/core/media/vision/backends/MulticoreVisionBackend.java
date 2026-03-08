package org.episteme.core.media.vision.backends;

import org.episteme.core.media.VisionBackend;
import org.episteme.core.media.vision.VisionAlgorithmProvider;
import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import com.google.auto.service.AutoService;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

/**
 * Implementation of VisionBackend and VisionAlgorithmProvider using Java Parallel Streams (multicore CPU).
 */
@AutoService({Backend.class, ComputeBackend.class, VisionBackend.class, CPUBackend.class})
public class MulticoreVisionBackend implements VisionBackend, CPUBackend {

    @Override public String getType() { return "vision"; }
    @Override public String getId() { return "multicore-vision"; }
    @Override public String getDescription() { return "Parallel CPU image processing using Java Streams."; }
    @Override public boolean isAvailable() { return true; }
    @Override public int getPriority() { return 5; }
    
    @Override
    public String getBackendName() {
        return "Multicore Vision Backend";
    }

    @Override
    public String getName() {
        return getBackendName();
    }

    @Override
    public Object createBackend() {
        return this;
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
        return op.process(image);
    }
    
    @Override
    public BufferedImage createImage(Object data, int width, int height) {
         if (data instanceof int[]) {
             BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
             img.setRGB(0, 0, width, height, (int[]) data, 0, width);
             return img;
         }
         throw new IllegalArgumentException("Unsupported data type for MulticoreVisionBackend");
    }

    @Override
    public void shutdown() {
        // No-op for parallel stream based backend
    }
}
