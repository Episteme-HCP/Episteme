/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.license;


//import org.jscience.util.*;
/**
 * can be used to obtain a license from some source if a call to
 * <code>Settings.checkLicense(Object)</code> initially fails. This callback
 * handler could be used e.g. to try to renew a license from some well known
 * server once the local license expired. Another alternative would be to open
 * up a dialog where the user can select a file to import a license.
 *
 * @author Holger Antelmann
 * @see org.jscience.util.Settings#checkLicense(Object)
 * @see org.jscience.util.Settings#setLicenseHandler(LicenseHandler)
 * @see LicenseManager
 * @see License
 */
public interface LicenseHandler {
    /**
     * called if the first attempt to call
     * <code>Settings.checkLicense(Object)</code> fails internally with a
     * <code>LicensingException</code>. The call to
     * <code>Settings.checkLicense(Object)</code> can only finish without
     * throwing a SecurityException if this method can successfully install a
     * valid license for the given licensee
     *
     * @param licensee DOCUMENT ME!
     *
     * @see org.jscience.util.Settings#checkLicense(Object)
     * @see LicenseManager
     */
    void aquireLicense(Object licensee);
}
