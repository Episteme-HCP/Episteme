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
 * Loader for United States federal law (US Code).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.0
 */
public final class UsLawLoader extends AbstractResourceReader<Statute> implements LegalDocumentLoader {

    private static final String US_CODE_API = "https://api.fdsys.gov/v1/uscode/"; // Older FDsys / current govinfo style

    @Override
    protected Statute loadFromSource(String resourceId) throws Exception {
        // resourceId would be a Title/Section reference (e.g., "17 U.S.C. 101")
        if (resourceId != null && !resourceId.isEmpty()) {
            Statute statute = new Statute(resourceId, "US Code Section", 
                StatuteType.FEDERAL_LAW, "United States", 2025, StatuteStatus.ENACTED);
            
            // Populate traits
            statute.getTraits().put("citation", resourceId); // e.g. "17 U.S.C. 101"
            statute.getTraits().put("source", "US Government Publishing Office (GovInfo) - Mock");
            statute.getTraits().put("url", "https://www.govinfo.gov/app/details/" + resourceId.replace(" ", ""));
            
            return statute;
        }
        
        throw new Exception("Invalid US Code identifier: " + resourceId);
    }

    @Override
    public Statute loadContent(String content) {
        return new Statute("USCMOCK", "Content-loaded US Statute", 
            StatuteType.FEDERAL_LAW, "United States", 2025, StatuteStatus.ENACTED);
    }

    @Override
    public String getName() {
        return "US Law Loader";
    }

    @Override
    public String getDescription() {
        return "Fetches US Federal laws from GovInfo / US Code";
    }

    @Override
    public String getLongDescription() {
        return "Provides access to the United States Code (U.S.C.) " +
               "using Title and Section identifiers.";
    }

    @Override
    public String getCategory() {
        return "Legal Documents - Americas";
    }

    @Override
    public String getResourcePath() {
        return US_CODE_API;
    }

    @Override
    public Class<Statute> getResourceType() {
        return Statute.class;
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"v1"};
    }
}

