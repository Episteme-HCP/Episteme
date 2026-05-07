/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.core.media.vision.ops;

import org.episteme.core.media.vision.ImageOp;
import java.awt.image.BufferedImage;

/**
 * Sobel Edge Detection operation.
 * <p>
 * This handles the gradient calculation. If used with NativeMulticoreVisionProvider,
 * it can be optimized.
 * </p>
 */
public class SobelOp implements ImageOp<BufferedImage> {

    @Override
    public BufferedImage process(BufferedImage input) {
        int width = input.getWidth();
        int height = input.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        
        float[] kernelX = {
            -1, 0, 1,
            -2, 0, 2,
            -1, 0, 1
        };
        
        float[] kernelY = {
            -1, -2, -1,
             0,  0,  0,
             1,  2,  1
        };
        
        // This is a naive implementation. 
        // A better way would be to let the VisionProvider handle it via its optimized kernels.
        // For now, we do a basic pixel-by-pixel if not accelerated.
        
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                float gx = 0, gy = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = input.getRGB(x + kx, y + ky) & 0xFF;
                        gx += pixel * kernelX[(ky + 1) * 3 + (kx + 1)];
                        gy += pixel * kernelY[(ky + 1) * 3 + (kx + 1)];
                    }
                }
                int g = (int) Math.sqrt(gx * gx + gy * gy);
                g = Math.min(255, g);
                result.setRGB(x, y, (g << 16) | (g << 8) | g);
            }
        }
        
        return result;
    }
}
