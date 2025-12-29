/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.chemistry;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;

import org.jscience.measure.Units;

import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Temperature;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads chemical elements from JSON configuration.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PeriodicTableLoader {

    public static List<Element> load(String resourcePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = PeriodicTableLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                is = PeriodicTableLoader.class.getResourceAsStream("/" + resourcePath);
            }
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            JsonNode root = mapper.readTree(is);
            List<Element> elements = new ArrayList<>();

            if (root.has("elements") && root.get("elements").isArray()) {
                for (JsonNode node : root.get("elements")) {
                    elements.add(parseElement(node));
                }
            }
            return elements;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load periodic table from " + resourcePath, e);
        }
    }

    private static Element parseElement(JsonNode node) {
        int atomicNumber = node.get("atomicNumber").asInt();
        String symbol = node.get("symbol").asText();
        String name = node.get("name").asText();

        // Units assumption based on standard scientific datasets (e.g. PubChem/NIST)
        // Mass in u (atomic mass units). 1 u = 1.66053906660e-27 kg approx.
        // For simplicity, we might store as Unified Atomic Mass Unit if defined,
        // or convert to KG. Let's use generic Mass for now.
        // JScience Units.GRAM might be easiest default for now if AMU missing.
        double atomicMassVal = node.get("atomicMass").asDouble();
        Quantity<Mass> atomicMass = Quantities.create(atomicMassVal, Units.GRAM.multiply(1.66053906660e-24));
        // 1 amu = 1.66e-24 g.

        int group = node.has("group") ? node.get("group").asInt() : 0;
        int period = node.has("period") ? node.get("period").asInt() : 0;

        String catStr = node.has("category") ? node.get("category").asText().toUpperCase().replace(" ", "_")
                : "UNKNOWN";
        Element.ElementCategory category;
        try {
            category = Element.ElementCategory.valueOf(catStr);
        } catch (IllegalArgumentException e) {
            category = Element.ElementCategory.UNKNOWN;
        }

        double electro = node.has("electronegativity") ? node.get("electronegativity").asDouble() : 0.0;

        Quantity<Temperature> melt = null;
        if (node.has("melt")) {
            melt = Quantities.create(node.get("melt").asDouble(), Units.KELVIN);
        }

        Quantity<Temperature> boil = null;
        if (node.has("boil")) {
            boil = Quantities.create(node.get("boil").asDouble(), Units.KELVIN);
        }

        Element e = new Element(name, symbol);
        e.setAtomicNumber(atomicNumber);
        e.setAtomicMass(atomicMass);
        e.setGroup(group);
        e.setPeriod(period);
        e.setCategory(category);
        e.setElectronegativity(electro);
        e.setMeltingPoint(melt);
        e.setBoilingPoint(boil);

        return e;
    }
}
