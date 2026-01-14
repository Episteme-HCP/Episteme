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

package org.jscience;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ResourceBundle;


/**
 * The Version class contains information about the current and latest
 * release.
 *
 * @author Mark Hale
 * @version 1.3
 */
public final class JScienceVersion extends Object implements Serializable {
    /** Major version number. */
    public final int major;

    /** Minor version number. */
    public final int minor;

    /** Java platform required. */
    public final String platform;

    /** The URL for the home of this version. */
    public final String home;

/**
     * Constructs a version object.
     *
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     * @param home DOCUMENT ME!
     * @param platform DOCUMENT ME!
     */
    private JScienceVersion(int major, int minor, String home, String platform) {
        this.major = major;
        this.minor = minor;
        this.home = home;
        this.platform = platform;
    }

    /**
     * Gets the current version.
     *
     * @return DOCUMENT ME!
     */
    public static JScienceVersion getCurrent() {
        ResourceBundle bundle = ResourceBundle.getBundle("JScienceBundle");
        int major = Integer.parseInt(bundle.getString("version.major"));
        int minor = Integer.parseInt(bundle.getString("version.minor"));
        String platform = bundle.getString("version.platform");
        String home = bundle.getString("version.home");

        return new JScienceVersion(major, minor, home, platform);
    }

    /**
     * Retrieves the latest version from the home URL.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static JScienceVersion getLatest() throws IOException {
        JScienceVersion latest = null;

        try {
            URL serurl = new URL(getCurrent().home + "version.ser");
            ObjectInputStream in = new ObjectInputStream(serurl.openStream());
            latest = (JScienceVersion) in.readObject();
            in.close();
        } catch (MalformedURLException murle) {
        } catch (ClassNotFoundException cnfe) {
        }

        return latest;
    }

    /**
     * Compares two versions for equality.
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        if (!(o instanceof JScienceVersion)) {
            return false;
        }

        JScienceVersion ver = (JScienceVersion) o;

        return (major == ver.major) && (minor == ver.minor) &&
        platform.equals(ver.platform);
    }

    /**
     * Returns the version number as a string.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new StringBuffer().append(major).append('.').append(minor)
                                 .toString();
    }

    /**
     * Returns true if this is later than another version.
     *
     * @param ver DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLater(JScienceVersion ver) {
        return (major > ver.major) ||
        ((major == ver.major) && (minor > ver.minor));
    }
}
