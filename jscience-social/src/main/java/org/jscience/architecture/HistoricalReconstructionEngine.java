package org.jscience.architecture;

import java.util.*;

/**
 * Engine for virtual 3D reconstruction from historical sources.
 */
public final class HistoricalReconstructionEngine {

    private HistoricalReconstructionEngine() {}

    public record BuildingFeature(String type, String material, double value) {}

    /**
     * Generates a structural model based on text descriptions.
     * Example: "Wall.Stone.10.2"
     */
    public static List<BuildingFeature> reconstruct(String description) {
        List<BuildingFeature> features = new ArrayList<>();
        String[] parts = description.split(";");
        for (String p : parts) {
            String[] tokens = p.trim().split("\\.");
            if (tokens.length >= 3) {
                features.add(new BuildingFeature(tokens[0], tokens[1], Double.parseDouble(tokens[2])));
            }
        }
        return features;
    }
}
