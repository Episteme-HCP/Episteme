/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.license;

import org.jscience.io.ExtendedFile;
import org.jscience.io.IOUtils;

import java.io.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * a License provides and limits access to certain functionality based on
 * individual classes or packages. Note that for a License to be accepted by
 * the LicenseManager, the License must be properly signed. A method for
 * properly signing a License is not available under GPL, but there are
 * License objects freely available on request.
 *
 * @author Holger Antelmann
 *
 * @see LicenseManager
 * @see LicensingException
 */
public final class License implements Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -7083336284375644716L;

    /** DOCUMENT ME! */
    public static final int INDEFINATE = -1;

    /** DOCUMENT ME! */
    private byte[] signature = null;

    /** DOCUMENT ME! */
    private Object licensee;

    /** DOCUMENT ME! */
    private String issuedFor;

    /** DOCUMENT ME! */
    private Date creationDate;

    /** DOCUMENT ME! */
    private Date expirationDate;

    /** DOCUMENT ME! */
    private int usageLimit;

    /** DOCUMENT ME! */
    private int usageLeft;

    /** DOCUMENT ME! */
    private HashMap<String, String> boundProperties;

    /** DOCUMENT ME! */
    private HashMap<String, String> boundEnv;

/**
     * only usable if properly signed
     */
    public License(Package licenseePackage, String issuedFor,
        Date expirationDate, int usageLimit) {
        this((Object) licenseePackage, issuedFor, expirationDate, usageLimit);
    }

/**
     * only usable if properly signed
     */
    public License(Class licenseeClass, String issuedFor, Date expirationDate,
        int usageLimit) {
        this((Object) licenseeClass, issuedFor, expirationDate, usageLimit);
    }

/**
     * only usable if properly signed
     */
    public License(String classOrPackageName, String issuedFor,
        Date expirationDate, int usageLimit) {
        this((Object) classOrPackageName, issuedFor, expirationDate, usageLimit);
    }

