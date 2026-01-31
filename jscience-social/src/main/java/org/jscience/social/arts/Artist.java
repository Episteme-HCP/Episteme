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

package org.jscience.social.arts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jscience.natural.biology.BiologicalSex;
import org.jscience.natural.biology.Individual;

// ... (existing imports, but I will just replace the top import block or use replace_file_content carefully)

// Actually, I should insert the import first, then replace usage.
// Let's do imports first.

import org.jscience.natural.biology.taxonomy.Species;
import org.jscience.social.economics.Organization;
import org.jscience.social.economics.Worker;
import org.jscience.social.economics.money.Money;
import org.jscience.natural.earth.Place;
import org.jscience.social.history.time.FuzzyTimePoint;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;

/**
 * Represents an artist (painter, sculptor, musician, etc.) as a specialized social role.
 * This class captures the artist's biographical data, media of expertise,
 * nationality, and belonging to artistic movements.
 * * @version 2.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Artist extends Worker {

    private static final long serialVersionUID = 2L;

    @Attribute
    private final Set<ArtMedium> media = new HashSet<>();
    @Attribute
    private String nationality;
    @Attribute
    private TimeCoordinate birthDate;
    @Attribute
    private TimeCoordinate deathDate;
    @Attribute
    private String movement; // e.g., "Impressionism", "Baroque"
    @Attribute
    private final List<String> notableWorks = new ArrayList<>();
    @Attribute
    private String biography;

    /**
     * Creates a new Artist role.
     * 
     * @param individual the individual playing the role
     * @param organization the organization the artist belongs to (can be self-employed)
     * @param function the specific function description (e.g., "Painter")
     */
    public Artist(Individual individual, Organization organization, String function) {
        super(individual, organization, function, Money.usd(Real.ZERO));
    }

    /**
     * Creates a new Artist role with "Artist" as the default function.
     * 
     * @param individual the individual playing the role
     * @param organization the organization the artist belongs to
     */
    public Artist(Individual individual, Organization organization) {
        this(individual, organization, "Artist");
    }

    public Set<ArtMedium> getMedia() {
        return Collections.unmodifiableSet(media);
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public TimeCoordinate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(TimeCoordinate date) {
        this.birthDate = date;
    }

    public TimeCoordinate getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(TimeCoordinate date) {
        this.deathDate = date;
    }

    public String getMovement() {
        return movement;
    }

    public void setMovement(String movement) {
        this.movement = movement;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String bio) {
        this.biography = bio;
    }

    public List<String> getNotableWorks() {
        return Collections.unmodifiableList(notableWorks);
    }

    public void addNotableWork(String work) {
        notableWorks.add(work);
    }

    public void addMedium(ArtMedium medium) {
        if (medium != null) {
            media.add(medium);
        }
    }

    /**
     * Checks if the artist is currently alive (deathDate is null).
     * @return true if alive
     */
    public boolean isAlive() {
        return deathDate == null;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", getName(), media, nationality);
    }

    /**
     * Factory method for Leonardo da Vinci.
     * @return Leonardo da Vinci artist profile
     */
    public static Artist leonardoDaVinci() {
        Species human = new Species("Human", "Homo sapiens");
        Individual leo = new Individual(UUID.randomUUID().toString(), human, BiologicalSex.MALE, LocalDate.of(1452, 4, 15));
        leo.setTrait("name", "Leonardo da Vinci");
        
        Organization selfEmployed = new Organization("Independent", new Place("Florence"), Money.usd(Real.ZERO));
        Artist a = new Artist(leo, selfEmployed, "Polymath");
        
        a.addMedium(ArtMedium.PAINTING);
        a.addMedium(ArtMedium.SCULPTURE);
        a.setNationality("Italian");
        a.setBirthDate(FuzzyTimePoint.of(1452, 4, 15));
        a.setDeathDate(FuzzyTimePoint.of(1519, 5, 2));
        a.setMovement("Renaissance");
        a.addNotableWork("Mona Lisa");
        a.addNotableWork("The Last Supper");
        return a;
    }

    /**
     * Factory method for Ludwig van Beethoven.
     * @return Beethoven artist profile
     */
    public static Artist beethoven() {
        Species human = new Species("Human", "Homo sapiens");
        Individual ludwig = new Individual(UUID.randomUUID().toString(), human, BiologicalSex.MALE, LocalDate.of(1770, 12, 17));
        ludwig.setTrait("name", "Ludwig van Beethoven");
        
        Organization selfEmployed = new Organization("Independent", new Place("Vienna"), Money.usd(Real.ZERO));
        Artist a = new Artist(ludwig, selfEmployed, "Composer");
        
        a.addMedium(ArtMedium.MUSIC);
        a.setNationality("German");
        a.setBirthDate(FuzzyTimePoint.of(1770, 12, 17));
        a.setDeathDate(FuzzyTimePoint.of(1827, 3, 26));
        a.setMovement("Romanticism");
        a.addNotableWork("Symphony No. 9");
        a.addNotableWork("Moonlight Sonata");
        return a;
    }
}

