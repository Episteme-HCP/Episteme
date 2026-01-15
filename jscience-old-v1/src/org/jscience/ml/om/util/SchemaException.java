/* ====================================================================
 * /util/SchemaException.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om.util;

import org.jscience.ml.om.FGCAException;


/**
 * The SchemaException indicates problems during loading or parsing of a
 * xml schema file.<br>
 * Typical causes of a SchemaException may be: Malformed XML Documents or
 * invalid schema elements (required value missing).
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public class SchemaException extends FGCAException {
    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
/**
     * Constructs a new SchemaException.<br>
     *
     * @param message Message which describes the cause of the
     *                problem.
     */
    public SchemaException(String message) {
        super(message);
    }

    // -------------------------------------------------------------------
/**
     * Constructs a new SchemaException.<br>
     *
     * @param message Message which describes the cause of the
     *                problem.
     * @param cause   A exception that was the root cause of this
     *                ConfigException.
     */
    public SchemaException(String message, Throwable cause) {
        super(message, cause);
    }
}
