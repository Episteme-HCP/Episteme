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

package org.jscience.natural.chemistry.loaders.cml;

import org.jscience.core.io.AbstractResourceReader;
import org.jscience.natural.chemistry.loaders.cml.cmlimpl.DocumentFactoryImpl;
import org.xml.sax.InputSource;

import java.io.*;

/**
 * Chemical Markup Language (CML) Reader.
 * <p>
 * CML is an XML-based format for representing molecular and chemical data
 * including structures, reactions, spectra, and computational chemistry results.
 * </p>
 * <p>
 * <b>Supported CML Elements:</b>
 * <ul>
 *   <li><b>Molecules:</b> molecule, atomArray, bondArray, atom, bond</li>
 *   <li><b>Reactions:</b> reaction, reactionList, reactant, product</li>
 *   <li><b>Crystals:</b> crystal, symmetry, cellParameter</li>
 *   <li><b>Spectra:</b> spectrum, peakList, peak</li>
 *   <li><b>Properties:</b> property, scalar, array, matrix</li>
 *   <li><b>Metadata:</b> identifier, name, formula</li>
 * </ul>
 * </p>
 * <p>
 * <b>Example:</b>
 * <pre>{@code
 * CMLReader reader = new CMLReader();
 * AbstractCMLDocument doc = reader.read(new File("molecule.cml"));
 * Element root = doc.getDocumentElement();
 * }</pre>
 * </p>
 * * @see <a href="http://www.xml-cml.org/">XML-CML</a>
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CMLReader extends AbstractResourceReader<AbstractCMLDocument> {

    private CMLDocumentFactory documentFactory;

    /**
     * Creates a new CML reader with the default document factory.
     */
    public CMLReader() {
        this.documentFactory = DocumentFactoryImpl.newInstance();
    }

    /**
     * Creates a new CML reader with a custom document factory.
     *
     * @param factory the document factory to use
     */
    public CMLReader(CMLDocumentFactory factory) {
        this.documentFactory = factory;
    }

    // ===== ResourceReader interface =====

    @Override
    public String getResourcePath() {
        return null; // File-based, path provided at load time
    }

    @Override
    public Class<AbstractCMLDocument> getResourceType() {
        return AbstractCMLDocument.class;
    }

    @Override
    public String getName() {
        return "CML Reader";
    }

    @Override
    public String getDescription() {
        return "Reads molecular and chemical data from CML format";
    }

    @Override
    public String getLongDescription() {
        return "Chemical Markup Language (CML) is an XML format for representing " +
               "molecular structures, reactions, spectra, and computational chemistry data.";
    }

    @Override
    public String getCategory() {
        return "Chemistry";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"3.0", "2.4", "2.3"};
    }

    @Override
    protected AbstractCMLDocument loadFromSource(String resourceId) throws Exception {
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
        throw new CMLException("Resource not found: " + resourceId);
    }

    @Override
    protected AbstractCMLDocument loadFromInputStream(InputStream is, String id) throws Exception {
        return read(is);
    }

    // ===== CML-specific methods =====

    /**
     * Sets the document factory used to create CML documents.
     *
     * @param factory the document factory
     */
    public void setDocumentFactory(CMLDocumentFactory factory) {
        this.documentFactory = factory;
    }

    /**
     * Gets the document factory used to create CML documents.
     *
     * @return the document factory
     */
    public CMLDocumentFactory getDocumentFactory() {
        return documentFactory;
    }

    /**
     * Reads a CML document from an input stream.
     *
     * @param input the input stream containing CML data
     * @return the parsed CML document
     * @throws CMLException if parsing fails
     */
    public AbstractCMLDocument read(InputStream input) throws CMLException {
        try {
            InputSource source = new InputSource(input);
            return documentFactory.parseSAX(source);
        } catch (IOException e) {
            throw new CMLException("Failed to parse CML: " + e.getMessage());
        }
    }

    /**
     * Reads a CML document from a file.
     *
     * @param file the file containing CML data
     * @return the parsed CML document
     * @throws CMLException if parsing fails
     */
    public AbstractCMLDocument read(File file) throws CMLException {
        try (FileInputStream fis = new FileInputStream(file)) {
            InputSource source = new InputSource(fis);
            source.setSystemId(file.toURI().toString());
            return documentFactory.parseSAX(source);
        } catch (IOException e) {
            throw new CMLException("Failed to read file: " + file + " - " + e.getMessage());
        }
    }

    /**
     * Reads a CML document from a string.
     *
     * @param cml the CML content as a string
     * @return the parsed CML document
     * @throws CMLException if parsing fails
     */
    public AbstractCMLDocument readFromString(String cml) throws CMLException {
        try {
            return documentFactory.parseString(cml);
        } catch (CMLException e) {
            throw new CMLException("Failed to parse CML string: " + e.getMessage());
        }
    }

    /**
     * Reads a CML document from a URL.
     *
     * @param url the URL to read from
     * @return the parsed CML document
     * @throws CMLException if parsing fails
     */
    public AbstractCMLDocument read(java.net.URL url) throws CMLException {
        try (InputStream is = url.openStream()) {
            InputSource source = new InputSource(is);
            source.setSystemId(url.toString());
            return documentFactory.parseSAX(source);
        } catch (IOException e) {
            throw new CMLException("Failed to read URL: " + url + " - " + e.getMessage());
        }
    }
}


