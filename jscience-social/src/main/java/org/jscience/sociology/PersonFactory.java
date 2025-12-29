/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

import org.jscience.util.identity.IdGenerator;
import org.jscience.util.identity.SSNGenerator;
import org.jscience.util.identity.UUIDGenerator;

import java.time.LocalDate;

/**
 * Factory for creating Person instances with auto-generated IDs.
 * <p>
 * Uses pluggable ID generators to create unique identifiers for persons.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PersonFactory {

    private final IdGenerator idGenerator;

    /**
     * Creates a factory with the default UUID generator.
     */
    public PersonFactory() {
        this(new UUIDGenerator());
    }

    /**
     * Creates a factory with a specific ID generator.
     *
     * @param idGenerator the generator to use for person IDs
     */
    public PersonFactory(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    /**
     * Creates a person with auto-generated ID.
     */
    public Person create(String name, Person.Gender gender, LocalDate birthDate, String nationality) {
        String id = idGenerator.generate();
        return new Person(id, name, gender, birthDate, nationality);
    }

    /**
     * Creates a person with minimal information.
     */
    public Person create(String name) {
        return create(name, Person.Gender.UNSPECIFIED, null, null);
    }

    /**
     * Creates a factory that generates SSN-style IDs.
     */
    public static PersonFactory withSSN() {
        return new PersonFactory(new SSNGenerator());
    }

    /**
     * Creates a factory that generates UUID IDs.
     */
    public static PersonFactory withUUID() {
        return new PersonFactory(new UUIDGenerator());
    }

    /**
     * Returns the ID format used by this factory.
     */
    public String getIdFormat() {
        return idGenerator.getFormat();
    }
}
