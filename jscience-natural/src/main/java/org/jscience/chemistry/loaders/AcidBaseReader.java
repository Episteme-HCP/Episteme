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

package org.jscience.chemistry.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads acid-base pKa/pKb data and indicator information.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class AcidBaseReader {

    public static String getCategory() {
        return "Chemistry";
    }

    public static String getDescription() {
        return "Acid-Base Data Reader.";
    }

    public static class Acid {
        public String name;
        public String formula;
        public double pKa;
        public boolean strong;

        public Acid(String name, String formula, double pKa, boolean strong) {
            this.name = name;
            this.formula = formula;
            this.pKa = pKa;
            this.strong = strong;
        }
    }

    public static class Base {
        public String name;
        public String formula;
        public double pKb;
        public boolean strong;

        public Base(String name, String formula, double pKb, boolean strong) {
            this.name = name;
            this.formula = formula;
            this.pKb = pKb;
            this.strong = strong;
        }
    }

    public static class Indicator {
        public String name;
        public double pHLow;
        public double pHHigh;
        public String colorAcid;
        public String colorBase;

        public Indicator(String name, double pHLow, double pHHigh, String colorAcid, String colorBase) {
            this.name = name;
            this.pHLow = pHLow;
            this.pHHigh = pHHigh;
            this.colorAcid = colorAcid;
            this.colorBase = colorBase;
        }
    }

    public static class AcidBaseData {
        public List<Acid> acids = new ArrayList<>();
        public List<Base> bases = new ArrayList<>();
        public List<Indicator> indicators = new ArrayList<>();
    }

    public static AcidBaseData load(InputStream is) throws IOException {
        AcidBaseData data = new AcidBaseData();
        if (is == null)
            return data;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(is);

        // Load acids
        JsonNode acidsNode = root.get("acids");
        if (acidsNode != null && acidsNode.isArray()) {
            for (JsonNode acidNode : acidsNode) {
                data.acids.add(new Acid(
                        acidNode.get("name").asText(),
                        acidNode.get("formula").asText(),
                        acidNode.get("pKa").asDouble(),
                        acidNode.get("strong").asBoolean()));
            }
        }

        // Load bases
        JsonNode basesNode = root.get("bases");
        if (basesNode != null && basesNode.isArray()) {
            for (JsonNode baseNode : basesNode) {
                data.bases.add(new Base(
                        baseNode.get("name").asText(),
                        baseNode.get("formula").asText(),
                        baseNode.get("pKb").asDouble(),
                        baseNode.get("strong").asBoolean()));
            }
        }

        // Load indicators
        JsonNode indicatorsNode = root.get("indicators");
        if (indicatorsNode != null && indicatorsNode.isArray()) {
            for (JsonNode indNode : indicatorsNode) {
                data.indicators.add(new Indicator(
                        indNode.get("name").asText(),
                        indNode.get("pHLow").asDouble(),
                        indNode.get("pHHigh").asDouble(),
                        indNode.get("colorAcid").asText(),
                        indNode.get("colorBase").asText()));
            }
        }

        return data;
    }
}



