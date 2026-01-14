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

package org.jscience.help.doclet;

import com.sun.javadoc.Tag;

import com.sun.tools.doclets.Taglet;

import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class NoosphereTaglet implements Taglet {
    /** DOCUMENT ME! */
    private final String name;

    /** DOCUMENT ME! */
    private final String desc;

/**
     * Creates a new NoosphereTaglet object.
     *
     * @param name DOCUMENT ME!
     * @param desc DOCUMENT ME!
     */
    public NoosphereTaglet(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param taglets DOCUMENT ME!
     * @param taglet DOCUMENT ME!
     */
    protected static void register(Map taglets, Taglet taglet) {
        String name = taglet.getName();
        Taglet old = (Taglet) taglets.get(name);

        if (old != null) {
            taglets.remove(name);
        }

        taglets.put(name, taglet);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inConstructor() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inField() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inMethod() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inOverview() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inPackage() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean inType() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInlineTag() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString(Tag tag) {
        return "<dt><b>" + desc + " references:</b></dt><dd>" +
        createPMlink(tag.text()) + "</dd>";
    }

    /**
     * DOCUMENT ME!
     *
     * @param tags DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString(Tag[] tags) {
        if (tags.length == 0) {
            return null;
        }

        StringBuffer buf = new StringBuffer("<dt><b>" + desc +
                " references:</b></dt><dd>");

        for (int i = 0; i < tags.length; i++) {
            if (i > 0) {
                buf.append(", ");
            }

            buf.append(createPMlink(tags[i].text()));
        }

        return buf.append("</dd>").toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private String createPMlink(String cname) {
        if (cname.indexOf(' ') != -1) {
            throw new IllegalArgumentException("Invalid canonical name: " +
                cname);
        }

        String host = desc + ".org";

        return "<a href=\"http://" + host + "/?op=getobj&from=objects&name=" +
        cname + "\">" + cname + "</a>";
    }
}
