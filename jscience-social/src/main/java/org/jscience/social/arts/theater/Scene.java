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

package org.jscience.social.arts.theater;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a scene in a theatrical show.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Scene {
    private final List<ScriptItem> scriptItems = new ArrayList<>();
    private org.jscience.social.arts.music.Composition composition;
    private Choreography choreography;

    public Scene() {}

    public void addItem(ScriptItem item) {
        if (item != null) {
            scriptItems.add(item);
        }
    }

    public List<ScriptItem> getScriptItems() {
        return Collections.unmodifiableList(scriptItems);
    }

    public org.jscience.social.arts.music.Composition getComposition() {
        return composition;
    }

    public void setComposition(org.jscience.social.arts.music.Composition composition) {
        this.composition = composition;
    }

    public Choreography getChoreography() {
        return choreography;
    }

    public void setChoreography(Choreography choreography) {
        this.choreography = choreography;
    }

    @Override
    public String toString() {
        return "Scene (" + scriptItems.size() + " items)";
    }
}

