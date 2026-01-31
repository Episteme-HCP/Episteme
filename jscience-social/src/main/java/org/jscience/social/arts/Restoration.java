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

package org.jscience.social.arts;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.social.history.time.TimeCoordinate;

/**
 * Represents a restoration event or conservation treatment performed on an artwork.
 * Tracks the technical process, the restorer's identity, and the outcome of the 
 * intervention.
 * * @version 2.0
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Restoration implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String processName;
    private final String restorer; // Individual or Organization name
    private final TimeCoordinate date;
    private final String outcome;
    private String comments;

    /**
     * Creates a new Restoration record.
     * 
     * @param processName the name of the restoration process (e.g., "Varnish removal")
     * @param restorer the name of the individual or organization performing the work
     * @param date the date when the restoration was completed
     * @param outcome the state of the artwork after the treatment
     */
    public Restoration(String processName, String restorer, TimeCoordinate date, String outcome) {
        this.processName = Objects.requireNonNull(processName, "Process name cannot be null");
        this.restorer = Objects.requireNonNull(restorer, "Restorer cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.outcome = Objects.requireNonNull(outcome, "Outcome cannot be null");
    }

    public String getProcessName() {
        return processName;
    }

    public String getRestorer() {
        return restorer;
    }

    public TimeCoordinate getDate() {
        return date;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
        return String.format("Restoration[%s by %s on %s: %s]", processName, restorer, date, outcome);
    }
}

