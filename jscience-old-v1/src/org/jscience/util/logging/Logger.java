/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.util.Filter;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Logger somewhat leans on the functionality of the java.util.logging
 * functionality of J2SE 1.4, but it's not quite the same (and was implemented
 * before J2SE 1.4).<p>In addition to the J2SE functionality, it provides
 * the full stack trace for the log. Whether or not the logging here is
 * written synchronously or asynchronously, is left to the implementation for
 * each LogWriter that is used with the Logger.</p>
 * <p>All logging methods may throw LogException - propagated from the
 * LogWriter objects contained in the logger to the calling thread. The
 * writing to the different LogWriters is always performed sequentially in the
 * order the LogWriters were added to the Logger.</p>
 * <p>It is recommended to always use a log method with the origin
 * parameter and pass the 'this' argument from the calling function; this will
 * provide useful additional information for the log.</p>
 *
 * @author Holger Antelmann
 * @see LogWriter
 * @see LogEntry
 * @see LogException
 */
public class Logger {
    /**
     * DOCUMENT ME!
     */
    protected ArrayList<LogWriter> writers;

    /**
     * DOCUMENT ME!
     */
    boolean includeStack = false;

    /**
     * DOCUMENT ME!
     */
    boolean enabled = true;

    /**
     * DOCUMENT ME!
     */
    Filter<LogEntry> filter = null;

    /**
     * DOCUMENT ME!
     */
    LogExceptionHandler handler = null;

    /**
     * creates an empty Logger that does not write to any log yet
     */
    public Logger() {
        writers = new ArrayList<LogWriter>();
    }

    /**
     * Creates a new Logger object.
     */
    public Logger(LogWriter... handler) {
        this();

        for (LogWriter w : handler) {
            writers.add(w);
        }
    }

