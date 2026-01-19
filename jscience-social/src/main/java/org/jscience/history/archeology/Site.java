package org.jscience.history.archeology;

import org.jscience.history.time.UncertainDate;
import org.jscience.geography.Place;
import org.jscience.geography.coordinates.Coordinates;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An Archeological Site.
 * Represents a place where artifacts are found.
 * Extends Place as it IS a geographical location.
 */
public class Site extends Place {

    private final UncertainDate occupationPeriod;
    private final Set<Artifact> artifacts;

    /**
     * Creates a new Site.
     * @param name Name of the site.
     * @param coordinates Geographic coordinates.
     * @param occupationPeriod The period during which the site was occupied.
     */
    public Site(String name, Coordinates coordinates, UncertainDate occupationPeriod) {
        super(name, coordinates);
        this.occupationPeriod = occupationPeriod;
        this.artifacts = new HashSet<>();
    }

    public UncertainDate getOccupationPeriod() {
        return occupationPeriod;
    }

    public Set<Artifact> getArtifacts() {
        return Collections.unmodifiableSet(artifacts);
    }

    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }
}
