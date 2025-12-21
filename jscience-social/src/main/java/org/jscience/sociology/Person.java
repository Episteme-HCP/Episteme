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
package org.jscience.sociology;

import org.jscience.economics.Money;
import org.jscience.geography.Place;
import java.util.*;

/**
 * Represents a person with demographics and social attributes.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Person {

    public enum Gender {
        MALE, FEMALE, OTHER, UNSPECIFIED
    }

    private final String id;
    private final String name;
    private final Gender gender;
    private final java.time.LocalDate birthDate;
    private final String nationality;
    private final List<String> roles;
    private final List<Role> structuralRoles;

    // V1 Features
    private Money wealth;
    private Place location;

    public Person(String id, String name, Gender gender, java.time.LocalDate birthDate,
            String nationality) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.nationality = nationality;

        this.roles = new ArrayList<>();
        this.structuralRoles = new ArrayList<>();
        // Default initial state
        this.wealth = Money.usd(0);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public java.time.LocalDate getBirthDate() {
        return birthDate;
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

    public Place getLocation() {
        return location;
    }

    public void move(Place newLocation) {
        if (this.location != null) {
            this.location.removeInhabitant(this);
        }
        this.location = newLocation;
        if (this.location != null) {
            this.location.addInhabitant(this);
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

    public int getAge() {
        return java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d years, %s)", name, gender, getAge(), nationality);
    }
}