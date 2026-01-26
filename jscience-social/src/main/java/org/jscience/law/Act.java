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
package org.jscience.law;

import org.jscience.sociology.Human;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.Commented;

import java.util.Date;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * A class representing a legal act or certificate (contract, deed, etc.) 
 * which defines social and legal relations such as ownership, citizenship, 
 * marriage, or nationality.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 */
// no relation with an act in a play, see Jsci.arts.theater.Act.
public class Act implements Identified<Identification>, Commented {
    
    /** Indicates a birth certificate. */
    public final static String BIRTH = "Birth";

    /** Indicates a residence permit or certificate. */
    public final static String RESIDENCE = "Residence";

    /** Indicates a certificate of nationality. */
    public final static String NATIONALITY = "Nationality";

    /** Indicates a marriage certificate. */
    public final static String MARRIAGE = "Marriage";

    /** Indicates a divorce decree. */
    public final static String DIVORCE = "Divorce";

    /** Indicates a title of property. */
    public final static String PROPERTY = "Property";

    /** Indicates a certificate of professional aptitude. */
    public final static String APTITUDE = "Aptitude";

    /** Indicates a death certificate. */
    public final static String DEATH = "Death";

    private Identification identification;
    private String matter;
    private Date date;
    private String object; // "you ... have the right to ... under these circumstances..."
    private Set<Human> subjects; // the people in the act
    
    private final Map<String, Object> traits = new HashMap<>();

    /**
     * Creates a new Act object.
     *
     * @param identification the unique identification of this act
     * @param matter the type or category of the act (e.g., BIRTH, MARRIAGE)
     * @param date the date when the act was established or signed
     * @param object the primary content or legal consequences of the act
     * @param subjects the set of individuals concerned by this act
     * @param comments additional notes or observations
     * @throws IllegalArgumentException if any argument is null or if subjects contains non-human entities
     */
    public Act(Identification identification, String matter, Date date,
        String object, Set<Human> subjects, String comments) {

        if (identification == null || matter == null || matter.isEmpty() ||
            date == null || object == null || object.isEmpty() ||
            subjects == null || comments == null) {
            throw new IllegalArgumentException("Arguments cannot be null or empty.");
        }

        for (Object subject : subjects) {
            if (!(subject instanceof Human)) {
                throw new IllegalArgumentException("The subjects Set must contain only Humans.");
            }
        }

        this.identification = identification;
        this.matter = matter;
        this.date = date;
        this.object = object;
        this.subjects = subjects;
        setComments(comments);
    }

    /**
     * Returns the identification of this act.
     * @return the identification
     */
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public Identification getId() {
        return identification;
    }

    /**
     * Returns the matter or category of this act.
     * @return the matter
     */
    public String getMatter() {
        return matter;
    }

    /**
     * Returns the date of the act.
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the primary object or clause of the act.
     * @return the object
     */
    public String getObject() {
        return object;
    }

    /**
     * Returns the set of human subjects involved in the act.
     * @return the set of subjects
     */
    public Set<Human> getSubjects() {
        return subjects;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }
}
