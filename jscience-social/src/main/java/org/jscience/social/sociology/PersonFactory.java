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

package org.jscience.social.sociology;

import java.time.LocalDate;
import org.jscience.core.util.identity.IdGenerator;
import org.jscience.core.util.identity.SSNGenerator;
import org.jscience.core.util.identity.UUIDGenerator;
import org.jscience.core.util.identity.Identification;
import org.jscience.natural.biology.BiologicalSex;

/**
 * Factory for creating {@link Person} instances with auto-generated unique identifiers.
 * <p>
 * Provides support for different ID generation strategies (UUID, SSN) via pluggable {@link IdGenerator}.
 * </p>
 * * @version 1.1
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
     * Creates a person with specific attributes and an auto-generated ID.
     *
     * @param name        the person's name
     * @param gender      the person's gender
     * @param birthDate   the person's birth date
     * @param nationality the person's nationality
     * @return a new Person instance
     */
    /**
     * Creates a person with specific attributes and an auto-generated ID.
     *
     * @param name        the person's name
     * @param gender      the person's gender
     * @param birthDate   the person's birth date
     * @param nationality the person's nationality
     * @return a new Person instance
     */
    public Person create(String name, Gender gender, LocalDate birthDate, String nationality) {
        Identification id = idGenerator.generate();
        BiologicalSex sex = (gender != null) ? gender.toSex() : BiologicalSex.UNKNOWN;
        return new Person(id, name, sex, birthDate, nationality);
    }

    /**
     * Creates a person with minimal information (name only).
     * Defaults other fields to unspecified/null.
     *
     * @param name the person's name
     * @return a new Person instance
     */
    public Person create(String name) {
        return create(name, Gender.UNKNOWN, null, null);
    }

    /**
     * Creates a factory instance configured to generate SSN-style IDs.
     * @return a new PersonFactory
     */
    public static PersonFactory withSSN() {
        return new PersonFactory(new SSNGenerator());
    }

    /**
     * Creates a factory instance configured to generate UUIDs.
     * @return a new PersonFactory
     */
    public static PersonFactory withUUID() {
        return new PersonFactory(new UUIDGenerator());
    }
}