/**
     * Creates a new License object.
     *
     * @param licensee       DOCUMENT ME!
     * @param issuedFor      DOCUMENT ME!
     * @param expirationDate DOCUMENT ME!
     * @param usageLimit     DOCUMENT ME!
     */
    private License(Object licensee, String issuedFor, Date expirationDate,
        int usageLimit) {
        creationDate = new Date();

        if (licensee instanceof String) {
            try {
                licensee = Class.forName((String) licensee);
            } catch (Throwable t) {
                // assume it's a package, so just store the string value
                licensee = licensee.toString();
            }
        }

        this.licensee = licensee;
        this.issuedFor = issuedFor;
        this.expirationDate = expirationDate;

        if (usageLimit < 0) {
            usageLimit = -1;
        }

        this.usageLimit = usageLimit;
        usageLeft = usageLimit;
        boundProperties = new HashMap<String, String>();
        boundEnv = new HashMap<String, String>();
    }

    /**
     * returns the data that serves as a basis for generating the
     * signature.
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public byte[] verification() {
        try {
            String v = licensee + " " + issuedFor + expirationDate + " " +
                usageLimit;

            // add bound values
            for (String key : boundProperties.keySet()) {
                v += (" " + key + ":" + boundProperties.get(key));
            }

            for (String key : boundEnv.keySet()) {
                v += (" " + key + ":" + boundEnv.get(key));
            }

            return v.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * allows to add a condition where this license can only be used if
     * the given environment variable corresponds to the given value. Bound
     * values can only be changed before a license was signed.
     *
     * @param envVariable DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws IllegalStateException if the license has already been signed
     * @throws NullPointerException DOCUMENT ME!
     */
    public void addBoundEnvironment(String envVariable, String value)
        throws IllegalStateException {
        if (signature != null) {
            throw new IllegalStateException(
                "bound values must be changed only before signing the license");
        }

        if ((envVariable == null) || (value == null)) {
            throw new NullPointerException();
        }

        boundEnv.put(envVariable, value);
    }

    /**
     * allows to add a condition where this license can only be used in
     * an environment where the given system property corresponds to the given
     * value. Bound values can only be changed before a license was signed.
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @throws IllegalStateException if the license has already been signed
     * @throws NullPointerException DOCUMENT ME!
     */
    public void addBoundProperty(String key, String value)
        throws IllegalStateException {
        if (signature != null) {
            throw new IllegalStateException(
                "bound values must be changed only before signing the license");
        }

        if ((key == null) || (value == null)) {
            throw new NullPointerException();
        }

        boundProperties.put(key, value);
    }

    /**
     * returns an unmodifiable view of the bound system properties of
     * this license
     *
     * @return DOCUMENT ME!
     */
    public Map<String, String> getBoundProperties() {
        return Collections.unmodifiableMap(boundProperties);
    }

    /**
     * returns an unmodifiable view of the bound environment variables
     * of this license
     *
     * @return DOCUMENT ME!
     */
    public Map<String, String> getBoundEnvironment() {
        return Collections.unmodifiableMap(boundEnv);
    }

    /**
     * returns the time when this license was created.
     *
     * @return DOCUMENT ME!
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * a hint to whom this license was issued
     *
     * @return DOCUMENT ME!
     */
    public String issuedFor() {
        return issuedFor;
    }

    /**
     * returns a copy of the signature that is used to verify that this
     * license has been created by an authorized issuer
     *
     * @return DOCUMENT ME!
     */
    public byte[] getSignature() {
        byte[] copy = new byte[signature.length];
        System.arraycopy(signature, 0, copy, 0, copy.length);

        return copy;
    }

    /**
     * the method that signes the license and thus makes it usable
     *
     * @param signature DOCUMENT ME!
     *
     * @throws LicensingException if the signature is invalid
     * @throws IllegalStateException if the license has already been signed
     */
    public synchronized void setSignature(byte[] signature)
        throws LicensingException {
        if (this.signature != null) {
            throw new IllegalStateException("license has already been signed");
        }

        this.signature = signature;
        org.jscience.util.MiscellaneousUtils.pause(500); // delay execution against cracking
        LicenseManager.getLicenseManager().verifyLicense(this);
    }

    /**
     * convenience method; overwrites the file if it exists
     *
     * @see #loadLicense(File)
     */
    public void saveTo(File file) throws IOException {
        ExtendedFile f = new ExtendedFile(file);
        f.writeObject(this, false);
    }

    /**
     * loads a serialized license from a file
     *
     * @see #saveTo(File)
     */
    public static License loadLicense(File file)
        throws IOException, LicensingException {
        try {
            License l = (License) new ExtendedFile(file).loadObject(true);

            return l;
        } catch (ClassNotFoundException ex) {
            throw new LicensingException(ex);
        } catch (ClassCastException ex) {
            throw new LicensingException(ex);
        }
    }

    /**
     * loads a serialized license from a stream
     *
     * @see #saveTo(File)
     */
    public static License loadLicense(InputStream stream)
        throws IOException, LicensingException {
        try {
            ObjectInputStream in = new ObjectInputStream(stream);
            Object obj = in.readObject();
            in.close();

            if (obj instanceof byte[]) {
                return loadLicense((byte[]) obj);
            } else {
                return (License) obj;
            }
        } catch (ClassNotFoundException ex) {
            throw new LicensingException(ex);
        } catch (ClassCastException ex) {
            throw new LicensingException(ex);
        }
    }

    /**
     * deserializes a license object from a byte pattern
     *
     * @see org.jscience.io.IOUtils#serialize(Object)
     */
    public static License loadLicense(byte[] bytes)
        throws IOException, LicensingException {
        try {
            License l = (License) IOUtils.deserialize(bytes);

            return l;
        } catch (ClassNotFoundException ex) {
            throw new LicensingException(ex);
        } catch (ClassCastException ex) {
            throw new LicensingException(ex);
        }
    }

    /**
     * returns either a Class or a String representing the Package
     *
     * @return DOCUMENT ME!
     */
    public Object getLicensee() {
        return licensee;
    }

    /**
     * returns true only if this license passes
     * <code>checkValidation()</code>. If you require more detail, call
     * <code>checkValidation()</code> directly.
     *
     * @see #checkValidation()
     */
    public boolean isValid() {
        try {
            checkValidation();

            return true;
        } catch (LicensingException ex) {
            return false;
        }
    }

    /**
     * checks whether this license is properly singed, not expired and
     * still has usage left. The license itself is not altered through this
     * call.
     *
     * @throws LicensingException
     *
     * @see #use()
     * @see LicenseManager#verifyLicense(License)
     */
    public synchronized void checkValidation() throws LicensingException {
        if (signature == null) {
            throw new LicensingException("license not signed");
        }

        if ((expirationDate != null) &&
                (System.currentTimeMillis() > expirationDate.getTime())) {
            throw new LicensingException("license expired");
        }

        if (usageLeft == 0) {
            throw new LicensingException("no usage left");
        }

        for (String key : boundProperties.keySet()) {
            if (!boundProperties.get(key).equals(System.getProperty(key))) {
                throw new LicensingException(
                    "bound system property has incorrect value: " + key);
            }
        }

        for (String env : boundEnv.keySet()) {
            if (!boundEnv.get(env).equals(System.getenv(env))) {
                throw new LicensingException(
                    "bound environment variable has incorrect value: " + env);
            }
        }

        // license is acutally already checked at signing time
        // but an updated certificate at deployment time may change the picture?
        LicenseManager.getLicenseManager().verifyLicense(this);
    }

    /**
     * returns the date after which this license expires. If the return
     * value is null, this license doesn't expire in time.
     *
     * @return DOCUMENT ME!
     */
    public Date validUntil() {
        return expirationDate;
    }

    /**
     * returns how many times <code>use()</code> can be called. if
     * <code>INDEFINATE</code> is returned, this license is not limited by
     * usage.
     *
     * @see #use()
     */
    public int usageLeft() {
        return usageLeft;
    }

    /**
     * uses this license; if usage is limited, the return value of
     * <code>usageLeft()</code> is decreased by one after a call to this
     * method.
     *
     * @see #isValid()
     */
    public synchronized int use() throws LicensingException {
        checkValidation();

        if (usageLeft < 0) {
            return -1;
        }

        return --usageLeft;
    }

    /**
     * includes only a subset of restrictions for this license
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return super.toString() + " (licensee: " + licensee +
        "; expirationDate: " + expirationDate + "; usageLimit: " +
        ((usageLimit == INDEFINATE) ? "indefinate"
                                    : (usageLimit + ", left: " + usageLeft)) +
        "; issued for: " + issuedFor + ")";
    }
}
