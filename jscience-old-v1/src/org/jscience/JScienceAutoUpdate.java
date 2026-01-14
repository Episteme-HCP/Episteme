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

package org.jscience;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class JScienceAutoUpdate {
    /** DOCUMENT ME! */
    private final static String zipfile = "JScience.zip";

    /** DOCUMENT ME! */
    private final static String email = "contact@jscience.org";

    /** DOCUMENT ME! */
    private URL url;

/**
     * Creates a new JScienceAutoUpdate object.
     *
     * @param home DOCUMENT ME!
     */
    private JScienceAutoUpdate(String home) {
        try {
            url = new URL(home + zipfile);
        } catch (MalformedURLException e) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        JScienceVersion current = JScienceVersion.getCurrent();
        System.out.println("Current version: " + current.toString());
        System.out.println("Checking for a later version...");

        try {
            JScienceVersion latest = JScienceVersion.getLatest();
            System.out.println("Latest version: " + latest.toString());

            if (latest.isLater(current)) {
                System.out.print("Downloading latest version...");

                JScienceAutoUpdate app = new JScienceAutoUpdate(current.home);
                app.download();
                System.out.println("done.");
            }
        } catch (IOException e) {
            System.out.println(
                "\nError transfering data - try again or contact " + email +
                " directly.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void download() throws IOException {
        final OutputStream out = new FileOutputStream(zipfile);
        final InputStream in = url.openStream();
        byte[] buf = new byte[in.available()];

        while (buf.length > 0) {
            in.read(buf);
            out.write(buf);
            buf = new byte[in.available()];
        }

        in.close();
        out.close();
    }
}
