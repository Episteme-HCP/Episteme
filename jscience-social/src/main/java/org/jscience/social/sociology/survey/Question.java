/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.social.sociology.survey;

import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;

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

