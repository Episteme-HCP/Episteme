/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.biology.loaders.phyloxml;

import org.episteme.core.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.*;
/**
 * PhyloXML Reader for phylogenetic tree data.
 */
public class PhyloXMLReader extends AbstractResourceReader<PhyloXMLDocument> {

    public PhyloXMLReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<PhyloXMLDocument> getResourceType() { return PhyloXMLDocument.class; }
    @Override public String getName() { return "PhyloXML Reader"; }
    @Override public String getDescription() { return "Reads phylogenetic trees from PhyloXML format"; }
    @Override public String getLongDescription() { 
        return "PhyloXML is the standard XML format for phylogenetic trees and associated data."; 
    }
    @Override public String getCategory() { return "Biology"; }
    @Override public String[] getSupportedVersions() { return new String[] {"1.10", "1.00"}; }

    @Override
    protected PhyloXMLDocument loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new PhyloXMLException("Resource not found: " + resourceId);
    }

    public PhyloXMLDocument read(InputStream input) throws PhyloXMLException {
        try {
            DocumentBuilder builder = org.episteme.core.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new PhyloXMLException("Failed to parse PhyloXML", e);
        }
    }

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
            result.addPhylogeny(parsePhylogeny((Element) phylogenies.item(i)));
        }
        return result;
    }

    private PhyloXMLPhylogeny parsePhylogeny(Element elem) {
        PhyloXMLPhylogeny phylo = new PhyloXMLPhylogeny();
        phylo.setRooted("true".equals(elem.getAttribute("rooted")));
        phylo.setBranchLengthUnit(elem.getAttribute("branch_length_unit"));
        
        phylo.setName(getChildText(elem, "name"));
        phylo.setDescription(getChildText(elem, "description"));
        
        Element cladeElem = getFirstChildElement(elem, "clade");
        if (cladeElem != null) {
            phylo.setRootClade(parseClade(cladeElem));
        }
        return phylo;
    }

    private PhyloXMLClade parseClade(Element elem) {
        PhyloXMLClade clade = new PhyloXMLClade();
        
        String branchLength = getChildText(elem, "branch_length");
        if (branchLength != null) {
            try { clade.setBranchLength(Double.parseDouble(branchLength)); } catch (Exception e) {}
        }
        
        clade.setName(getChildText(elem, "name"));
        
        Element confElem = getFirstChildElement(elem, "confidence");
        if (confElem != null) {
            try { clade.addConfidence(confElem.getAttribute("type"), Double.parseDouble(confElem.getTextContent().trim())); } catch (Exception e) {}
        }
        
        Element taxElem = getFirstChildElement(elem, "taxonomy");
        if (taxElem != null) clade.setTaxonomy(parseTaxonomy(taxElem));
        
        Element seqElem = getFirstChildElement(elem, "sequence");
        if (seqElem != null) clade.setSequence(parseSequence(seqElem));
        
        Element eventsElem = getFirstChildElement(elem, "events");
        if (eventsElem != null) clade.setEvents(parseEvents(eventsElem));
        
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element && ("clade".equals(child.getLocalName()) || "clade".equals(((Element)child).getTagName()))) {
                clade.addChild(parseClade((Element) child));
            }
        }
        return clade;
    }

    private PhyloXMLTaxonomy parseTaxonomy(Element elem) {
        PhyloXMLTaxonomy tax = new PhyloXMLTaxonomy();
        tax.setScientificName(getChildText(elem, "scientific_name"));
        tax.setCommonName(getChildText(elem, "common_name"));
        tax.setRank(getChildText(elem, "rank"));
        tax.setCode(getChildText(elem, "code"));
        
        Element idElem = getFirstChildElement(elem, "id");
        if (idElem != null) {
            tax.setIdProvider(idElem.getAttribute("provider"));
            tax.setId(idElem.getTextContent().trim());
        }
        return tax;
    }

    private PhyloXMLSequenceInfo parseSequence(Element elem) {
        PhyloXMLSequenceInfo seq = new PhyloXMLSequenceInfo();
        seq.setName(getChildText(elem, "name"));
        seq.setSymbol(getChildText(elem, "symbol"));
        seq.setAccession(getChildText(elem, "accession"));
        seq.setLocation(getChildText(elem, "location"));
        seq.setMolSeq(getChildText(elem, "mol_seq"));
        return seq;
    }

    private PhyloXMLEvents parseEvents(Element elem) {
        PhyloXMLEvents events = new PhyloXMLEvents();
        String spec = getChildText(elem, "speciations");
        if (spec != null) try { events.setSpeciations(Integer.parseInt(spec)); } catch (Exception e) {}
        String dupl = getChildText(elem, "duplications");
        if (dupl != null) try { events.setDuplications(Integer.parseInt(dupl)); } catch (Exception e) {}
        events.setType(getChildText(elem, "type"));
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
                if (name.equals(elem.getLocalName()) || name.equals(elem.getTagName())) return elem;
            }
        }
        return null;
    }
}

