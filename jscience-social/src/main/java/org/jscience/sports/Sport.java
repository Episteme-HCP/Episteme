package org.jscience.sports;

import org.jscience.util.identity.Identifiable;
import java.util.Objects;

/**
 * Represents a sport or athletic activity.
 */
public class Sport implements Identifiable<String> {

    private final String name;
    private final boolean teamSport;
    private final Category category;

    public Sport(String name, boolean teamSport, Category category) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.teamSport = teamSport;
        this.category = category;
    }
    
    public Sport(String name, boolean teamSport) {
        this(name, teamSport, null);
    }

    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public boolean isTeamSport() {
        return teamSport;
    }
    
    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sport sport)) return false;
        return teamSport == sport.teamSport && Objects.equals(name, sport.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, teamSport);
    }
}


