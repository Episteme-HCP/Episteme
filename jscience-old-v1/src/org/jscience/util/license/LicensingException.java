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

package org.jscience.util.license;

import java.security.GeneralSecurityException;


/**
 * thrown to indicate that no valid license for a requested Object could be
 * obtained.
 *
 * @author Holger Antelmann
 *
 * @see LicenseManager
 * @see License
 */
public class LicensingException extends GeneralSecurityException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -5481412368042467878L;

/**
     * Creates a new LicensingException object.
     */
    LicensingException() {
        super("valid license required");
    }

/**
     * Creates a new LicensingException object.
     *
     * @param classWithoutLicense DOCUMENT ME!
     */
    LicensingException(Class classWithoutLicense) {
        super("no valid license for class: " + classWithoutLicense);
    }

/**
     * Creates a new LicensingException object.
     *
     * @param ex DOCUMENT ME!
     */
    LicensingException(Exception ex) {
        super(ex);
    }

/**
     * Creates a new LicensingException object.
     *
     * @param msg DOCUMENT ME!
     */
    LicensingException(String msg) {
        super(msg);
    }

/**
     * Creates a new LicensingException object.
     *
     * @param license DOCUMENT ME!
     */
    LicensingException(License license) {
        super((license == null) ? "no license available"
                                : ("not a valid license: " + license));
    }
}
