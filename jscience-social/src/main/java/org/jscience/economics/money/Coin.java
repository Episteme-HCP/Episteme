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

package org.jscience.economics.money;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

import java.io.Serializable;
import java.time.Instant;
import java.time.Year;
import java.util.Objects;

/**
 * Represents a physical coin or banknote.
 * <p>
 * Each coin has a unique identification (serial number), a face value,
 * and an emission date. Common denominations include:
 * <ul>
 *   <li>Euro: 1¢, 2¢, 5¢, 10¢, 20¢, 50¢, €1, €2</li>
 *   <li>USD: 1¢, 5¢, 10¢, 25¢, 50¢, $1</li>
 * </ul>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public final class Coin implements Identified<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private final Identification identification;

    @Attribute
    private final Instant emission;

    @Attribute
    private final Money value;

    @Attribute
    private final String mintMark;

    /**
     * Creates a new Coin.
     *
     * @param identification unique serial number
     * @param emission       the date of minting
     * @param value          the face value
     * @throws NullPointerException if any argument is null
     */
    public Coin(Identification identification, Instant emission, Money value) {
        this(identification, emission, value, null);
    }

    /**
     * Creates a new Coin with mint mark.
     *
     * @param identification unique serial number
     * @param emission       the date of minting
     * @param value          the face value
     * @param mintMark       the mint mark (e.g., "D" for Denver)
     */
    public Coin(Identification identification, Instant emission, Money value, String mintMark) {
        this.identification = Objects.requireNonNull(identification, "Identification cannot be null");
        this.emission = Objects.requireNonNull(emission, "Emission cannot be null");
        this.value = Objects.requireNonNull(value, "Value cannot be null");
        this.mintMark = mintMark;
    }

    /**
     * Creates a new Coin from amount and currency.
     *
     * @param value          numeric face value
     * @param identification unique serial number
     * @param emission       the date of minting
     * @param currency       the currency
     */
    public Coin(double value, Identification identification, Instant emission, Currency currency) {
        this(identification, emission, Money.valueOf(Real.of(value), currency));
    }

    @Override
    public String getId() {
        return identification.getId();
    }

    /**
     * Returns the coin identification.
     * @return the identification
     */
    @Override
    public Identification getIdentification() {
        return identification;
    }

    /**
     * Returns the emission date.
     * @return the emission instant
     */
    public Instant getEmission() {
        return emission;
    }

    /**
     * Returns the emission year.
     * @return the year
     */
    public Year getEmissionYear() {
        return Year.from(emission.atZone(java.time.ZoneOffset.UTC));
    }

    /**
     * Returns the face value.
     * @return the value
     */
    public Money getValue() {
        return value;
    }

    /**
     * Returns the currency.
     * @return the currency
     */
    public Currency getCurrency() {
        return value.getCurrency();
    }

    /**
     * Returns the mint mark.
     * @return the mint mark, or null if not specified
     */
    public String getMintMark() {
        return mintMark;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coin)) return false;
        Coin other = (Coin) obj;
        return Objects.equals(identification, other.identification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identification);
    }

    @Override
    public String toString() {
        String mark = mintMark != null ? " (" + mintMark + ")" : "";
        return String.format("Coin %s%s: %s, minted %s", 
            identification, mark, value, getEmissionYear());
    }

    // Factory methods for common coins

    /**
     * Creates a US penny.
     */
    public static Coin usPenny(Identification id, Instant emission) {
        return new Coin(id, emission, Money.valueOf(0.01, Currency.USD));
    }

    /**
     * Creates a US quarter.
     */
    public static Coin usQuarter(Identification id, Instant emission) {
        return new Coin(id, emission, Money.valueOf(0.25, Currency.USD));
    }

    /**
     * Creates a Euro coin.
     */
    public static Coin euro(Identification id, Instant emission, double value) {
        return new Coin(id, emission, Money.valueOf(value, Currency.EUR));
    }
}
