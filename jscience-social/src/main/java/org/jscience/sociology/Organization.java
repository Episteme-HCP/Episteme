/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.sociology;

import java.time.LocalDate;

/**
 * Represents an organization (company, NGO, government agency, etc.).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Organization extends Group {

    public enum Sector {
        PUBLIC, PRIVATE, NON_PROFIT, GOVERNMENT, ACADEMIC, MILITARY
    }

    private final LocalDate foundedDate;
    private final Sector sector;
    private String headquarters;
    private long employeeCount;
    private String industry;

    public Organization(String name, LocalDate foundedDate, Sector sector) {
        super(name, Type.ORGANIZATION);
        this.foundedDate = foundedDate;
        this.sector = sector;
    }

    public LocalDate getFoundedDate() {
        return foundedDate;
    }

    public Sector getSector() {
        return sector;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public long getEmployeeCount() {
        return employeeCount;
    }

    public String getIndustry() {
        return industry;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public void setEmployeeCount(long employeeCount) {
        this.employeeCount = employeeCount;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public int getAge() {
        return java.time.Period.between(foundedDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return String.format("Organization '%s' (%s, %s): %d employees, founded %d",
                getName(), sector, industry, employeeCount, foundedDate.getYear());
    }

    // Notable organizations
    public static Organization unitedNations() {
        Organization un = new Organization("United Nations", LocalDate.of(1945, 10, 24), Sector.GOVERNMENT);
        un.setHeadquarters("New York, USA");
        un.setIndustry("International Relations");
        un.setEmployeeCount(44000);
        return un;
    }
}
