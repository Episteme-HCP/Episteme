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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jscience.biology.HomoSapiens;
import org.jscience.biology.Individual;
import org.jscience.economics.money.Money;
import org.jscience.geography.Place;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a human being with demographic and social attributes.
 * Extends the biological {@link Individual} to incorporate societal concepts
 * like nationality, roles, wealth, and marriage.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Person extends Individual implements org.jscience.geography.Locatable, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Gender categories for social modeling.
     */
    public enum Gender {
        MALE, FEMALE, OTHER, UNSPECIFIED;

        /**
         * Maps social gender to biological sex.
         * @return corresponding Individual.Sex
         */
        public Individual.Sex toSex() {
            switch (this) {
                case MALE: return Individual.Sex.MALE;
                case FEMALE: return Individual.Sex.FEMALE;
                default: return Individual.Sex.UNKNOWN;
            }
        }

        /**
         * Maps biological sex to social gender.
         * @param sex original biological sex
         * @return corresponding Gender
         */
        public static Gender fromSex(Individual.Sex sex) {
            if (sex == null) return UNSPECIFIED;
            switch (sex) {
                case MALE: return MALE;
                case FEMALE: return FEMALE;
                default: return UNSPECIFIED;
            }
        }
    }

    @Attribute
    private final String nationality;
    
    @Attribute
    private final List<String> roles = new ArrayList<>();
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Role> structuralRoles = new ArrayList<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Person> spouses = new HashSet<>();
    
    @Attribute
    private Money wealth;
    
    @Attribute
    private String address;

    /**
     * Creates a new person with full attributes.
     *
     * @param id          unique identifier
     * @param name        full name
     * @param sex         biological sex
     * @param birthDate   date of birth
     * @param nationality country of citizenship
     */
    public Person(String id, String name, Individual.Sex sex, LocalDate birthDate, String nationality) {
        super(id, HomoSapiens.SPECIES, sex, birthDate);
        setTrait("name", name);
        this.nationality = (nationality != null) ? nationality : "Unknown";
        this.wealth = Money.usd(0);
    }

    /**
     * Creates a new person using social Gender instead of biological Sex.
     */
    public Person(String id, String name, Gender gender, LocalDate birthDate, String nationality) {
        this(id, name, gender != null ? gender.toSex() : Individual.Sex.UNKNOWN, birthDate, nationality);
    }

    /**
     * Convenient constructor for a person with minimal info.
     * Generates a random ID and sets default age to 20.
     */
    public Person(String name, Individual.Sex sex) {
        this(UUID.randomUUID().toString(), name, sex, LocalDate.now().minusYears(20), "Unknown");
    }

    /**
     * Convenient constructor using social Gender.
     */
    public Person(String name, Gender gender) {
        this(name, gender != null ? gender.toSex() : Individual.Sex.UNKNOWN);
    }

    /**
     * Returns the person's gender based on their biological sex.
     * @return the gender
     */
    public Gender getGender() {
        return Gender.fromSex(getSex());
    }

    /**
     * Returns the person's nationality.
     * @return nationality string
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * Returns an unmodifiable list of roles assigned to this person.
     * @return roles list
     */
    public List<String> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    /**
     * Returns the current wealth of the person.
     * @return wealth as Money
     */
    public Money getWealth() {
        return wealth;
    }

    /**
     * Increases the person's wealth.
     * @param amount amount to add
     * @throws NullPointerException if amount is null
     */
    public void earn(Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (this.wealth.getCurrency().equals(amount.getCurrency())) {
            this.wealth = this.wealth.add(amount);
        }
    }

    /**
     * Decreases the person's wealth.
     * @param amount amount to subtract
     * @throws NullPointerException if amount is null
     */
    public void spend(Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (this.wealth.getCurrency().equals(amount.getCurrency())) {
            this.wealth = this.wealth.subtract(amount);
        }
    }

    /**
     * Returns the set of current and former spouses.
     * @return unmodifiable set of spouses
     */
    public Set<Person> getSpouses() {
        return Collections.unmodifiableSet(spouses);
    }

    /**
     * Registers a marriage or relationship partner.
     * @param spouse the spouse to add
     */
    public void addSpouse(Person spouse) {
        if (spouse != null) {
            spouses.add(spouse);
        }
    }

    /**
     * Removes a partner from the spouse list.
     * @param spouse the spouse to remove
     */
    public void removeSpouse(Person spouse) {
        spouses.remove(spouse);
    }

    /**
     * Relocates the person to a new place and updates inhabitant records.
     * @param newLocation the destination place
     */
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

    /**
     * Assigns a new role title to the person.
     * @param role the role to add
     * @return this person instance for chaining
     */
    public Person addRole(String role) {
        if (role != null) {
            roles.add(role);
        }
        return this;
    }

    /**
     * Registers a structural sociological role.
     * @param role the structural role
     */
    public void addStructuralRole(Role role) {
        if (role != null && !structuralRoles.contains(role)) {
            structuralRoles.add(role);
        }
    }

    /**
     * Removes a structural sociological role.
     * @param role the role to remove
     */
    public void removeStructuralRole(Role role) {
        structuralRoles.remove(role);
    }

    /**
     * Returns an unmodifiable list of structural roles.
     * @return structural roles list
     */
    public List<Role> getStructuralRoles() {
        return Collections.unmodifiableList(structuralRoles);
    }

    /**
     * Returns the physical address.
     * @return address string
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the physical address.
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d years, %s)", getName(), getSex(), getAge(), nationality);
    }
}
