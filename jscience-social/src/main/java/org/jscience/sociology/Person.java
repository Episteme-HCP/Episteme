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

import org.jscience.biology.Individual;
import org.jscience.biology.HomoSapiens;
import org.jscience.economics.Money;
import org.jscience.geography.Place;
import java.time.LocalDate;
import java.util.*;

/**
 * Represents a person with demographics and social attributes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Person extends Individual
        implements org.jscience.geography.Locatable {

    public enum Gender {
        MALE, FEMALE, OTHER, UNSPECIFIED;
        
        public Individual.Sex toSex() {
            switch(this) {
                case MALE: return Individual.Sex.MALE;
                case FEMALE: return Individual.Sex.FEMALE;
                default: return Individual.Sex.UNKNOWN;
            }
        }
        
        public static Gender fromSex(Individual.Sex sex) {
            switch(sex) {
                case MALE: return MALE;
                case FEMALE: return FEMALE;
                default: return UNSPECIFIED;
            }
        }
    }

    private final String nationality;
    private final List<String> roles;
    private final List<Role> structuralRoles;
    
    // V1 Features Modernized
    private final Set<Person> spouses = new HashSet<>();
    private Money wealth;
    
    // address remains as specific data
    private String address;

    public Person(String id, String name, Individual.Sex sex, LocalDate birthDate,
            String nationality) {
        super(id, HomoSapiens.SPECIES, sex, birthDate);
        setTrait("name", name);
        this.nationality = nationality;

        this.roles = new ArrayList<>();
        this.structuralRoles = new ArrayList<>();
        // Default initial state
        this.wealth = Money.usd(0);
    }

    public Person(String id, String name, Gender gender, LocalDate birthDate, String nationality) {
        this(id, name, gender.toSex(), birthDate, nationality);
    }

    public Person(String name, Individual.Sex sex) {
        this(java.util.UUID.randomUUID().toString(), name, sex, LocalDate.now().minusYears(20), "Unknown");
    }

    public Person(String name, Gender gender) {
        this(name, gender.toSex());
    }

    @Override
    public String getName() {
        return super.getName();
    }

    public Gender getGender() {
        return Gender.fromSex(getSex());
    }

    public String getNationality() {
        return nationality;
    }

    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public Money getWealth() {
        return wealth;
    }

    public void earn(Money amount) {
        if (this.wealth.getCurrency().equals(amount.getCurrency())) {
            this.wealth = this.wealth.add(amount);
        }
    }

    public void spend(Money amount) {
        if (this.wealth.getCurrency().equals(amount.getCurrency())) {
            this.wealth = this.wealth.subtract(amount);
        }
    }

    // getLocation provided by Individual

    // --- Relationship Management (V1 Spouses modernized) ---
    
    public Set<Person> getSpouses() {
        return Collections.unmodifiableSet(spouses);
    }
    
    public void addSpouse(Person spouse) {
        if (spouse != null) {
            spouses.add(spouse);
        }
    }
    
    public void removeSpouse(Person spouse) {
        spouses.remove(spouse);
    }
    
    // --- Location Management ---
    // Note: getLocation() is provided by Individual via Positioned<Place>
    
    public void move(Place newLocation) {
        Place oldLocation = getPosition();
        if (oldLocation != null) {
            oldLocation.removeInhabitant(this);
        }
        setPosition(newLocation);
        if (newLocation != null) {
            newLocation.addInhabitant(this);
        }
    }

    public Person addRole(String role) {
        roles.add(role);
        return this;
    }

    public void addRole(Role role) {
        if (!structuralRoles.contains(role)) {
            structuralRoles.add(role);
        }
    }

    public List<Role> getStructuralRoles() {
        return Collections.unmodifiableList(structuralRoles);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d years, %s)", getName(), getSex(), getAge(), nationality);
    }
}


