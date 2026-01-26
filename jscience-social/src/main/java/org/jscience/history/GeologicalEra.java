package org.jscience.history;


import org.jscience.history.time.TimeCoordinate;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a geological era or division of time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class GeologicalEra extends Event {

    private static final long serialVersionUID = 2L;

    public GeologicalEra(String name, String description, TimeCoordinate when) {
        super(name, description, when, Category.NATURAL);
    }

    public GeologicalEra(String name, TimeCoordinate when) {
        super(name, when);
    }
}
