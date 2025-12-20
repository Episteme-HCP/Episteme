package org.jscience.economics;

import org.jscience.sociology.Person;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a person working in an organization.
 */
public class Worker implements Serializable {

    private final Person person;
    private Organization organization;

    private String jobTitle;
    private Money salary;

    private Set<Worker> chiefs;
    private Set<Worker> subalterns;

    public Worker(Person person, Organization organization, String jobTitle, Money salary) {
        this.person = person;
        this.organization = organization;
        this.jobTitle = jobTitle;
        this.salary = salary;

        this.chiefs = new HashSet<>();
        this.subalterns = new HashSet<>();
    }

    public Person getPerson() {
        return person;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Money getSalary() {
        return salary;
    }

    public void setSalary(Money salary) {
        this.salary = salary;
    }

    public Set<Worker> getChiefs() {
        return chiefs;
    }

    public Set<Worker> getSubalterns() {
        return subalterns;
    }

    public void addSubaltern(Worker worker) {
        if (worker != this) {
            this.subalterns.add(worker);
            worker.getChiefs().add(this);
        }
    }

    public void removeSubaltern(Worker worker) {
        this.subalterns.remove(worker);
        worker.getChiefs().remove(this);
    }
}
