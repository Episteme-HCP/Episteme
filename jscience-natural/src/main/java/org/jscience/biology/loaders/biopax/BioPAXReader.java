/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import org.jscience.io.AbstractResourceReader;
import org.jscience.io.SecureXMLFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.*;

/**
 * BioPAX Reader for biological pathways and networks.
 * <p>
 * BioPAX (Biological Pathway Exchange) is an RDF/OWL-based format for
 * exchanging pathway, metabolic, and signaling data.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioPAXReader extends AbstractResourceReader<BioPAXModel> {

    private static final String BP_NS = "http://www.biopax.org/release/biopax-level3.owl#";
    private static final String RDF_NS = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

    public BioPAXReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<BioPAXModel> getResourceType() { return BioPAXModel.class; }
    @Override public String getName() { return "BioPAX Reader"; }
    @Override public String getDescription() { return "Reads biological pathways from BioPAX format"; }
    @Override public String getLongDescription() { return "BioPAX is the standard format for biological pathway exchange including metabolic and signaling networks."; }
    @Override public String getCategory() { return "Biology"; }
    @Override
    public String[] getSupportedVersions() {
        return new String[] { "Level 3" };
    }

    @Override
    protected BioPAXModel loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new BioPAXException("Resource not found: " + resourceId);
    }

    @Override
    protected BioPAXModel loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    public BioPAXModel read(InputStream input) throws BioPAXException {
        try {
            DocumentBuilder builder = SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new BioPAXException("Failed to parse BioPAX", e);
        }
    }

    public BioPAXModel read(File file) throws BioPAXException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new BioPAXException("Failed to read file: " + file, e);
        }
    }

    private BioPAXModel parseDocument(Document doc) {
        BioPAXModel model = new BioPAXModel();
        
        // 1. Parse Physical Entities (foundations)
        NodeList entities = doc.getElementsByTagNameNS(BP_NS, "Protein");
        for (int i = 0; i < entities.getLength(); i++) {
            model.addEntity(parsePhysicalEntity((Element) entities.item(i), "Protein"));
        }
        
        entities = doc.getElementsByTagNameNS(BP_NS, "SmallMolecule");
        for (int i = 0; i < entities.getLength(); i++) {
            model.addEntity(parsePhysicalEntity((Element) entities.item(i), "SmallMolecule"));
        }
        
        entities = doc.getElementsByTagNameNS(BP_NS, "Complex");
        for (int i = 0; i < entities.getLength(); i++) {
            model.addEntity(parsePhysicalEntity((Element) entities.item(i), "Complex"));
        }
        
        // 2. Parse Interactions
        NodeList reactions = doc.getElementsByTagNameNS(BP_NS, "BiochemicalReaction");
        for (int i = 0; i < reactions.getLength(); i++) {
            model.addInteraction(parseBiochemicalReaction((Element) reactions.item(i)));
        }
        
        NodeList catalyses = doc.getElementsByTagNameNS(BP_NS, "Catalysis");
        for (int i = 0; i < catalyses.getLength(); i++) {
            model.addInteraction(parseCatalysis((Element) catalyses.item(i), model));
        }
        
        // 3. Parse Pathways
        NodeList pathways = doc.getElementsByTagNameNS(BP_NS, "Pathway");
        for (int i = 0; i < pathways.getLength(); i++) {
            model.addPathway(parsePathway((Element) pathways.item(i), model));
        }
        
        return model;
    }

    private BioPAXPathway parsePathway(Element elem, BioPAXModel model) {
        BioPAXPathway pathway = new BioPAXPathway();
        pathway.setRdfId(getRdfId(elem));
        pathway.setDisplayName(getPropertyText(elem, "displayName"));
        pathway.setName(getPropertyText(elem, "name"));
        pathway.setComment(getPropertyText(elem, "comment"));
        pathway.setOrganism(getPropertyText(elem, "organism"));
        
        // Link components
        NodeList components = elem.getElementsByTagNameNS(BP_NS, "pathwayComponent");
        for (int i = 0; i < components.getLength(); i++) {
            String ref = getResourceRef((Element) components.item(i));
            if (ref != null) {
                // Find and add interaction from model
                for (BioPAXInteraction inter : model.getInteractions()) {
                    if (ref.equals(inter.getRdfId())) {
                        pathway.addComponent(inter);
                        break;
                    }
                }
            }
        }
        
        return pathway;
    }

    private BioPAXPhysicalEntity parsePhysicalEntity(Element elem, String type) {
        BioPAXPhysicalEntity entity = new BioPAXPhysicalEntity();
        entity.setType(type);
        entity.setRdfId(getRdfId(elem));
        entity.setDisplayName(getPropertyText(elem, "displayName"));
        
        // Special fields for small molecules
        entity.setChemicalFormula(getPropertyText(elem, "chemicalFormula"));
        
        // Xrefs
        NodeList xrefElems = elem.getElementsByTagNameNS(BP_NS, "xref");
        for (int i = 0; i < xrefElems.getLength(); i++) {
            // This usually points to UnificationXref or RelationshipXref
            // For simplicity, we assume simple properties if nested or reference
            // A real reader would follow the reference.
        }
        
        return entity;
    }

    private BioPAXBiochemicalReaction parseBiochemicalReaction(Element elem) {
        BioPAXBiochemicalReaction reaction = new BioPAXBiochemicalReaction();
        reaction.setType("BiochemicalReaction");
        reaction.setRdfId(getRdfId(elem));
        reaction.setDisplayName(getPropertyText(elem, "displayName"));
        reaction.setConversionDirection(getPropertyText(elem, "conversionDirection"));
        
        // Left
        NodeList nodes = elem.getElementsByTagNameNS(BP_NS, "left");
        for (int i = 0; i < nodes.getLength(); i++) {
            // In a real BioPAX reader we'd resolve the participant correctly
            // Here we create a placeholder PhysicalEntity for the bridge to match by ID later if needed,
            // or we expect the model to already have them.
        }
        
        return reaction;
    }

    private BioPAXCatalysis parseCatalysis(Element elem, BioPAXModel model) {
        BioPAXCatalysis catalysis = new BioPAXCatalysis();
        catalysis.setType("Catalysis");
        catalysis.setRdfId(getRdfId(elem));
        
        String controllerRef = getResourceRef(getFirstChildNS(elem, BP_NS, "controller"));
        if (controllerRef != null) {
            catalysis.setController(model.getEntity(controllerRef));
        }
        
        return catalysis;
    }

    private String getRdfId(Element elem) {
        String id = elem.getAttributeNS(RDF_NS, "ID");
        if (id == null || id.isEmpty()) {
            id = elem.getAttributeNS(RDF_NS, "about");
            if (id != null) id = id.replace("#", "");
        }
        return id;
    }

    private String getPropertyText(Element parent, String propName) {
        Element child = getFirstChildNS(parent, BP_NS, propName);
        return child != null ? child.getTextContent().trim() : null;
    }

    private String getResourceRef(Element elem) {
        if (elem == null) return null;
        String ref = elem.getAttributeNS(RDF_NS, "resource");
        return (ref != null && !ref.isEmpty()) ? ref.replace("#", "") : null;
    }

    private Element getFirstChildNS(Element parent, String ns, String name) {
        NodeList list = parent.getElementsByTagNameNS(ns, name);
        return list.getLength() > 0 ? (Element) list.item(0) : null;
    }
}
