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

package org.jscience.biology.loaders.pdbml;

import org.jscience.io.AbstractResourceReader;

import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.*;

/**
 * PDBML Reader for Protein Data Bank macromolecular structures.
 * <p>
 * PDBML is the XML representation of PDB files containing 3D structures
 * of proteins, nucleic acids, and complex assemblies.
 * </p>
 * <p>
 * <b>Supported Elements:</b>
 * <ul>
 *   <li>Entry metadata (ID, title, authors)</li>
 *   <li>Atomic coordinates (atom_site)</li>
 *   <li>Polymer chains and sequences</li>
 *   <li>Secondary structure (helix, sheet)</li>
 *   <li>Experimental details (resolution, method)</li>
 * </ul>
 * </p>
 * * @see <a href="https://www.rcsb.org/">RCSB PDB</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PDBMLReader extends AbstractResourceReader<PDBMLStructure> {


    public PDBMLReader() {
    }

    @Override public String getResourcePath() { return null; }
    @Override public Class<PDBMLStructure> getResourceType() { return PDBMLStructure.class; }
    @Override public String getName() { return "PDBML Reader"; }
    @Override public String getDescription() { return "Reads protein 3D structures from PDBML format"; }
    @Override public String getLongDescription() { return "PDBML is the XML format for Protein Data Bank structures including atomic coordinates and secondary structure."; }
    @Override public String getCategory() { return "Biology"; }
    @Override public String[] getSupportedVersions() { return new String[] {"4.x", "3.x"}; }

    @Override
    protected PDBMLStructure loadFromSource(String resourceId) throws Exception {
        File file = new File(resourceId);
        if (file.exists()) return read(file);
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is != null) return read(is);
        }
        throw new PDBMLException("Resource not found: " + resourceId);
    }

    @Override
    protected PDBMLStructure loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    /**
     * Reads a PDBML structure from an input stream.
     */
    public PDBMLStructure read(InputStream input) throws PDBMLException {
        try {
            DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
            Document doc = builder.parse(input);
            return parseDocument(doc);
        } catch (Exception e) {
            throw new PDBMLException("Failed to parse PDBML", e);
        }
    }

    /**
     * Reads a PDBML structure from a file.
     */
    public PDBMLStructure read(File file) throws PDBMLException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return read(fis);
        } catch (IOException e) {
            throw new PDBMLException("Failed to read file: " + file, e);
        }
    }

    private PDBMLStructure parseDocument(Document doc) {
        PDBMLStructure structure = new PDBMLStructure();
        
        // Parse entry information
        parseEntryInfo(doc, structure);
        
        // Parse atomic coordinates
        parseAtomSites(doc, structure);
        
        // Parse polymer entities
        parsePolymerEntities(doc, structure);
        
        // Parse secondary structure
        parseSecondaryStructure(doc, structure);
        
        // Parse experimental data
        parseExperimentalData(doc, structure);
        
        return structure;
    }

    private void parseEntryInfo(Document doc, PDBMLStructure structure) {
        // Get entry ID
        NodeList entryList = doc.getElementsByTagName("PDBx:entry");
        if (entryList.getLength() > 0) {
            Element entry = (Element) entryList.item(0);
            structure.setEntryId(entry.getAttribute("id"));
        }
        
        // Title
        NodeList titles = doc.getElementsByTagName("PDBx:title");
        if (titles.getLength() > 0) {
            structure.setTitle(titles.item(0).getTextContent().trim());
        }
        
        // Deposition date
        NodeList dates = doc.getElementsByTagName("PDBx:recvd_initial_deposition_date");
        if (dates.getLength() > 0) {
            structure.setDepositionDate(dates.item(0).getTextContent().trim());
        }
    }

    private void parseAtomSites(Document doc, PDBMLStructure structure) {
        NodeList atomSites = doc.getElementsByTagName("PDBx:atom_site");
        for (int i = 0; i < atomSites.getLength(); i++) {
            Element atomElem = (Element) atomSites.item(i);
            AtomSite atom = new AtomSite();
            
            atom.setId(parseInt(getChildText(atomElem, "PDBx:id"), i));
            atom.setAtomSymbol(getChildText(atomElem, "PDBx:type_symbol"));
            atom.setAtomName(getChildText(atomElem, "PDBx:label_atom_id"));
            atom.setResidueName(getChildText(atomElem, "PDBx:label_comp_id"));
            atom.setChainId(getChildText(atomElem, "PDBx:label_asym_id"));
            atom.setResidueSeq(parseInt(getChildText(atomElem, "PDBx:label_seq_id"), 0));
            
            atom.setX(parseDouble(getChildText(atomElem, "PDBx:Cartn_x"), 0));
            atom.setY(parseDouble(getChildText(atomElem, "PDBx:Cartn_y"), 0));
            atom.setZ(parseDouble(getChildText(atomElem, "PDBx:Cartn_z"), 0));
            
            atom.setOccupancy(parseDouble(getChildText(atomElem, "PDBx:occupancy"), 1.0));
            atom.setTempFactor(parseDouble(getChildText(atomElem, "PDBx:B_iso_or_equiv"), 0));
            
            structure.addAtom(atom);
        }
    }

    private void parsePolymerEntities(Document doc, PDBMLStructure structure) {
        NodeList entities = doc.getElementsByTagName("PDBx:entity_poly");
        for (int i = 0; i < entities.getLength(); i++) {
            Element entityElem = (Element) entities.item(i);
            PolymerEntity entity = new PolymerEntity();
            
            entity.setEntityId(entityElem.getAttribute("entity_id"));
            entity.setType(getChildText(entityElem, "PDBx:type"));
            entity.setSequence(getChildText(entityElem, "PDBx:pdbx_seq_one_letter_code"));
            entity.setSequenceCanonical(getChildText(entityElem, "PDBx:pdbx_seq_one_letter_code_can"));
            
            structure.addEntity(entity);
        }
    }

    private void parseSecondaryStructure(Document doc, PDBMLStructure structure) {
        // Helices
        NodeList helices = doc.getElementsByTagName("PDBx:struct_conf");
        for (int i = 0; i < helices.getLength(); i++) {
            Element helixElem = (Element) helices.item(i);
            SecondaryStructure ss = new SecondaryStructure();
            
            ss.setId(helixElem.getAttribute("id"));
            ss.setType(getChildText(helixElem, "PDBx:conf_type_id"));
            ss.setBeginChainId(getChildText(helixElem, "PDBx:beg_label_asym_id"));
            ss.setBeginSeqId(parseInt(getChildText(helixElem, "PDBx:beg_label_seq_id"), 0));
            ss.setEndChainId(getChildText(helixElem, "PDBx:end_label_asym_id"));
            ss.setEndSeqId(parseInt(getChildText(helixElem, "PDBx:end_label_seq_id"), 0));
            
            structure.addSecondaryStructure(ss);
        }
        
        // Sheets
        NodeList sheets = doc.getElementsByTagName("PDBx:struct_sheet_range");
        for (int i = 0; i < sheets.getLength(); i++) {
            Element sheetElem = (Element) sheets.item(i);
            SecondaryStructure ss = new SecondaryStructure();
            
            ss.setId(sheetElem.getAttribute("id"));
            ss.setType("SHEET");
            ss.setBeginChainId(getChildText(sheetElem, "PDBx:beg_label_asym_id"));
            ss.setBeginSeqId(parseInt(getChildText(sheetElem, "PDBx:beg_label_seq_id"), 0));
            ss.setEndChainId(getChildText(sheetElem, "PDBx:end_label_asym_id"));
            ss.setEndSeqId(parseInt(getChildText(sheetElem, "PDBx:end_label_seq_id"), 0));
            
            structure.addSecondaryStructure(ss);
        }
    }

    private void parseExperimentalData(Document doc, PDBMLStructure structure) {
        // Method
        NodeList methods = doc.getElementsByTagName("PDBx:exptl");
        if (methods.getLength() > 0) {
            Element methodElem = (Element) methods.item(0);
            structure.setExperimentalMethod(methodElem.getAttribute("method"));
        }
        
        // Resolution
        NodeList reflns = doc.getElementsByTagName("PDBx:refine");
        if (reflns.getLength() > 0) {
            Element refine = (Element) reflns.item(0);
            String resolution = getChildText(refine, "PDBx:ls_d_res_high");
            if (resolution != null) {
                structure.setResolution(parseDouble(resolution, 0));
            }
        }
    }

    private String getChildText(Element parent, String tagName) {
        NodeList children = parent.getElementsByTagName(tagName);
        if (children.getLength() > 0) {
            return children.item(0).getTextContent().trim();
        }
        return null;
    }

    private double parseDouble(String value, double defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
