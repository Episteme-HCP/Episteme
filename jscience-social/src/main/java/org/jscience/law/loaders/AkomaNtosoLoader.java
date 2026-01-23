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
package org.jscience.law.loaders;

import org.jscience.io.AbstractResourceReader;
import org.jscience.law.Statute;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loader for Akoma Ntoso (XML-based) legal documents.
 * Akoma Ntoso defines a set of simple, technology-neutral XML representations 
 * of parliamentary, legislative and judiciary documents.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
public final class AkomaNtosoLoader extends AbstractResourceReader<Statute> implements LegalDocumentLoader {

    @Override
    protected Statute loadFromSource(String resourceId) throws Exception {
        // In a real implementation, this would fetch the XML from a URL or file
        // For now, it might be the resourceId itself if it's XML content, 
        // but normally resourceId is a path or URI.
        throw new UnsupportedOperationException("Loading from URI not yet implemented. Use loadContent().");
    }

    @Override
    public Statute loadContent(String xml) {
        String title = extract(xml, "<docTitle>(.*?)</docTitle>");
        return new Statute("AN-" + title.hashCode(), title, Statute.Type.REGULATION, "AkomaNtoso", 2025, Statute.Status.ENACTED);
    }

    private String extract(String xml, String regex) {
        Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xml);
        return m.find() ? m.group(1).trim() : "Unknown";
    }

    @Override
    public String getName() {
        return "Akoma Ntoso Loader";
    }

    @Override
    public String getDescription() {
        return "Loader for Akoma Ntoso XML legal documents";
    }

    @Override
    public String getLongDescription() {
        return "Supports the Akoma Ntoso international standard for legislative and judiciary documents.";
    }

    @Override
    public String getCategory() {
        return "Legal Documents";
    }

    @Override
    public String getResourcePath() {
        return "/law/akomantoso/";
    }

    @Override
    public Class<Statute> getResourceType() {
        return Statute.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"AKN 3.0", "AKN 2.0"};
    }
}
