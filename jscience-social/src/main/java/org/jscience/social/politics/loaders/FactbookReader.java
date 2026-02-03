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

package org.jscience.social.politics.loaders;

import org.jscience.social.politics.Country;
import org.jscience.social.politics.GovernmentType;
import org.jscience.social.economics.money.Money;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jscience.core.io.AbstractResourceReader;
import org.jscience.core.io.MiniCatalog;
import org.jscience.core.util.persistence.PersistenceManager;
import java.util.Optional;

/**
 * Production loader for the CIA World Factbook XML data.
 * 
 * Parses country information including:
 * Reader for the CIA World Factbook XML data.
 * <p>
 * This loader parses country-level demographic, geographic, and political data
 * and populates {@link Country} objects.
 * </p>
 * 
 * @see <a href="https://www.cia.gov/the-world-factbook/">CIA World Factbook</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class FactbookReader extends AbstractResourceReader<List<Country>> {

    private static final Logger LOG = LoggerFactory.getLogger(FactbookReader.class);

    public FactbookReader() {
    }

    @Override
    public String getCategory() {
        return org.jscience.core.ui.i18n.I18N.getInstance().get("category.politics", "Politics");
    }

    @Override
    public String getDescription() {
        return org.jscience.core.ui.i18n.I18N.getInstance().get("reader.factbook.desc", "CIA World Factbook Reader (XML).");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.core.ui.i18n.I18N.getInstance().get("reader.factbook.longdesc", "Parses XML data from the CIA World Factbook, providing detailed country information including demographics, geography, and government.");
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

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"2024", "2023"};
    }

    @Override
    protected List<Country> loadFromSource(String id) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(id)) {
            if (is == null) {
                throw new Exception("Resource not found: " + id);
            }
            return load(is);
        }
    }

    @Override
    public MiniCatalog<List<Country>> getMiniCatalog() {
        return new MiniCatalog<>() {
            @Override
            public List<List<Country>> getAll() {
                return List.of(getSampleData());
            }

            @Override
            public Optional<List<Country>> findByName(String name) {
                return Optional.of(getSampleData());
            }

            @Override
            public int size() {
                return 1;
            }
        };
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
            DocumentBuilder builder = org.jscience.core.io.SecureXMLFactory.createSecureDocumentBuilder();
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
                        // Persist to the global knowledge graph
                        PersistenceManager.getInstance().save(country);
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

        // Government type
        String government = getTextValue(elem, "government");
        if (government != null && !government.isEmpty()) {
            country.setGovernmentType(GovernmentType.of(government));
        }

        // Region/Continent
        String region = getTextValue(elem, "region");
        if (region != null && !region.isEmpty()) {
            country.setContinent(region);
        }

        // Independence
        String independence = getTextValue(elem, "independence");
        if (independence != null && !independence.isEmpty()) {
            try {
                // Simplistic parsing for year
                String yearStr = independence.replaceAll("[^0-9]", "");
                if (yearStr.length() >= 4) {
                    country.setIndependenceYear(Integer.parseInt(yearStr.substring(0, 4)));
                }
            } catch (NumberFormatException e) {
                LOG.debug("Could not parse independence year: {}", independence);
            }
        }

        // Economy & Demographics
        String lifeExp = getTextValue(elem, "lifeExpectancy");
        if (lifeExp != null && !lifeExp.isEmpty()) {
            try {
                country.setLifeExpectancy(org.jscience.core.measure.Quantities.create(Double.parseDouble(lifeExp), org.jscience.core.measure.Units.YEAR));
            } catch (NumberFormatException e) { }
        }

        String stability = getTextValue(elem, "stability");
        if (stability != null && !stability.isEmpty()) {
            try {
                country.setStability(org.jscience.core.measure.Quantities.create(Double.parseDouble(stability), org.jscience.core.measure.Units.ONE));
            } catch (NumberFormatException e) { }
        }

        String milSpending = getTextValue(elem, "militarySpending");
        if (milSpending != null && !milSpending.isEmpty()) {
            try {
                // Assume USD for simplicity in XML tag
                country.setMilitarySpending(org.jscience.social.economics.money.Money.usd(Double.parseDouble(milSpending)));
            } catch (NumberFormatException e) { }
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
                .replaceAll("kmÃ‚Â²", "")
                .trim();
        return Double.parseDouble(cleaned);
    }

    private List<Country> getSampleData() {
        List<Country> samples = new ArrayList<>();

        // France
        Country france = new Country("France", "FR", "FRA", 250, "Paris", "Europe", 67_750_000L, 643_801.0);
        france.setGovernmentType(GovernmentType.of("Semi-presidential republic"));
        france.setIndependenceYear(843);
        france.setLifeExpectancy(Quantities.create(82.5, Units.YEAR));
        france.setPopulationGrowthRate(Quantities.create(0.21, Units.PERCENT));
        france.setCurrencyCode("EUR");
        france.setStability(Quantities.create(0.92, Units.ONE));
        france.setMilitarySpending(Money.eur(50.0 * 1_000_000_000));
        france.getMajorIndustries().addAll(List.of("aerospace", "automotive", "pharmaceuticals", "tourism"));
        france.getNaturalResources().addAll(List.of("coal", "iron ore", "bauxite", "zinc"));
        france.getBorderCountries().addAll(List.of("BEL", "LUX", "DEU", "CHE", "ITA", "ESP", "AND", "MCO"));
        samples.add(france);

        // United States
        Country usa = new Country("United States", "US", "USA", 840, "Washington, D.C.", "North America", 331_900_000L,
                9_833_517.0);
        usa.setGovernmentType(GovernmentType.REPUBLIC);
        usa.setIndependenceYear(1776);
        usa.setLifeExpectancy(Quantities.create(78.5, Units.YEAR));
        usa.setPopulationGrowthRate(Quantities.create(0.4, Units.PERCENT));
        usa.setCurrencyCode("USD");
        usa.setStability(Quantities.create(0.95, Units.ONE));
        usa.setMilitarySpending(Money.usd(850.0 * 1_000_000_000));
        usa.getMajorIndustries().addAll(List.of("technology", "aerospace", "automotive", "healthcare"));
        usa.getNaturalResources().addAll(List.of("coal", "copper", "lead", "uranium", "natural gas"));
        usa.getBorderCountries().addAll(List.of("CAN", "MEX"));
        samples.add(usa);

        // China
        Country china = new Country("China", "CN", "CHN", 156, "Beijing", "Asia", 1_411_750_000L, 9_596_960.0);
        china.setGovernmentType(GovernmentType.COMMUNIST_STATE);
        china.setIndependenceYear(1949);
        china.setLifeExpectancy(Quantities.create(77.3, Units.YEAR));
        china.setPopulationGrowthRate(Quantities.create(0.03, Units.PERCENT));
        china.setCurrencyCode("CNY");
        china.setStability(Quantities.create(0.90, Units.ONE));
        china.setMilitarySpending(Money.usd(230.0 * 1_000_000_000)); // USD equivalent
        china.getMajorIndustries().addAll(List.of("manufacturing", "mining", "electronics", "textiles"));
        china.getNaturalResources().addAll(List.of("coal", "iron ore", "rare earths", "tungsten"));
        china.getBorderCountries().addAll(List.of("AFG", "BTN", "IND", "KAZ", "PRK", "KGZ", "LAO", "MNG"));
        samples.add(china);

        // Brazil
        Country brazil = new Country("Brazil", "BR", "BRA", 76, "BrasÃ­lia", "South America", 214_000_000L, 8_515_767.0);
        brazil.setGovernmentType(GovernmentType.REPUBLIC);
        brazil.setIndependenceYear(1822);
        brazil.setLifeExpectancy(Quantities.create(75.9, Units.YEAR));
        brazil.setPopulationGrowthRate(Quantities.create(0.52, Units.PERCENT));
        brazil.setCurrencyCode("BRL");
        brazil.setStability(Quantities.create(0.70, Units.ONE));
        brazil.setMilitarySpending(Money.usd(20.0 * 1_000_000_000));
        brazil.getMajorIndustries().addAll(List.of("agriculture", "mining", "manufacturing", "services"));
        brazil.getNaturalResources().addAll(List.of("iron ore", "manganese", "bauxite", "gold", "timber"));
        brazil.getBorderCountries()
                .addAll(List.of("ARG", "BOL", "COL", "GUF", "GUY", "PRY", "PER", "SUR", "URY", "VEN"));
        samples.add(brazil);
        
        // Russia (Merged)
        Country russia = new Country("Russia", "RU", "RUS", 643, "Moscow", "Europe", 144_100_000L, 17_098_242.0);
        russia.setGovernmentType(GovernmentType.FEDERATION);
        russia.setStability(Quantities.create(0.40, Units.ONE));
        russia.setMilitarySpending(Money.usd(100.0 * 1_000_000_000));
        samples.add(russia);
        
        // India (Merged)
        Country india = new Country("India", "IN", "IND", 356, "New Delhi", "Asia", 1_380_000_000L, 3_287_263.0);
        india.setGovernmentType(GovernmentType.of("Federal parliamentary republic"));
        india.setStability(Quantities.create(0.85, Units.ONE));
        india.setMilitarySpending(Money.usd(75.0 * 1_000_000_000));
        samples.add(india);
        
        // Canada (Merged)
        Country canada = new Country("Canada", "CA", "CAN", 124, "Ottawa", "North America", 38_000_000L, 9_984_670.0);
        canada.setGovernmentType(GovernmentType.of("Federal parliamentary constitutional monarchy"));
        canada.setStability(Quantities.create(0.99, Units.ONE));
        canada.setMilitarySpending(Money.usd(25.0 * 1_000_000_000));
        samples.add(canada);

        return samples;
    }

    @Override public String getName() { return org.jscience.core.ui.i18n.I18N.getInstance().get("reader.factbook.name", "CIA Factbook Reader"); }
}


