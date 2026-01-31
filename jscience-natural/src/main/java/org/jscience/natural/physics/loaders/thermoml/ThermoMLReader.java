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

package org.jscience.natural.physics.loaders.thermoml;

import org.jscience.core.io.AbstractResourceReader;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.io.InputStream;

/**
 * Modernized ThermoML Reader for the IUPAC/NIST thermodynamic property data.
 * <p>
 * This reader integrates ThermoML data into the JScience physics model, converting
 * raw XML values into type-safe {@link org.jscience.core.measure.Quantity} objects.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThermoMLReader extends AbstractResourceReader<ThermoMLDataReport> {

    @Override
    protected ThermoMLDataReport loadFromSource(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            return read(file);
        }
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) return read(is);
        }
        throw new Exception("Source not found: " + path);
    }

    @Override
    protected ThermoMLDataReport loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    public ThermoMLDataReport read(InputStream input) throws Exception {
        DocumentBuilder builder = org.jscience.core.io.SecureXMLFactory.createSecureDocumentBuilder();
        Document doc = builder.parse(input);
        return parse(doc.getDocumentElement());
    }

    public ThermoMLDataReport read(File file) throws Exception {
        DocumentBuilder builder = org.jscience.core.io.SecureXMLFactory.createSecureDocumentBuilder();
        Document doc = builder.parse(file);
        return parse(doc.getDocumentElement());
    }

    private ThermoMLDataReport parse(Element root) {
        ThermoMLDataReport report = new ThermoMLDataReport();
        
        // Compounds
        NodeList compounds = root.getElementsByTagName("Compound");
        for (int i = 0; i < compounds.getLength(); i++) {
            report.addCompound(parseCompound((Element) compounds.item(i), i));
        }

        // Properties
        NodeList pureData = root.getElementsByTagName("PureOrMixtureData");
        for (int i = 0; i < pureData.getLength(); i++) {
            parseData((Element) pureData.item(i), report);
        }

        return report;
    }

    private ThermoMLCompound parseCompound(Element elem, int index) {
        ThermoMLCompound c = new ThermoMLCompound();
        c.setCompoundIndex(index);
        c.setName(getText(elem, "sStandardName"));
        c.setCasNumber(getText(elem, "nRegNum"));
        c.setMolecularFormula(getText(elem, "sMolecularFormula"));
        return c;
    }

    private void parseData(Element elem, ThermoMLDataReport report) {
        NodeList values = elem.getElementsByTagName("NumValues");
        for (int i = 0; i < values.getLength(); i++) {
            Element valElem = (Element) values.item(i);
            ThermoMLPropertyValue prop = new ThermoMLPropertyValue();
            
            // Extract property name and magnitude
            NodeList propGroup = valElem.getElementsByTagName("VariableValue");
            if (propGroup.getLength() > 0) {
                Element varVal = (Element) propGroup.item(0);
                prop.setPropertyName(getText(varVal, "nPropertyName"));
                double magnitude = getDouble(varVal, "nPropNumber");
                prop.setMagnitude(Quantities.create(magnitude, Units.ONE)); // Default to ONE, specialized in post-processing
            }

            // Extract conditions (Temperature, Pressure)
            NodeList conditions = valElem.getElementsByTagName("Regime");
            for (int j = 0; j < conditions.getLength(); j++) {
                Element cond = (Element) conditions.item(j);
                String condName = getText(cond, "nConstraintName");
                double condVal = getDouble(cond, "nConstraintValue");
                if ("Temperature".equalsIgnoreCase(condName)) {
                    prop.setTemperature(Quantities.create(condVal, Units.KELVIN));
                } else if ("Pressure".equalsIgnoreCase(condName)) {
                    prop.setPressure(Quantities.create(condVal, Units.PASCAL));
                }
            }

            report.addPropertyValue(prop);
        }
    }

    private String getText(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return null;
    }

    private double getDouble(Element parent, String tagName) {
        String text = getText(parent, tagName);
        if (text != null) {
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    @Override public String getName() { return "ThermoML Reader"; }
    @Override public String getDescription() { return "IUPAC/NIST Thermodynamic property reader"; }
    @Override public String getLongDescription() { return "Parses IUPAC/NIST ThermoML files to extract high-accuracy thermodynamic data for chemical compounds."; }
    @Override public String getCategory() { return "Physics / Chemistry"; }
    @Override public Class<ThermoMLDataReport> getResourceType() { return ThermoMLDataReport.class; }
    @Override public String getResourcePath() { return "thermoml"; }
    @Override public String[] getSupportedVersions() { return new String[]{"1.0", "1.1"}; }
}

