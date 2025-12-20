package org.jscience.philosophy;

/**
 * Represents a logical premise or statement.
 */
public class Premise {

    private final String statement;
    private final boolean truthValue;

    public Premise(String statement) {
        this(statement, true); // Assumed true by default in arguments
    }

    public Premise(String statement, boolean truthValue) {
        this.statement = statement;
        this.truthValue = truthValue;
    }

    public String getStatement() {
        return statement;
    }

    public boolean isTrue() {
        return truthValue;
    }

    @Override
    public String toString() {
        return (truthValue ? "[TRUE] " : "[FALSE] ") + statement;
    }
}
