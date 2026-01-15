/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.io.ExtendedFile;
import org.jscience.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * A LogWriter that logs the entries synchronously serialized to a file.
 * The implementation assumes that all given parameters in a LogEntry are
 * serializable. To read from a BinaryFileLog, you can also use the
 * ObjectEnumerator from the class <code>org.jscience.io.ExtendedFile</code>.
 * Note that if the BinaryFileLog was initialized with either the option
 * keepConnectionAlive set to false or the option append set to true, the
 * individual log entries  are separately serialized and written as byte[]; so
 * if you want to recover the LogEntry objects, you'll need to deserialize
 * then. For the deserialization, you can use the org.jscience.io.IOUtils
 * class.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.io.ExtendedFile#objectEnumerator(boolean)
 * @see org.jscience.io.IOUtils#deserialize(byte[])
 * @since 12/22/2002
 */
public class BinaryFileLog extends AbstractLogWriter {
    /** DOCUMENT ME! */
    ExtendedFile file;

    /** DOCUMENT ME! */
    ObjectOutputStream out;

    /** DOCUMENT ME! */
    boolean serialize = false;

/**
     * Creates a new BinaryFileLog object.
     *
     * @param file                DOCUMENT ME!
     * @param keepConnectionAlive DOCUMENT ME!
     * @param append              DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public BinaryFileLog(File file, boolean keepConnectionAlive, boolean append)
        throws IOException {
        this.file = new ExtendedFile(file);

        if (keepConnectionAlive) {
            out = new ObjectOutputStream(new FileOutputStream(file, append));
        } else {
            if (!append && file.exists()) {
                file.delete();
            }
        }

        serialize = append;
    }

    /**
     * writes the complete serialized information and flushes
     * synchronously
     *
     * @param pattern DOCUMENT ME!
     *
     * @throws LogException DOCUMENT ME!
     */
    public synchronized void writeLogEntry(Object pattern)
        throws LogException {
        try {
            if (out == null) {
                file.writeObject(IOUtils.serialize(pattern), true);
            } else {
                if (serialize) {
                    pattern = IOUtils.serialize(pattern);
                }

                out.writeObject(pattern);
            }
        } catch (IOException ex) {
            throw new LogException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public synchronized void close() throws IOException {
        if (out != null) {
            out.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    protected void finalize() throws IOException {
        close();
    }
}
