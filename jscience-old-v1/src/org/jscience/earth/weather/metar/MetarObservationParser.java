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

package org.jscience.earth.weather.metar;

import java.io.*;

import java.net.URL;
import java.net.URLConnection;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MetarObservationParser {
    /** DOCUMENT ME! */
    protected String code = null;

/**
     * Creates a new MetarObservationParser object.
     */
    public MetarObservationParser() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param newCode DOCUMENT ME!
     */
    public void setCode(String newCode) {
        code = newCode.toUpperCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public MetarObservation retrieve() throws IOException {
        URL u = new URL(
                "ftp://tgftp.nws.noaa.gov/data/observations/metar/decoded/" +
                code + ".TXT");

        // changed server address to new more reliable one
        URLConnection uconn = u.openConnection();
        uconn.setUseCaches(false);

        StringWriter sout = new StringWriter();
        PrintWriter out = new PrintWriter(sout);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    uconn.getInputStream()));
        String line = null;

        while ((line = in.readLine()) != null) {
            out.println(line);
        }

        return parseObservation(sout.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected MetarObservation parseObservation(String text) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(text));
            MetarObservation ob = new MetarObservation();
            ob.setLocation(in.readLine());
            ob.setTime(in.readLine());

            String line = null;

            while ((line = in.readLine()) != null) {
                int pos = line.indexOf(":");

                if (pos == -1) {
                    continue;
                }

                String name = line.substring(0, pos);
                String value = line.substring(pos + 2);
                pos = value.lastIndexOf(":0");

                if (pos > -1) {
                    value = value.substring(0, pos);
                }

                if (name.equals("ob")) {
                    ob.setCodedObservation(value);
                } else {
                    ob.setProperty(name, value);
                }
            }

            return ob;
        } catch (IOException ioe) {
            //impossible
            return null;
        }
    }
}
