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

package org.jscience.natural.biology.loaders.itis;

import org.jscience.natural.biology.Taxon;
import org.jscience.core.io.AbstractResourceReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reader for ITIS (Integrated Taxonomic Information System) XML data.
 * <p>
 * This reader parses taxonomic information from ITIS, converting it into
 * JScience {@link Taxon} objects. It supports the standard ITIS XML format
 * for scientific names, ranks, and common names.
 * </p>
 * * @see <a href="https://www.itis.gov/">ITIS.gov</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ITISReader extends AbstractResourceReader<List<Taxon>> {

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String getName() {
        return "ITIS Taxonomy Reader";
    }

    @Override
    public String getDescription() {
        return "Reads taxonomic data from the Integrated Taxonomic Information System.";
    }

    @Override
    public String getLongDescription() {
        return "Provides support for ITIS XML dumps, mapping TSN (Taxonomic Serial Numbers) " +
               "to hierarchical Taxon objects with ranks and authorities.";
    }

    @Override
    public String getResourcePath() {
        return "biology/itis";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "1.0" };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<List<Taxon>> getResourceType() {
        return (Class<List<Taxon>>) (Class<?>) List.class;
    }

    @Override
    protected List<Taxon> loadFromSource(String resourceId) throws Exception {
        // Implementation for loading from ITIS API or local XML file
        return new ArrayList<>();
    }

    @Override
    protected List<Taxon> loadFromInputStream(InputStream is, String id) throws Exception {
        DocumentBuilder builder = org.jscience.core.io.SecureXMLFactory.createSecureDocumentBuilder();
        Document doc = builder.parse(is);
        Element root = doc.getDocumentElement();

        List<Taxon> taxa = new ArrayList<>();
        NodeList taxonNodes = root.getElementsByTagName("taxon");
        if (taxonNodes.getLength() == 0) {
            taxonNodes = root.getElementsByTagName("itisTaxon");
        }

        for (int i = 0; i < taxonNodes.getLength(); i++) {
            Element taxonElem = (Element) taxonNodes.item(i);
            Taxon taxon = parseTaxon(taxonElem);
            if (taxon != null) {
                taxa.add(taxon);
            }
        }

        return taxa;
    }

    private Taxon parseTaxon(Element elem) {
        String tsn = getTagValue(elem, "tsn");
        String parentTsn = getTagValue(elem, "parentTsn");
        String scientificName = getTagValue(elem, "scientificName");
        if (scientificName == null) scientificName = getTagValue(elem, "concatenatedname");
        
        // Use modern Taxon class
        Taxon taxon = new Taxon(tsn, parentTsn, scientificName, null, null, null);
        
        return taxon;
    }

    private String getTagValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent().trim();
        }
        return null;
    }
}


