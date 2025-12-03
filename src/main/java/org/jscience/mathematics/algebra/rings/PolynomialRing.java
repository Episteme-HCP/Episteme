package org.jscience.mathematics.algebra.rings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jscience.mathematics.algebra.Ring;
import java.util.TreeMap;

/**
 * Represents a Polynomial Ring R[x] over a ring R.
 * <p>
 * Elements are polynomials with coefficients in R.
 * </p>
 * 
 * @param <E> the type of coefficients (must be a Ring element)
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PolynomialRing<E> implements Ring<PolynomialRing.Polynomial<E>> {

    private final Ring<E> coefficientRing;
    private final String variableName;

    public PolynomialRing(Ring<E> coefficientRing, String variableName) {
        this.coefficientRing = coefficientRing;
        this.variableName = variableName;
    }

    public Ring<E> getCoefficientRing() {
        return coefficientRing;
    }

    @Override
    public Polynomial<E> operate(Polynomial<E> left, Polynomial<E> right) {
        return add(left, right);
    }

    @Override
    public Polynomial<E> add(Polynomial<E> a, Polynomial<E> b) {
        return a.add(b);
    }

    @Override
    public Polynomial<E> zero() {
        return new Polynomial<>(Collections.emptyMap(), this);
    }

    @Override
    public Polynomial<E> negate(Polynomial<E> element) {
        return element.negate();
    }

    @Override
    public Polynomial<E> inverse(Polynomial<E> element) {
        return negate(element);
    }

    @Override
    public Polynomial<E> multiply(Polynomial<E> a, Polynomial<E> b) {
        return a.multiply(b);
    }

    @Override
    public Polynomial<E> one() {
        return new Polynomial<>(Collections.singletonMap(0, coefficientRing.one()), this);
    }

    @Override
    public boolean isCommutative() {
        return coefficientRing.isCommutative();
    }

    @Override
    public boolean isMultiplicationCommutative() {
        return coefficientRing.isMultiplicationCommutative();
    }

    @Override
    public String description() {
        return coefficientRing.description() + "[" + variableName + "]";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Polynomial<E> element) {
        return element != null && element.ring == this;
    }

    /**
     * Represents a polynomial P(x) = sum(a_i * x^i).
     */
    public static class Polynomial<E> {
        private final Map<Integer, E> coefficients; // degree -> coefficient
        private final PolynomialRing<E> ring;

        private Polynomial(Map<Integer, E> coefficients, PolynomialRing<E> ring) {
            this.coefficients = new TreeMap<>(coefficients); // Sorted by degree
            this.ring = ring;
            // Remove zero coefficients
            this.coefficients.entrySet().removeIf(entry -> entry.getValue().equals(ring.coefficientRing.zero()));
        }

        public Polynomial<E> add(Polynomial<E> other) {
            Map<Integer, E> newCoeffs = new TreeMap<>(this.coefficients);
            for (Map.Entry<Integer, E> entry : other.coefficients.entrySet()) {
                int deg = entry.getKey();
                E val = entry.getValue();
                newCoeffs.merge(deg, val, ring.coefficientRing::add);
            }
            return new Polynomial<>(newCoeffs, ring);
        }

        public Polynomial<E> negate() {
            Map<Integer, E> newCoeffs = new TreeMap<>();
            for (Map.Entry<Integer, E> entry : this.coefficients.entrySet()) {
                newCoeffs.put(entry.getKey(), ring.coefficientRing.negate(entry.getValue()));
            }
            return new Polynomial<>(newCoeffs, ring);
        }

        public Polynomial<E> multiply(Polynomial<E> other) {
            Map<Integer, E> newCoeffs = new TreeMap<>();
            for (Map.Entry<Integer, E> term1 : this.coefficients.entrySet()) {
                for (Map.Entry<Integer, E> term2 : other.coefficients.entrySet()) {
                    int newDeg = term1.getKey() + term2.getKey();
                    E newVal = ring.coefficientRing.multiply(term1.getValue(), term2.getValue());
                    newCoeffs.merge(newDeg, newVal, ring.coefficientRing::add);
                }
            }
            return new Polynomial<>(newCoeffs, ring);
        }

        public E evaluate(E x) {
            E result = ring.coefficientRing.zero();
            E xPow = ring.coefficientRing.one();

            // Evaluate sum(a_i * x^i)
            // Optimization: Horner's method would be better but requires ordering.
            // Since we have a TreeMap, we can iterate.
            // But Horner's works best high-to-low.

            E currentVal = ring.coefficientRing.zero();
            // Iterate high degree to low for Horner's
            List<Integer> degrees = new ArrayList<>(coefficients.keySet());
            Collections.reverse(degrees);

            if (degrees.isEmpty())
                return result;

            int currentDeg = degrees.get(0);

            // Naive implementation for general rings (can be optimized)
            for (Map.Entry<Integer, E> entry : coefficients.entrySet()) {
                E term = entry.getValue();
                int deg = entry.getKey();
                E power = ring.coefficientRing.one();
                for (int i = 0; i < deg; i++) {
                    power = ring.coefficientRing.multiply(power, x);
                }
                term = ring.coefficientRing.multiply(term, power);
                result = ring.coefficientRing.add(result, term);
            }
            return result;
        }

        public Map<Integer, E> getCoefficients() {
            return Collections.unmodifiableMap(coefficients);
        }

        @Override
        public String toString() {
            if (coefficients.isEmpty())
                return "0";
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            // Iterate in descending order of degree
            List<Integer> degrees = new ArrayList<>(coefficients.keySet());
            Collections.reverse(degrees);

            for (int deg : degrees) {
                E coeff = coefficients.get(deg);
                if (!first)
                    sb.append(" + ");
                sb.append(coeff);
                if (deg > 0) {
                    sb.append(ring.variableName);
                    if (deg > 1)
                        sb.append("^").append(deg);
                }
                first = false;
            }
            return sb.toString();
        }
    }

    public Polynomial<E> create(Map<Integer, E> coefficients) {
        return new Polynomial<>(coefficients, this);
    }
}
