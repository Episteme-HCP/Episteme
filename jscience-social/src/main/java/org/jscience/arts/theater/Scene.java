package org.jscience.arts.theater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a scene in a theatrical show.
 */
public class Scene {
    private final List<ScriptItem> scriptItems = new ArrayList<>();
    private org.jscience.arts.music.Composition composition;
    private Choreography choreography;

    public Scene() {}

    public void addItem(ScriptItem item) {
        if (item != null) {
            scriptItems.add(item);
        }
    }

    public List<ScriptItem> getScriptItems() {
        return Collections.unmodifiableList(scriptItems);
    }

    public org.jscience.arts.music.Composition getComposition() {
        return composition;
    }

    public void setComposition(org.jscience.arts.music.Composition composition) {
        this.composition = composition;
    }

    public Choreography getChoreography() {
        return choreography;
    }

    public void setChoreography(Choreography choreography) {
        this.choreography = choreography;
    }

    @Override
    public String toString() {
        return "Scene (" + scriptItems.size() + " items)";
    }
}
