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

package org.episteme.social.economics;

import org.episteme.natural.biology.taxonomy.Species;
import org.episteme.natural.earth.Place;
import org.episteme.social.economics.money.Account;
import org.episteme.social.economics.money.Money;
import org.episteme.social.geography.BusinessPlace;
import org.episteme.social.sociology.OrganizationSector;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a formal social and economic entity (Company, NGO, Government).
 * Consolidates Economic and Sociological aspects.
 * Extends {@link SocialEntity} to participate in simulations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Organization extends Community implements Property {

    private static final long serialVersionUID = 2L;

    // Economic Attributes
    @Attribute
    private Money value;
    @Attribute
    private Money capital;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<EconomicAgent> owners; // Could be Persons or other Orgs

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Organigram organigram;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Account> accounts;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Organization> providers = new HashSet<>();
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Organization> clients = new HashSet<>();
    
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Resource> resources = new HashSet<>(); // Inventory

    // Sociological Attributes
    @Attribute
    private final LocalDate foundedDate;
    @Attribute
    private OrganizationSector sector;
    @Attribute
    private String industry;
    @Attribute
    private String headquarters;
    @Attribute
    private String missionStatement;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Organization> departments = new ArrayList<>();

    // Business Place (Location)
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private BusinessPlace place;

    /**
     * Full Constructor.
     */
    public Organization(String id, String name, LocalDate foundedDate, OrganizationSector sector) {
        super(new org.episteme.core.util.identity.UUIDIdentification(id), name, new Species("Organization", "Legal Entity"), null); 
        // Providing default species to satisfy Population contract
        
        this.foundedDate = (foundedDate != null) ? foundedDate : LocalDate.now();
        this.sector = (sector != null) ? sector : OrganizationSector.OTHER;
        
        this.owners = new HashSet<>();
        this.accounts = new HashSet<>();
        this.value = Money.usd(0);
        this.capital = Money.usd(0);
        this.organigram = new Organigram(name);
        this.missionStatement = "";
    }

    /**
     * Convenience Constructor.
     */
    public Organization(String name, OrganizationSector sector) {
        this(UUID.randomUUID().toString(), name, LocalDate.now(), sector);
        // Note: UUID logic might need to be compliant with PsychologicalGroup/SimulationEntity if PsychologicalGroup uses String ID.
    }

    /**
     * Constructor for Bank compatibility (modern API).
     */
    public Organization(String name, Place place, Money capital) {
        this(UUID.randomUUID().toString(), name, LocalDate.now(), OrganizationSector.OTHER);
        this.place = (BusinessPlace) (place instanceof BusinessPlace ? place : new BusinessPlace(place.getName(), place.getCenter())); 
        // Cast or wrap place. Ideally BusinessPlace should be used.
        this.capital = capital != null ? capital : Money.usd(0);
        this.value = this.capital;
    }

    /**
     * Constructor for Bank compatibility (detailed).
     */
    public Organization(String name, Identification id, Set<EconomicAgent> owners, Place place, Set<Account> accounts) {
        super(id, name, new Species("Organization", "Legal Entity"), place);
        this.foundedDate = LocalDate.now();
        this.sector = OrganizationSector.FINANCIAL; // Defaulting for Bank usage context
        this.owners = owners != null ? owners : new HashSet<>();
        this.place = (place instanceof BusinessPlace) ? (BusinessPlace) place : (place != null ? new BusinessPlace(place.getName(), place.getCenter()) : null);
        this.accounts = accounts != null ? accounts : new HashSet<>();
        this.capital = Money.usd(0);
        this.value = Money.usd(0);
        this.organigram = new Organigram(name);
        this.missionStatement = "";
    }
    
    // Legacy/native methods
    public Money getValue() { return value; }
    public void setValue(Money value) { this.value = value; }

    public Money getCapital() { return capital; }
    public void setCapital(Money capital) { this.capital = capital; }

    public Set<EconomicAgent> getOwners() { return Collections.unmodifiableSet(owners); }
    public void addOwner(EconomicAgent owner) { owners.add(owner); }
    
    public Set<Resource> getResources() { return resources; }
    public void setResources(Set<Resource> resources) { this.resources = resources; }

    // Sociological Methods

    public LocalDate getFoundedDate() { return foundedDate; }
    public OrganizationSector getSector() { return sector; }
    public void setSector(OrganizationSector sector) { this.sector = sector; }
    
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    public void addDepartment(Organization dept) { departments.add(dept); }
    public List<Organization> getDepartments() { return Collections.unmodifiableList(departments); }

    public Organigram getOrganigram() {
        return organigram;
    }

    @Override
    public String toString() {
        return String.format("Organization[%s (%s), %s, Cap: %s]", getName(), sector, industry, capital);
    }
}

