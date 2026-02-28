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

package org.episteme.social.economics.money;

import org.episteme.social.economics.Organization;
import org.episteme.core.mathematics.numbers.real.Real;
import org.episteme.core.util.identity.ComprehensiveIdentification;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.persistence.Attribute;
import org.episteme.core.util.persistence.Id;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;
import org.episteme.social.history.time.TimeCoordinate;

import java.util.Objects;

/**
 * Represents a paper check as a form of payment.
 * <p>
 * A check is a written order directing a bank to pay money from the
 * emitter's account to the receiver.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class Check implements ComprehensiveIdentification {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification id;

    @Attribute
    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Account emitter;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Organization receiver;

    @Attribute
    private final TimeCoordinate emission;

    @Attribute
    private final Money value;

    /**
     * Creates a new Check.
     *
     * @param identification unique check number
     * @param emitter        the account issuing the check
     * @param receiver       the organization receiving payment
     * @param emission       the date/time of issue
     * @param value          the check amount
     * @throws NullPointerException if any argument is null
     */
    public Check(Identification id, Account emitter,
                 Organization receiver, TimeCoordinate emission, Money value) {
        this.id = Objects.requireNonNull(id, "Identification cannot be null");
        this.emitter = Objects.requireNonNull(emitter, "Emitter cannot be null");
        this.receiver = Objects.requireNonNull(receiver, "Receiver cannot be null");
        this.emission = Objects.requireNonNull(emission, "Emission date cannot be null");
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        setName("Check " + id);
    }

    /**
     * Creates a new Check from amount and currency.
     *
     * @param identification unique check number
     * @param emitter        the account issuing the check
     * @param receiver       the organization receiving payment
     * @param emission       the date/time of issue
     * @param amount         the numeric amount
     * @param currency       the currency
     */
    public Check(Identification identification, Account emitter,
                 Organization receiver, TimeCoordinate emission, double amount, Currency currency) {
        this(identification, emitter, receiver, emission, 
             Money.valueOf(Real.of(amount), currency));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the check identification.
     * @return the identification
     */
    public Identification getIdentification() {
        return id;
    }

    /**
     * Returns the emitter account.
     * @return the emitter
     */
    public Account getEmitter() {
        return emitter;
    }

    /**
     * Returns the receiver organization.
     * @return the receiver
     */
    public Organization getReceiver() {
        return receiver;
    }

    /**
     * Returns the emission date/time.
     * @return the emission instant
     */
    public TimeCoordinate getEmission() {
        return emission;
    }

    /**
     * Returns the check value.
     * @return the amount
     */
    public Money getValue() {
        return value;
    }

    /**
     * Returns the currency of this check.
     * @return the currency
     */
    public Currency getCurrency() {
        return value.getCurrency();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Check other)) return false;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Check %s from %s to %s: %s on %s",
            id, emitter.getName(), receiver.getName(), value, emission);
    }
}

