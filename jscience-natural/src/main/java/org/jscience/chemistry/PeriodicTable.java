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

package org.jscience.chemistry;

import org.jscience.chemistry.elements.*;
import org.jscience.measure.Units;

import org.jscience.measure.quantity.MassDensity;
import org.jscience.measure.quantity.Mass;
import org.jscience.measure.quantity.Temperature;

import org.jscience.measure.Quantity;
import org.jscience.measure.Unit;
import org.jscience.util.SimpleJson;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.nio.charset.StandardCharsets;

import org.jscience.measure.Quantities;

/**
 * This class provides access to the elements of the periodic table.
 * Modernized for JScience V5 to return {@link Element} objects with
 * {@link Quantity} properties.
 * Loads data from elements.json.
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class PeriodicTable {

    // Custom Units derived for XML/JSON data
    private static final Unit<Temperature> KELVIN = Units.KELVIN;

    // 1 u Ã¢â€°Ë† 1.66053906660e-27 kg
    private static final Unit<Mass> UNIFIED_ATOMIC_MASS = Units.KILOGRAM.multiply(1.66053906660e-27);

    // Note: Creating composite units directly using operations on base units
    private static final Unit<MassDensity> G_PER_CM3 = Units.GRAM.divide(Units.CENTIMETER.pow(3))
            .asType(MassDensity.class);

    private static final Map<String, Element> table = new HashMap<>();
    private static final Map<String, String> symbolToName = new HashMap<>();
    private static boolean loaded = false;

    private PeriodicTable() {
    }

    /**
     * Returns the name for a symbol (e.g. "H" -> "Hydrogen").
     *
     * @param symbol the chemical symbol.
     * @return the corresponding name.
     */
    public static String getName(String symbol) {
        ensureLoaded();
        return symbolToName.get(symbol);
    }

    /**
     * Returns an element from its name or symbol.
     *
     * @param nameOrSymbol the element name (e.g. "Hydrogen") or symbol (e.g. "H").
     * @return the corresponding element or {@code null} if not found.
     */
    public static Element getElement(String nameOrSymbol) {
        ensureLoaded();
        if (nameOrSymbol == null)
            return null;

        // Try as symbol first
        if (nameOrSymbol.length() <= 3 && symbolToName.containsKey(nameOrSymbol)) {
            String name = symbolToName.get(nameOrSymbol);
            return table.get(name.toLowerCase());
        }

        return table.get(nameOrSymbol.toLowerCase());
    }

    /**
     * Retrieves an element by its chemical symbol (Legacy alias).
     * 
     * @param symbol the symbol (e.g. "H", "He").
     * @return the corresponding element or null if not found.
     */
    public static Element bySymbol(String symbol) {
        return getElement(symbol);
    }

    /**
     * Registers an element manually.
     * 
     * @param element the element to register.
     */
    public static void registerElement(Element element) {
        if (element != null && element.getSymbol() != null) {
            String lowerName = element.getName().toLowerCase();
            synchronized (table) {
                table.put(lowerName, element);
                symbolToName.put(element.getSymbol(), element.getName());
            }
        }
    }

    public static int getElementCount() {
        ensureLoaded();
        synchronized (table) {
            return table.size();
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadElements() {
        try {
            List<Element> elements = org.jscience.chemistry.loaders.PeriodicTableReader.loadFromResource("elements.json");
            for (Element e : elements) {
                registerElement(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


