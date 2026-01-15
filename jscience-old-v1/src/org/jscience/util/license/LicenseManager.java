/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.util.license;

import org.jscience.io.IOUtils;
import org.jscience.io.ObjectEnumerator;
import org.jscience.net.Spider;
import org.jscience.util.ResourceNotFoundException;
import org.jscience.util.SecurityNames;
import org.jscience.util.Settings;
import org.jscience.util.logging.Level;
import org.jscience.util.logging.Logger;

import java.io.*;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * LicenseManager provides the ability to use limited licenses based on classes
 * and package names.
 * Note that the LicenseManager only accepts Licenses that are properly signed,
 * i.e. they will have to be created by an authorized issuer.
 * <p/>
 * Once the LicenseManager is initialized, a set of licenses is installed
 * from the <dfn>licenses.dat</dfn> file in the resource directory from the
 * distribution; if any licenses therein are invalid or the file is non-existent,
 * the resulting errors are ignored and the LicenseManager contains no licenses
 * to begin with.
 * <p/>
 * Generally, an application can go without using the LicenseManager explicitly.
 * The class <code>org.jscience.util.Settings</code> provides a few convenience
 * methods that may suffice for a standard application.
 *
 * @author Holger Antelmann
 * @see org.jscience.util.Settings
 * @see License
 * @see Licensed
 * @see LicenseHandler
 * @see LicensingException
 * @see org.jscience.util.Settings#checkLicense(Object)
 * @see org.jscience.util.Settings#installLicenses(java.io.InputStream)
 * @see org.jscience.util.Settings#installLicense(org.jscience.License)
 * @since 12/31/2003
 */
public final class LicenseManager {
    // bound to the certificate file
    private static int validationHashCode = -1884203205;
    private static LicenseManager manager = null;

    String licenseLocation = "org/jscience/licenses.dat";
    URL certLocation;
    private HashMap<Object, License> licenses = new HashMap<Object, License>();
    private byte[] certificateContent;
    Certificate certificate;
    Logger logger = new Logger();

    private LicenseManager() throws SecurityException {
        try {
            certLocation = Settings.getResource("org/jscience/antelmann.cer");
            certificateContent = new Spider(certLocation).getBytes();
        } catch (ResourceNotFoundException ex) {
            throw new SecurityException("the license certificate cannot be found", ex);
        } catch (IOException ex) {
            throw new SecurityException("the license certificate cannot be loaded", ex);
        }
        try {
            String s = new String(certificateContent, "US-ASCII");
            if (s.hashCode() != validationHashCode) {
                throw new SecurityException("the certificate has been tampered with");
            }
            CertificateFactory cf = CertificateFactory.getInstance(SecurityNames.certificateType[0]);
            InputStream in = new ByteArrayInputStream(certificateContent);
            certificate = cf.generateCertificate(in);
            URL licenseURL = Settings.getResource(licenseLocation);
            loadFrom(licenseURL.openStream());
        } catch (LicensingException ignore) {
            // the license file didn't contain proper licenses - so what
        } catch (ResourceNotFoundException ignore) {
            // the license file doesn't exist - so what
        } catch (GeneralSecurityException ex) {
            throw new SecurityException(ex);
        } catch (UnsupportedEncodingException ex) {
            throw new SecurityException(ex);
        } catch (IOException ex) {
            throw new SecurityException(ex);
        }
    }

    /**
     * returns the logger that logs changes and access to the LicenseManager
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * returns the global LicenseManager object (there is only one)
     *
     * @throws java.lang.SecurityException if the correct certificate cannot be found
     */
    public static synchronized LicenseManager getLicenseManager() throws SecurityException {
        if (manager == null) {
            manager = new LicenseManager();
        }
        return manager;
    }

    /**
     * a convenience method that installs serialized licenses from a file
     */
    public synchronized void loadFrom(File file) throws FileNotFoundException, LicensingException {
        loadFrom(new FileInputStream(file));
    }

    /**
     * a convenience method that installs serialized licenses from a URL
     */
    public synchronized void loadFrom(URL url) throws IOException, LicensingException {
        loadFrom(url.openStream());
    }

