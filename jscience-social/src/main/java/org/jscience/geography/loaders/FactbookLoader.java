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

package org.jscience.geography.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.geography.Region;
import org.jscience.geography.Coordinate;

import java.io.InputStream;
import java.util.*;

/**
 * Loads country data from CIA World Factbook-style data.
 * <p>
 * Provides extended country information including:
 * <ul>
 * <li>Geography: area, coordinates, borders, coastline</li>
 * <li>Demographics: population, growth rate, life expectancy</li>
 * <li>Government: type, capital, independence date</li>
 * <li>Economy: GDP, major industries</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FactbookLoader implements org.jscience.io.InputLoader<Map<String, FactbookLoader.CountryProfile>> {

    private static final String RESOURCE_PATH = "/data/factbook_countries.json";
    private static FactbookLoader instance;
    private final ObjectMapper mapper;
    private Map<String, CountryProfile> cachedProfiles;

    // ... (CountryProfile inner class same as before)
    public static class CountryProfile {
        private Region region;
        private Coordinate capital;
        private double areaSqKm;
        private double coastlineKm;
        private String governmentType;
        private int independenceYear;
        private double populationGrowthRate;
        private double lifeExpectancy;
        private double birthRate;
        private double deathRate;
        private List<String> majorIndustries = new ArrayList<>();
        private List<String> naturalResources = new ArrayList<>();
        private List<String> borderCountries = new ArrayList<>();
        private String continent;
        private String currencyCode;

        public Region getRegion() {
            return region;
        }

        public void setRegion(Region region) {
            this.region = region;
        }

        public Coordinate getCapital() {
            return capital;
        }

        public void setCapital(Coordinate capital) {
            this.capital = capital;
        }

        public double getAreaSqKm() {
            return areaSqKm;
        }

        public void setAreaSqKm(double areaSqKm) {
            this.areaSqKm = areaSqKm;
        }

        public double getCoastlineKm() {
            return coastlineKm;
        }

        public void setCoastlineKm(double coastlineKm) {
            this.coastlineKm = coastlineKm;
        }

        public String getGovernmentType() {
            return governmentType;
        }

        public void setGovernmentType(String governmentType) {
            this.governmentType = governmentType;
        }

        public int getIndependenceYear() {
            return independenceYear;
        }

        public void setIndependenceYear(int independenceYear) {
            this.independenceYear = independenceYear;
        }

        public double getPopulationGrowthRate() {
            return populationGrowthRate;
        }

        public void setPopulationGrowthRate(double populationGrowthRate) {
            this.populationGrowthRate = populationGrowthRate;
        }

        public double getLifeExpectancy() {
            return lifeExpectancy;
        }

        public void setLifeExpectancy(double lifeExpectancy) {
            this.lifeExpectancy = lifeExpectancy;
        }

        public double getBirthRate() {
            return birthRate;
        }

        public void setBirthRate(double birthRate) {
            this.birthRate = birthRate;
        }

        public double getDeathRate() {
            return deathRate;
        }

        public void setDeathRate(double deathRate) {
            this.deathRate = deathRate;
        }

        public List<String> getMajorIndustries() {
            return majorIndustries;
        }

        public List<String> getNaturalResources() {
            return naturalResources;
        }

        public List<String> getBorderCountries() {
            return borderCountries;
        }

        public String getContinent() {
            return continent;
        }

        public void setContinent(String continent) {
            this.continent = continent;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }
    }

    private FactbookLoader() {
        this.mapper = new ObjectMapper();
    }

    public static synchronized FactbookLoader getInstance() {
        if (instance == null) {
            instance = new FactbookLoader();
        }
        return instance;
    }

    @Override
    public Map<String, CountryProfile> load(String resourceId) throws Exception {
        return loadAll();
    }

    @Override
    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<Map<String, FactbookLoader.CountryProfile>> getResourceType() {
        return (Class<Map<String, FactbookLoader.CountryProfile>>) (Class<?>) Map.class;
    }

    /**
     * Loads all country profiles.
     *
     * @return Map of country code to CountryProfile
     */
    public Map<String, CountryProfile> loadAll() {
        if (cachedProfiles != null) {
            return cachedProfiles;
        }

        cachedProfiles = new LinkedHashMap<>();

        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                return getSampleData();
            }

            JsonNode root = mapper.readTree(is);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    CountryProfile profile = parseCountry(node);
                    if (profile != null && profile.getRegion() != null) {
                        cachedProfiles.put(profile.getRegion().getCode(), profile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getSampleData();
        }

        return cachedProfiles;
    }

    /**
     * Gets a specific country profile by code.
     */
    public CountryProfile getByCode(String code) {
        return loadAll().get(code);
    }

    /**
     * Gets countries by continent.
     */
    public List<CountryProfile> getByContinent(String continent) {
        List<CountryProfile> result = new ArrayList<>();
        for (CountryProfile profile : loadAll().values()) {
            if (continent.equalsIgnoreCase(profile.getContinent())) {
                result.add(profile);
            }
        }
        return result;
    }

    private CountryProfile parseCountry(JsonNode node) {
        String name = node.path("name").asText(null);
        String code = node.path("code").asText(null);
        if (name == null || code == null)
            return null;

        CountryProfile profile = new CountryProfile();
        Region region = new Region(name, Region.Type.COUNTRY);
        region.setCode(code);
        region.setPopulation(node.path("population").asLong(0));
        profile.setRegion(region);

        // Geography
        profile.setAreaSqKm(node.path("area_sq_km").asDouble(0));
        profile.setCoastlineKm(node.path("coastline_km").asDouble(0));
        profile.setContinent(node.path("continent").asText("Unknown"));

        // Capital coordinates
        JsonNode capitalNode = node.path("capital");
        if (!capitalNode.isMissingNode()) {
            double lat = capitalNode.path("lat").asDouble(0);
            double lon = capitalNode.path("lon").asDouble(0);
            profile.setCapital(new Coordinate(lat, lon));
        }

        // Government
        profile.setGovernmentType(node.path("government_type").asText(null));
        profile.setIndependenceYear(node.path("independence_year").asInt(0));
        profile.setCurrencyCode(node.path("currency").asText(null));

        // Demographics
        profile.setPopulationGrowthRate(node.path("population_growth_rate").asDouble(0));
        profile.setLifeExpectancy(node.path("life_expectancy").asDouble(0));
        profile.setBirthRate(node.path("birth_rate").asDouble(0));
        profile.setDeathRate(node.path("death_rate").asDouble(0));

        // Arrays
        loadStringArray(node.path("industries"), profile.getMajorIndustries());
        loadStringArray(node.path("resources"), profile.getNaturalResources());
        loadStringArray(node.path("borders"), profile.getBorderCountries());

        return profile;
    }

    private void loadStringArray(JsonNode arrayNode, List<String> target) {
        if (arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                target.add(item.asText());
            }
        }
    }

    private Map<String, CountryProfile> getSampleData() {
        Map<String, CountryProfile> samples = new LinkedHashMap<>();

        // France
        CountryProfile france = new CountryProfile();
        Region frRegion = new Region("France", Region.Type.COUNTRY);
        frRegion.setCode("FRA");
        frRegion.setPopulation(67_750_000L);
        france.setRegion(frRegion);
        france.setCapital(new Coordinate(48.8566, 2.3522));
        france.setAreaSqKm(643_801);
        france.setCoastlineKm(4_853);
        france.setContinent("Europe");
        france.setGovernmentType("Semi-presidential republic");
        france.setIndependenceYear(843);
        france.setLifeExpectancy(82.5);
        france.setPopulationGrowthRate(0.21);
        france.setCurrencyCode("EUR");
        france.getMajorIndustries().addAll(List.of("aerospace", "automotive", "pharmaceuticals", "tourism"));
        france.getNaturalResources().addAll(List.of("coal", "iron ore", "bauxite", "zinc"));
        france.getBorderCountries().addAll(List.of("BEL", "LUX", "DEU", "CHE", "ITA", "ESP", "AND", "MCO"));
        samples.put("FRA", france);

        // United States
        CountryProfile usa = new CountryProfile();
        Region usRegion = new Region("United States", Region.Type.COUNTRY);
        usRegion.setCode("USA");
        usRegion.setPopulation(331_900_000L);
        usa.setRegion(usRegion);
        usa.setCapital(new Coordinate(38.8951, -77.0364));
        usa.setAreaSqKm(9_833_517);
        usa.setCoastlineKm(19_924);
        usa.setContinent("North America");
        usa.setGovernmentType("Federal presidential constitutional republic");
        usa.setIndependenceYear(1776);
        usa.setLifeExpectancy(78.5);
        usa.setPopulationGrowthRate(0.4);
        usa.setCurrencyCode("USD");
        usa.getMajorIndustries().addAll(List.of("technology", "aerospace", "automotive", "healthcare"));
        usa.getNaturalResources().addAll(List.of("coal", "copper", "lead", "uranium", "natural gas"));
        usa.getBorderCountries().addAll(List.of("CAN", "MEX"));
        samples.put("USA", usa);

        // China
        CountryProfile china = new CountryProfile();
        Region cnRegion = new Region("China", Region.Type.COUNTRY);
        cnRegion.setCode("CHN");
        cnRegion.setPopulation(1_411_750_000L);
        china.setRegion(cnRegion);
        china.setCapital(new Coordinate(39.9042, 116.4074));
        china.setAreaSqKm(9_596_960);
        china.setCoastlineKm(14_500);
        china.setContinent("Asia");
        china.setGovernmentType("Communist party-led state");
        china.setIndependenceYear(1949);
        china.setLifeExpectancy(77.3);
        china.setPopulationGrowthRate(0.03);
        china.setCurrencyCode("CNY");
        china.getMajorIndustries().addAll(List.of("manufacturing", "mining", "electronics", "textiles"));
        china.getNaturalResources().addAll(List.of("coal", "iron ore", "rare earths", "tungsten"));
        china.getBorderCountries().addAll(List.of("AFG", "BTN", "IND", "KAZ", "PRK", "KGZ", "LAO", "MNG"));
        samples.put("CHN", china);

        // Brazil
        CountryProfile brazil = new CountryProfile();
        Region brRegion = new Region("Brazil", Region.Type.COUNTRY);
        brRegion.setCode("BRA");
        brRegion.setPopulation(214_000_000L);
        brazil.setRegion(brRegion);
        brazil.setCapital(new Coordinate(-15.7801, -47.9292));
        brazil.setAreaSqKm(8_515_767);
        brazil.setCoastlineKm(7_491);
        brazil.setContinent("South America");
        brazil.setGovernmentType("Federal presidential constitutional republic");
        brazil.setIndependenceYear(1822);
        brazil.setLifeExpectancy(75.9);
        brazil.setPopulationGrowthRate(0.52);
        brazil.setCurrencyCode("BRL");
        brazil.getMajorIndustries().addAll(List.of("agriculture", "mining", "manufacturing", "services"));
        brazil.getNaturalResources().addAll(List.of("iron ore", "manganese", "bauxite", "gold", "timber"));
        brazil.getBorderCountries()
                .addAll(List.of("ARG", "BOL", "COL", "GUF", "GUY", "PRY", "PER", "SUR", "URY", "VEN"));
        samples.put("BRA", brazil);

        return samples;
    }
}


