/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import java.nio.ByteOrder;
import org.jscience.mathematics.linearalgebra.matrices.DenseMatrix;
import org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix;

/**
 * Header Data Unit representing an Image (N-dimensional array).
 * <p>
 * This class handles the reading of binary data as a generic Matrix.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ImageHDU extends HDU {

    public ImageHDU(Header header) {
        super(header);
    }

    @Override
    public long getDataSize() {
        // Calculate size based on BITPIX and NAXISn
        int bitpix = Math.abs(header.getIntValue(FitsConstants.KEY_BITPIX, 8));
        int naxis = header.getIntValue(FitsConstants.KEY_NAXIS, 0);

        if (naxis == 0)
            return 0;

        long count = 1;
        for (int i = 1; i <= naxis; i++) {
            count *= header.getIntValue(FitsConstants.KEY_NAXIS + i, 1);
        }

        long bits = count * bitpix;
        return (bits + 7) / 8; // Bytes
    }

    /**
     * Reads the image data as a 2D Matrix of Reals.
     * 
     * @param channel the channel to read from
     * @return the matrix representation of the image
     * @throws IOException if an error occurs
     */
    public RealDoubleMatrix asMatrix(ReadableByteChannel channel) throws IOException {
        int naxis = header.getIntValue(FitsConstants.KEY_NAXIS, 0);
        if (naxis != 2) {
            throw new IOException("Only 2D images supported for Matrix conversion (found NAXIS=" + naxis + ")");
        }
        int width = header.getIntValue(FitsConstants.KEY_NAXIS + "1", 0);
        int height = header.getIntValue(FitsConstants.KEY_NAXIS + "2", 0);
        int bitpix = header.getIntValue(FitsConstants.KEY_BITPIX, 8);

        // FITS data is Big Endian
        long size = getDataSize();
        ByteBuffer buffer = ByteBuffer.allocate((int) size);
        buffer.order(ByteOrder.BIG_ENDIAN);

        // Read fully
        while (buffer.hasRemaining()) {
            int r = channel.read(buffer);
            if (r < 0)
                throw new IOException("Unexpected EOF in ImageHDU data");
        }
        buffer.flip();

        // Convert to double array for RealDoubleMatrix
        double[] data = new double[width * height];

        for (int i = 0; i < width * height; i++) {
            switch (bitpix) {
                case FitsConstants.BITPIX_DOUBLE:
                    data[i] = buffer.getDouble();
                    break;
                case FitsConstants.BITPIX_FLOAT:
                    data[i] = buffer.getFloat();
                    break;
                case FitsConstants.BITPIX_INT:
                    data[i] = buffer.getInt();
                    break;
                case FitsConstants.BITPIX_SHORT:
                    data[i] = buffer.getShort();
                    break;
                case FitsConstants.BITPIX_BYTE:
                    data[i] = buffer.get() & 0xFF; // Unsigned byte
                    break;
                case FitsConstants.BITPIX_LONG:
                    data[i] = buffer.getLong();
                    break;
                default:
                    throw new IOException("Unsupported BITPIX: " + bitpix);
            }
        }

        // consume padding
        long remaining = FitsConstants.BLOCK_SIZE - (size % FitsConstants.BLOCK_SIZE);
        if (remaining < FitsConstants.BLOCK_SIZE) {
            ByteBuffer pad = ByteBuffer.allocate((int) remaining);
            while (pad.hasRemaining())
                channel.read(pad);
        }

        return RealDoubleMatrix.of(data, height, width);
    }
}
