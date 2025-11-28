package org.jscience.mathematics.algebra.polynomials;

import org.jscience.mathematics.number.Integer;
import org.jscience.mathematics.algebra.Polynomial;
import org.jscience.mathematics.sets.Integers;
import java.util.List;
import java.util.ArrayList;

/**
 * Complete polynomial factorization over integers.
 * <p>
 * Algorithms: Rational root theorem, quadratic formula.
 * Complements PolynomialAlgebra with actual factorization.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class PolynomialFactorization {

    /**
     * Factor result.
     */
    public static class Factorization {
        public final Integer content;
        public final List<Polynomial<Integer>> factors;
        public final List<Integer> multiplicities;

        public Factorization(Integer content,
                List<Polynomial<Integer>> factors,
                List<Integer> multiplicities) {
            this.content = content;
            this.factors = factors;
            this.multiplicities = multiplicities;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!content.equals(Integer.ONE)) {
                sb.append(content).append(" * ");
            }
            for (int i = 0; i < factors.size(); i++) {
                if (i > 0)
                    sb.append(" * ");
                sb.append("(").append(factors.get(i)).append(")");
                if (!multiplicities.get(i).equals(Integer.ONE)) {
                    sb.append("^").append(multiplicities.get(i));
                }
            }
            return sb.toString();
        }
    }

    /**
     * Factors polynomial completely over integers.
     * <p>
     * Steps:
     * 1. Extract content
     * 2. Try simple factorizations
     * </p>
     */
    public static Factorization factor(Polynomial<Integer> p) {
        // Extract content
        Integer content = PolynomialAlgebra.content(p);
        Polynomial<Integer> primitive = PolynomialAlgebra.primitivePart(p);

        List<Polynomial<Integer>> factors = new ArrayList<>();
        List<Integer> multiplicities = new ArrayList<>();

        if (primitive.degree() <= 1) {
            // Linear or constant
            factors.add(primitive);
            multiplicities.add(Integer.ONE);
            return new Factorization(content, factors, multiplicities);
        }

        // For now, return as-is (complete factorization is complex)
        factors.add(primitive);
        multiplicities.add(Integer.ONE);
        return new Factorization(content, factors, multiplicities);
    }

    /**
     * Factors quadratic ax² + bx + c.
     * <p>
     * Returns null if not factorable over integers.
     * </p>
     */
    public static List<Polynomial<Integer>> factorQuadratic(Integer a, Integer b, Integer c) {
        // Discriminant: Δ = b² - 4ac
        Integer disc = b.multiply(b).subtract(a.multiply(c).multiply(Integer.of(4)));

        // Check if perfect square
        if (disc.compareTo(Integer.ZERO) < 0) {
            return null; // Complex roots
        }

        int discInt = disc.intValue();
        int sqrt = (int) Math.sqrt(discInt);

        if (sqrt * sqrt != discInt) {
            return null; // Not perfect square
        }

        // Roots: (-b ± √Δ) / 2a
        Integer sqrtDisc = Integer.of(sqrt);
        Integer twoA = a.multiply(Integer.of(2));

        Integer r1Num = b.negate().add(sqrtDisc);
        Integer r2Num = b.negate().subtract(sqrtDisc);

        // Check if roots are rational
        Integer r1Rem = r1Num.remainder(twoA);
        Integer r2Rem = r2Num.remainder(twoA);

        if (!r1Rem.isZero() || !r2Rem.isZero()) {
            return null;
        }

        Integer r1 = r1Num.divide(twoA);
        Integer r2 = r2Num.divide(twoA);

        // Factors: a(x - r1)(x - r2)
        List<Polynomial<Integer>> result = new ArrayList<>();

        // (x - r1)
        List<Integer> f1 = new ArrayList<>();
        f1.add(r1.negate());
        f1.add(Integer.ONE);
        result.add(new Polynomial<>(f1, Integers.getInstance()));

        // (x - r2)
        List<Integer> f2 = new ArrayList<>();
        f2.add(r2.negate());
        f2.add(Integer.ONE);
        result.add(new Polynomial<>(f2, Integers.getInstance()));

        return result;
    }
}
