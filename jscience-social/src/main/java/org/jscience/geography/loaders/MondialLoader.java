/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.geography.loaders;

import org.jscience.politics.Country;
import org.w3c.dom.*;
import javax.xml.parsers.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader for the Mondial database (XML).
 * Populates Country and Region objects.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class MondialLoader {

    public List<Country> loadCountries(InputStream input) throws Exception {
        List<Country> countries = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(input);
        doc.getDocumentElement().normalize();

        NodeList countryNodes = doc.getElementsByTagName("country");

        for (int i = 0; i < countryNodes.getLength(); i++) {
            Node node = countryNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String name = getTagValue("name", elem);
                String code = elem.getAttribute("car_code");
                if (code == null || code.isEmpty())
                    code = elem.getAttribute("id");

                Country country = new Country(name, code);

                String areaStr = elem.getAttribute("area");
                if (areaStr != null && !areaStr.isEmpty()) {
                    try {
                        country.setAreaSqKm(Double.parseDouble(areaStr));
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }

                // Population is often a separate child node 'population', taking the last one
                // (most recent)
                NodeList popNodes = elem.getElementsByTagName("population");
                if (popNodes.getLength() > 0) {
                    Element popElem = (Element) popNodes.item(popNodes.getLength() - 1); // Last one
                    String popStr = popElem.getTextContent();
                    try {
                        country.setPopulation(Long.parseLong(popStr.trim()));
                    } catch (NumberFormatException e) {
                        // ignore
                    }
                }

                countries.add(country);
            }
        }
        return countries;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nl = element.getElementsByTagName(tag);
        if (nl != null && nl.getLength() > 0) {
            NodeList childNodes = nl.item(0).getChildNodes();
            if (childNodes != null && childNodes.getLength() > 0) {
                Node node = childNodes.item(0);
                return node.getNodeValue();
            }
        }
        return "";
    }
}
