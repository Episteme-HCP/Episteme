package org.jscience.mathematics.algebra.groups;

import org.jscience.mathematics.structures.groups.Group;
import org.jscience.mathematics.numbers.complex.Quaternion;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * Represents the Quaternion Group Q8.
 * <p>
 * A non-abelian group of order 8: {1, -1, i, -i, j, -j, k, -k}.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QuaternionGroup implements Group<Quaternion> {

    private static final Set<Quaternion> ELEMENTS = new HashSet<>(Arrays.asList(
            Quaternion.ONE, Quaternion.ONE.negate(),
            Quaternion.I, Quaternion.I.negate(),
            Quaternion.J, Quaternion.J.negate(),
            Quaternion.K, Quaternion.K.negate()));

    @Override
    public Quaternion operate(Quaternion left, Quaternion right) {
        Quaternion result = left.multiply(right);
        return result;
    }

    @Override
    public Quaternion identity() {
        return Quaternion.ONE;
    }

    @Override
    public Quaternion inverse(Quaternion element) {
        return element.inverse();
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    public Set<Quaternion> getElements() {
        return ELEMENTS;
    }

    @Override
    public String description() {
        return "Quaternion Group Q8";
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Quaternion element) {
        return ELEMENTS.contains(element);
    }
}
