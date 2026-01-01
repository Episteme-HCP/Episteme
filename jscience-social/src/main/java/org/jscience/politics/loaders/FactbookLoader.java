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

package org.jscience.politics.loaders;

import org.jscience.politics.Country;
import org.jscience.io.InputLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Production loader for the CIA World Factbook XML data.
 * 
 * Parses country information including:
 * - Basic country details (name, capital, area)
 * - Population and demographics
 * - Government type
 * - Economic indicators
 * - Geographic information
 * 
 * @see <a href="https://www.cia.gov/the-world-factbook/">CIA World Factbook</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FactbookLoader implements InputLoader<List<Country>> {

    private static final Logger LOG = LoggerFactory.getLogger(FactbookLoader.class);

    public FactbookLoader() {
    }

    @Override
    public List<Country> load(String resourceId) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null) {
                LOG.warn("Factbook resource not found: {}, returning empty list", resourceId);
                return new ArrayList<>();
            }
            return load(is);
        }
    }

    @Override
    public String getResourcePath() {
        return "/data/politics/";
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Country>> getResourceType() {
        return (Class<List<Country>>) (Class<?>) List.class;
    }

    /**
     * Loads country data from CIA Factbook XML format.
     * 
     * The Factbook XML structure varies, but typically includes:
     * &lt;countries&gt;
     * &lt;country name="..." code="..."&gt;
     * &lt;capital&gt;...&lt;/capital&gt;
     * &lt;area&gt;...&lt;/area&gt;
     * &lt;population&gt;...&lt;/population&gt;
     * &lt;government&gt;...&lt;/government&gt;
     * &lt;/country&gt;
     * &lt;/countries&gt;
     * 
     * @param input XML input stream
     * @return List of Country objects
     * @throws Exception if parsing fails
     */
    public List<Country> load(InputStream input) throws Exception {
        List<Country> countries = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            LOG.debug("Parsing Factbook XML, root element: {}", doc.getDocumentElement().getNodeName());

            // Try common element names for countries
            NodeList countryNodes = findCountryNodes(doc);

            if (countryNodes.getLength() == 0) {
                LOG.warn("No country nodes found in Factbook XML");
                return countries;
            }

            LOG.info("Found {} countries in Factbook data", countryNodes.getLength());

            for (int i = 0; i < countryNodes.getLength(); i++) {
                Element elem = (Element) countryNodes.item(i);
                try {
                    Country country = parseCountry(elem);
                    if (country != null) {
                        countries.add(country);
                    }
                } catch (Exception e) {
                    LOG.warn("Failed to parse country at index {}: {}", i, e.getMessage());
                }
            }

            LOG.info("Successfully loaded {} countries from Factbook", countries.size());

        } catch (Exception e) {
            LOG.error("Failed to parse Factbook XML", e);
            throw e;
        }

        return countries;
    }

    /**
     * Finds country nodes in the document, trying multiple possible element names.
     */
    private NodeList findCountryNodes(Document doc) {
        // Try different possible element names
        String[] possibleNames = { "country", "Country", "nation", "state" };

        for (String name : possibleNames) {
            NodeList nodes = doc.getElementsByTagName(name);
            if (nodes.getLength() > 0) {
                LOG.debug("Found countries using element name: {}", name);
                return nodes;
            }
        }

        // If no specific elements found, check if root element is a country list
        if ("countries".equalsIgnoreCase(doc.getDocumentElement().getNodeName()) ||
                "factbook".equalsIgnoreCase(doc.getDocumentElement().getNodeName())) {
            return doc.getDocumentElement().getChildNodes();
        }

        return doc.getElementsByTagName("country");
    }

    /**
     * Parses a single country element.
     */
    private Country parseCountry(Element elem) {
        // Skip non-element nodes
        if (elem.getNodeType() != Node.ELEMENT_NODE) {
            return null;
        }

        // Get country name from attribute or child element
        String name = getTextValue(elem, "name");
        if (name == null || name.isEmpty()) {
            name = elem.getAttribute("name");
        }
        if (name == null || name.isEmpty()) {
            LOG.debug("Skipping element without name: {}", elem.getNodeName());
            return null;
        }

        // Get country code (ISO 3166)
        String code = getTextValue(elem, "code");
        if (code == null || code.isEmpty()) {
            code = elem.getAttribute("code");
        }
        if (code == null) {
            code = "";
        }

        Country country = new Country(name, code);

        // Parse additional fields
        parseOptionalFields(elem, country);

        return country;
    }

    /**
     * Parses optional country fields.
     */
    private void parseOptionalFields(Element elem, Country country) {
        // Capital city
        String capital = getTextValue(elem, "capital");
        if (capital != null && !capital.isEmpty()) {
            country.setCapital(capital);
        }

        // Area (square kilometers)
        String area = getTextValue(elem, "area");
        if (area != null && !area.isEmpty()) {
            try {
                double areaValue = parseNumeric(area);
                country.setAreaSqKm(areaValue);
            } catch (NumberFormatException e) {
                LOG.debug("Could not parse area value: {}", area);
            }
        }

        // Population
        String population = getTextValue(elem, "population");
        if (population != null && !population.isEmpty()) {
            try {
                double popValue = parseNumeric(population);
                country.setPopulation((long) popValue);
            } catch (NumberFormatException e) {
                LOG.debug("Could not parse population value: {}", population);
            }
        }

        // Government type - logged but not stored (no field on Country)
        String government = getTextValue(elem, "government");
        if (government != null && !government.isEmpty()) {
            LOG.debug("Country {} government: {}", country.getName(), government);
        }

        // Region/Continent
        String region = getTextValue(elem, "region");
        if (region != null && !region.isEmpty()) {
            country.setContinent(region);
        }
    }

    /**
     * Gets text value of a child element.
     */
    private String getTextValue(Element element, String tagName) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            if (node != null) {
                String text = node.getTextContent();
                return text != null ? text.trim() : null;
            }
        }
        return null;
    }

    /**
     * Parses numeric values, removing common formatting characters.
     */
    private double parseNumeric(String value) {
        // Remove common formatting: commas, spaces, units
        String cleaned = value.replaceAll("[,\\s]", "")
                .replaceAll("sq\\s*km", "")
                .replaceAll("km²", "")
                .trim();
        return Double.parseDouble(cleaned);
    }
}