    /**
     * a convenience method that installs serialized licenses from a stream.
     * Possible IOExceptions are ignored.
     */
    public synchronized void loadFrom(InputStream stream) throws LicensingException {
        logger.log(this, Level.CONFIG, "loading licenses from stream");
        ObjectEnumerator e = new ObjectEnumerator(stream, true);
        while (e.hasMoreElements()) {
            Object obj = e.nextElement();
            if (obj instanceof License) install((License) obj);
        }
    }

    /**
     * each license is written as a serialized byte array
     */
    public synchronized void store(OutputStream stream) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(stream);
        Iterator<License> i = getLicenses().iterator();
        while (i.hasNext()) out.writeObject(IOUtils.serialize(i.next()));
        out.flush();
        out.close();
    }

    /**
     * returns an umnodifiable view of the embedded licenses
     */
    public synchronized Collection<License> getLicenses() {
        return Collections.unmodifiableCollection(licenses.values());
    }

    /**
     * checks for a valid license for the given object and only returns
     * gracefully if a valid license was found. The <code>use()</code> method
     * of the license object will be called if a valid license was found.
     *
     * @throws LicensingException if either no license was found or the found
     *                            license was invalid
     * @see License#use()
     */
    public synchronized void check(Object licensee) throws LicensingException {
        logger.log(this, Level.FINE, "license check for " + licensee);
        License license = getLicense(licensee);
        if (license == null)
            throw new LicensingException("no license available for " + ((licensee instanceof Package) ?
                    licensee : licensee.getClass().getName()));
        //verifyLicense(license);
        license.use();
    }

    /**
     * installs the given license into this LicenseManager, so that it can be
     * found if a check on the licensee is made.
     * Only properly signed licenses can be installed.
     *
     * @return the previously installed license for the licensee embedded within
     *         the given license - if any
     * @throws LicensingException if the license cannot be verified (i.e. if it is
     *                            not properly signed)
     * @see #check(Object)
     * @see #verifyLicense(License)
     */
    public synchronized License install(License l) throws LicensingException {
        verifyLicense(l);
        logger.log(this, Level.CONFIG, "license installed", l);
        return licenses.put(l.getLicensee(), l);
    }

    public synchronized License removeLicense(License l) {
        return licenses.remove(l.getLicensee());
    }

    public synchronized void removeAllLicenses() {
        logger.log(this, Level.CONFIG, "all licenses removed");
        licenses.clear();
    }

    /**
     * returns the license in use for the given licensee
     */
    public synchronized License getLicense(Object licensee) {
        if ((licensee instanceof Package)) {
            licensee = ((Package) licensee).getName();
        } else if (!(licensee instanceof Class) && !(licensee instanceof String)) {
            licensee = licensee.getClass();
        }
        License l = licenses.get(licensee);
        if (l != null) return l;
        String pName;
        if (licensee instanceof String) {
            pName = (String) licensee;
        } else if (licensee instanceof Package) {
            pName = ((Package) licensee).getName();
        } else {
            pName = ((Class) licensee).getPackage().getName();
        }
        while (pName != null) {
            l = licenses.get(pName);
            if (l != null) return l;
            pName = getParentPackageName(pName);
        }
        return licenses.get(pName);
    }

    static String getParentPackageName(String name) {
        int i = name.lastIndexOf(".");
        if (i < 0) return null;
        return name.substring(0, i);
    }

    /**
     * checks the signature against the certificate.
     * Note: a license passing this verification may still be invalid due
     * to expiration or other constrains.
     */
    public synchronized void verifyLicense(License l) throws LicensingException {
        try {
            if (l.getSignature() == null)
                throw new LicensingException("license has not been signed: " + l);
            Signature sig = Signature.getInstance(SecurityNames.ditigalSignatureAlgorithm[2]);
            sig.initVerify(certificate.getPublicKey());
            sig.update(l.verification());
            if (!sig.verify(l.getSignature()))
                throw new LicensingException("not a valid license: " + l);
        } catch (GeneralSecurityException ex) {
            throw new LicensingException(ex);
        }
    }
}
