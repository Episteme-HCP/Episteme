package org.jscience.history.archeology;

import org.jscience.history.time.UncertainDate;

/**
 * An Artifact found at a site.
 * Formerly 'Item'.
 */
public class Artifact {

    private final String name;
    private final String description;
    private final UncertainDate dating; // Radioactive carbon dating etc.

    public Artifact(String name, String description, UncertainDate dating) {
        this.name = name;
        this.description = description;
        this.dating = dating;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UncertainDate getDating() {
        return dating;
    }
}
