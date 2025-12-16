package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Interface representing an element in an optical system (lens, mirror, etc.).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public interface OpticalElement {

    /**
     * Traces a light ray through this optical element.
     * 
     * @param incoming The incoming light ray.
     * @return The outgoing light ray after interaction with the element, or null if
     *         absorbed/blocked.
     */
    LightRay trace(LightRay incoming);

    /**
     * Returns the focal length of this element.
     * 
     * @return The focal length (positive for converging, negative for diverging).
     */
    Real getFocalLength();
}
