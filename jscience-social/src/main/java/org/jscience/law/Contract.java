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
package org.jscience.law;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents a legal contract between parties.
 * <p>
 * Modernized from v1 with support for contract lifecycle and terms.
 * </p>
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Contract {

    public enum Type {
        EMPLOYMENT, SERVICE, SALES, LEASE, LICENSE, PARTNERSHIP,
        NON_DISCLOSURE, INSURANCE, LOAN, WARRANTY
    }

    public enum Status {
        DRAFT, PENDING_SIGNATURE, ACTIVE, SUSPENDED, TERMINATED, EXPIRED
    }

    private final String id;
    private final Type type;
    private final String firstParty;
    private final String secondParty;
    private final LocalDate effectiveDate;
    private LocalDate expirationDate;
    private Status status;
    private final List<String> terms = new ArrayList<>();
    private final Map<String, Object> metadata = new HashMap<>();

    public Contract(String id, Type type, String firstParty, String secondParty,
            LocalDate effectiveDate) {
        this.id = id;
        this.type = type;
        this.firstParty = firstParty;
        this.secondParty = secondParty;
        this.effectiveDate = effectiveDate;
        this.status = Status.DRAFT;
    }

    // Getters
    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getFirstParty() {
        return firstParty;
    }

    public String getSecondParty() {
        return secondParty;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public Status getStatus() {
        return status;
    }

    public List<String> getTerms() {
        return Collections.unmodifiableList(terms);
    }

    // Setters
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addTerm(String term) {
        terms.add(term);
    }

    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        return metadata.get(key);
    }

    /**
     * Checks if contract is currently active.
     */
    public boolean isActive() {
        if (status != Status.ACTIVE)
            return false;
        LocalDate today = LocalDate.now();
        if (today.isBefore(effectiveDate))
            return false;
        if (expirationDate != null && today.isAfter(expirationDate))
            return false;
        return true;
    }

    /**
     * Signs and activates the contract.
     */
    public void sign() {
        if (status == Status.DRAFT || status == Status.PENDING_SIGNATURE) {
            status = Status.ACTIVE;
        }
    }

    /**
     * Terminates the contract.
     */
    public void terminate() {
        status = Status.TERMINATED;
    }

    /**
     * Returns duration of contract in days (or -1 if no expiration).
     */
    public long getDurationDays() {
        if (expirationDate == null)
            return -1;
        return java.time.temporal.ChronoUnit.DAYS.between(effectiveDate, expirationDate);
    }

    @Override
    public String toString() {
        return String.format("Contract[%s] %s between %s and %s (%s)",
                id, type, firstParty, secondParty, status);
    }
}
