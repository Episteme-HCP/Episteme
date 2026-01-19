package org.jscience.politics;

import org.jscience.history.time.UncertainDate;
import org.jscience.geography.Place;
import java.util.*;

/**
 * Modernized Conflict class using UncertainDate and immutable design.
 */
public final class Conflict {

    public enum ConflictType {
        WAR, CIVIL_WAR, REBELLION, COUP, REVOLUTION, 
        BORDER_CONFLICT, COLONIAL_WAR, RELIGIOUS_WAR
    }

    public enum Status {
        ONGOING, CEASEFIRE, TREATY_SIGNED, DECISIVE_VICTORY, STALEMATE
    }

    private final String name;
    private final ConflictType type;
    private final UncertainDate startDate;
    private final UncertainDate endDate;
    private final Place primaryLocation;
    private final List<String> belligerents;
    private final Status status;
    private final String description;

    private Conflict(Builder builder) {
        this.name = Objects.requireNonNull(builder.name);
        this.type = builder.type != null ? builder.type : ConflictType.WAR;
        this.startDate = Objects.requireNonNull(builder.startDate);
        this.endDate = builder.endDate;
        this.primaryLocation = builder.primaryLocation;
        this.belligerents = List.copyOf(builder.belligerents);
        this.status = builder.status != null ? builder.status : Status.ONGOING;
        this.description = builder.description;
    }

    public String getName() { return name; }
    public ConflictType getType() { return type; }
    public UncertainDate getStartDate() { return startDate; }
    public Optional<UncertainDate> getEndDate() { return Optional.ofNullable(endDate); }
    public Optional<Place> getPrimaryLocation() { return Optional.ofNullable(primaryLocation); }
    public List<String> getBelligerents() { return belligerents; }
    public Status getStatus() { return status; }
    public Optional<String> getDescription() { return Optional.ofNullable(description); }

    /**
     * Calculates the duration of the conflict in years (approximate).
     */
    public OptionalDouble getDurationYears() {
        if (endDate == null) return OptionalDouble.empty();
        
        var start = startDate.getEarliestPossible();
        var end = endDate.getLatestPossible();
        if (start == null || end == null) return OptionalDouble.empty();
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        return OptionalDouble.of(days / 365.25);
    }

    public boolean isOngoing() {
        return status == Status.ONGOING;
    }

    public boolean involves(String party) {
        return belligerents.stream()
            .anyMatch(b -> b.equalsIgnoreCase(party));
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s - %s)", 
            name, type,
            startDate.toString(),
            endDate != null ? endDate.toString() : "ongoing");
    }

    public static Builder builder(String name, UncertainDate startDate) {
        return new Builder(name, startDate);
    }

    public static class Builder {
        private final String name;
        private final UncertainDate startDate;
        private ConflictType type;
        private UncertainDate endDate;
        private Place primaryLocation;
        private final List<String> belligerents = new ArrayList<>();
        private Status status;
        private String description;

        public Builder(String name, UncertainDate startDate) {
            this.name = name;
            this.startDate = startDate;
        }

        public Builder type(ConflictType type) { this.type = type; return this; }
        public Builder endDate(UncertainDate end) { this.endDate = end; return this; }
        public Builder location(Place place) { this.primaryLocation = place; return this; }
        public Builder addBelligerent(String party) { belligerents.add(party); return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder description(String desc) { this.description = desc; return this; }

        public Conflict build() {
            return new Conflict(this);
        }
    }
}
