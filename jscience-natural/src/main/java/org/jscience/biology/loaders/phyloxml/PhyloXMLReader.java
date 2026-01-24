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

package org.jscience.biology.loaders.phyloxml;

import org.jscience.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * PhyloXML Reader for phylogenetic tree data.
 * <p>
 * PhyloXML is an XML format for phylogenetic trees and associated data.
 * This reader parses phylogeny elements including clades, taxonomic information,
 * and evolutionary events.
 * </p>
 * <p>
 * <b>Supported Elements:</b>
 * <ul>
 *   <li>Phylogeny metadata (name, description, rooted status)</li>
 *   <li>Clade hierarchy with branch lengths</li>
 *   <li>Taxonomy (scientific name, common name, rank)</li>
 *   <li>Sequence data associations</li>
 *   <li>Evolutionary events (speciation, duplication)</li>
 *   <li>Confidence values (bootstrap, posterior probability)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @see <a href="http://www.phyloxml.org/">PhyloXML.org</a>
 */
public class PhyloXMLReader extends AbstractResourceReader<PhyloXMLDocument> {

    private static final Logger LOGGER = Logger.getLogger(PhyloXMLReader.class.getName());
    
    private static final String NS_PHYLOXML = "http://www.phyloxml.org";
    
    public PhyloXMLReader() {
    }

    // ===== ResourceReader interface =====
    
    @Override
    public String getResourcePath() {
        return null; // File-based, path provided at load time
    }

    @Override
    public Class<PhyloXMLDocument> getResourceType() {
        return PhyloXMLDocument.class;
    }

    @Override
    public String getName() {
        return "PhyloXML Reader";
    }

    @Override
    public String getDescription() {
        return "Reads phylogenetic trees from PhyloXML format";
    }

    @Override
    public String getLongDescription() {
        return "PhyloXML is an XML format for phylogenetic trees and associated data " +
               "including taxonomy, sequences, and evolutionary events.";
    }

    @Override
    public String getCategory() {
        return "Biology";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"1.10", "1.00"};
    }

    @Override
    protected PhyloXMLDocument loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) {
            return read(file);
        }
        // Try as resource path
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) {
                return read(is);
            }
        }
        throw new PhyloXMLException("Resource not found: " + resourceId);
    }

    @Override
    protected PhyloXMLDocument loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads phylogenetic data from an input stream.
     */
    public PhyloXMLDocument read(InputStream input) throws PhyloXMLException {
        try {
            DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new PhyloXMLException("Failed to parse PhyloXML", e);
        }
    }

    /**
     * Reads phylogenetic data from a file.
     */
    public PhyloXMLDocument read(File file) throws PhyloXMLException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new PhyloXMLException("Failed to read file: " + file, e);
        }
    }

    private PhyloXMLDocument parseDocument(Document doc) {
        PhyloXMLDocument result = new PhyloXMLDocument();
        
        NodeList phylogenies = doc.getElementsByTagName("phylogeny");
        for (int i = 0; i < phylogenies.getLength(); i++) {
            Element phyloElem = (Element) phylogenies.item(i);
            result.addPhylogeny(parsePhylogeny(phyloElem));
        }
        
        return result;
    }

    private Phylogeny parsePhylogeny(Element elem) {
        Phylogeny phylo = new Phylogeny();
        
        // Attributes
        phylo.setRooted("true".equals(elem.getAttribute("rooted")));
        phylo.setBranchLengthUnit(elem.getAttribute("branch_length_unit"));
        
        // Name
        String name = getChildText(elem, "name");
        if (name != null) phylo.setName(name);
        
        // Description
        String desc = getChildText(elem, "description");
        if (desc != null) phylo.setDescription(desc);
        
        // Root clade
        Element cladeElem = getFirstChildElement(elem, "clade");
        if (cladeElem != null) {
            phylo.setRootClade(parseClade(cladeElem));
        }
        
        return phylo;
    }

    private Clade parseClade(Element elem) {
        Clade clade = new Clade();
        
        // Branch length
        String branchLength = getChildText(elem, "branch_length");
        if (branchLength != null) {
            try {
                clade.setBranchLength(Double.parseDouble(branchLength));
            } catch (NumberFormatException e) {
                LOGGER.fine("Invalid branch length: " + branchLength);
            }
        }
        
        // Name
        String name = getChildText(elem, "name");
        if (name != null) clade.setName(name);
        
        // Confidence
        Element confElem = getFirstChildElement(elem, "confidence");
        if (confElem != null) {
            String type = confElem.getAttribute("type");
            String valueStr = confElem.getTextContent();
            try {
                clade.addConfidence(type, Double.parseDouble(valueStr.trim()));
            } catch (NumberFormatException e) {
                LOGGER.fine("Invalid confidence value: " + valueStr);
            }
        }
        
        // Taxonomy
        Element taxElem = getFirstChildElement(elem, "taxonomy");
        if (taxElem != null) {
            clade.setTaxonomy(parseTaxonomy(taxElem));
        }
        
        // Sequence
        Element seqElem = getFirstChildElement(elem, "sequence");
        if (seqElem != null) {
            clade.setSequence(parseSequence(seqElem));
        }
        
        // Events
        Element eventsElem = getFirstChildElement(elem, "events");
        if (eventsElem != null) {
            clade.setEvents(parseEvents(eventsElem));
        }
        
        // Child clades (recursive)
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element && "clade".equals(child.getLocalName())) {
                clade.addChild(parseClade((Element) child));
            }
        }
        
        return clade;
    }

    private Taxonomy parseTaxonomy(Element elem) {
        Taxonomy tax = new Taxonomy();
        tax.setScientificName(getChildText(elem, "scientific_name"));
        tax.setCommonName(getChildText(elem, "common_name"));
        tax.setRank(getChildText(elem, "rank"));
        
        String code = getChildText(elem, "code");
        if (code != null) tax.setCode(code);
        
        Element idElem = getFirstChildElement(elem, "id");
        if (idElem != null) {
            tax.setIdProvider(idElem.getAttribute("provider"));
            tax.setId(idElem.getTextContent().trim());
        }
        
        return tax;
    }

    private SequenceInfo parseSequence(Element elem) {
        SequenceInfo seq = new SequenceInfo();
        seq.setName(getChildText(elem, "name"));
        seq.setSymbol(getChildText(elem, "symbol"));
        seq.setAccession(getChildText(elem, "accession"));
        seq.setLocation(getChildText(elem, "location"));
        seq.setMolSeq(getChildText(elem, "mol_seq"));
        return seq;
    }

    private Events parseEvents(Element elem) {
        Events events = new Events();
        
        String speciations = getChildText(elem, "speciations");
        if (speciations != null) {
            try {
                events.setSpeciations(Integer.parseInt(speciations.trim()));
            } catch (NumberFormatException e) { }
        }
        
        String duplications = getChildText(elem, "duplications");
        if (duplications != null) {
            try {
                events.setDuplications(Integer.parseInt(duplications.trim()));
            } catch (NumberFormatException e) { }
        }
        
        String type = getChildText(elem, "type");
        if (type != null) events.setType(type);
        
        return events;
    }

    private String getChildText(Element parent, String childName) {
        Element child = getFirstChildElement(parent, childName);
        return child != null ? child.getTextContent().trim() : null;
    }

    private Element getFirstChildElement(Element parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                Element elem = (Element) child;
                if (name.equals(elem.getLocalName()) || name.equals(elem.getTagName())) {
                    return elem;
                }
            }
        }
        return null;
    }
}
