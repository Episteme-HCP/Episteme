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

import org.jscience.economics.money.SymbolSource;

import org.jscience.util.UnavailableDataException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.HashSet;
import java.util.Set;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//and was originally released under the LGPL
/**
 * A class to search for symbols on Yahoo Finance. Results are returned as
 * a linked list of SearchHits
 */
public class YahooSymbolSource extends ParserCallback implements SymbolSource {
    /** DOCUMENT ME! */
    private int tableCurrent = 0;

    /** DOCUMENT ME! */
    private int cellCurrent = 0;

    /** DOCUMENT ME! */
    private int rowCurrent = 0;

    /** DOCUMENT ME! */
    private int subCurrent = 0;

    /** DOCUMENT ME! */
    private HashSet results;

    /** DOCUMENT ME! */
    private String symbol = "";

    /**
     * DOCUMENT ME!
     *
     * @param expression DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnavailableDataException DOCUMENT ME!
     */
    public Set search(String expression) throws UnavailableDataException {
        String u = "http://finance.yahoo.com/l?m=US&s=" + expression + "&t=";

        try {
            results = new HashSet();

            URL url = new URL(u);

            URLConnection conn = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

            ParserDelegator p = new ParserDelegator();
            p.parse(in, this, true);
        } catch (Exception e) {
            throw new UnavailableDataException("Couldn't search.");
        }

        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param pos DOCUMENT ME!
     */
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if (t == HTML.Tag.TABLE) {
            tableCurrent++;
            cellCurrent = 0;
            rowCurrent = 0;
            subCurrent = 0;
        }

        if (t == HTML.Tag.TR) {
            rowCurrent++;
            cellCurrent = 0;
            subCurrent = 0;
        }

        if (t == HTML.Tag.TD) {
            cellCurrent++;
            subCurrent = 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param pos DOCUMENT ME!
     */
    public void handleText(char[] data, int pos) {
        boolean debug = false;

        if (debug) {
            System.out.println("Table: " + tableCurrent + " Row: " +
                rowCurrent + " Cell: " + cellCurrent + " Sub: + " + subCurrent +
                " = " + new String(data));
        }

        // Everything is in table number 8
        if ((tableCurrent == 8) && (rowCurrent > 1)) {
            if ((cellCurrent == 1) && (subCurrent == 0)) {
                symbol = new String(data);
            }

            if ((cellCurrent == 2) && (subCurrent == 0)) {
                String hit = new String(data);
                String symhit = new String(symbol);

                SearchHit sh = new SearchHit(symhit, hit);
                results.add(sh);
            }
        }

        subCurrent++;
    }
}
