/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.psychology;

import java.time.Instant;

/**
 * Represents a cognitive decision made by an agent. * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 
 */
public class Decision {

    private final String subject; // The agent making the decision
    private final String choice; // The option selected
    private final String rationale;
    private final Instant timestamp;

    public Decision(String subject, String choice, String rationale) {
        this.subject = subject;
        this.choice = choice;
        this.rationale = rationale;
        this.timestamp = Instant.now();
    }

    public String getSubject() {
        return subject;
    }

    public String getChoice() {
        return choice;
    }

    public String getRationale() {
        return rationale;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s decided to %s because %s", timestamp, subject, choice, rationale);
    }
}
