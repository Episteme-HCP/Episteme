package org.jscience.history;

import java.io.Serializable;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a geological era or division of time.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class GeologicalEra extends Event implements Serializable {

    private static final long serialVersionUID = 2L;

    public GeologicalEra(String name, String description, TemporalCoordinate when) {
        super(name, description, when, Category.NATURAL);
    }

    public GeologicalEra(String name, TemporalCoordinate when) {
        super(name, when);
    }
}
