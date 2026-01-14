/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.measure.Quantity;
import org.jscience.measure.Quantities;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Temperature;

/**
 * Stellar spectral classification.
 * Morgan-Keenan (MK) system.
 * Data loaded from spectral_classes.json.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
    private Quantity<Temperature> minTemp;
    private Quantity<Temperature> maxTemp;
    private String color;
    private String features;
    private Real bvColorIndex; // Approximate B-V index upper bound

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
            // Use DTO or just assume JSON maps to setters
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

    public static void register(SpectralClass spectralClass) {
        if (spectralClass != null && spectralClass.getSymbol() != null) {
            REGISTRY.add(spectralClass);
            BY_SYMBOL.put(spectralClass.getSymbol(), spectralClass);
        }
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

    public Quantity<Temperature> getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double val) {
        this.minTemp = Quantities.create(val, Units.KELVIN);
    }

    public void setMinTemp(Quantity<Temperature> minTemp) {
        this.minTemp = minTemp;
    }

    public Quantity<Temperature> getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double val) {
        this.maxTemp = Quantities.create(val, Units.KELVIN);
    }

    public void setMaxTemp(Quantity<Temperature> maxTemp) {
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

    public Real getBvColorIndex() {
        return bvColorIndex;
    }

    public void setBvColorIndex(double val) {
        this.bvColorIndex = Real.of(val);
    }

    public void setBvColorIndex(Real bvColorIndex) {
        this.bvColorIndex = bvColorIndex;
    }

    /**
     * Estimates spectral class from effective temperature.
     */
    public static SpectralClass fromTemperature(double temperature) {
        return fromTemperature(Quantities.create(temperature, Units.KELVIN));
    }

    public static SpectralClass fromTemperature(Quantity<Temperature> temperature) {
        for (SpectralClass sc : REGISTRY) {
            // Check if temperature is within range [min, max)
            // minTemp <= temperature < maxTemp
            // Note: Use compareTo for Quantities
            if (temperature.compareTo(sc.minTemp) >= 0 && temperature.compareTo(sc.maxTemp) < 0) {
                return sc;
            }
        }
        // Fallback logic
        if (!REGISTRY.isEmpty()) {
            if (temperature.compareTo(REGISTRY.get(0).maxTemp) >= 0)
                return REGISTRY.get(0); // Hottest
            return REGISTRY.get(REGISTRY.size() - 1); // Coolest
        }
        return null;
    }

    /**
     * Estimates spectral class from B-V color index.
     */
    public static SpectralClass fromColorIndex(double bv) {
        return fromColorIndex(Real.of(bv));
    }

    public static SpectralClass fromColorIndex(Real bv) {
        for (SpectralClass sc : REGISTRY) {
            if (bv.compareTo(sc.bvColorIndex) < 0) {
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


