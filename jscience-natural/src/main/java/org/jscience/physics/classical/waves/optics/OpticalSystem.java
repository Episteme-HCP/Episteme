package org.jscience.physics.classical.waves.optics;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an optical system consisting of multiple optical elements.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class OpticalSystem {

    private List<OpticalElement> elements = new ArrayList<>();

    public void addElement(OpticalElement element) {
        elements.add(element);
    }

    public List<OpticalElement> getElements() {
        return elements;
    }

    /**
     * Traces a ray through the entire optical system sequentially.
     * 
     * @param inputRay The input light ray.
     * @return The final output ray after passing through all elements, or null if
     *         lost.
     */
    public LightRay traceSystem(LightRay inputRay) {
        LightRay currentRay = inputRay;
        for (OpticalElement element : elements) {
            currentRay = element.trace(currentRay);
            if (currentRay == null) {
                return null;
            }
        }
        return currentRay;
    }
}
