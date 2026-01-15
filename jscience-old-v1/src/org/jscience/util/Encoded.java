/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.util;

import org.jscience.io.IOUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Encoded encapsulates a serializable object in an encrypted format
 * that requires the same synchronous key for both, encoding and decoding.
 * The key itself is not stored with the object, but a signature identifying
 * the key.
 * <p/>
 * Instances of this class can be used to conveniently send objects over
 * untrusted network connections.
 * <p/>
 * This class just exist for convenience; it has many security flaws for
 * any serious application (even though being so simple).
 * For starters: as the embedded byte array is already known to contain
 * a serialized object, an intelligent attack on the key algorithm
 * can make many useful assumptions based on the fact that the right decryption
 * must lead to a byte array denoting a serialized Java object.
 *
 * @author Holger Antelmann
 * @see SynchronousKey
 */
public class Encoded implements Serializable {
    static final long serialVersionUID = 8521814906577190091L;

    byte[] encryptedObject;
    String key;

    /**
     * stores the encryped bytes of the given objects (using the key)
     * and the signature of the given key (not the key itself)
     *
     * @param sourceObject must be serializable
     * @see SynchronousKey#getKeySignature()
     */
    public Encoded(Object sourceObject, SynchronousKey key)
            throws IOException {
        encryptedObject = key.encode(IOUtils.serialize(sourceObject));
        this.key = key.getKeySignature();
    }

    /**
     * decodes the embedded encrypted object given the right key
     *
     * @throws IOException if the object could not be deserialized, which may
     *                     also be a result of the wrong key
     */
    public Object decode(SynchronousKey key)
            throws IOException, ClassNotFoundException {
        return IOUtils.deserialize(key.decode(encryptedObject));
    }

    /**
     * returns the key signature that can be used to identify the key needed
     * to decode the object - provided the key is known.
     */
    String getKeySignature() {
        return key;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Encoded)) return false;
        Encoded e = (Encoded) obj;
        if (!key.equals(e.key)) return false;
        return ((Arrays.equals(encryptedObject, e.encryptedObject)) ?
                true : false
        );
    }
}
