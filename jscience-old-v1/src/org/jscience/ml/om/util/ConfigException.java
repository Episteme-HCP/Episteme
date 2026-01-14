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

/* ====================================================================
 * /util/ConfigException.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om.util;

import org.jscience.ml.om.FGCAException;


/**
 * The ConfigException indicates problems during loading or initializing of
 * configuration information.<br>
 * Typical causes of a ConfigException may be: Corrupted JAR file, corrupted
 * JAR file entry, corrupted config file (header information bad, or not
 * given, type or classpath not set...)
 *
 * @author doergn@users.sourceforge.net
 *
 * @since 1.0
 */
public class ConfigException extends FGCAException {
    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------
/**
     * Constructs a new ConfigException.<br>
     *
     * @param message Message which describes the cause of the
     *                problem.
     */
    public ConfigException(String message) {
        super(message);
    }

    // -------------------------------------------------------------------
/**
     * Constructs a new ConfigException.<br>
     *
     * @param message Message which describes the cause of the
     *                problem.
     * @param cause   A exception that was the root cause of this
     *                ConfigException.
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
