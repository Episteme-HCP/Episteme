/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
