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
