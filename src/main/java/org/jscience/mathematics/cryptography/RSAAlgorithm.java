/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.cryptography;

import org.jscience.mathematics.number.Integer;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * The RSA (Rivest–Shamir–Adleman) encryption algorithm.
 * <p>
 * RSA is a public-key cryptosystem that is widely used for secure data
 * transmission.
 * It is based on the practical difficulty of the factorization of the product
 * of
 * two large prime numbers, the "factoring problem".
 * </p>
 * 
 * <h2>References</h2>
 * <ul>
 * <li><a href="https://en.wikipedia.org/wiki/RSA_(cryptosystem)">Wikipedia: RSA
 * (cryptosystem)</a></li>
 * <li>Rivest, R. L., Shamir, A., & Adleman, L. (1978).
 * "A Method for Obtaining Digital Signatures and Public-Key Cryptosystems".
 * <i>Communications of the ACM</i>, 21(2), 120–126.</li>
 * <li>Menezes, A. J., van Oorschot, P. C., & Vanstone, S. A. (1996).
 * <i>Handbook of Applied Cryptography</i>. CRC Press.</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class RSAAlgorithm {

    private static final SecureRandom random = new SecureRandom();

    public static class KeyPair {
        public final PublicKey publicKey;
        public final PrivateKey privateKey;

        public KeyPair(PublicKey publicKey, PrivateKey privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    public static class PublicKey {
        public final BigInteger n; // modulus
        public final BigInteger e; // public exponent

        public PublicKey(BigInteger n, BigInteger e) {
            this.n = n;
            this.e = e;
        }
    }

    public static class PrivateKey {
        public final BigInteger n; // modulus
        public final BigInteger d; // private exponent

        public PrivateKey(BigInteger n, BigInteger d) {
            this.n = n;
            this.d = d;
        }
    }

    /**
     * Generates RSA key pair.
     * 
     * @param bitLength key size in bits (e.g., 2048, 4096)
     * @return public and private keys
     */
    public static KeyPair generateKeyPair(int bitLength) {
        // Generate two large primes p and q
        BigInteger p = BigInteger.probablePrime(bitLength / 2, random);
        BigInteger q = BigInteger.probablePrime(bitLength / 2, random);

        // Compute n = p * q
        BigInteger n = p.multiply(q);

        // Compute φ(n) = (p-1)(q-1)
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        // Choose e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1
        BigInteger e = BigInteger.of(65537); // Common choice (2^16 + 1)

        // Ensure gcd(e, phi) = 1
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e = e.add(BigInteger.ONE);
        }

        // Compute d ≡ e^(-1) (mod φ(n))
        BigInteger d = e.modInverse(phi);

        return new KeyPair(new PublicKey(n, e), new PrivateKey(n, d));
    }

    /**
     * Encrypts message using public key: c = m^e mod n
     */
    public static BigInteger encrypt(BigInteger message, PublicKey publicKey) {
        if (message.compareTo(publicKey.n) >= 0) {
            throw new IllegalArgumentException("Message too large for key size");
        }
        return message.modPow(publicKey.e, publicKey.n);
    }

    /**
     * Decrypts ciphertext using private key: m = c^d mod n
     */
    public static BigInteger decrypt(BigInteger ciphertext, PrivateKey privateKey) {
        return ciphertext.modPow(privateKey.d, privateKey.n);
    }

    /**
     * Signs message using private key.
     */
    public static BigInteger sign(BigInteger message, PrivateKey privateKey) {
        return message.modPow(privateKey.d, privateKey.n);
    }

    /**
     * Verifies signature using public key.
     */
    public static boolean verify(BigInteger message, BigInteger signature, PublicKey publicKey) {
        BigInteger verified = signature.modPow(publicKey.e, publicKey.n);
        return verified.equals(message);
    }
}


