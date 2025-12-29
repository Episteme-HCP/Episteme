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

package org.jscience.mathematics.loaders;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.OpenOption;

/**
 * Represents a scientific data file (e.g., FITS, HDF5, NetCDF).
 * <p>
 * This interface provides a unified way to access metadata and data blocks
 * within a structured scientific file. Implementations leverage Java NIO.2
 * for efficient I/O operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface DataFile extends AutoCloseable {

    /**
     * Returns the path to this file.
     *
     * @return the file path, or {@code null} if this is an in-memory or
     *         stream-based file.
     */
    Path getPath();

    /**
     * Opens this file with the specified options.
     *
     * @param options the options specifying how the file is opened
     * @throws IOException if an I/O error occurs
     */
    void open(OpenOption... options) throws IOException;

    /**
     * Returns the underlying byte channel for this file.
     * <p>
     * This allows direct low-level access if needed, though usually standard
     * read/write methods should be preferred.
     * </p>
     *
     * @return the byte channel, or {@code null} if not open.
     */
    SeekableByteChannel getChannel();

    /**
     * Closes this file and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs
     */
    @Override
    void close() throws IOException;

    /**
     * Returns a description of the file format version or standard (e.g. "FITS
     * 4.0").
     * 
     * @return the format description.
     */
    String getFormatDescription();
}