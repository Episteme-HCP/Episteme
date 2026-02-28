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

package org.episteme.social.politics;

import org.episteme.core.mathematics.numbers.real.Real;

/**
 * Models the estimated political influence of lobbying on policy decisions.
 * Uses logarithmic scaling of spend compared to public opinion and baseline bias.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class LobbyingInfluenceModel {

    private LobbyingInfluenceModel() {}

    /**
     * Calculates the estimated policy shift resulting from lobbying expenditures.
     * 
     * @param spend         monetary amount spent on lobbying
     * @param publicOpinion baseline level of public support (0.0 to 1.0)
     * @param bias          institutional or personal bias of decision makers
     * @return a Real number representing the estimated shift in policy position
     */
    public static Real policyShift(double spend, double publicOpinion, double bias) {
        // Shift increases with spend, but decreases if public opinion is strongly aligned against the spender
        double opinionFactor = Math.max(0, 1.0 - publicOpinion);
        return Real.of(Math.log1p(spend) * opinionFactor + bias);
    }
}

