package org.jscience.arts.theater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an act (sequence of scenes) in a play or opera.
 */
public class Act {
    private final List<Scene> scenes;

    public Act(List<Scene> scenes) {
        this.scenes = new ArrayList<>(Objects.requireNonNull(scenes, "Scenes cannot be null"));
        if (scenes.isEmpty()) {
            throw new IllegalArgumentException("An act must have at least one scene");
        }
    }

    public List<Scene> getScenes() {
        return Collections.unmodifiableList(scenes);
    }

    @Override
    public String toString() {
        return String.format("Act (%d scenes)", scenes.size());
    }
}
