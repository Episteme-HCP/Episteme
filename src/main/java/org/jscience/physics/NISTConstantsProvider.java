package org.jscience.physics;

import java.util.Map;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Provider interface for NIST physical constants.
 * <p>
 * Intended to allow fetching the latest CODATA values from NIST archives.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface NISTConstantsProvider {

    /**
     * Fetches the latest constants.
     * 
     * @return map of constant name to value (Real)
     * @throws Exception if unreachable
     */
    Map<String, Real> fetchConstants() throws Exception;

    /**
     * Default stub implementation.
     */
    class Stub implements NISTConstantsProvider {
        @Override
        public Map<String, Real> fetchConstants() {
            // Placeholder: In a real implementation, this would query physics.nist.gov
            System.out.println("Connecting to NIST (simulated)...");
            return java.util.Collections.emptyMap();
        }
    }
}
