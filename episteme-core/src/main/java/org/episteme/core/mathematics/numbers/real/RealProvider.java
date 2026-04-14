package org.episteme.core.mathematics.numbers.real;

import java.math.BigDecimal;
import org.episteme.core.technical.algorithm.AlgorithmProvider;

/**
 * Service Provider Interface for high-precision Real number implementations.
 * <p>
 * This allows the core module to use native implementations (like MPFR via NativeRealBig)
 * when available, while falling back to the default RealBig (BigDecimalMath) otherwise.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface RealProvider extends AlgorithmProvider {

    /**
     * Creates a Real instance from a BigDecimal.
     */
    Real create(BigDecimal value);

    /**
     * Creates a Real instance from a String.
     */
    Real of(String value);

    /**
     * Returns a mathematical constant by name (e.g., "pi", "e").
     * Implementations should return a Real with at least the requested precision.
     */
    default Real getConstant(String name) {
        return null;
    }

    @Override
    default String getAlgorithmType() {
        return "Numbers/Real";
    }
}
