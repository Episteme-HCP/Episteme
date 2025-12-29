package org.jscience.device.sensors;

import org.jscience.device.Sensor;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface for pH meters.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface PHMeter extends Sensor<Real> {
    Real NEUTRAL_PH = Real.of(7.0);
    Real MIN_PH = Real.ZERO;
    Real MAX_PH = Real.of(14.0);

    Real getAccuracy();

    /**
     * Measure the pH given an actual physical pH value.
     */
    Real measure(Real actualPH);

    /**
     * Classifies the pH value (Acidic, Alkaline, Neutral).
     */
    String classify(Real pH);

    Real getHydrogenConcentration(Real pH);
}
