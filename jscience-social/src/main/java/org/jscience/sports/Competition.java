package org.jscience.sports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.economics.Money;

/**
 * Represents a sports competition (e.g., tournament, league, championship).
 */
public class Competition {

    private final String name;
    private final List<Match> matches = new ArrayList<>();
    private final List<Money> prizes = new ArrayList<>();
    private String description;

    public Competition(String name) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
    }

    public String getName() {
        return name;
    }

    public void addMatch(Match match) {
        if (match != null) {
            matches.add(match);
            match.setCompetition(this);
        }
    }

    public List<Match> getMatches() {
        return Collections.unmodifiableList(matches);
    }

    public void addPrize(Money prize) {
        if (prize != null) {
            prizes.add(prize);
        }
    }

    public List<Money> getPrizes() {
        return Collections.unmodifiableList(prizes);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name;
    }
}
