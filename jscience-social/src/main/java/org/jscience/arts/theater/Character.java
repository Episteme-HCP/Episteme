package org.jscience.arts.theater;

import java.util.Objects;

/**
 * Represents a character in a theatrical play.
 */
public class Character {

    public enum Importance {
        PROTAGONIST, ANTAGONIST, DEUTERAGONIST, TERTIARY, FOIL, NARRATOR
    }

    private final String name;
    private final String description;
    private final Importance importance;

    public Character(String name, String description, Importance importance) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.importance = importance != null ? importance : Importance.TERTIARY;
    }

    public Character(String name) {
        this(name, "", Importance.TERTIARY);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Importance getImportance() { return importance; }

    @Override
    public String toString() {
        return name + " (" + importance + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character that = (Character) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
