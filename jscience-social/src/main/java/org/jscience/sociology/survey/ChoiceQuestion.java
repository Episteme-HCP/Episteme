package org.jscience.sociology.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A question with a set of predefined options.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
public class ChoiceQuestion extends Question {

    private final List<String> options = new ArrayList<>();
    private boolean allowOther;

    public ChoiceQuestion(String text, QuestionType type) {
        super(text, type);
        if (type != QuestionType.MULTIPLE_CHOICE && type != QuestionType.CHECKBOXES && type != QuestionType.DROPDOWN) {
            throw new IllegalArgumentException("Invalid type for ChoiceQuestion: " + type);
        }
    }

    public List<String> getOptions() {
        return Collections.unmodifiableList(options);
    }

    public void addOption(String option) {
        options.add(option);
    }
    
    public void setOptions(List<String> options) {
        this.options.clear();
        this.options.addAll(options);
    }

    public boolean isAllowOther() {
        return allowOther;
    }

    public void setAllowOther(boolean allowOther) {
        this.allowOther = allowOther;
    }
}
