
package org.jscience.physics.astronomy.spectroscopy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Stellar spectral classification.
 * Morgan-Keenan (MK) system.
 * Data loaded from spectral_classes.json.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class SpectralClass {

    private static final List<SpectralClass> REGISTRY = new ArrayList<>();
    private static final Map<String, SpectralClass> BY_SYMBOL = new HashMap<>();

    static {
        loadData();
    }

    // Standard Instances for backward compatibility
    public static final SpectralClass O = get("O");
    public static final SpectralClass B = get("B");
    public static final SpectralClass A = get("A");
    public static final SpectralClass F = get("F");
    public static final SpectralClass G = get("G");
    public static final SpectralClass K = get("K");
    public static final SpectralClass M = get("M");
    public static final SpectralClass L = get("L");
    public static final SpectralClass T = get("T");
    public static final SpectralClass Y = get("Y");

    private String symbol;
    private double minTemp;
    private double maxTemp;
    private String color;
    private String features;
    private double bvColorIndex; // Approximate B-V index upper bound

    // Simple constructor for Jackson
    public SpectralClass() {
    }

    private static void loadData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = SpectralClass.class.getResourceAsStream("spectral_classes.json");
            if (is == null) {
                // Fallback or error? For now, we print stacktrace but ideally logging
                System.err.println("Could not find spectral_classes.json");
                return;
            }
            List<SpectralClass> classes = mapper.readValue(is, new TypeReference<List<SpectralClass>>() {
            });
            for (SpectralClass sc : classes) {
                REGISTRY.add(sc);
                BY_SYMBOL.put(sc.symbol, sc);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load spectral classes", e);
        }
    }

    public static SpectralClass get(String symbol) {
        return BY_SYMBOL.get(symbol);
    }

    public static List<SpectralClass> values() {
        return Collections.unmodifiableList(REGISTRY);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public double getBvColorIndex() {
        return bvColorIndex;
    }

    public void setBvColorIndex(double bvColorIndex) {
        this.bvColorIndex = bvColorIndex;
    }

    /**
     * Estimates spectral class from effective temperature.
     */
    public static SpectralClass fromTemperature(double temperature) {
        for (SpectralClass sc : REGISTRY) {
            if (temperature >= sc.minTemp && temperature < sc.maxTemp) {
                return sc;
            }
        }
        // Fallback logic
        if (!REGISTRY.isEmpty()) {
            if (temperature >= REGISTRY.get(0).maxTemp)
                return REGISTRY.get(0); // Hottest
            return REGISTRY.get(REGISTRY.size() - 1); // Coolest
        }
        return null;
    }

    /**
     * Estimates spectral class from B-V color index.
     */
    public static SpectralClass fromColorIndex(double bv) {
        for (SpectralClass sc : REGISTRY) {
            if (bv < sc.bvColorIndex) {
                return sc;
            }
        }
        if (!REGISTRY.isEmpty())
            return REGISTRY.get(REGISTRY.size() - 1);
        return null;
    }

    @Override
    public String toString() {
        return "SpectralClass " + symbol;
    }
}
