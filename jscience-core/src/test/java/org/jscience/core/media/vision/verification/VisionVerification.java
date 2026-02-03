/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.core.media.vision.verification;

import org.jscience.core.media.vision.VisionContext;
import org.jscience.core.media.vision.VisionProvider;
import org.jscience.core.media.vision.ops.GrayscaleOp;
import org.jscience.core.media.vision.ops.ResizeOp;
import org.jscience.core.media.vision.ops.RotateOp;
import org.jscience.core.media.vision.ops.FlipOp;
import org.jscience.core.media.vision.ops.SobelOp;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification tests for vision operations using the Java AWT backend.
 */
public class VisionVerification {

    @Test
    public void testVisionOps() {
        VisionContext ctx = VisionContext.current();
        ctx.setBackend(VisionContext.Backend.JAVA_AWT);
        VisionProvider<BufferedImage> provider = ctx.getProvider();
        
        // Create a 100x100 dummy image (red)
        int[] data = new int[100 * 100];
        for (int i = 0; i < data.length; i++) data[i] = 0xFFFF0000;
        BufferedImage img = provider.createImage(data, 100, 100);
        
        // Test Resize
        BufferedImage resized = provider.apply(img, new ResizeOp(50, 50));
        assertEquals(50, resized.getWidth());
        assertEquals(50, resized.getHeight());
        
        // Test Grayscale
        BufferedImage gray = provider.apply(img, new GrayscaleOp());
        assertEquals(BufferedImage.TYPE_BYTE_GRAY, gray.getType());
        
        // Test Rotate
        BufferedImage rotated = provider.apply(img, new RotateOp(90));
        assertNotNull(rotated);
        
        // Test Flip
        BufferedImage flipped = provider.apply(img, new FlipOp(FlipOp.Direction.HORIZONTAL));
        assertEquals(100, flipped.getWidth());
        
        // Test Sobel
        BufferedImage edges = provider.apply(img, new SobelOp());
        assertNotNull(edges);
    }
}
