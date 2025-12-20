/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.economics;

import java.time.LocalDate;
import java.util.*;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a worker/employee in an economic system.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Worker {

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, TEMPORARY, INTERN, FREELANCE
    }

    public enum Skill {
        UNSKILLED, SEMI_SKILLED, SKILLED, PROFESSIONAL, EXECUTIVE
    }

    private final String id;
    private final String name;
    private EmploymentType employmentType;
    private Skill skillLevel;
    private String occupation;
    private String employer;
    private LocalDate hireDate;
    private Money salary;
    private int hoursPerWeek;
    private final List<String> skills = new ArrayList<>();

    public Worker(String id, String name) {
        this.id = id;
        this.name = name;
        this.employmentType = EmploymentType.FULL_TIME;
        this.skillLevel = Skill.UNSKILLED;
        this.hoursPerWeek = 40;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public Skill getSkillLevel() {
        return skillLevel;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getEmployer() {
        return employer;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public Money getSalary() {
        return salary;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public List<String> getSkills() {
        return Collections.unmodifiableList(skills);
    }

    // Setters
    public void setEmploymentType(EmploymentType type) {
        this.employmentType = type;
    }

    public void setSkillLevel(Skill level) {
        this.skillLevel = level;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setSalary(Money salary) {
        this.salary = salary;
    }

    public void setHoursPerWeek(int hours) {
        this.hoursPerWeek = hours;
    }

    public void addSkill(String skill) {
        if (!skills.contains(skill)) {
            skills.add(skill);
        }
    }

    /**
     * Calculates years of experience at current employer.
     */
    public int getYearsOfExperience() {
        if (hireDate == null)
            return 0;
        return java.time.Period.between(hireDate, LocalDate.now()).getYears();
    }

    /**
     * Calculates hourly rate from annual salary.
     */
    public Money getHourlyRate() {
        if (salary == null || hoursPerWeek == 0)
            return null;
        double annualHours = hoursPerWeek * 52;
        Real hourlyAmount = salary.getAmount().divide(Real.of(annualHours));
        return new Money(hourlyAmount, salary.getCurrency());
    }

    /**
     * Checks if worker is considered full-time.
     */
    public boolean isFullTime() {
        return hoursPerWeek >= 35;
    }

    @Override
    public String toString() {
        return String.format("Worker[%s] %s - %s at %s (%s)",
                id, name, occupation, employer, employmentType);
    }
}
