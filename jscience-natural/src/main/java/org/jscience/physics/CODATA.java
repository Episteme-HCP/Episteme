/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.physics;

import org.jscience.bibliography.Citation;
import org.jscience.bibliography.SimpleCitation;
import org.jscience.bibliography.Standard;

public class CODATA implements Standard {

    public static final CODATA V2022 = new CODATA("2022");

    private final String version;
    private final Citation citation;

    public CODATA(String version) {
        this.version = version;
        this.citation = new SimpleCitation(
                "CODATA" + version,
                "CODATA Recommended Values of the Fundamental Physical Constants: " + version,
                "Tiesinga et al.",
                "2022", // Approximate year of publication
                "10.1103/RevModPhys.93.025010" // Example DOI (check specific version)
        );
    }

    @Override
    public String getName() {
        return "CODATA";
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Citation getCitation() {
        return citation;
    }
}
