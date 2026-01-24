package org.jscience.history.archeology;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

/**
 * A distinct layer or unit of context in a stratigraphic sequence.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Stratum implements Identified<String>, Named, Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    private final String id;

    @Attribute
    private final String name;

    @Attribute
    private final String description;

    @Attribute
    private final Quantity<Length> depth;

    @Attribute
    private final Map<String, StratigraphyModel.Relationship> relations = new HashMap<>();

    public Stratum(String id, String name, String description, Quantity<Length> depth) {
        this.id = Objects.requireNonNull(id, "Stratum ID cannot be null");
        this.name = name != null ? name : id;
        this.description = description;
        this.depth = depth != null ? depth : Quantity.of(0.0, Units.METRE);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Quantity<Length> getDepth() {
        return depth;
    }

    public Map<String, StratigraphyModel.Relationship> getRelations() {
        return Collections.unmodifiableMap(relations);
    }

    public void addRelation(String otherStratumId, StratigraphyModel.Relationship relationship) {
        relations.put(otherStratumId, relationship);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stratum stratum)) return false;
        return Objects.equals(id, stratum.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Stratum[%s: %s]", id, name);
    }
}
