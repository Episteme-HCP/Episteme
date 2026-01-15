/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

/**
 * A LogExceptionHandler is used to control the behavior of a Logger in regards
 * to occuring LogException events.
 *
 * @author Holger Antelmann
 * @see LogException
 * @see Logger#setExceptionHandler(LogExceptionHandler)
 */
public interface LogExceptionHandler {
    /**
     * used to simply ignore exceptions thrown by a logger
     *
     * @see Logger#ignoreLogExceptions()
     */
    public static LogExceptionHandler EXCEPTION_IGNORER = new LogExceptionHandler() {
            public void handleLogException(Logger logger, LogException ignore,
                LogWriter irrelevant) {
            }
        };

    /**
     * is called when a logger encounteres a LogException while the
     * handler logs an entry. This method can control whether the exception is
     * propagated back to the Logger or ignored by the application.
     *
     * @see Logger#setExceptionHandler(LogExceptionHandler)
     */
    void handleLogException(Logger logger, LogException ex, LogWriter writer)
        throws LogException;
}
