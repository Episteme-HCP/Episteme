package org.jscience.architecture.loaders;

import org.jscience.io.ResourceReader;
import java.util.Map;
import java.util.HashMap;

/**
 * Loader for acoustic material absorption coefficients.
 */
public class AcousticEnvironmentLoader implements ResourceReader<Map<String, double[]>> {

    private final String source;

    public AcousticEnvironmentLoader(String source) {
        this.source = source;
    }

    /**
     * Absorption coefficients at standard frequencies: 125, 250, 500, 1000, 2000, 4000 Hz.
     */
    @Override
    public Map<String, double[]> load(String resourceId) throws Exception {
        Map<String, double[]> materials = new HashMap<>();
        
        // Standard absorption coefficients (from ISO 354 / ASTM C423)
        materials.put("concrete_painted", new double[]{0.01, 0.01, 0.02, 0.02, 0.02, 0.03});
        materials.put("brick_unglazed", new double[]{0.03, 0.03, 0.03, 0.04, 0.05, 0.07});
        materials.put("glass_window", new double[]{0.35, 0.25, 0.18, 0.12, 0.07, 0.04});
        materials.put("plaster_gypsum", new double[]{0.29, 0.10, 0.05, 0.04, 0.07, 0.09});
        materials.put("wood_panel", new double[]{0.42, 0.21, 0.10, 0.08, 0.06, 0.06});
        materials.put("carpet_heavy", new double[]{0.02, 0.06, 0.14, 0.37, 0.60, 0.65});
        materials.put("curtain_heavy", new double[]{0.07, 0.31, 0.49, 0.75, 0.70, 0.60});
        materials.put("acoustic_tile", new double[]{0.70, 0.66, 0.72, 0.92, 0.88, 0.75});
        materials.put("fiberglass_50mm", new double[]{0.30, 0.70, 0.90, 0.90, 0.85, 0.80});
        materials.put("audience_seated", new double[]{0.60, 0.74, 0.88, 0.96, 0.93, 0.85});
        materials.put("air_per_meter", new double[]{0.0, 0.0, 0.0, 0.003, 0.009, 0.03});
        
        return materials;
    }

    @Override
    public String getResourcePath() {
        return source;
    }

    @Override
    public Class<Map<String, double[]>> getResourceType() {
        @SuppressWarnings("unchecked")
        Class<Map<String, double[]>> type = (Class<Map<String, double[]>>) (Class<?>) Map.class;
        return type;
    }

    @Override
    public String getName() {
        return "Acoustic Material Loader";
    }

    @Override
    public String getDescription() {
        return "Loads absorption coefficients for acoustic materials at standard frequencies.";
    }

    @Override
    public String getLongDescription() {
        return "Provides ISO/ASTM standard absorption coefficients at 125, 250, 500, 1000, 2000, 4000 Hz.";
    }

    @Override
    public String getCategory() {
        return "Architecture / Acoustics";
    }
}
