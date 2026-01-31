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
import java.time.Period;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a formal organization such as a company, NGO, or government agency.
 * Extends {@link Group} to include organizational metadata like sector, industry, and headquarters.
 * Supports hierarchical structure through departments (sub-organizations).
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Organization extends Group {

    private static final long serialVersionUID = 2L;

    /**
     * @deprecated Use {@link OrganizationSector} instead.
     */
    @Deprecated
    public enum Sector {
        /** @deprecated Use {@link OrganizationSector#PUBLIC} */
        @Deprecated PUBLIC, 
        /** @deprecated Use {@link OrganizationSector#PRIVATE} */
        @Deprecated PRIVATE, 
        /** @deprecated Use {@link OrganizationSector#NON_PROFIT} */
        @Deprecated NON_PROFIT, 
        /** @deprecated Use {@link OrganizationSector#GOVERNMENT} */
        @Deprecated GOVERNMENT, 
        /** @deprecated Use {@link OrganizationSector#ACADEMIC} */
        @Deprecated ACADEMIC, 
        /** @deprecated Use {@link OrganizationSector#MILITARY} */
        @Deprecated MILITARY;
        
        @Deprecated
        public OrganizationSector toOrganizationSector() {
            try {
                return OrganizationSector.valueOf(this.name());
            } catch (IllegalArgumentException e) {
                return OrganizationSector.OTHER;
            }
        }
    }

    @Attribute
    private final LocalDate foundedDate;
    
    @Attribute
    private OrganizationSector sector;
    
    @Attribute
    private String headquarters;
    
    @Attribute
    private long employeeCount;
    
    @Attribute
    private String industry;

    @Attribute
    private String missionStatement = "";

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Organization> departments = new ArrayList<>();

    /**
     * Creates a new organization.
     *
     * @param name        the name of the organization
     * @param foundedDate the date the organization was established
     * @param sector      the economic or social sector
     * @throws NullPointerException if any argument is null
     */
    public Organization(String name, LocalDate foundedDate, OrganizationSector sector) {
        super(name, GroupKind.ORGANIZATION);
        this.foundedDate = Objects.requireNonNull(foundedDate, "Founding date cannot be null");
        this.sector = Objects.requireNonNull(sector, "Sector cannot be null");
    }

    @Deprecated
    public Organization(String name, LocalDate foundedDate, Sector sector) {
        this(name, foundedDate, sector != null ? sector.toOrganizationSector() : OrganizationSector.OTHER);
    }

    /**
     * Returns the founding date.
     * @return the date
     */
    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    /**
     * Returns the organization's sector.
     * @return the sector
     */
    public OrganizationSector getSector() {
        return sector;
    }

    public void setSector(OrganizationSector sector) {
        this.sector = Objects.requireNonNull(sector);
    }

    @Deprecated
    public Sector getLegacySector() {
        try {
            return Sector.valueOf(sector.name());
        } catch (Exception e) {
            return Sector.PRIVATE;
        }
    }

    /**
     * Returns the headquarters location.
     * @return headquarters address or description
     */
    public String getHeadquarters() {
        return headquarters;
    }

    /**
     * Sets the headquarters location.
     * @param headquarters the location string
     */
    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    /**
     * Returns the current employee or member count.
     * @return employee count
     */
    public long getEmployeeCount() {
        return employeeCount;
    }

    /**
     * Sets the current employee or member count.
     * @param employeeCount count
     */
    public void setEmployeeCount(long employeeCount) {
        this.employeeCount = employeeCount;
    }

    /**
     * Returns the industry or field of operation.
     * @return industry string
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * Sets the industry or field of operation.
     * @param industry industry string
     */
    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getMissionStatement() {
        return missionStatement;
    }

    public void setMissionStatement(String missionStatement) {
        this.missionStatement = Objects.requireNonNull(missionStatement);
    }

    public void addDepartment(Organization department) {
        if (department != null && !departments.contains(department)) {
            departments.add(department);
        }
    }

    public List<Organization> getDepartments() {
        return Collections.unmodifiableList(departments);
    }

    /**
     * Calculates the age of the organization in years.
     * @return age in years
     */
    public int getAge() {
        return Period.between(foundedDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.format("Organization '%s' (%s, %s): %d employees, founded %d",
                getName(), sector, industry, employeeCount, foundedDate.getYear());
    }
}

