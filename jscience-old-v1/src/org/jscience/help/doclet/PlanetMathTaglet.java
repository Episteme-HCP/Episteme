package org.jscience.help.doclet;

import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class PlanetMathTaglet extends NoosphereTaglet {
/**
     * Creates a new PlanetMathTaglet object.
     */
    public PlanetMathTaglet() {
        super("planetmath", "PlanetMath");
    }

    /**
     * DOCUMENT ME!
     *
     * @param taglets DOCUMENT ME!
     */
    public static void register(Map taglets) {
        register(taglets, new PlanetMathTaglet());
    }
}
