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

import org.jscience.economics.money.ChangeSource;
import org.jscience.economics.money.Currency;

import org.jscience.util.UnavailableDataException;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//and was originally released under the LGPL
/**
 * Implementation of ConvertSource for Yahoo Finance.
 */
public class YahooChangeSource extends ParserCallback implements ChangeSource {
    /** DOCUMENT ME! */
    private float result = -1;

    /** DOCUMENT ME! */
    private int tableMatch = 6;

    /** DOCUMENT ME! */
    private int rowMatch = 2;

    /** DOCUMENT ME! */
    private int cellMatch = 2;

    /** DOCUMENT ME! */
    private int tableCurrent = 0;

    /** DOCUMENT ME! */
    private int rowCurrent = 0;

    /** DOCUMENT ME! */
    private int cellCurrent = 0;

    /** DOCUMENT ME! */
    private boolean found = false;

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     * @param source DOCUMENT ME!
     * @param target DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnavailableDataException DOCUMENT ME!
     */
    public float getConverted(float amount, Currency source, Currency target)
        throws UnavailableDataException {
        String u = "http://finance.yahoo.com/m5?a=" + amount + "&s=" +
            source.getCode() + "&t=" + target.getCode();
        tableCurrent = 0;
        rowCurrent = 0;
        cellCurrent = 0;

        try {
            URL url = new URL(u);
            URLConnection conn = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

            ParserDelegator p = new ParserDelegator();
            p.parse(in, this, true);
        } catch (MalformedURLException mue) {
            throw new UnavailableDataException("Couldn't build a URL to send.");
        } catch (IOException e) {
            throw new UnavailableDataException(
                "Error reading from Yahoo's site.");
        }

        return result;
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
        }

        if (t == HTML.Tag.TR) {
            rowCurrent++;
            cellCurrent = 0;
        }

        if (t == HTML.Tag.TD) {
            cellCurrent++;

            if ((tableMatch == tableCurrent) && (cellMatch == cellCurrent) &&
                    (rowCurrent == rowMatch)) {
                found = true;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param pos DOCUMENT ME!
     */
    public void handleText(char[] data, int pos) {
        if (found == true) {
            // We have to remove anything that is not a number, or decimal point,
            // otherwise parseFloat gets confused...
            ByteArrayInputStream uncleanFloat = new ByteArrayInputStream(new String(
                        data).getBytes());
            ByteArrayOutputStream cleanFloat = new ByteArrayOutputStream();

            int nextByte = uncleanFloat.read();

            while (nextByte != -1) {
                if (((nextByte >= 48) && (nextByte <= 57)) || (nextByte == 46)) {
                    cleanFloat.write(nextByte);
                }

                nextByte = uncleanFloat.read();
            }

            result = Float.parseFloat(new String(cleanFloat.toByteArray()));
            found = false;
        }
    }
}
