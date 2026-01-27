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

package org.jscience.arts.theater;

import org.jscience.arts.Artwork;
import org.jscience.arts.ArtForm;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.earth.Place;

/**
 * Represents the choreography for a show.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Choreography extends Artwork {

    private final List<String> steps;

    public Choreography(String name, String description, TimeCoordinate productionDate, 
                        Place productionPlace, List<String> steps) {
        super(name, description, productionDate, productionPlace, ArtForm.DANCE);
        this.steps = new ArrayList<>(Objects.requireNonNull(steps, "Steps cannot be null"));
        if (steps.isEmpty()) {
            throw new IllegalArgumentException("Choreography must have at least one step");
        }
    }

    public List<String> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    @Override
    public String toString() {
        return String.format("%s (%d steps)", getName(), steps.size());
    }
}
