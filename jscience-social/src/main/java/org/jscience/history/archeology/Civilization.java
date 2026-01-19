package org.jscience.history.archeology;

import org.jscience.history.time.UncertainDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A Civilization or Culture.
 * Defined by a time range and a set of regions (Sites).
 */
public class Civilization {
    
    private final String name;
    private final UncertainDate duration;
    private final Set<Site> sites;
    private String description;

    public Civilization(String name, UncertainDate duration) {
        this.name = name;
        this.duration = duration;
        this.sites = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public UncertainDate getDuration() {
        return duration;
    }

    public Set<Site> getSites() {
        return Collections.unmodifiableSet(sites);
    }

    public void addSite(Site site) {
        sites.add(site);
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
