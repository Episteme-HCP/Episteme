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

package org.jscience.io;

import org.jscience.util.AbstractIterator;
import org.jscience.util.logging.Logger;

import java.io.*;


/**
 * used to iterate over (potentially serialized) objects contained in a
 * binary file or inputstream.
 *
 * @author Holger Antelmann
 *
 * @see ExtendedFile#objectEnumerator()
 * @see ExtendedFile#objectEnumerator(boolean)
 * @see ExtendedFile#objectEnumerator(boolean,Logger)
 */
public class ObjectEnumerator extends AbstractIterator {
    /** DOCUMENT ME! */
    InputStream stream;

    /** DOCUMENT ME! */
    ObjectInputStream in;

    /** DOCUMENT ME! */
    boolean useDeserialization = false;

    /** DOCUMENT ME! */
    Logger logger;

/**
     * Creates a new ObjectEnumerator object.
     *
     * @param file               DOCUMENT ME!
     * @param useDeserialization DOCUMENT ME!
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public ObjectEnumerator(File file, boolean useDeserialization)
        throws FileNotFoundException {
        this(file, useDeserialization, null);
    }

/**
     * Creates a new ObjectEnumerator object.
     *
     * @param file               DOCUMENT ME!
     * @param useDeserialization DOCUMENT ME!
     * @param exceptionLogger    DOCUMENT ME!
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public ObjectEnumerator(File file, boolean useDeserialization,
        Logger exceptionLogger) throws FileNotFoundException {
        this(new FileInputStream(file), useDeserialization, exceptionLogger);
    }

/**
     * Creates a new ObjectEnumerator object.
     *
     * @param stream             DOCUMENT ME!
     * @param useDeserialization DOCUMENT ME!
     */
    public ObjectEnumerator(InputStream stream, boolean useDeserialization) {
        this(stream, useDeserialization, null);
    }

/**
     * Creates a new ObjectEnumerator object.
     *
     * @param stream             DOCUMENT ME!
     * @param useDeserialization DOCUMENT ME!
     * @param exceptionLogger    DOCUMENT ME!
     */
    public ObjectEnumerator(InputStream stream, boolean useDeserialization,
        Logger exceptionLogger) {
        this.stream = stream;
        this.useDeserialization = useDeserialization;
        logger = exceptionLogger;

        try {
            in = new ObjectInputStream(stream);
        } catch (IOException ex) {
            if (logger != null) {
                logger.log(ex);
            }
        }
    }

    /**
     * closes the embedded input stream
     *
     * @throws IOException DOCUMENT ME!
     */
    protected void finalize() throws IOException {
        in.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object getNextObject() throws Exception {
        // ensure to not run into EOF exception
        if (stream.available() < 1) {
            in.close();

            return null;
        }

        Exception exception = null;
        Object obj = null;

        try {
            obj = in.readObject();

            if ((useDeserialization) && (obj instanceof byte[])) {
                obj = IOUtils.deserialize((byte[]) obj);
            }
        } catch (Exception ex) {
            if (logger != null) {
                logger.log(ex);
            }

            exception = ex;
        }

        if (exception != null) {
            throw exception;
        }

        return obj;
    }
}