    /**
     * uses the configuration from the given logger (including writers and filter)
     */
    public Logger(Logger logger) {
        this();
        addWritersFromLogger(logger);
        filter = logger.getFilter();
        includeStack = logger.includeStack;
        enabled = logger.enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LogWriter[] getWriters() {
        return (LogWriter[]) writers.toArray(new LogWriter[writers.size()]);
    }

    /**
     * adds all LogWriter handler from the given logger
     *
     * @param logger DOCUMENT ME!
     */
    public synchronized void addWritersFromLogger(Logger logger) {
        writers.addAll(logger.writers);
    }

    /**
     * returns true if a stack trace is produced when a Logger method
     * creates a LogEntry (false by default)
     *
     * @return DOCUMENT ME!
     */
    public boolean includesStack() {
        return includeStack;
    }

    /*
    * determines whether a stack trace is produced when a Logger
    * method creates a LogEntry. Having this set to true will slow
    * down the logging (as the stack trace needs to be produced),
    * but provides more information.
    */
    public void setIncludeStack(boolean on) {
        includeStack = on;
    }

    /**
     * adds the given handler to the list of handlers this Logger
     * writes to
     *
     * @param writer DOCUMENT ME!
     * @return true if the set did not already contain the writer
     */
    public synchronized boolean addWriter(LogWriter writer) {
        return writers.add(writer);
    }

    /**
     * removes the given writer from the list of log writers
     *
     * @param writer DOCUMENT ME!
     * @return true if the set contained the handler
     */
    public synchronized boolean removeWriter(LogWriter writer) {
        return writers.remove(writer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param writer DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public boolean containsWriter(LogWriter writer) {
        return writers.contains(writer);
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void removeAllWriters() {
        writers.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(String message, Object... parameters)
            throws LogException {
        log(null, message, parameters);
    }

    /**
     * DOCUMENT ME!
     *
     * @param level   DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Level level, String message, Object... parameters)
            throws LogException {
        log(null, level, message, parameters);
    }

    /**
     * DOCUMENT ME!
     *
     * @param thrown DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Throwable thrown) throws LogException {
        String className = ((thrown.getStackTrace().length > 0)
                ? thrown.getStackTrace()[0].getClassName()
                : thrown.getClass().getName());
        log(Level.EXCEPTION, thrown.getMessage(), System.currentTimeMillis(),
                className, null, thrown, Thread.currentThread().getName(), null,
                null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param thrown  DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(String message, Throwable thrown, Object... parameters)
            throws LogException {
        log(null, null, message, thrown, parameters);
    }

    /**
     * DOCUMENT ME!
     *
     * @param origin  DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Object origin, String message, Object... parameters)
            throws LogException {
        log(origin, null, message, null, parameters);
    }

    /**
     * DOCUMENT ME!
     *
     * @param origin  DOCUMENT ME!
     * @param level   DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Object origin, Level level, String message,
                    Object... parameters) throws LogException {
        log(origin, level, message, null, parameters);
    }

    /**
     * DOCUMENT ME!
     *
     * @param origin DOCUMENT ME!
     * @param thrown DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Object origin, Throwable thrown) throws LogException {
        log(origin, Level.EXCEPTION, thrown.getMessage(), thrown);
    }

    /**
     * DOCUMENT ME!
     *
     * @param origin  DOCUMENT ME!
     * @param level   DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param thrown  DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Object origin, Level level, String message,
                    Throwable thrown, Object... parameters) throws LogException {
        if (!enabled) {
            return;
        }

        if (writers.isEmpty()) {
            return;
        }

        StackTraceElement[] stack = null;

        if (includeStack) {
            Thread t = Thread.currentThread();
            stack = t.getStackTrace();
        }

        log(level, message, System.currentTimeMillis(),
                (origin != null)
                        ? ((origin instanceof Class) ? ((Class) origin).getName()
                        : origin.getClass().getName()) : null,
                (origin != null) ? origin.toString() : null, thrown,
                Thread.currentThread().getName(), stack, parameters);
    }

    /**
     * DOCUMENT ME!
     *
     * @param level        DOCUMENT ME!
     * @param message      DOCUMENT ME!
     * @param time         DOCUMENT ME!
     * @param sourceClass  DOCUMENT ME!
     * @param sourceString DOCUMENT ME!
     * @param thrown       DOCUMENT ME!
     * @param threadName   DOCUMENT ME!
     * @param stack        DOCUMENT ME!
     * @param parameters   DOCUMENT ME!
     * @throws LogException DOCUMENT ME!
     */
    public void log(Level level, String message, long time, String sourceClass,
                    String sourceString, Throwable thrown, String threadName,
                    StackTraceElement[] stack, Object[] parameters)
            throws LogException {
        if (!enabled) {
            return;
        }

        if (writers.isEmpty()) {
            return;
        }

        log(new LogEntry(level, message, time, sourceClass, sourceString,
                thrown, threadName, stack, parameters));
    }

    /**
     * the logger only logs if the return value is true (which it is by
     * default)
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setEnabled(boolean flag) {
        enabled = flag;
    }

    /**
     * returns null by default
     *
     * @return DOCUMENT ME!
     */
    public Filter<LogEntry> getFilter() {
        return filter;
    }

    /**
     * returns true only if the given entry will be logged by this
     * logger and thus propagated to its handlers
     *
     * @see #getWriters()
     */
    public boolean accept(LogEntry entry) {
        return ((filter == null) || (filter.accept(entry)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void setFilter(Filter<LogEntry> filter) {
        this.filter = filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param handler DOCUMENT ME!
     */
    public void setExceptionHandler(LogExceptionHandler handler) {
        this.handler = handler;
    }

    /**
     * if the LogExceptionHandler is null (which it is by default), any
     * LogException during logging is propagated to the calling application.
     *
     * @see #ignoreLogExceptions()
     */
    public LogExceptionHandler getExceptionHandler() {
        return handler;
    }

    /**
     * sets a LogExceptionHandler that simply ignores any given
     * LogException, so no Exception will be propagated to the calling
     * application if logging fails. To reverse the effect of calling this
     * method, simply set the LogExceptionHandler back to a null value - or
     * use any other customized LogExceptionHandler.
     *
     * @see #setExceptionHandler(LogExceptionHandler)
     */
    public void ignoreLogExceptions() {
        handler = LogExceptionHandler.EXCEPTION_IGNORER;
    }

    /**
     * logs the entry providing the logger is enabled and the filter
     * (if available) accepts the entry.
     *
     * @param entry DOCUMENT ME!
     * @throws LogException if a LogWriter throws a LogException during
     *                      logging. The exception handling can be controlled by setting an
     *                      LogExceptionHandler. If no LogExceptionHandler is configured,
     *                      the Exception is simply propagated to the calling method.
     * @see #getFilter()
     * @see #isEnabled()
     * @see #ignoreLogExceptions()
     * @see #setExceptionHandler(LogExceptionHandler)
     */
    public synchronized void log(LogEntry entry) throws LogException {
        if (!enabled) {
            return;
        }

        if ((filter != null) && (!filter.accept(entry))) {
            return;
        }

        Iterator i = writers.iterator();
        LogWriter writer = null;

        while (i.hasNext()) {
            try {
                writer = (LogWriter) i.next();

                if (writer.accept(entry)) {
                    writer.write(entry);
                }
            } catch (LogException ex) {
                if (handler == null) {
                    throw ex;
                }

                handler.handleLogException(this, ex, writer);
            }
        }
    }
}
