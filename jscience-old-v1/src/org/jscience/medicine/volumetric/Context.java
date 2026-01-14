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

package org.jscience.medicine.volumetric;

import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Context {
    /** DOCUMENT ME! */
    Vector attrs = new Vector();

    /** DOCUMENT ME! */
    Hashtable lookup = new Hashtable();

    /** DOCUMENT ME! */
    String ident = "Java3D Volume Rendering Demo Attrs";

    /** DOCUMENT ME! */
    URL codebase;

/**
     * Creates a new Context object.
     *
     * @param initCodebase DOCUMENT ME!
     */
    public Context(URL initCodebase) {
        codebase = initCodebase;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newAttr DOCUMENT ME!
     */
    void addAttr(Attr newAttr) {
        attrs.add(newAttr);
        lookup.put(newAttr.getName(), newAttr);
    }

    /**
     * DOCUMENT ME!
     */
    void reset() {
        for (Enumeration e = attrs.elements(); e.hasMoreElements();) {
            Attr attr = (Attr) e.nextElement();
            attr.reset();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     * @param description DOCUMENT ME!
     */
    void save(String filename, String description) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(filename));
            out.println(ident);
            out.println("Description: " + description);

            for (Enumeration e = attrs.elements(); e.hasMoreElements();) {
                Attr attr = (Attr) e.nextElement();
                out.println(attr);
            }

            out.close();
        } catch (Exception e) {
            System.out.println("Exception " + e + " writing attrs");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filename DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String restore(String filename) {
        String description = null;
        reset();

        try {
            URL vrsdat = null;

            try {
                vrsdat = new URL(codebase + filename);
            } catch (MalformedURLException e) {
                System.out.println("VolFile: " + e.getMessage());
            }

            LineNumberReader in = new LineNumberReader(new InputStreamReader(
                        vrsdat.openStream()));
            String line;
            line = in.readLine();

            if (!line.equals(ident)) {
                System.err.println("File " + filename +
                    " doesn't have a valid header: \"" + line + "\"");
            }

            line = in.readLine();

            int space = line.indexOf(' ');
            description = line.substring(space + 1);

            if (description.equals("null")) {
                description = null;
            }

            while ((line = in.readLine()) != null) {
                space = line.indexOf(' ');

                String name = line.substring(0, space);
                String value = line.substring(space + 1);

                //System.out.println("setting attr " + name + " to " + value);
                Attr attr = getAttr(name);

                if (attr != null) {
                    attr.set(value);
                    attr.updateComponent();
                } else {
                    System.err.println("restore: attr " + name + " not found");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception " + e + " reading attrs");
        }

        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    Attr getAttr(String label) {
        String name = Attr.toName(label);
        Attr retval = (Attr) lookup.get(name);

        if (retval == null) {
            System.err.println("Attr not found: " + name);

            // TODO: dump the names in the table
            // TODO: throw a runtime error instead of NPE
            throw new NullPointerException();
        }

        return retval;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    URL getCodeBase() {
        return codebase;
    }
}
