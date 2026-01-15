/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

/**
 * A very simple encryption algorithm for demonstration purposes only. It
 * is far from being safe, but it will require anyone to at least put some
 * manual effort into reading any message that uses Encoded objects using this
 * algorithm when send over the Internet. (And BTW, since this is open source,
 * it would be a BAD IDEA to use this synchronous key for anything else but a
 * demonstration!)
 *
 * @author Holger Antelmann
 */
public class SimpleKey implements SynchronousKey {
    /** DOCUMENT ME! */
    private int key = 7;

/**
     * Creates a new SimpleKey object.
     */
    public SimpleKey() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param encrypted DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] decode(byte[] encrypted) {
        byte[] bytes = new byte[encrypted.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (encrypted[i] + (i + key));
        }

        return bytes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param plainSource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] encode(byte[] plainSource) {
        byte[] bytes = new byte[plainSource.length];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (plainSource[i] - (i + key));
        }

        return bytes;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeySignature() {
        return "org.jscience.util.SimpleKey";
    }
}
