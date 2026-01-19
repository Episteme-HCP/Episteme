package org.jscience.arts.theater;

/**
 * Represents stage directions or descriptions in a script.
 */
public record StageDirection(String description) implements ScriptItem {
    @Override
    public String getDisplayContent() {
        return "[" + description + "]";
    }
}
