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
