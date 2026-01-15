/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
