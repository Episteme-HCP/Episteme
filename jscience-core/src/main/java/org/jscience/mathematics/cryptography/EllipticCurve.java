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

package org.jscience.mathematics.cryptography;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Elliptic Curve Cryptography (ECC).
 * <p>
 * More efficient than RSA - same security with smaller keys.
 * Based on algebraic structure of elliptic curves over finite fields.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class EllipticCurve {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Point on elliptic curve yÃ‚Â² = xÃ‚Â³ + ax + b (mod p)
     */
    public static class Point {
        public final BigInteger x;
        public final BigInteger y;
        public final boolean isInfinity;

        public static final Point INFINITY = new Point();

        private Point() {
            this.x = null;
            this.y = null;
            this.isInfinity = true;
        }

        public Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
            this.isInfinity = false;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Point))
                return false;
            Point other = (Point) obj;
            if (isInfinity)
                return other.isInfinity;
            if (other.isInfinity)
                return false;
            return x.equals(other.x) && y.equals(other.y);
        }

        @Override
        public String toString() {
            return isInfinity ? "Ã¢Ë†Å¾" : "(" + x + ", " + y + ")";
        }
    }

    /**
     * Elliptic curve parameters: yÃ‚Â² = xÃ‚Â³ + ax + b (mod p)
     */
    public static class Curve {
        public final BigInteger a;
        public final BigInteger b;
        public final BigInteger p; // prime modulus
        public final Point G; // generator point
        public final BigInteger n; // order of G

        public Curve(BigInteger a, BigInteger b, BigInteger p, Point G, BigInteger n) {
            this.a = a;
            this.b = b;
            this.p = p;
            this.G = G;
            this.n = n;
        }

        /**
         * secp256k1 - used in Bitcoin.
         */
        public static Curve secp256k1() {
            BigInteger p = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
            BigInteger a = BigInteger.ZERO;
            BigInteger b = BigInteger.valueOf(7);
            BigInteger Gx = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
            BigInteger Gy = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
            BigInteger n = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);

            return new Curve(a, b, p, new Point(Gx, Gy), n);
        }
    }

    /**
     * Point addition on elliptic curve.
     */
    public static Point add(Point P, Point Q, Curve curve) {
        if (P.isInfinity)
            return Q;
        if (Q.isInfinity)
            return P;

        BigInteger p = curve.p;

        if (P.x.equals(Q.x)) {
            if (P.y.equals(Q.y)) {
                // Point doubling
                return doublePoint(P, curve);
            } else {
                // P + (-P) = Ã¢Ë†Å¾
                return Point.INFINITY;
            }
        }

        // Slope: ÃŽÂ» = (y2 - y1) / (x2 - x1)
        BigInteger dy = Q.y.subtract(P.y).mod(p);
        BigInteger dx = Q.x.subtract(P.x).mod(p);
        BigInteger lambda = dy.multiply(dx.modInverse(p)).mod(p);

        // x3 = ÃŽÂ»Ã‚Â² - x1 - x2
        BigInteger x3 = lambda.pow(2).subtract(P.x).subtract(Q.x).mod(p);

        // y3 = ÃŽÂ»(x1 - x3) - y1
        BigInteger y3 = lambda.multiply(P.x.subtract(x3)).subtract(P.y).mod(p);

        return new Point(x3, y3);
    }

    /**
     * Point doubling: P + P = 2P
     */
    public static Point doublePoint(Point P, Curve curve) {
        if (P.isInfinity)
            return P;

        BigInteger p = curve.p;
        BigInteger a = curve.a;

        // ÃŽÂ» = (3xÃ‚Â² + a) / 2y
        BigInteger numerator = P.x.pow(2).multiply(BigInteger.valueOf(3)).add(a).mod(p);
        BigInteger denominator = P.y.multiply(BigInteger.valueOf(2)).mod(p);
        BigInteger lambda = numerator.multiply(denominator.modInverse(p)).mod(p);

        // x3 = ÃŽÂ»Ã‚Â² - 2x
        BigInteger x3 = lambda.pow(2).subtract(P.x.multiply(BigInteger.valueOf(2))).mod(p);

        // y3 = ÃŽÂ»(x - x3) - y
        BigInteger y3 = lambda.multiply(P.x.subtract(x3)).subtract(P.y).mod(p);

        return new Point(x3, y3);
    }

    /**
     * Scalar multiplication: k * P (adds P to itself k times).
     * Uses double-and-add algorithm for efficiency.
     */
    public static Point multiply(BigInteger k, Point P, Curve curve) {
        Point result = Point.INFINITY;
        Point addend = P;

        while (k.compareTo(BigInteger.ZERO) > 0) {
            if (k.testBit(0)) {
                result = add(result, addend, curve);
            }
            addend = doublePoint(addend, curve);
            k = k.shiftRight(1);
        }

        return result;
    }

    /**
     * Generates ECDH key pair.
     */
    public static class KeyPair {
        public final BigInteger privateKey; // random scalar
        public final Point publicKey; // d * G

        public KeyPair(BigInteger privateKey, Point publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }

    /**
     * Generate key pair for ECDH.
     */
    public static KeyPair generateKeyPair(Curve curve) {
        // Private key: random integer in [1, n-1]
        BigInteger privateKey;
        do {
            privateKey = new BigInteger(curve.n.bitLength(), random);
        } while (privateKey.compareTo(BigInteger.ONE) < 0 || privateKey.compareTo(curve.n) >= 0);

        // Public key: d * G
        Point publicKey = multiply(privateKey, curve.G, curve);

        return new KeyPair(privateKey, publicKey);
    }

    /**
     * ECDH shared secret computation.
     */
    public static Point computeSharedSecret(BigInteger privateKey, Point otherPublicKey, Curve curve) {
        return multiply(privateKey, otherPublicKey, curve);
    }
}


