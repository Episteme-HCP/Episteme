/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

/**
 * LogException is thrown when a problem is encountered while writing to a
 * LogWriter
 *
 * @author Holger Antelmann
 *
 * @see LogWriter
 */
public class LogException extends RuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -6491689248403436729L;

/**
     * Creates a new LogException object.
     */
    public LogException() {
        super();
    }

/**
     * Creates a new LogException object.
     *
     * @param text DOCUMENT ME!
     */
    public LogException(String text) {
        super(text);
    }

/**
     * Creates a new LogException object.
     *
     * @param cause DOCUMENT ME!
     */
    public LogException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new LogException object.
     *
     * @param text  DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public LogException(String text, Throwable cause) {
        super(text, cause);
    }
}
