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

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


/**
 * CypherKey provides a very easy-to-use, yet effective encryption
 * mechanism. The key (including the password) is transient.
 *
 * @author Holger Antelmann
 */
public class CypherKey implements SynchronousKey {
    /** DOCUMENT ME! */
    static String defaultAlgorithm = "PBEWithMD5AndDES";

    /** DOCUMENT ME! */
    static byte[] defaultSalt = {
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e,
            (byte) 0xc8, (byte) 0xee, (byte) 0x99,
        //(byte)0xff, (byte)0x01, (byte)0x1a, (byte)0x74
        };

    /** DOCUMENT ME! */
    static int defaultIterationCount = 17;

    /** DOCUMENT ME! */
    private transient String algorithm;

    /** DOCUMENT ME! */
    private transient byte[] salt;

    /** DOCUMENT ME! */
    private transient int iterationCount;

    /** DOCUMENT ME! */
    private transient Cipher pbeCipher;

    /** DOCUMENT ME! */
    private transient SecretKey pbeKey;

    /** DOCUMENT ME! */
    private transient PBEParameterSpec pbeParamSpec;

/**
     * Creates a new CypherKey object.
     *
     * @param password DOCUMENT ME!
     * @throws GeneralSecurityException DOCUMENT ME!
     */
    public CypherKey(char[] password) throws GeneralSecurityException {
        this(defaultAlgorithm, defaultSalt, defaultIterationCount, password);
    }

/**
     * Creates a new CypherKey object.
     *
     * @param algorithm      DOCUMENT ME!
     * @param salt           DOCUMENT ME!
     * @param iterationCount DOCUMENT ME!
     * @param password       DOCUMENT ME!
     * @throws GeneralSecurityException DOCUMENT ME!
     */
    public CypherKey(String algorithm, byte[] salt, int iterationCount,
        char[] password) throws GeneralSecurityException {
        this.salt = salt;
        this.iterationCount = iterationCount;
        pbeCipher = Cipher.getInstance(algorithm);
        pbeParamSpec = new PBEParameterSpec(salt, iterationCount);

        SecretKeyFactory keyFac = SecretKeyFactory.getInstance(algorithm);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        pbeKey = keyFac.generateSecret(pbeKeySpec);
        pbeKeySpec.clearPassword();
    }

    /*
    public Cipher getCipher () { return pbeCipher; }
    
    
    public PBEParameterSpec getPBEParameterSpec () { return pbeParamSpec; }
    
    
    public SecretKey getSecretKey () { return pbeKey; }
    */
    public byte[] decode(byte[] encrypted) {
        try {
            pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);

            return pbeCipher.doFinal(encrypted);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param plainSource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public byte[] encode(byte[] plainSource) {
        try {
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

            return pbeCipher.doFinal(plainSource);
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKeySignature() {
        return (getClass().getName() + " using " + algorithm +
        " and secret password");
    }
}
