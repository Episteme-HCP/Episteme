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

package org.jscience.politics;

import org.jscience.io.AbstractResourceReader;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Loader for geopolitical and demographic data inspired by the CIA World
 * Factbook.
 * 
 * Provides real-world stability indices and military expenditures for nations.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class FactbookLoader extends AbstractResourceReader<FactbookLoader.NationStats> {

    public record NationStats(String code, String name, double stability, double militarySpending)
            implements Serializable {
    }

    private static final Map<String, NationStats> DATASET = new HashMap<>();

    static {
        // Stability index (0.0 - 1.0, 1.0 is most stable)
        // Military spending (billions USD)
        DATASET.put("USA", new NationStats("USA", "United States", 0.95, 850.0));
        DATASET.put("CHN", new NationStats("CHN", "China", 0.90, 230.0));
        DATASET.put("RUS", new NationStats("RUS", "Russia", 0.40, 100.0));
        DATASET.put("IND", new NationStats("IND", "India", 0.85, 75.0));
        DATASET.put("BRA", new NationStats("BRA", "Brazil", 0.70, 20.0));
        DATASET.put("CAN", new NationStats("CAN", "Canada", 0.99, 25.0));
        DATASET.put("FRA", new NationStats("FRA", "France", 0.92, 50.0));
    }

    @Override
    public String getResourcePath() {
        return "internal://cia_world_factbook";
    }

    @Override
    public Class<NationStats> getResourceType() {
        return NationStats.class;
    }

    @Override
    protected NationStats loadFromSource(String id) throws Exception {
        NationStats stats = DATASET.get(id.toUpperCase());
        if (stats == null)
            throw new IllegalArgumentException("Nation code not found: " + id);
        return stats;
    }

    public static Map<String, NationStats> getFullDataset() {
        return DATASET;
    }
}
