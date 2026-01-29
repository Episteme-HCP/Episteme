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

package org.jscience.sociology.survey;

import org.jscience.sociology.Person;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import org.jscience.history.time.TimeCoordinate;
import org.jscience.history.time.TimePoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a filled-out survey response by a participant.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class SurveyResponse implements ComprehensiveIdentification {



    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Survey survey;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Person respondent; // Can be null for anonymous

    @Attribute
    private final TimeCoordinate timestamp;

    @Attribute
    private final Map<String, Object> answers = new HashMap<>(); // Question ID -> Answer value

    public SurveyResponse(Survey survey, Person respondent) {
        this.id = new UUIDIdentification();
        this.survey = Objects.requireNonNull(survey, "Survey cannot be null");
        this.respondent = respondent;
        this.timestamp = TimePoint.now();
        setName("Response to " + survey.getTitle());
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Person getRespondent() {
        return respondent;
    }

    public TimeCoordinate getTimestamp() {
        return timestamp;
    }

    public void setAnswer(Question question, Object value) {
        if (value == null) {
            answers.remove(question.getId().toString());
        } else {
            answers.put(question.getId().toString(), value);
        }
    }

    public Object getAnswer(Question question) {
        return answers.get(question.getId().toString());
    }
    
    public Map<String, Object> getAnswers() {
        return new HashMap<>(answers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SurveyResponse that)) return false;
        return Objects.equals(id, that.id);
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
