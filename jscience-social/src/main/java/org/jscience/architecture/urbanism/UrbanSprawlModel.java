package org.jscience.architecture.urbanism;



/**
 * Models urban sprawl using cellular automata concepts.
 */
public final class UrbanSprawlModel {

    private UrbanSprawlModel() {}

    /**
     * Simulates growth probability based on proximity to infrastructure.
     */
    public static double growthProbability(double distToRoad, double distToCenter, double currentDensity) {
        return (1.0 / (1.0 + distToRoad)) * (1.0 / (1.0 + distToCenter)) * (1.0 - currentDensity);
    }
}
