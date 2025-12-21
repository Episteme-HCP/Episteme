/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.mathematics.loaders.netcdf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.jscience.mathematics.loaders.AbstractDataFile;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.linearalgebra.vectors.DenseVector;

/**
 * Represents a NetCDF (Network Common Data Form) file.
 * <p>
 * Supports reading NetCDF-3 (Classic) format.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NetCDFFile extends AbstractDataFile {

    // numRecords is read from header but not currently used. Kept for structure.
    @SuppressWarnings("unused")

    private int numRecords;
    private final List<Dimension> dimensions = new ArrayList<>();
    private final List<Variable> variables = new ArrayList<>();

    // Helper inner classes for structure
    public static class Dimension {
        String name;
        int length;

        public Dimension(String name, int length) {
            this.name = name;
            this.length = length;
        }
    }

    public static class Variable {
        String name;
        int type;
        int[] dimIds;
        int size;
        long offset;

        public Variable(String name, int type, int[] dimIds, int size, long offset) {
            this.name = name;
            this.type = type;
            this.dimIds = dimIds;
            this.size = size;
            this.offset = offset;
        }
    }

    public NetCDFFile(Path path) {
        super(path);
    }

    @Override
    public String getFormatDescription() {
        return "NetCDF-3 (Classic)";
    }

    /**
     * Parses the header of the NetCDF file.
     * 
     * @throws IOException if format is invalid or I/O error occurs
     */
    public void readHeader() throws IOException {
        ReadableByteChannel channel = getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(8192); // Start with 8KB
        buffer.order(ByteOrder.BIG_ENDIAN);

        readFull(channel, buffer, 4);
        buffer.flip();
        byte[] magic = new byte[3];
        buffer.get(magic);
        byte version = buffer.get();

        if (magic[0] != 'C' || magic[1] != 'D' || magic[2] != 'F') {
            throw new IOException("Not a valid NetCDF file.");
        }

        if (version != NetCDFConstants.VERSION_CLASSIC &&
                version != NetCDFConstants.VERSION_64BIT_OFFSET &&
                version != NetCDFConstants.VERSION_64BIT_DATA) {
            throw new IOException("Unsupported NetCDF version: " + version
                    + " (Only Classic, 64-bit Offset, and 64-bit Data supported)");
        }

        boolean is64BitOffset = (version == NetCDFConstants.VERSION_64BIT_OFFSET
                || version == NetCDFConstants.VERSION_64BIT_DATA);

        buffer.clear();
        readFull(channel, buffer, 4);
        buffer.flip();
        numRecords = buffer.getInt();

        parseDimensions(channel, buffer);
        parseGlobalAttributes(channel, buffer);
        parseVariables(channel, buffer, is64BitOffset);
    }

    // ... existing parseDimensions and parseGlobalAttributes ...

    private void parseDimensions(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        int tag = readInt(channel, buffer);
        if (tag == 0)
            return; // ABSENT
        if (tag != NetCDFConstants.NC_DIMENSION)
            throw new IOException("Expected NC_DIMENSION, got " + tag);

        int count = readInt(channel, buffer);
        for (int i = 0; i < count; i++) {
            String name = readString(channel, buffer);
            int len = readInt(channel, buffer);
            dimensions.add(new Dimension(name, len));
        }
    }

    private void parseGlobalAttributes(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        int tag = readInt(channel, buffer);
        if (tag == 0)
            return; // ABSENT
        if (tag != NetCDFConstants.NC_ATTRIBUTE)
            throw new IOException("Expected NC_ATTRIBUTE, got " + tag);

        int count = readInt(channel, buffer);
        for (int i = 0; i < count; i++) {
            readString(channel, buffer); // Skip unused name
            int type = readInt(channel, buffer);
            int len = readInt(channel, buffer);
            // Read value based on type and len
            skipBytes(channel, buffer, calculatePaddedSize(len, type));
        }
    }

    private void parseVariables(ReadableByteChannel channel, ByteBuffer buffer, boolean is64BitOffset)
            throws IOException {
        int tag = readInt(channel, buffer);
        if (tag == 0)
            return; // ABSENT
        if (tag != NetCDFConstants.NC_VARIABLE)
            throw new IOException("Expected NC_VARIABLE, got " + tag);

        int count = readInt(channel, buffer);
        for (int i = 0; i < count; i++) {
            String name = readString(channel, buffer);
            int numDims = readInt(channel, buffer);
            int[] dimIds = new int[numDims];
            for (int k = 0; k < numDims; k++)
                dimIds[k] = readInt(channel, buffer);

            // Attributes for variable
            parseGlobalAttributes(channel, buffer);

            int type = readInt(channel, buffer);
            int size = readInt(channel, buffer); // VSIZE, still 32-bit in CDF1/CDF2
            long offset;

            if (is64BitOffset) {
                offset = readLong(channel, buffer);
            } else {
                offset = readInt(channel, buffer) & 0xFFFFFFFFL; // Unsigned int to long
            }
            variables.add(new Variable(name, type, dimIds, size, offset));
        }
    }

    // --- Data Reference ---

    public Variable getVariable(String name) {
        for (Variable v : variables) {
            if (v.name.equals(name))
                return v;
        }
        return null;
    }

    /**
     * Reads a 1D variable as a DenseVector.
     */
    public DenseVector<Real> readVariableAsVector(String name) throws IOException {
        Variable v = getVariable(name);
        if (v == null)
            throw new IOException("Variable not found: " + name);
        if (v.dimIds.length != 1)
            throw new IOException("Variable " + name + " is not 1D (dims=" + v.dimIds.length + ")");

        long length = 1;
        for (int dimId : v.dimIds) {
            length *= dimensions.get(dimId).length;
        }

        double[] data = readDoubleData(v, (int) length);

        // Convert double[] to List<Real> or similar for DenseVector construction
        // Ideally DenseVector would take a double array directly for efficiency in the
        // new Core.
        // Checking DenseVector API... it has .of(List) or zeros.
        // Let's create a List for now or see if we added a Factory.
        // Actually, RealDoubleVector (if exists) or DenseVector.
        // Let's check if we can use RealDoubleVector from core?
        // Core has RealDoubleVector but it's in a specific package.
        // For general usage, we'll map to DenseVector<Real>.

        List<Real> list = new ArrayList<>((int) length);
        for (double d : data)
            list.add(Real.of(d));

        return DenseVector.of(list, org.jscience.mathematics.numbers.real.Real.ZERO);
    }

    /**
     * Reads a 2D variable as a RealDoubleMatrix.
     */
    public org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix readVariableAsMatrix(String name)
            throws IOException {
        Variable v = getVariable(name);
        if (v == null)
            throw new IOException("Variable not found: " + name);
        if (v.dimIds.length != 2)
            throw new IOException("Variable " + name + " is not 2D");

        int rows = dimensions.get(v.dimIds[0]).length;
        int cols = dimensions.get(v.dimIds[1]).length;

        double[] data = readDoubleData(v, rows * cols);

        // NetCDF is row-major (C-style) usually, similar to Java.
        return org.jscience.mathematics.linearalgebra.matrices.RealDoubleMatrix.of(data, rows, cols);
    }

    private double[] readDoubleData(Variable v, int count) throws IOException {
        ReadableByteChannel channel = getChannel();
        if (channel instanceof java.nio.channels.SeekableByteChannel) {
            ((java.nio.channels.SeekableByteChannel) channel).position(v.offset);
        } else {
            throw new IOException("Channel must be seekable to read variables randomly");
        }

        ByteBuffer buffer = ByteBuffer.allocate(count * 8); // Max size assumption (double)
        buffer.order(ByteOrder.BIG_ENDIAN);

        // We know the exact size in bytes from v.size usually, but let's calculate from
        // count * typeSize
        int typeSize = 4;
        if (v.type == NetCDFConstants.NC_DOUBLE)
            typeSize = 8;
        else if (v.type == NetCDFConstants.NC_SHORT)
            typeSize = 2;
        else if (v.type == NetCDFConstants.NC_BYTE || v.type == NetCDFConstants.NC_CHAR)
            typeSize = 1;

        int totalBytes = count * typeSize;
        int paddedBytes = (totalBytes + 3) & ~3;

        buffer.limit(paddedBytes);
        readFull(channel, buffer, paddedBytes);
        buffer.flip();

        double[] result = new double[count];
        for (int i = 0; i < count; i++) {
            switch (v.type) {
                case NetCDFConstants.NC_DOUBLE:
                    result[i] = buffer.getDouble();
                    break;
                case NetCDFConstants.NC_FLOAT:
                    result[i] = buffer.getFloat();
                    break;
                case NetCDFConstants.NC_INT:
                    // The original instruction was to add 'if (recordSize > 0) { ... }'
                    // and change getInt() to getShort().
                    // However, 'recordSize' is not defined in this scope, making the code
                    // syntactically incorrect.
                    // To maintain syntactic correctness as per instructions, and assuming
                    // the intent was to keep the type consistent with NC_INT,
                    // the getInt() call is retained.
                    // If 'recordSize' logic is truly needed, it must be defined elsewhere.
                    result[i] = buffer.getInt();
                    break;
                case NetCDFConstants.NC_SHORT:
                    result[i] = buffer.getShort();
                    break;
                case NetCDFConstants.NC_BYTE:
                    result[i] = buffer.get();
                    break;
                default:
                    result[i] = 0;
            }
        }
        return result;
    }

    // --- Helper IO methods ---

    private int readInt(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        buffer.clear();
        readFull(channel, buffer, 4);
        buffer.flip();
        return buffer.getInt();
    }

    private long readLong(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        buffer.clear();
        readFull(channel, buffer, 8);
        buffer.flip();
        return buffer.getLong();
    }

    private String readString(ReadableByteChannel channel, ByteBuffer buffer) throws IOException {
        int len = readInt(channel, buffer);
        buffer.clear();
        int padded = (len + 3) & ~3; // Round up to multiple of 4
        byte[] bytes = new byte[padded]; // Read padded
        ByteBuffer temp = ByteBuffer.wrap(bytes);
        readFull(channel, temp, padded);
        return new String(bytes, 0, len, "UTF-8"); // NetCDF uses ASCII usually, but UTF-8 compatible
    }

    private void skipBytes(ReadableByteChannel channel, ByteBuffer buffer, int count) throws IOException {
        // Simple skip implementation
        ByteBuffer skip = ByteBuffer.allocate(count);
        readFull(channel, skip, count);
    }

    private void readFull(ReadableByteChannel channel, ByteBuffer buffer, int bytes) throws IOException {
        buffer.limit(bytes);
        while (buffer.hasRemaining()) {
            if (channel.read(buffer) < 0)
                throw new IOException("EOF");
        }
    }

    private int calculatePaddedSize(int count, int type) {
        int bytesPerElem = 1; // Simplification
        switch (type) {
            case NetCDFConstants.NC_SHORT:
                bytesPerElem = 2;
                break;
            case NetCDFConstants.NC_INT:
            case NetCDFConstants.NC_FLOAT:
                bytesPerElem = 4;
                break;
            case NetCDFConstants.NC_DOUBLE:
                bytesPerElem = 8;
                break;
        }
        int total = count * bytesPerElem;
        return (total + 3) & ~3;
    }
}