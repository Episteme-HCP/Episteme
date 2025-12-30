/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.politics.loaders;

import org.jscience.politics.Country;
import org.jscience.io.InputLoader;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader for the CIA World Factbook (XML version).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FactbookLoader implements InputLoader<List<Country>> {

    public FactbookLoader() {
    }

    @Override
    public List<Country> load(String resourceId) throws Exception {
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null) {
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
        return (Class) List.class;
    }

    public List<Country> load(InputStream input) throws Exception {
        // Implementation similar to Mondial, adapted for Factbook schema
        // This is a stub implementation as Factbook XML structure varies.
        List<Country> countries = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();

        // Assuming a generic structure for now
        NodeList countryNodes = doc.getElementsByTagName("country");
        for (int i = 0; i < countryNodes.getLength(); i++) {
            Element elem = (Element) countryNodes.item(i);
            String name = elem.getAttribute("name");
            if (name != null) {
                countries.add(new Country(name, ""));
            }
        }
        return countries;
    }
}
