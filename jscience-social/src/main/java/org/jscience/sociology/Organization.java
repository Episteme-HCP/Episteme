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

import java.time.LocalDate;

/**
 * Represents an organization (company, NGO, government agency, etc.).
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
