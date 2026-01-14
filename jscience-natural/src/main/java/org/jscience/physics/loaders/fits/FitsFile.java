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

package org.jscience.physics.loaders.fits;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import org.jscience.io.AbstractDataFile;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;

/**
 * Represents a FITS (Flexible Image Transport System) file.
 * <p>
 * Compliant with FITS Standard 4.0.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FITSFile extends AbstractDataFile {

    public FITSFile(Path path) {
        super(path);
    }

    @Override
    public String getFormatDescription() {
        return "FITS 4.0";
    }

    /**
     * Reads the primary image HDU as a Matrix.
     * <p>
     * Scans the file for the first valid ImageHDU and returns its content.
     * </p>
     * 
     * @return the image data as a RealDoubleMatrix
     * @throws IOException if no image found or read error
     */
    public RealDoubleMatrix asMatrix() throws IOException {
        // Need to parse HDUs sequentially until we find an image
        ReadableByteChannel channel = getChannel();
        if (channel instanceof java.nio.channels.SeekableByteChannel) {
            ((java.nio.channels.SeekableByteChannel) channel).position(0);
        }

        while (true) {
            Header header = Header.read(channel);
            // Check for END of file logically? Header.read throws EOF if real EOF.
            // Check keywords
            String simple = header.getStringValue(FITSConstants.KEY_SIMPLE);
            String xtension = header.getStringValue(FITSConstants.KEY_XTENSION);

            if (simple != null || "IMAGE".equals(xtension)) {
                int naxis = header.getIntValue(FITSConstants.KEY_NAXIS, 0);
                if (naxis == 2) {
                    ImageHDU hdu = new ImageHDU(header);
                    return hdu.asMatrix(channel);
                }
            }

            // Skip data block if not what we want
            // We need a generic way to skip data based on header
            // Re-using logic from ImageHDU for size calculation is best
            // For now, let's just instantiate ImageHDU to calculate size and skip
            ImageHDU valueCalc = new ImageHDU(header);
            long size = valueCalc.getDataSize(); // This logic is generic enough for bits/naxis

            long padding = FITSConstants.BLOCK_SIZE - (size % FITSConstants.BLOCK_SIZE);
            if (padding == FITSConstants.BLOCK_SIZE)
                padding = 0;
            long total = size + padding;

            ByteBuffer skip = ByteBuffer.allocate(8192);
            long skipped = 0;
            while (skipped < total) {
                long toRead = Math.min(skip.capacity(), total - skipped);
                skip.limit((int) toRead);
                skip.rewind();
                int r = channel.read(skip);
                if (r < 0)
                    break;
                skipped += r;
            }
            if (skipped < total)
                break; // End of file
        }
        throw new IOException("No valid 2D Image HDU found in FITS file.");
    }
}
