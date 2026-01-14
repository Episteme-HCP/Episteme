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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//original author Craig Cavanaugh rideatrail@fwi.com
//and was originally released under the LGPL
/**
 * Currency data source for Oanda.
 */
public class OandaChangeSource implements ChangeSource {
    /** DOCUMENT ME! */
    public static URLConnection connection = null;

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
        return Float.parseFloat(getOandaExchangeRate(Float.toString(amount),
                source.getCode(), target.getCode()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     * @param cur1 DOCUMENT ME!
     * @param cur2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnavailableDataException DOCUMENT ME!
     */
    private static String getOandaExchangeRate(String amount, String cur1,
        String cur2) throws UnavailableDataException {
        // http://www.oanda.com/converter/classic?value=1.0&exch=USD&expr=CAD
        String req = "http://www.oanda.com/converter/classic?value=" + amount +
            "&exch=" + cur1 + "&expr=" + cur2;

        try {
            OandaExchangeRateParser parser = new OandaExchangeRateParser();
            URL url = new URL(req);
            connection = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));
            ParserDelegator pd = new ParserDelegator();
            pd.parse(in, parser, true);
            connection = null;

            return parser.getResult();
        } catch (MalformedURLException mue) {
            throw new UnavailableDataException("Couldn't build a URL to send.");
        } catch (IOException e) {
            throw new UnavailableDataException(
                "Error reading from Oanda's site.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.4 $
     */
    private static class OandaExchangeRateParser extends HTMLEditorKit.ParserCallback {
        /** DOCUMENT ME! */
        private String result = "";

        /** DOCUMENT ME! */
        private boolean inrange = false;

        /** DOCUMENT ME! */
        private boolean found = false;

        /** DOCUMENT ME! */
        private int fontCount = 0;

        /** DOCUMENT ME! */
        private int fontMatch = 3;

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String getResult() {
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
            if ((t == HTML.Tag.FONT) && inrange) {
                fontCount++;

                if (fontCount == fontMatch) {
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
                try {
                    String tempStr = new String(data);
                    int index = tempStr.indexOf(',');

                    if (index != -1) {
                        StringBuffer buf = new StringBuffer(tempStr);
                        buf.deleteCharAt(index);
                        tempStr = buf.toString();
                    }

                    result = new Float(tempStr).toString();
                } catch (NumberFormatException e) {
                    result = "";
                }

                found = false;
                inrange = false;
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param data DOCUMENT ME!
         * @param pos DOCUMENT ME!
         */
        public void handleComment(char[] data, int pos) {
            if (new String(data).trim().startsWith("conversion result starts")) {
                inrange = true;
            }
        }
    }
}
