/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.physics.loaders;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Bridge for parsing VOTable (Virtual Observatory Tabular) XML format.
 * <p>
 * VOTable is the standard XML format for tabular data in Astronomy, 
 * used extensively by VizieR, SIMBAD, and Aladin.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VOTableBridge {

    /**
     * Parses a raw VOTable XML string into a list of records.
     *
     * @param xml raw VOTable XML
     * @return list of rows, each row mapping column names to values
     */
    public static List<Map<String, String>> parse(String xml) {
        List<Map<String, String>> results = new ArrayList<>();
        try {
            DocumentBuilder builder = org.episteme.core.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

            // Extract table fields (metadata)
            NodeList fields = doc.getElementsByTagName("FIELD");
            List<String> columnNames = new ArrayList<>();
            for (int i = 0; i < fields.getLength(); i++) {
                Element field = (Element) fields.item(i);
                String name = field.getAttribute("name");
                if (name == null || name.isEmpty()) name = field.getAttribute("ID");
                columnNames.add(name);
            }

            // Extract table data
            NodeList rows = doc.getElementsByTagName("TR");
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                NodeList cells = row.getElementsByTagName("TD");
                Map<String, String> record = new LinkedHashMap<>();
                for (int j = 0; j < cells.getLength(); j++) {
                    if (j < columnNames.size()) {
                        String colName = columnNames.get(j);
                        String value = cells.item(j).getTextContent().trim();
                        record.put(colName, value);
                    }
                }
                if (!record.isEmpty()) {
                    results.add(record);
                }
            }
        } catch (Exception e) {
            System.err.println("VOTableBridge error: " + e.getMessage());
        }
        return results;
    }
}

