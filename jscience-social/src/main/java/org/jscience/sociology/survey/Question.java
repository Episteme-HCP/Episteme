package org.jscience.sociology.survey;

import org.jscience.util.identity.Identified;
import java.io.Serializable;
import java.util.UUID;

/**
 * Base class for all survey questions.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public abstract class Question implements Identified<String>, Serializable {
    
    public enum Type {
        TEXT,
        MULTIPLE_CHOICE,
        CHECKBOXES,
        DROPDOWN,
        LINEAR_SCALE,
        DATE
    }

    private final String id;
    private String text;
    private final Type type;
    private boolean required;

    public Question(String text, Type type) {
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.type = type;
        this.required = false;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Type getType() {
        return type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
