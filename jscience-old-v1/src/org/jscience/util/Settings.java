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

package org.jscience.util;

import org.jscience.util.license.License;
import org.jscience.util.license.LicenseHandler;
import org.jscience.util.license.LicenseManager;
import org.jscience.util.license.LicensingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Settings provides access to several properties and resources that are used
 * throughout the classes of this Antelmann.com framework. <p>
 * The default settings are loaded from a well known location
 * which denotes a file that is commonly distributed along with the
 * antelmann.jar file.
 * The default settings can be overwritten by simply altering the Properties
 * returned by <code>getProperties()</code>, where a subsequent call to that
 * method will reflect the changes (similar to <code>System.getProperties()</code>.
 * <p/>
 * If the Settings are used in special environments (such as within an application
 * server), you may need to manually use the <code>setClassLoader(ClassLoader)</code>
 * method to specify a ClassLoader appropriate for the given context, before
 * accessing any resources.
 *
 * @author Holger Antelmann
 * @since 3/24/2002
 */
public final class Settings {
    static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    /**
     * denotes the relative location of the file for the default Properties,
     * so that it can be found through the <code>ClassLoader</code>
     */
    public static String defaultPropertyFile = "org/jscience/JScienceBundle.properties";
    static Properties currentProperties = null;

    static LicenseHandler licenseHandler = null;

    private Settings() {
    }

    /**
     * allows to react on failed license-checks by establishing a proper handler
     *
     * @see #getLicenseHandler()
     * @see #checkLicense(Object)
     */
    public static void setLicenseHandler(LicenseHandler handler) {
        licenseHandler = handler;
    }

    /**
     * returns the LicenseHandler currently in place to react if a call
     * to checkLicense(Object) fails initially.
     *
     * @see #checkLicense(Object)
     * @see #setLicenseHandler(LicenseHandler)
     */
    public LicenseHandler getLicenseHandler() {
        return licenseHandler;
    }

    /**
     * checks whether there is a valid license installed for accessing
     * the given object - a convenience method normally used by an application
     * to check whether access to an object is licensed.
     * The method will only return normally if a valid license is
     * available for the given licensee.
     * <p/>
     * By establishing a LicenseHandler, this method - in the event of the
     * license check initially failing - will not immediately
     * fail by throwing an exception, but will first try to e.g. obtain
     * or renew a license through a call to the LicenseHander.
     * After the LicenseHandler was called, the method will once again
     * check with the LicenseManager, whether a valid license is present.
     *
     * @param licensee any object for wich a license is to be checked
     * @throws java.lang.SecurityException if the license check failed;
     *                                     the embedded exception will be
     *                                     a LicensingException
     * @see #setLicenseHandler(LicenseHandler)
     * @see org.jscience.util.license.License
     * @see org.jscience.util.license.LicenseManager
     * @see org.jscience.util.license.LicensingException
     */
    public static void checkLicense(Object licensee) throws SecurityException {
        if (!Debug.licensingEnabled) return;
        try {
            LicenseManager.getLicenseManager().check(licensee);
        } catch (LicensingException ex) {
            if (licenseHandler != null) {
                licenseHandler.aquireLicense(licensee);
                try {
                    LicenseManager.getLicenseManager().check(licensee);
                } catch (LicensingException that_is_it) {
                    throw new SecurityException(that_is_it);
                }
            } else {
                throw new SecurityException(ex);
            }
        }
    }

    /**
     * installs one or more License Objects from the given stream - a convenience method.
     *
     * @throws java.lang.SecurityException if one of the licenses within the stream
     *                                     could not be verified. The embedded cause
     *                                     will be a LicensingException
     * @see org.jscience.util.license.License
     * @see org.jscience.util.license.LicenseManager
     * @see org.jscience.util.license.LicensingException
     */
    public static void installLicenses(InputStream stream) throws SecurityException {
        try {
            LicenseManager.getLicenseManager().loadFrom(stream);
        } catch (LicensingException ex) {
            throw new SecurityException(ex);
        }
    }

    /**
     * installs the given license to the LicenseManager - a convenience method.
     *
     * @throws java.lang.SecurityException if the licenses could not be verified.
     *                                     The embedded cause will be a LicensingException
     * @see org.jscience.util.license.License
     * @see org.jscience.util.license.LicenseManager
     * @see org.jscience.util.license.LicensingException
     */
    public static void installLicense(License l) throws SecurityException {
        try {
            LicenseManager.getLicenseManager().install(l);
        } catch (LicensingException ex) {
            throw new SecurityException(ex);
        }
    }

    /**
     * sets the ClassLoader to be used in <code>getResource(String)</code>;
     * by default, the system class loader is used
     *
     * @see #getResource(String)
     */
    public static void setClassLoader(ClassLoader cl) {
        classLoader = cl;
    }

    /**
     * sets the properties to be used by Settings; this should not be used
     * unless you need to overwrite the default properties
     */
    public static void init(Properties newProperties) {
        currentProperties = newProperties;
    }

    /**
     * sets the class loader to be used to the system class loader (used by default)
     */
    public static void setSystemClassLoader() {
        setClassLoader(ClassLoader.getSystemClassLoader());
    }

    /*
    * sets the class loader to be used to the context class loader of the
    * current thread
    */
    public static void setContextClassLoader() {
        setClassLoader(Thread.currentThread().getContextClassLoader());
    }

    /**
     * returns the ClassLoader currently used to locate resources
     */
    public static ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * This method encapsulates calls to the embedded ClassLoader to ease
     * dealing with problems when not finding the resource.
     * Instead of just returning null if a resource is not found, an exception is
     * thrown (which is particularly helpful for image loading in swing).
     * If you are working in special environments where the system class loader
     * may not find the resource you are looking for, you need to set the class
     * loader of this class to the one you need.
     *
     * @throws ResourceNotFoundException if the given resource could not be found
     * @see #setClassLoader(ClassLoader)
     */
    public static URL getResource(String resource) throws ResourceNotFoundException {
        URL url = classLoader.getResource(resource);
        if (url == null) {
            String s = "the given resource could not be found: \"" + resource + "\"";
            throw new ResourceNotFoundException(s, resource);
        } else {
            return url;
        }
    }

    /**
     * This method will try to find the value for the given key
     * and return it - or return the defaultValue if the property
     * was not found or the default property file could not be opened
     */
    public static String getProperty(String key, String defaultValue) {
        String value = null;
        try {
            value = getProperty(key);
        } catch (ResourceNotFoundException e) {
            return defaultValue;
        }
        if (value == null) return defaultValue;
        return value;
    }

    /**
     * provides a shortcut for <code>getProperties().getPropterty(key)</code>
     *
     * @throws ResourceNotFoundException if the property file could not be located
     * @see #getProperties()
     */
    public static String getProperty(String key) throws ResourceNotFoundException {
        return getProperties().getProperty(key);
    }

    /**
     * sets a property within these settings
     */
    public static Object setProperty(String key, String value) {
        return getProperties().setProperty(key, value);
    }

    /**
     * This method returns the current application properties;
     * changes to the Properties will be reflected in subsequent
     * calls to this method.
     *
     * @throws ResourceNotFoundException if the property file could not be located
     * @see #reset()
     */
    public static synchronized Properties getProperties() throws ResourceNotFoundException {
        if (currentProperties == null) {
            initProperties();
        }
        return currentProperties;
    }

    static synchronized void initProperties() throws ResourceNotFoundException {
        try {
            Properties p = new Properties();
            InputStream in = getResource(defaultPropertyFile).openStream();
            p.load(in);
            currentProperties = p;
        } catch (IOException e) {
            //currentProperties = new Properties();
            ResourceNotFoundException ex = new ResourceNotFoundException("cannot open default property file");
            ex.initCause(e);
            throw ex;
        }
    }

    /**
     * first, this method tries to initialize with the default properties,
     * but even if that fails, the properties are loaded from the given location.
     */
    public static void load(File fileProperties) throws IOException {
        try {
            getProperties();
        } catch (ResourceNotFoundException ex) {
            currentProperties = new Properties();
        }
        currentProperties.load(new FileInputStream(fileProperties));
    }

    /**
     * first, this method tries to initialize with the default properties,
     * but even if that fails, the properties are loaded from the given location.
     */
    public static void load(URL urlProperties) throws IOException {
        try {
            getProperties();
        } catch (ResourceNotFoundException ex) {
            currentProperties = new Properties();
        }
        currentProperties.load(urlProperties.openStream());
    }

    /**
     * This method forces to reload the default settings from file.
     * Any user settings that were set before are not present anymore when
     * calling <code>getProperties()</code> hereafter.
     *
     * @see #getProperties()
     * @see #defaultPropertyFile
     */
    public static void reset() {
        currentProperties = null;
        getProperties();
    }

    /**
     * returns a fresh set of default Properties directly from the Antelmann.com website
     */
    public static Properties getOnlineProperties() {
        Properties properties = new Properties();
        try {
            URL url = new URL(getProperties().getProperty("application.online.properties.url"));
            properties.load(url.openStream());
        } catch (IOException e) {
        }
        return properties;
    }
}
