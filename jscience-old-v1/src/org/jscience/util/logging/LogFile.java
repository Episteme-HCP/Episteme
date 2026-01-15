/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.util.Stopwatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * LogFile provides great convenience for logging information from a
 * program into a file. Every call to a write method is flushed immediatey, so
 * the content of a LogFile always accurately corresponds to how far a logging
 * program actually ran; i.e. the logging as a LogWriter is done
 * synchronously. IOExceptions are replaced with LogException, which should
 * make it easier to write the code for the logging program - with the usual
 * drawbacks of ensuring proper Exception handling. For most cases, however,
 * this should make things easier.
 *
 * @author Holger Antelmann
 *
 * @see LogException
 * @see Logger
 * @see LogEntry
 * @see DBLineLogFormatter
 */
public class LogFile extends AbstractLogWriter {
    /** DOCUMENT ME! */
    FileWriter fileWriter = null;

    /** DOCUMENT ME! */
    File file;

    /** DOCUMENT ME! */
    Stopwatch timer;

    /** DOCUMENT ME! */
    Logger logger;

    /** DOCUMENT ME! */
    String linebreak = System.getProperty("line.separator", "\n");

    /** DOCUMENT ME! */
    private boolean flush = true;

/**
     * This constructor simply calls <code>LogFile(new File(fileName))</code>
     *
     * @param fileName DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public LogFile(String fileName) throws IOException {
        this(new File(fileName));
    }

/**
     * uses a DBLineLogFormatter
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public LogFile(File file) throws IOException {
        this(file, new DBLineLogFormatter());
    }

/**
     * constructs a LogFile based on the given file and formatter
     *
     * @param file      DOCUMENT ME!
     * @param formatter DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public LogFile(File file, LogEntryFormatter formatter)
        throws IOException {
        //super(formatter);
        logger = new Logger(this);
        reInitialize(file, formatter);
    }

    /**
     * This method initializes the LogFile to a new file.
     *
     * @param file DOCUMENT ME!
     * @param formatter DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public synchronized void reInitialize(File file, LogEntryFormatter formatter)
        throws IOException {
        timer = new Stopwatch();

        if (fileWriter != null) {
            try {
                close();
            } catch (IOException ignore) {
            }
        }

        fileWriter = new FileWriter(file, true);
        setLogFormatter(formatter);
    }

    /**
     * resets the time of the embedded Stopwatch that gets initialized
     * with the constructor
     *
     * @see org.jscience.util.Stopwatch
     */
    public void resetTime() {
        timer.reset();
        logger.log(this, Level.CONFIG, "time reset");
    }

    /**
     * returns the elapsed time since LogFile initialization or since
     * the last call to resetTime().
     *
     * @return DOCUMENT ME!
     */
    public long elapsedTime() {
        return (timer.elapsed());
    }

    /**
     * This method writes a one-line version String of the entry into
     * the file.
     *
     * @see DBLineLogFormatter
     */
    public synchronized void writeLogEntry(Object pattern)
        throws LogException {
        try {
            write(pattern.toString());
        } catch (IOException ex) {
            throw new LogException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LogEntryFormatter getLogFormatter() {
        return super.getLogFormatter();
    }

    /**
     * DOCUMENT ME!
     *
     * @param formatter DOCUMENT ME!
     */
    public synchronized void setLogFormatter(LogEntryFormatter formatter) {
        super.setLogFormatter(formatter);
    }

    /**
     * writes a LogFile entry with the elapsed time since
     * initialization or since the last call to resetTime().
     *
     * @throws LogException DOCUMENT ME!
     */
    public void writeElapsedTime() throws LogException {
        logger.log(this, Level.CONFIG, timer.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeln() throws IOException {
        write(linebreak);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeln(String text) throws IOException {
        write(text + linebreak);
    }

    /**
     * This is the only method actually writing to the file; all other
     * 'write' methods call this method
     *
     * @param text DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    protected synchronized void write(String text) throws IOException {
        fileWriter.write(text);

        if (flush) {
            fileWriter.flush();
        }
    }

    /**
     * only needed if <code>getAlwaysFlush()</code> returns false
     *
     * @throws IOException DOCUMENT ME!
     */
    public void flush() throws IOException {
        fileWriter.flush();
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setAlwaysFlush(boolean flag) {
        flush = flag;
    }

    /**
     * true by default
     *
     * @return DOCUMENT ME!
     */
    public boolean getAlwaysFlush() {
        return flush;
    }

    /**
     * note that this function doesn't write serialized objects, but it
     * calls toString() on the object and writes the String to the file
     *
     * @param o DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeln(Object o) throws IOException {
        writeln(o.toString());
    }

    /**
     * closes the underlying writer. A note that the file was closed
     * will be written into the log.
     *
     * @throws IOException DOCUMENT ME!
     */
    public synchronized void close() throws IOException {
        logger.log(this, Level.CONFIG, "LogFile closed");
        flush();
        timer.stop();
        fileWriter.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public File getFile() {
        return file;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ("org.jscience.util.LogFile: " + file + "(" + super.toString() +
        ")");
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void finalize() throws Exception {
        close();
    }
}
