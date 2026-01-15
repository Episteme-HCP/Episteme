package org.jscience.help.doclet;

import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class PlanetPhysicsTaglet extends NoosphereTaglet {
/**
     * Creates a new PlanetPhysicsTaglet object.
     */
    public PlanetPhysicsTaglet() {
        super("planetphysics", "PlanetPhysics");
    }

    /**
     * DOCUMENT ME!
     *
     * @param taglets DOCUMENT ME!
     */
    public static void register(Map taglets) {
        register(taglets, new PlanetPhysicsTaglet());
    }
}
