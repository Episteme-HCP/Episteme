package org.jscience.mathematics.cryptography;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;

/**
 * Cryptographic hash functions.
 * <p>
 * One-way functions for data integrity, digital signatures, password hashing.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class HashFunctions {

    /**
     * SHA-256 hash function.
     * <p>
     * Produces 256-bit (32-byte) hash. Collision-resistant.
     * </p>
     */
    public static byte[] sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * SHA-256 for strings.
     */
    public static String sha256(String text) {
        byte[] hash = sha256(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * SHA-512 hash function.
     */
    public static byte[] sha512(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 not available", e);
        }
    }

    /**
     * SHA-512 for strings.
     */
    public static String sha512(String text) {
        byte[] hash = sha512(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    /**
     * MD5 hash (deprecated for security, but useful for checksums).
     */
    @Deprecated
    public static byte[] md5(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not available", e);
        }
    }

    /**
     * HMAC-SHA256 (keyed hash for message authentication).
     */
    public static byte[] hmacSha256(byte[] key, byte[] data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(key, "HmacSHA256");
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 failed", e);
        }
    }

    /**
     * bcrypt-like password hashing (using PBKDF2).
     * <p>
     * Slow by design to resist brute-force attacks.
     * </p>
     */
    public static byte[] pbkdf2(String password, byte[] salt, int iterations) {
        try {
            javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                    password.toCharArray(), salt, iterations, 256);
            javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("PBKDF2 failed", e);
        }
    }

    /**
     * Converts byte array to hex string.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Constant-time comparison to prevent timing attacks.
     */
    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length)
            return false;

        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
