package org.jscience.mathematics.logic.fuzzy;

import org.jscience.mathematics.logic.Logic;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Fuzzy Logic system where truth values are Real numbers in [0, 1].
 * <p>
 * Uses Zadeh operators:
 * <ul>
 * <li>AND(a, b) = min(a, b)</li>
 * <li>OR(a, b) = max(a, b)</li>
 * <li>NOT(a) = 1 - a</li>
 * </ul>
 * </p>
 * 
 * <h2>References</h2>
 * <ul>
 * <li>Lotfi A. Zadeh, "Fuzzy Sets", Information and Control,
 * Vol. 8, No. 3, 1965, pp. 338-353 (foundational paper)</li>
 * <li>George J. Klir and Bo Yuan, "Fuzzy Sets and Fuzzy Logic: Theory and
 * Applications",
 * Prentice Hall, 1995</li>
 * </ul>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FuzzyLogic implements Logic<Real> {

    private static final FuzzyLogic INSTANCE = new FuzzyLogic();

    public static FuzzyLogic getInstance() {
        return INSTANCE;
    }

    private FuzzyLogic() {
    }

    @Override
    public Real trueValue() {
        return Real.ONE;
    }

    @Override
    public Real falseValue() {
        return Real.ZERO;
    }

    @Override
    public Real and(Real a, Real b) {
        return (a.compareTo(b) <= 0) ? a : b; // min(a, b)
    }

    @Override
    public Real or(Real a, Real b) {
        return (a.compareTo(b) >= 0) ? a : b; // max(a, b)
    }

    @Override
    public Real not(Real a) {
        return Real.ONE.subtract(a);
    }
}
