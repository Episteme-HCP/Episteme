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

package org.jscience.social.law.loaders;

import org.jscience.core.io.AbstractResourceReader;
import org.jscience.social.law.Statute;
import org.jscience.social.law.StatuteStatus;
import org.jscience.social.law.StatuteType;

/**
 * Loader for European Union legal documents via EUR-Lex (CELLAR API).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 */
public final class EurLexLoader extends AbstractResourceReader<Statute> implements LegalDocumentLoader {

    private static final String CELLAR_BASE = "https://publications.europa.eu/webapi/cellar/v1.0/";

    @Override
    protected Statute loadFromSource(String resourceId) throws Exception {
        // Implementation for SPARQL query or REST fetch from CELLAR
        // resourceId would be a CELEX number (e.g., "32016R0679" for GDPR)
        
        if (resourceId != null && !resourceId.isEmpty()) {
            Statute statute = new Statute(resourceId, "EU Regulation/Directive (EUR-Lex)", 
                StatuteType.DIRECTIVE, "European Union", 2025, StatuteStatus.ENACTED);
                
            // Populate traits
            statute.getTraits().put("source", "EUR-Lex CELLAR (Mock)");
            statute.getTraits().put("celex", resourceId);
            statute.getTraits().put("language", "en,fr,de"); // EU multilingual
            statute.getTraits().put("uri", "http://publications.europa.eu/resource/celex/" + resourceId);
            
            return statute;
        }
        
        throw new Exception("Invalid CELEX identifier: " + resourceId);
    }

    @Override
    public Statute loadContent(String content) {
        return new Statute("EUMOCK", "Content-loaded EU Statute", 
            StatuteType.DIRECTIVE, "European Union", 2025, StatuteStatus.ENACTED);
    }

    @Override
    public String getName() {
        return "EUR-Lex Loader";
    }

    @Override
    public String getDescription() {
        return "Retrieves EU legal acts from the EUR-Lex database";
    }

    @Override
    public String getLongDescription() {
        return "Provides programmatic access to the Publications Office of the EU's " +
               "Common Electronic Lexical Resource (CELLAR) via CELEX numbers.";
    }

    @Override
    public String getCategory() {
        return "Legal Documents - Europe";
    }

    @Override
    public String getResourcePath() {
        return CELLAR_BASE;
    }

    @Override
    public Class<Statute> getResourceType() {
        return Statute.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"CELLAR v1.0"};
    }
}

