/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.sociology;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;


import org.episteme.natural.biology.BiologicalSex;
import org.episteme.social.economics.money.Money;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.UUIDIdentification;
import org.episteme.natural.engineering.eventdriven.EventDrivenEngine;

/**
 * Represents a human individual as a social agent.
 * Extends Human directly.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */

@Persistent
public class Person extends Human {

    private static final long serialVersionUID = 3L;

    @Attribute
    private String nationality;
    
    @Attribute
    private final List<Role> roles = new ArrayList<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Person> spouses = new HashSet<>();
    
    @Attribute
    private Money wealth;
    
    @Attribute
    private String address;

    @Attribute
    private Gender gender;
    
    // Constructors

    public Person(Identification id, String name, BiologicalSex sex, LocalDate birthDate, String nationality) {
        this(id, name, sex, birthDate, nationality, null);
    }

    public Person(Identification id, String name, BiologicalSex sex, LocalDate birthDate, String nationality, EventDrivenEngine engine) {
        super(id, sex, birthDate, engine); // Initialize Human with engine
        setName(name); // Set Name in Human/Individual
        this.nationality = (nationality != null) ? nationality : "Unknown";
        this.wealth = Money.usd(0);
        this.gender = Gender.fromSex(sex);
    }

    public Person(String id, String name, BiologicalSex sex, LocalDate birthDate, String nationality) {
        this(new UUIDIdentification(UUID.fromString(id)), name, sex, birthDate, nationality);
    }
    
    /**
     * Convenience constructor.
     */
    public Person(String name, BiologicalSex sex) {
        this(new UUIDIdentification(UUID.randomUUID()), name, sex, LocalDate.now().minusYears(20), "Unknown");
    }

    // Social Attributes

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public Money getWealth() {
        return wealth;
    }

    public void setWealth(Money wealth) {
        this.wealth = wealth;
    }

    public void earn(Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (this.wealth == null) this.wealth = amount;
        else if (this.wealth.getCurrency().equals(amount.getCurrency())) {
            this.wealth = this.wealth.add(amount);
        }
    }

    public void spend(Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (this.wealth != null && this.wealth.getCurrency().equals(amount.getCurrency())) {
            this.wealth = this.wealth.subtract(amount);
        }
    }

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
    
    public void addRole(Role role) {
        if (role != null && !roles.contains(role)) {
            roles.add(role);
        }
    }

    public void removeRole(Role role) {
        roles.remove(role);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%s (%s/%s, %d years, %s)", getName(), getSex(), gender, getAge(), nationality);
    }
}
