package org.jscience.law;

import org.jscience.geography.Place;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a legal jurisdiction or area of authority.
 */
public class Jurisdiction {

    private final String name;
    private final Place territory;
    private final Set<Statute> statutes;
    private final Jurisdiction parentJurisdiction; // e.g., State in a Country

    public Jurisdiction(String name, Place territory) {
        this(name, territory, null);
    }

    public Jurisdiction(String name, Place territory, Jurisdiction parentJurisdiction) {
        this.name = name;
        this.territory = territory;
        this.parentJurisdiction = parentJurisdiction;
        this.statutes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public Place getTerritory() {
        return territory;
    }

    public Jurisdiction getParentJurisdiction() {
        return parentJurisdiction;
    }

    public void addStatute(Statute statute) {
        statutes.add(statute);
    }

    public Set<Statute> getStatutes() {
        return Collections.unmodifiableSet(statutes);
    }

    /**
     * Checks if a statute is applicable in this jurisdiction (including inherited).
     */
    public boolean isApplicable(Statute statute) {
        if (statutes.contains(statute))
            return true;
        if (parentJurisdiction != null)
            return parentJurisdiction.isApplicable(statute);
        return false;
    }
}
