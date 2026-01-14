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

package org.jscience.linguistics.kif;

import java.io.*;


/**
 * Class for invoking CELT.
 */
public class CELT {
    /**
     * DOCUMENT ME!
     */
    private Process _CELT;

    /**
     * DOCUMENT ME!
     */
    private BufferedReader _reader;

    /**
     * DOCUMENT ME!
     */
    private BufferedWriter _writer;

    /**
     * DOCUMENT ME!
     */
    private BufferedReader _error;

/**
     * ************************************************************
     * Create a running instance of CELT.
     *
     * @throws IOException should not normally be thrown unless either
     *                     Prolog executable or CELT startup file name are incorrect
     */
    public CELT() throws IOException {
        String CELT_PATH;
        String PL_EXECUTABLE;
        String line = null;
        String erline = null;

        if (KBmanager.getMgr().getPref("prolog") == null) {
            KBmanager.getMgr()
                     .setPref("prolog",
                "C:\\Program Files\\pl-5.2.10\\bin\\plcon.exe");
        }

        PL_EXECUTABLE = KBmanager.getMgr().getPref("prolog");

        if (KBmanager.getMgr().getPref("celtdir") == null) {
            KBmanager.getMgr()
                     .setPref("celtdir",
                "C:\\PEASE\\CELT-ACE\\latestDemo\\May29");
        }

        CELT_PATH = KBmanager.getMgr().getPref("celtdir");
        System.out.println("INFO in CELT(): Setting prolog to: " +
            PL_EXECUTABLE);

        if (!(new File(PL_EXECUTABLE)).exists()) {
            throw new IOException("Error in CELT(): File " + PL_EXECUTABLE +
                " does not exist.");
        }

        if (!(new File(CELT_PATH)).exists()) {
            throw new IOException("Error in CELT(): File " + CELT_PATH +
                " does not exist.");
        }

        StringBuffer kif = new StringBuffer();

        Process _CELT = Runtime.getRuntime()
                               .exec(PL_EXECUTABLE + " " + CELT_PATH +
                File.separator + "Startup.pl");
        InputStream stderr = _CELT.getErrorStream();
        InputStreamReader isrerror = new InputStreamReader(stderr);
        _error = new BufferedReader(isrerror);

        InputStream stdout = _CELT.getInputStream();
        InputStreamReader isrout = new InputStreamReader(stdout);
        _reader = new BufferedReader(isrout);

        OutputStream stdin = _CELT.getOutputStream();
        OutputStreamWriter oswin = new OutputStreamWriter(stdin);
        _writer = new BufferedWriter(oswin);

        do {
            if (_error.ready()) {
                erline = _error.readLine();
                System.out.println("error: " + erline);
            } else if (_reader.ready()) {
                line = _reader.readLine();
                System.out.println("line: " + line);
            } else {
                line = null;
                erline = null;
            }

            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException ie) {
                System.out.println("Error in CELT.(): " + ie.getMessage());
            }
        } while ((line == null) ||
                !line.equalsIgnoreCase("Done initializing."));
    }

    /**
     * Submit a sentence, terminated by a period or question mark and
     * return a KIF formula that is equivalent.
     *
     * @param sentence
     *
     * @return answer from CELT
     *
     * @throws IOException should not normally be thrown
     */
    public String submit(String sentence) throws IOException {
        String line = null;
        String erline = null;
        boolean inKIF = false;
        boolean fail = false;
        StringBuffer kif = new StringBuffer();

        System.out.println("xml_eng2log(\"" + sentence +
            "\",X),format('~w',X).");
        _writer.write("xml_eng2log(\"" + sentence +
            "\",X),format('~w',X).\n\n\n", 0, sentence.length() + 35);
        _writer.flush();

        do {
            if (_error.ready()) {
                erline = _error.readLine();
                System.out.println("error: |" + erline + "|");
            } else if (_reader.ready()) {
                line = _reader.readLine();
                System.out.println("line: |" + line + "|");
            } else {
                line = null;
                erline = null;
            }

            if (line != null) {
                if (line.equalsIgnoreCase("</KIF>")) {
                    inKIF = false;
                } else if (inKIF) {
                    kif = kif.append(line + "\n");
                }

                if (line.indexOf("Could not parse") == 0) {
                    fail = true;
                }

                if (line.equalsIgnoreCase("<KIF>")) {
                    inKIF = true;
                }
            }

            try {
                synchronized (this) {
                    this.wait(100);
                    System.out.println("Waiting.");
                }
            } catch (InterruptedException ie) {
                System.out.println("Error in CELT.submit(): " +
                    ie.getMessage());
            }
        } while ((line == null) || (line.indexOf("</translation>") != 0));

        // && (erline != null && (!erline.equalsIgnoreCase("No")))));
        if (fail) {
            System.out.println("Failed to parse.");

            return null;
        } else {
            System.out.println("Parse successful.");

            return kif.toString();
        }
    }

    /**
     * 
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            System.out.println("in CELT main.");

            CELT celt = new CELT();
            System.out.println("completed initialization.");
            System.out.println(celt.submit("John kicks the cart."));
            System.out.println(celt.submit(
                    "John pokes the antelope with a fork."));
            System.out.println(celt.submit("Who moves the cart?"));
        } catch (IOException ioe) {
            System.out.println("Error in CELT.main(): " + ioe.getMessage());
        }
    }
}
