/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A question with a set of predefined options.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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

