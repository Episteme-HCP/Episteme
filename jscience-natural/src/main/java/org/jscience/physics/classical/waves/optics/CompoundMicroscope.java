package org.jscience.physics.classical.waves.optics;

import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a compound microscope optical system.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class CompoundMicroscope {

    private final Real objectiveFocalLength;
    private final Real eyepieceFocalLength;
    private final Real tubeLength;
    private final Real objectiveRadius;

    public CompoundMicroscope(Real objectiveFocalLength, Real eyepieceFocalLength,
            Real tubeLength, Real objectiveRadius) {
        this.objectiveFocalLength = objectiveFocalLength;
        this.eyepieceFocalLength = eyepieceFocalLength;
        this.tubeLength = tubeLength;
        this.objectiveRadius = objectiveRadius;
    }

    /**
     * Returns the objective magnification.
     * M_obj = L / f_obj
     */
    public Real getObjectiveMagnification() {
        return tubeLength.divide(objectiveFocalLength);
    }

    /**
     * Returns the eyepiece magnification.
     * M_eye = 0.25m / f_eye (using 25cm near point)
     */
    public Real getEyepieceMagnification() {
        return Real.of(0.25).divide(eyepieceFocalLength);
    }

    /**
     * Returns total magnification.
     */
    public Real getTotalMagnification() {
        return getObjectiveMagnification().multiply(getEyepieceMagnification());
    }

    /**
     * Calculates the image position for a given object distance.
     * Uses thin lens equation: 1/f = 1/do + 1/di
     * 
     * @param objectDistance Distance from object to objective lens
     * @return Image distance from objective lens
     */
    public Real calculateImageDistance(Real objectDistance) {
        // 1/di = 1/f - 1/do
        Real invDi = Real.ONE.divide(objectiveFocalLength).subtract(Real.ONE.divide(objectDistance));
        return Real.ONE.divide(invDi);
    }

    /**
     * Returns the working distance for a given object position.
     * Working distance is typically just outside the focal length.
     */
    public Real getWorkingDistance() {
        return objectiveFocalLength.multiply(Real.of(1.1)); // ~10% beyond focal point
    }

    // Getters
    public Real getObjectiveFocalLength() {
        return objectiveFocalLength;
    }

    public Real getEyepieceFocalLength() {
        return eyepieceFocalLength;
    }

    public Real getTubeLength() {
        return tubeLength;
    }

    public Real getObjectiveRadius() {
        return objectiveRadius;
    }
}
