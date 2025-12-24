/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.anatomy;

import java.util.Map;

/**
 * Data registry for Anatomy Viewer.
 * Maps extensive anatomical names to potential download URLs.
 * <p>
 * In a real scenario, this would load from a JSON manifest.
 * For now, we point to the GitHub raw content of the low-poly models if
 * available,
 * or just act as a metadata holder.
 * </p>
 */
public class AnatomyData {

    // Example mapping
    public static final Map<String, String> PARTS = Map.of(
            "Skull", "https://github.com/Kevin-Mattheus-Moerman/BodyParts3D/raw/master/data/stl/FMA7128.stl", // Example
                                                                                                              // FMA ID
            "Mandible", "https://github.com/Kevin-Mattheus-Moerman/BodyParts3D/raw/master/data/stl/FMA52748.stl");

    public static String getUrl(String partName) {
        return PARTS.get(partName);
    }
}
