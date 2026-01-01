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

package org.jscience.mathematics.loaders.netcdf;

/**
 * Constants for NetCDF-3 (Classic) format.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class NetCDFConstants {

    private NetCDFConstants() {
    }

    // Magic Headers: 'C', 'D', 'F', version
    public static final byte[] MAGIC = { (byte) 'C', (byte) 'D', (byte) 'F' };
    public static final byte VERSION_CLASSIC = 1;
    public static final byte VERSION_64BIT_OFFSET = 2;
    public static final byte VERSION_64BIT_DATA = 5;

    // Tags
    public static final int NC_DIMENSION = 10;
    public static final int NC_VARIABLE = 11;
    public static final int NC_ATTRIBUTE = 12;

    // Zero-padding
    public static final int PADDING_BLOCK = 4;

    // Data Types (nc_type)
    public static final int NC_BYTE = 1;
    public static final int NC_CHAR = 2;
    public static final int NC_SHORT = 3;
    public static final int NC_INT = 4;
    public static final int NC_FLOAT = 5;
    public static final int NC_DOUBLE = 6;
}

