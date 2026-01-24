package org.jscience.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a historical era or period.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class HistoricalEra extends Event implements Serializable {

    private static final long serialVersionUID = 3L;

    @Attribute
    private String region;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<HistoricalEvent> keyEvents = new ArrayList<>();

    public HistoricalEra(String name, String description, TemporalCoordinate when) {
        super(name, description, when, Category.CULTURAL);
    }

    public HistoricalEra(String name, TemporalCoordinate when) {
        this(name, null, when);
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void addKeyEvent(HistoricalEvent event) {
        keyEvents.add(Objects.requireNonNull(event, "HistoricalEvent cannot be null"));
    }

    public List<HistoricalEvent> getKeyEvents() {
        return Collections.unmodifiableList(keyEvents);
    }
}
