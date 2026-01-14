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

import java.io.File;


/**
 * a collection of security related names of supported algorithms.
 *
 * @author Holger Antelmann
 */
public class SecurityNames {
    /** DOCUMENT ME! */
    public static final File defaultKeyStoreFile = new File(System.getProperty(
                "user.home"), ".keystore");

    /** DOCUMENT ME! */
    public static final String[] messageDigestAlgorithm = {
            "MD2", "MD5", "SHA-1", "SHA-256", "SHA-284", "SHA-512"
        };

    /** DOCUMENT ME! */
    public static final String[] keyAlgorithm = { "DSA", "RSA" };

    /** DOCUMENT ME! */
    public static final String[] ditigalSignatureAlgorithm = {
            "MD2withRSA", "MD5withRSA", "SHA1withDSA", "SHA1withRSA"
        };

    /** DOCUMENT ME! */
    public static final String[] randomNumberGenerationAlgorithm = { "SHA1PRNG" };

    /** DOCUMENT ME! */
    public static final String[] certificateType = { "X.509" };

    /** DOCUMENT ME! */
    public static final String[] sslProtocol = { "TLS" };

    /** DOCUMENT ME! */
    public static final String[] keyStoreType = { "JKS", "JCEKS", "PKCS12" };

    /** DOCUMENT ME! */
    public static final String[] cypherAlgorithm = {
            "AES", "Blowfish", "DES", "DESde", "RC2", "RC4", "RC5", "RSA",
            "PBEWithMD5AndDES",
            "PBEWithHmacSHA1AndDESede" // a examples of further possibilities
        };

    /** DOCUMENT ME! */
    public static final String[] cypherMode = {
            "NONE", "CBC", "CFB", "ECB", "OFB", "PCBC"
        };

    /** DOCUMENT ME! */
    public static final String[] cypherPadding = {
            "NoPadding", "PKCS5Padding", "SSL3Padding", "ECB", "OFB",
            "OAEPWithMD5AndMGF1Padding" // an example of further possibilities
        };

    /** DOCUMENT ME! */
    public static final String[] keyAgreement = { "DiffieHellman" };

    /** DOCUMENT ME! */
    public static final String[] keyGenerator = {
            "AES", "Blowfish", "DES", "DESde", "HmacMD5 ", "HmacSHA1",
        };

    /** DOCUMENT ME! */
    public static final String[] keyPairGenerator = { "DiffieHellman" };

    /** DOCUMENT ME! */
    public static final String[] secretKeyFactory = {
            "AES", "DES", "DESde", "PBEWithMD5AndDES",
            "PBEWithHmacSHA1AndDESede " // examples of further possibilities
        };

    /** DOCUMENT ME! */
    public static final String[] keyFactory = { "DiffieHellman" };

    /** DOCUMENT ME! */
    public static final String[] algorithmParameterGenerator = { "DiffieHellman" };

    /** DOCUMENT ME! */
    public static final String[] algorithmParameters = {
            "Blowfish", "DES", "DESde", "DiffieHellman  ", "PBE",
        };

    /** DOCUMENT ME! */
    public static final String[] mac = {
            "HmacMD5", "HmacSHA1",
            "PBEWithHmacSHA1" // an example of further possibilities
        };
}
