/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

/**
 * indicates that some content was not in the expected format
 *
 * @author Holger Antelmann
 */
public class FormatException extends Exception {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -1514322501591901535L;

    /** DOCUMENT ME! */
    Object formattedContent = null;

/**
     * Creates a new FormatException object.
     */
    public FormatException() {
    }

/**
     * Creates a new FormatException object.
     *
     * @param content DOCUMENT ME!
     */
    public FormatException(Object content) {
        formattedContent = content;
    }

/**
     * Creates a new FormatException object.
     *
     * @param msg DOCUMENT ME!
     */
    public FormatException(String msg) {
        super(msg);
    }

/**
     * Creates a new FormatException object.
     *
     * @param msg     DOCUMENT ME!
     * @param content DOCUMENT ME!
     */
    public FormatException(String msg, Object content) {
        super(msg);
        formattedContent = content;
    }

/**
     * Creates a new FormatException object.
     *
     * @param cause DOCUMENT ME!
     */
    public FormatException(Throwable cause) {
        super(cause);
    }

/**
     * Creates a new FormatException object.
     *
     * @param msg   DOCUMENT ME!
     * @param cause DOCUMENT ME!
     */
    public FormatException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * DOCUMENT ME!
     *
     * @param content DOCUMENT ME!
     */
    public void setFormattedContent(Object content) {
        formattedContent = content;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getFormattedContent() {
        return formattedContent;
    }
}
