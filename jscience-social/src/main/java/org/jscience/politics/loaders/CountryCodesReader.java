/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.politics.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;
import org.jscience.politics.CountryCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reader for loading {@link CountryCode}s from JSON.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CountryCodesReader extends AbstractResourceReader<CountryCode> {

    private static final Logger LOG = LoggerFactory.getLogger(CountryCodesReader.class);
    private static final String RESOURCE_PATH = "/org/jscience/politics/countries.json";
    
    private static CountryCodesReader instance;
    private final ObjectMapper mapper = new ObjectMapper();

    public static synchronized CountryCodesReader getInstance() {
        if (instance == null) {
            instance = new CountryCodesReader();
        }
        return instance;
    }
    
    @Override
    public String getName() {
        return "Country Codes Reader";
    }

    @Override
    public String getCategory() {
        return "Politics";
    }

    @Override
    public String getDescription() {
        return "Reads Country Codes from JSON resource.";
    }
    
    @Override
    public String getLongDescription() {
        return "Reads ISO 3166-1 alpha-2 country codes from a JSON file resource located at " + RESOURCE_PATH;
    }
    
    @Override
    public String[] getSupportedVersions() {
        return new String[] { "1.0" };
    }

    @Override
    public String getResourcePath() {
        return RESOURCE_PATH;
    }

    @Override
    public Class<CountryCode> getResourceType() {
        return CountryCode.class;
    }

    @Override
    protected CountryCode loadFromSource(String id) throws Exception {
        return loadAllFromSource().stream()
                .filter(c -> c.name().equals(id)) // Fixed: getName() -> name()
                .findFirst()
                .orElse(null);
    }

    @Override
    protected List<CountryCode> loadAllFromSource() throws Exception {
        List<CountryCode> countries = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(RESOURCE_PATH)) {
            if (is == null) {
                LOG.error("Resource not found: " + RESOURCE_PATH);
                return countries;
            }

            JsonNode root = mapper.readTree(is);
            JsonNode countriesNode = root.get("countries");
            
            if (countriesNode != null && countriesNode.isArray()) {
                for (JsonNode node : countriesNode) {
                    countries.add(parseCountryCode(node));
                }
            }
        }
        return countries;
    }
    
    @Override
    protected MiniCatalog<CountryCode> getMiniCatalog() {
        return new MiniCatalog<>() {
            private List<CountryCode> cachedList;

            @Override
            public List<CountryCode> getAll() {
                if (cachedList == null) {
                    try {
                        cachedList = loadAllFromSource();
                    } catch (Exception e) {
                        LOG.error("Failed to load mini catalog", e);
                        cachedList = new ArrayList<>();
                    }
                }
                return cachedList;
            }

            @Override
            public Optional<CountryCode> findByName(String name) {
                return getAll().stream()
                        .filter(c -> c.name().equals(name)) // Fixed: getName() -> name()
                        .findFirst();
            }

            @Override
            public int size() {
                return getAll().size();
            }
        };
    }

    private CountryCode parseCountryCode(JsonNode node) {
        String code = node.get("code").asText();
        String englishName = node.has("englishName") ? node.get("englishName").asText() : "";
        boolean builtIn = node.has("builtIn") && node.get("builtIn").asBoolean();
        return new CountryCode(code, englishName, builtIn);
    }
}
