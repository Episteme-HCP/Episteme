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

package org.jscience.politics;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a cast ballot in an election, supporting various voting methods.
 * A ballot can capture a single choice, a ranked list of preferences, or ratings for candidates.
 *
 * @param voterId       Unique identifier for the voter (anonymized in practice)
 * @param rankedChoices List of candidates in order of preference (1st, 2nd, etc.)
 * @param ratedChoices  Map of candidates to their assigned ratings/scores
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public record Ballot(
    String voterId,
    List<String> rankedChoices,
    Map<String, Integer> ratedChoices
) implements Serializable {

    /**
     * Creates a simple single-choice ballot (e.g., for First-Past-The-Post).
     * 
     * @param voterId Identifier of the voter
     * @param choice  The selected candidate or choice
     * @return A new Ballot instance
     */
    public static Ballot singleChoice(String voterId, String choice) {
        return new Ballot(voterId, Collections.singletonList(choice), null);
    }
}
