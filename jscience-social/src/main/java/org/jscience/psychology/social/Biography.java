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

package org.jscience.psychology.social;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jscience.util.Commented;

/**
 * Represents the structured chronological sequence of events spanning an individual's lifetime.
 * A biography is composed of multiple parallel {@link HumanTimeline} instances categorized by 
 * specific domains of life (e.g., work, health, social).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Biography implements Commented, Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, HumanTimeline> timelines;
    private String comments;
    private final Map<String, Object> traits = new HashMap<>();

    /**
     * Creates a new Biography with default categorical timelines initialized.
     */
    public Biography() {
        this.timelines = new HashMap<>();
        this.comments = "";
        
        initializeDefaultTimelines();
    }

    private void initializeDefaultTimelines() {
        String[] subjects = {
            HumanTimeline.HOME, HumanTimeline.SOCIAL, HumanTimeline.SEX,
            HumanTimeline.FRIENDS, HumanTimeline.LEISURE, HumanTimeline.HEALTH,
            HumanTimeline.AWE, HumanTimeline.RELIGIOUS, HumanTimeline.PERSONAL,
            HumanTimeline.EMOTION, HumanTimeline.WORK, HumanTimeline.MONEY
        };
        
        for (String category : subjects) {
            timelines.put(category, new HumanTimeline(category));
        }
    }

    /**
     * Returns the timeline for a specific life category.
     *
     * @param category the name of the category
     * @return the associated timeline, or {@code null} if not found
     */
    public HumanTimeline getTimeline(String category) {
        return timelines.get(category);
    }

    /**
     * Registers a new domain-specific timeline to this biography.
     *
     * @param timeline the timeline to add
     * @throws NullPointerException if timeline is null
     */
    public void addTimeline(HumanTimeline timeline) {
        Objects.requireNonNull(timeline, "Timeline cannot be null");
        timelines.put(timeline.getCategory(), timeline);
    }

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = Objects.requireNonNull(comments, "Comments cannot be null");
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }
}
