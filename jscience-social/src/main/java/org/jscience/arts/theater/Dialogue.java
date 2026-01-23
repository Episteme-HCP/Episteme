package org.jscience.arts.theater;


/**
 * Represents lines spoken by a character.
 */
public record Dialogue(Character character, String lines) implements ScriptItem {
    @Override
    public String getDisplayContent() {
        return character.getName().toUpperCase() + ": " + lines;
    }
}
