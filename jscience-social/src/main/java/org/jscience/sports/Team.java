package org.jscience.sports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.util.identity.Identifiable;
import org.jscience.sociology.Person;
import org.jscience.economics.Money;

/**
 * Represents a sports team.
 */
public class Team implements Identifiable<String> {

    private final String name;
    private final Sport sport;
    private final List<Person> members = new ArrayList<>();
    private Money budget;

    public Team(String name, Sport sport) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.sport = Objects.requireNonNull(sport, "Sport cannot be null");
    }

    @Override
    public String getId() {
        return name;
    }

    public String getName() {
        return name;
    }

    public Sport getSport() {
        return sport;
    }

    public void addMember(Person person) {
        if (person != null) {
            members.add(person);
        }
    }

    public List<Person> getMembers() {
        return Collections.unmodifiableList(members);
    }
    
    public Money getBudget() {
        return budget;
    }

    public void setBudget(Money budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, sport.getName());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team team)) return false;
        return Objects.equals(name, team.name) && Objects.equals(sport, team.sport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sport);
    }
}


