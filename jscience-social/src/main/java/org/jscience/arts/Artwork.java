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
package org.jscience.arts;

import java.util.UUID;
import org.jscience.util.identity.Identifiable;
import org.jscience.sociology.Person;

/**
 * Represents a piece of artwork.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Artwork implements Identifiable<String> {

    public enum Type {
        PAINTING, SCULPTURE, LITERATURE, MUSIC, FILM, ARCHITECTURE, OTHER
    }

    private final String id;
    private final String title;
    private final Person creator;
    private final int year;
    private final Type type;

    public Artwork(String title, Person creator, int year, Type type) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.creator = creator;
        this.year = year;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Person getCreator() {
        return creator;
    }

    public int getYear() {
        return year;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("'%s' by %s (%d)", title, creator != null ? creator.getName() : "Unknown", year);
    }
}
