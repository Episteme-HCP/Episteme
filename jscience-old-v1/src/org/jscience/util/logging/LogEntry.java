/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import java.io.Serializable;


/**
 * LogEntry represents a single record of a log logged by a Logger object
 * and handled by a LogWriter object. As opposed to
 * java.util.logging.LogRecord, this class also maintains a stack trace. Note
 * on serialization: as this class contains an array of Object, the
 * serialization of an instance may throw an IOException if one of the
 * contained objects is not serializable. A workaround may be to use the
 * <code>Object.toString()</code> function to serialize the object; this could
 * either be done through subclassing or through the LogWriter object that
 * handles the serialization.
 *
 * @author Holger Antelmann
 *
 * @see Logger
 * @see LogWriter
 */
public class LogEntry implements Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1463226425379920488L;

    /** DOCUMENT ME! */
    Level level = Level.DEFAULT;

    /** DOCUMENT ME! */
    String message;

    /** DOCUMENT ME! */
    long time = System.currentTimeMillis();

    /** DOCUMENT ME! */
    String sourceClassName;

    /** DOCUMENT ME! */
    String sourceString;

    /** DOCUMENT ME! */
    Throwable thrown;

    /** DOCUMENT ME! */
    String threadName;

    /** DOCUMENT ME! */
    StackTraceElement[] stack;

    /** DOCUMENT ME! */
    Object[] parameters;

/**
     * Creates a new LogEntry object.
     */
    public LogEntry() {
    }

/**
     * Creates a new LogEntry object.
     *
     * @param level           DOCUMENT ME!
     * @param message         DOCUMENT ME!
     * @param time            DOCUMENT ME!
     * @param sourceClassName DOCUMENT ME!
     * @param sourceString    DOCUMENT ME!
     * @param thrown          DOCUMENT ME!
     * @param threadName      DOCUMENT ME!
     * @param stack           DOCUMENT ME!
     */
    public LogEntry(Level level, String message, long time,
        String sourceClassName, String sourceString, Throwable thrown,
        String threadName, StackTraceElement[] stack, Object... parameters) {
        setLevel(level);
        this.message = message;
        this.time = time;
        this.sourceClassName = sourceClassName;
        this.thrown = thrown;
        this.threadName = threadName;
        this.stack = stack;
        this.parameters = parameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Level getLevel() {
        return level;
    }

    /**
     * DOCUMENT ME!
     *
     * @param level DOCUMENT ME!
     */
    public void setLevel(Level level) {
        this.level = level;

        if (level == null) {
            this.level = Level.DEFAULT;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMessage() {
        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSourceString() {
        return sourceString;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sourceString DOCUMENT ME!
     */
    public void setSourceString(String sourceString) {
        this.sourceString = sourceString;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSourceClassName() {
        return sourceClassName;
    }

    /**
     * uses the context class loader of the current thread
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Class getSourceClass() throws ClassNotFoundException {
        return Thread.currentThread().getContextClassLoader()
                     .loadClass(sourceClassName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param classLoader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Class getSourceClass(ClassLoader classLoader)
        throws ClassNotFoundException {
        return ClassLoader.getSystemClassLoader().loadClass(sourceClassName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param sourceClassName DOCUMENT ME!
     */
    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Throwable getThrown() {
        return thrown;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thrown DOCUMENT ME!
     */
    public void setThrown(Throwable thrown) {
        this.thrown = thrown;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getThreadName() {
        return threadName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param threadName DOCUMENT ME!
     */
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StackTraceElement[] getStackTrace() {
        return stack;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stack DOCUMENT ME!
     */
    public void setStackTrace(StackTraceElement[] stack) {
        this.stack = stack;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parameters DOCUMENT ME!
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }
}
