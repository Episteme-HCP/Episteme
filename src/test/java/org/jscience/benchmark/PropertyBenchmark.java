/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.benchmark;

import org.jscience.measure.Quantity;
import org.jscience.physics.classical.matter.Material;

import java.util.HashMap;
import java.util.Map;

/**
 * Benchmarks for the Property System.
 * <p>
 * Measures performance of property access, listeners, and updates.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class PropertyBenchmark {

    public static void run() {
        System.out.println("\n--- Property Lookup Benchmarks ---");

        benchmarkHashMap();
        benchmarkPropertySet();
    }

    private static void benchmarkHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("density", "8960 kg/m^3");

        SimpleBenchmarkRunner.run("HashMap<String, Object> Lookup", 1000, 10000, () -> {
            Object val = map.get("density");
            if (val == null)
                throw new RuntimeException("Lookup failed");
        });
    }

    private static void benchmarkPropertySet() {
        Material copper = Material.COPPER;
        // Pre-load to avoid I/O during benchmark
        copper.getDensity();

        SimpleBenchmarkRunner.run("Material.get(PropertyKey) Lookup", 1000, 10000, () -> {
            Quantity<?> q = copper.getDensity();
            if (q == null)
                throw new RuntimeException("Lookup failed");
        });
    }
}
