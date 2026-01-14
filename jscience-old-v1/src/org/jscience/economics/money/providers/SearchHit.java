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

package org.jscience.economics.money.providers;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//and was originally released under the LGPL
/**
 * A small class representing a search result.
 */
public class SearchHit {
    /** DOCUMENT ME! */
    private String symbol;

    /** DOCUMENT ME! */
    private String result;

/**
     * Creates a new SearchHit object.
     *
     * @param psymbol DOCUMENT ME!
     * @param presult DOCUMENT ME!
     */
    public SearchHit(String psymbol, String presult) {
        symbol = psymbol;

        // Massage the result data
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < presult.length(); i++) {
            if ((Character.isJavaIdentifierStart(presult.charAt(i))) ||
                    (Character.isWhitespace(presult.charAt(i)))) {
                sb.append(presult.charAt(i));
            }
        }

        result = sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getResult() {
        return result;
    }
}
