/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
public class AcidBaseLoader {

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
