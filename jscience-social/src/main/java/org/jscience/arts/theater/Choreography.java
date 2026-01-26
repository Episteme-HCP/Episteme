package org.jscience.arts.theater;

import org.jscience.arts.Artwork;
import org.jscience.arts.ArtForm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.earth.Place;

/**
 * Represents the choreography for a show.
 */
public class Choreography extends Artwork {

    private final List<String> steps;

    public Choreography(String name, String description, TimeCoordinate productionDate, 
                        Place productionPlace, List<String> steps) {
        super(name, description, productionDate, productionPlace, ArtForm.DANCE);
        this.steps = new ArrayList<>(Objects.requireNonNull(steps, "Steps cannot be null"));
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("Choreography must have at least one step");
        }
    }

    public List<String> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    @Override
    public String toString() {
        return String.format("%s (%d steps)", getName(), steps.size());
    }
}
