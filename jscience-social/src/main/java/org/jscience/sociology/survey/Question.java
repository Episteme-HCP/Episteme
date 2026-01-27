/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.sociology.survey;

import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Base class for all survey questions.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public abstract class Question implements ComprehensiveIdentification {
    
    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Attribute
    private final QuestionType type;

    @Attribute
    private boolean required;

    @Deprecated
    public enum Type {
        @Deprecated TEXT,
        @Deprecated PARAGRAPH,
        @Deprecated MULTIPLE_CHOICE,
        @Deprecated CHECKBOXES,
        @Deprecated DROPDOWN,
        @Deprecated LINEAR_SCALE,
        @Deprecated DATE,
        @Deprecated TIME;
        
        @Deprecated
        public QuestionType toQuestionType() {
            return QuestionType.valueOf(name());
        }
    }

    public Question(String text, QuestionType type) {
        this.id = new UUIDIdentification();
        setName(text);
        this.type = type;
        this.required = false;
    }

    @Deprecated
    public Question(String text, Type type) {
        this(text, type.toQuestionType());
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getText() {
        return getName();
    }

    public void setText(String text) {
        setName(text);
    }

    public QuestionType getType() {
        return type;
    }
    
    @Deprecated
    public Type getLegacyType() {
        try {
            return Type.valueOf(type.name());
        } catch (IllegalArgumentException e) {
            return Type.TEXT;
        }
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question question)) return false;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
