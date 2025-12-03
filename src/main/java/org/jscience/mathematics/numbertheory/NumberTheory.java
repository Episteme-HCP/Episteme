package org.jscience.mathematics.numbertheory;

import org.jscience.mathematics.number.Natural;
import org.jscience.mathematics.number.Integer;
import java.math.BigInteger;
import java.util.Random;

/**
 * Number theory algorithms and utilities.
 * <p>
 * Primality testing, GCD, modular arithmetic, and more.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class NumberTheory {

    private static final Random random = new Random();

    /**
     * Miller-Rabin primality test.
     * <p>
     * Probabilistic test with error probability ≤ 4^(-k).
     * </p>
     * 
     * @param n number to test
     * @param k number of rounds (higher = more accurate)
     * @return true if probably prime
     */
    public static boolean millerRabin(BigInteger n, int k) {
        if (n.compareTo(BigInteger.valueOf(2)) < 0)
            return false;
        if (n.equals(BigInteger.valueOf(2)) || n.equals(BigInteger.valueOf(3)))
            return true;
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO))
            return false;

        // Write n-1 as 2^r * d
        BigInteger d = n.subtract(BigInteger.ONE);
        int r = 0;
        while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.valueOf(2));
            r++;
        }

        // Witness loop
        for (int i = 0; i < k; i++) {
            BigInteger a = randomBigInteger(BigInteger.valueOf(2), n.subtract(BigInteger.valueOf(2)));
            BigInteger x = a.modPow(d, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }

            boolean composite = true;
            for (int j = 0; j < r - 1; j++) {
                x = x.modPow(BigInteger.valueOf(2), n);
                if (x.equals(n.subtract(BigInteger.ONE))) {
                    composite = false;
                    break;
                }
            }

            if (composite)
                return false;
        }

        return true;
    }

    private static BigInteger randomBigInteger(BigInteger min, BigInteger max) {
        BigInteger range = max.subtract(min);
        int len = range.bitLength();
        BigInteger result;
        do {
            result = new BigInteger(len, random);
        } while (result.compareTo(range) >= 0);
        return result.add(min);
    }

    /**
     * Euclidean algorithm for GCD.
     */
    public static Natural gcd(Natural a, Natural b) {
        BigInteger x = a.toBigInteger();
        BigInteger y = b.toBigInteger();

        while (!y.equals(BigInteger.ZERO)) {
            BigInteger temp = y;
            y = x.mod(y);
            x = temp;
        }

        return Natural.of(x);
    }

    /**
     * Least common multiple.
     */
    public static Natural lcm(Natural a, Natural b) {
        if (a.equals(Natural.ZERO) || b.equals(Natural.ZERO)) {
            return Natural.ZERO;
        }
        return Natural.of(a.toBigInteger().multiply(b.toBigInteger()).divide(gcd(a, b).toBigInteger()));
    }

    /**
     * Extended Euclidean algorithm: finds x, y such that ax + by = gcd(a, b).
     */
    public static Integer[] extendedGCD(Integer a, Integer b) {
        if (b.equals(Integer.ZERO)) {
            return new Integer[] { a, Integer.ONE, Integer.ZERO };
        }

        Integer[] result = extendedGCD(b, Integer.of(a.toBigInteger().mod(b.toBigInteger())));
        Integer gcd = result[0];
        Integer x1 = result[1];
        Integer y1 = result[2];

        Integer x = y1;
        Integer y = x1.subtract(a.divide(b).multiply(y1));

        return new Integer[] { gcd, x, y };
    }

    /**
     * Modular exponentiation: (base^exp) mod m.
     */
    public static Natural modPow(Natural base, Natural exp, Natural m) {
        return Natural.of(base.toBigInteger().modPow(exp.toBigInteger(), m.toBigInteger()));
    }

    /**
     * Modular inverse: finds x such that (a * x) ≡ 1 (mod m).
     */
    public static Natural modInverse(Natural a, Natural m) {
        Integer[] result = extendedGCD(Integer.of(a.toBigInteger()), Integer.of(m.toBigInteger()));
        Integer gcd = result[0];
        Integer x = result[1];

        if (!gcd.equals(Integer.ONE)) {
            throw new ArithmeticException("Modular inverse does not exist");
        }

        return Natural.of(x.toBigInteger().mod(m.toBigInteger()));
    }

    /**
     * Euler's totient function φ(n): count of numbers ≤ n coprime to n.
     */
    public static Natural eulerTotient(Natural n) {
        if (n.equals(Natural.ONE))
            return Natural.ONE;

        BigInteger result = n.toBigInteger();
        BigInteger num = n.toBigInteger();

        for (BigInteger p = BigInteger.valueOf(2); p.multiply(p).compareTo(num) <= 0; p = p.add(BigInteger.ONE)) {
            if (num.mod(p).equals(BigInteger.ZERO)) {
                while (num.mod(p).equals(BigInteger.ZERO)) {
                    num = num.divide(p);
                }
                result = result.subtract(result.divide(p));
            }
        }

        if (num.compareTo(BigInteger.ONE) > 0) {
            result = result.subtract(result.divide(num));
        }

        return Natural.of(result);
    }
}
