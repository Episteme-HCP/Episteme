/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.core.media.vision.backends;

import org.episteme.core.media.vision.VisionAlgorithmBackend;
import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.technical.backend.Backend;
import com.google.auto.service.AutoService;
import java.awt.image.BufferedImage;

/**
 * Basic VisionAlgorithmBackend using standard Java AWT (BufferedImage).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService({Backend.class, VisionAlgorithmBackend.class})
public class JavaAWTVisionBackend implements VisionAlgorithmBackend<BufferedImage> {

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
        throw new IllegalArgumentException("Unsupported data type for JavaAWTVisionBackend");
    }

    @Override public String getType() { return "vision"; }
    @Override public String getId() { return "java-awt-vision"; }
    @Override public String getName() { return "Java AWT Vision Backend"; }
    @Override public String getDescription() { return "Standard Java AWT/BufferedImage implementation for image processing."; }
    @Override public boolean isAvailable() { return true; }
    @Override public int getPriority() { return 10; }
    
    @Override 
    public Object createBackend() { 
        return this; // Stateless service
    }
}
