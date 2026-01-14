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

package org.jscience.sociology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simulated Opinion Poll "device" for gauging public sentiment.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpinionPoll {

    private final String topic;
    private final Random random;

    public OpinionPoll(String topic) {
        this.topic = topic;
        this.random = new Random();
    }

    /**
     * Conducts a poll with a specified sample size.
     * 
     * @param sampleSize number of people polled
     * @return Map of Option -> Percentage
     */
    public Map<String, Double> conductPoll(int sampleSize, String... options) {
        Map<String, Double> results = new HashMap<>();
        if (options.length == 0)
            return results;

        int[] counts = new int[options.length];
        for (int i = 0; i < sampleSize; i++) {
            counts[random.nextInt(options.length)]++;
        }

        for (int i = 0; i < options.length; i++) {
            results.put(options[i], (double) counts[i] / sampleSize * 100.0);
        }

        return results;
    }

    public String getTopic() {
        return topic;
    }
}
