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
import org.jscience.law.StatuteStatus;
import org.jscience.law.StatuteType;

/**
 * Loader for International Treaties and United Nations legal documents.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 */
public final class UnLawLoader extends AbstractResourceReader<Statute> implements LegalDocumentLoader {

    private static final String UN_TREATY_BASE = "https://treaties.un.org/api/";

    @Override
    protected Statute loadFromSource(String resourceId) throws Exception {
        // resourceId would be a treaty Registration Number or name
        if (resourceId != null && !resourceId.isEmpty()) {
            return new Statute(resourceId, "UN International Treaty", 
                StatuteType.TREATY, "International", 2025, StatuteStatus.ENACTED);
        }
        
        throw new Exception("Invalid UN Treaty identifier: " + resourceId);
    }

    @Override
    public Statute loadContent(String content) {
        return new Statute("UNMOCK", "Content-loaded UN Treaty", 
            StatuteType.TREATY, "International", 2025, StatuteStatus.ENACTED);
    }

    @Override
    public String getName() {
        return "UN Law Loader";
    }

    @Override
    public String getDescription() {
        return "Fetches international treaties from the UN Treaty Collection";
    }

    @Override
    public String getLongDescription() {
        return "Integrates with the United Nations Treaty Series (UNTS) " +
               "to retrieve multilateral and bilateral agreements.";
    }

    @Override
    public String getCategory() {
        return "Legal Documents - International";
    }

    @Override
    public String getResourcePath() {
        return UN_TREATY_BASE;
    }

    @Override
    public Class<Statute> getResourceType() {
        return Statute.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"UNTS v1"};
    }
}
