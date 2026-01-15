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
