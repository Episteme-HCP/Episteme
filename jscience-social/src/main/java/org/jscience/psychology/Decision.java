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

package org.jscience.psychology;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.history.time.TimePoint;

/**
 * Represents a discrete decision made by an agent (Individual or Organization).
 * Captures the choice, the rationale behind it, and the temporal context.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public record Decision(
    String subject,
    String choice,
    String rationale,
    TimeCoordinate timestamp
) implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new Decision with the current timestamp.
     * 
     * @param subject   the agent making the decision
     * @param choice    the selected option
     * @param rationale the reasoning behind the choice
     */
    public Decision(String subject, String choice, String rationale) {
        this(
            Objects.requireNonNull(subject, "Subject cannot be null"),
            Objects.requireNonNull(choice, "Choice cannot be null"),
            Objects.requireNonNull(rationale, "Rationale cannot be null"),
            TimePoint.now()
        );
    }
}
