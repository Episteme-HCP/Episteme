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

package org.jscience.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A general perpose Exception that can wrap another exception.
 * <p/>
 * It is common practice in BioJava to throw a NestedException or a subclass of it
 * when something goes wrong. The exception can be used to catch another
 * throwable, thus keeping a complete record of where the original error
 * originated while adding annotation to the stack-trace. It also affords a neat
 * way to avoid exception-bloat on method calls, particularly when objects are
 * composed from several objects from different packages.
 *
 * @author Matthew Pocock
 */
public class NestedException extends Exception {
    private Throwable subThrowable = null;

    /**
     * Creates a new NestedException object.
     *
     * @param message DOCUMENT ME!
     */
    public NestedException(String message) {
        super(message);
    }

    /**
     * Creates a new NestedException object.
     *
     * @param ex DOCUMENT ME!
     */
    public NestedException(Throwable ex) {
        this.subThrowable = ex;
    }

    /**
     * Creates a new NestedException object.
     *
     * @param ex      DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public NestedException(Throwable ex, String message) {
        super(message);
        this.subThrowable = ex;
    }

    /**
     * Creates a new NestedException object.
     */
    public NestedException() {
        super();
    }

    /**
     * DOCUMENT ME!
     */
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ps DOCUMENT ME!
     */
    public void printStackTrace(PrintStream ps) {
        printStackTrace(new PrintWriter(ps));
    }

    /**
     * DOCUMENT ME!
     *
     * @param pw DOCUMENT ME!
     */
    public void printStackTrace(PrintWriter pw) {
        if (subThrowable != null) {
            StringWriter sw1 = new StringWriter();
            subThrowable.printStackTrace(new PrintWriter(sw1));

            String mes1 = sw1.toString();
            StringWriter sw2 = new StringWriter();
            super.printStackTrace(new PrintWriter(sw2));

            String mes2 = sw2.toString();

            try {
                List lines1 = lineSplit(new BufferedReader(new StringReader(mes1)));
                List lines2 = lineSplit(new BufferedReader(new StringReader(mes2)));

                ListIterator li1 = lines1.listIterator(lines1.size());
                ListIterator li2 = lines2.listIterator(lines2.size());

                while (li1.hasPrevious() && li2.hasPrevious()) {
                    Object s1 = li1.previous();
                    Object s2 = li2.previous();

                    if (s1.equals(s2)) {
                        li1.remove();
                    } else {
                        break;
                    }
                }

                for (Iterator i = lines1.iterator(); i.hasNext();) {
                    pw.println(i.next());
                }

                pw.print("rethrown as ");
                pw.print(mes2);
            } catch (IOException ioe) {
                throw new Error("Coudn't merge stack-traces");
            }
        } else {
            super.printStackTrace(pw);
        }

        pw.flush();
    }

    private List lineSplit(BufferedReader in) throws IOException {
        List lines = new ArrayList();

        for (String line = in.readLine(); line != null; line = in.readLine()) {
            lines.add(line);
        }

        return lines;
    }
}
