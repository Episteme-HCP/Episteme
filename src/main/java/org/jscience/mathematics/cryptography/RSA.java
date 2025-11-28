package org.jscience.mathematics.cryptography;

import org.jscience.mathematics.number.Natural;
import org.jscience.mathematics.number.Integer;
import org.jscience.mathematics.numbertheory.NumberTheory;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * RSA (Rivest-Shamir-Adleman) public-key cryptography.
 * <p>
 * Based on difficulty of factoring large semiprimes.
 * Scientific foundation: number theory, modular exponentiation.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class RSA {

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
        BigInteger e = BigInteger.valueOf(65537); // Common choice (2^16 + 1)

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
