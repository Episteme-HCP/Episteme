/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

/**
 * An interface to support very simple synchronous key encryption. The entire
 * data to be encrypted needs to fit into memory, i.e. this scheme is not
 * appropriate to encode/decode large files at once.
 *
 * @author Holger Antelmann
 * @see Encoded
 */
public interface SynchronousKey {
    /**
     * decodes the given byte array
     *
     * @param encrypted DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    byte[] decode(byte[] encrypted);

    /**
     * encodes the given byte array
     *
     * @param plainSource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    byte[] encode(byte[] plainSource);

    /**
     * returns a signature that is used to identify the key that needs
     * to be known for both, encoding and decoding
     *
     * @return DOCUMENT ME!
     */
    String getKeySignature();
}
