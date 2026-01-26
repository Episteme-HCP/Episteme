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

package org.jscience.linguistics.loaders.tigerxml;

import org.jscience.io.AbstractResourceReader;
import org.jscience.linguistics.loaders.tigerxml.core.CorpusBuilder;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;

/**
 * Reader for TIGER-XML corpus files.
 * Implementation of {@link AbstractResourceReader} for {@link Corpus}.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TigerXMLReader extends AbstractResourceReader<Corpus> {

    @Override
    protected Corpus loadFromSource(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("TigerXML file not found: " + path);
        }
        
        Corpus corpus = new Corpus();
        corpus.setVerbosity(getVerbosity());
        
        DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(file);
        Element rootElement = doc.getDocumentElement();
        
        CorpusBuilder.buildCorpus(corpus, rootElement);
        return corpus;
    }

    @Override
    protected Corpus loadFromInputStream(InputStream is, String id) throws Exception {
        DocumentBuilder builder = org.jscience.io.SecureXMLFactory.createSecureDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(is);
        Element rootElement = doc.getDocumentElement();
        
        Corpus corpus = new Corpus();
        corpus.setId(id);
        CorpusBuilder.buildCorpus(corpus, rootElement);
        return corpus;
    }

    private int getVerbosity() {
        return 0; // Default
    }

    @Override
    public String getResourcePath() {
        return "corpora/tigerxml";
    }

    @Override
    public Class<Corpus> getResourceType() {
        return Corpus.class;
    }

    @Override
    public String getName() {
        return "TIGER-XML Reader";
    }

    @Override
    public String getDescription() {
        return "Loads linguistically annotated corpora in TIGER-XML format.";
    }

    @Override
    public String getLongDescription() {
        return "Comprehensive reader for TIGER-XML, a format for representing graph-based syntactic annotations. " +
               "It supports both terminal and non-terminal nodes, primary and secondary edges, and morphological " +
               "information. Efficiently reconstructs syntax trees and provides access to serialized corpus data.";
    }

    @Override
    public String getCategory() {
        return "Linguistics / Corpus Engineering";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[]{"2.0", "1.0"};
    }
}
