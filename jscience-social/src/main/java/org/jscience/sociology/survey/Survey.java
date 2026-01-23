package org.jscience.sociology.survey;

import org.jscience.util.Named;
import org.jscience.util.identity.Identified;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Represents a complete survey or questionnaire.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class Survey implements Identified<String>, Named {

    private final String id;
    private String title;
    private String description;
    private final List<Question> questions = new ArrayList<>();

    public Survey(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }
}
