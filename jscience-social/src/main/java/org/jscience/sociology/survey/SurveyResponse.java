package org.jscience.sociology.survey;

import org.jscience.sociology.Person;
import org.jscience.util.identity.Identified;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a filled-out survey response by a participant.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class SurveyResponse implements Identified<String> {

    private final String id;
    private final Survey survey;
    private final Person respondent; // Can be null for anonymous
    private final Instant timestamp;
    private final Map<String, Object> answers = new HashMap<>(); // Question ID -> Answer value

    public SurveyResponse(Survey survey, Person respondent) {
        this.id = UUID.randomUUID().toString();
        this.survey = survey;
        this.respondent = respondent;
        this.timestamp = Instant.now();
    }

    @Override
    public String getId() {
        return id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public Person getRespondent() {
        return respondent;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setAnswer(Question question, Object value) {
        if (value == null) {
            answers.remove(question.getId());
        } else {
            answers.put(question.getId(), value);
        }
    }

    public Object getAnswer(Question question) {
        return answers.get(question.getId());
    }
    
    public Map<String, Object> getAnswers() {
        return new HashMap<>(answers);
    }
}
