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
 * Represents an international treaty between nations.
 * * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Treaty {

    public enum Type {
        PEACE, TRADE, ALLIANCE, ENVIRONMENTAL, HUMAN_RIGHTS,
        ARMS_CONTROL, EXTRADITION, CULTURAL
    }

    public enum Status {
        NEGOTIATING, SIGNED, RATIFIED, IN_FORCE, SUSPENDED, TERMINATED
    }

    private final String name;
    private final Type type;
    private final LocalDate signedDate;
    private final List<String> signatories = new ArrayList<>();
    private Status status;
    private String depositaryOrg; // e.g., "United Nations"

    public Treaty(String name, Type type, LocalDate signedDate) {
        this.name = name;
        this.type = type;
        this.signedDate = signedDate;
        this.status = Status.SIGNED;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public LocalDate getSignedDate() {
        return signedDate;
    }

    public Status getStatus() {
        return status;
    }

    public String getDepositaryOrg() {
        return depositaryOrg;
    }

    public List<String> getSignatories() {
        return Collections.unmodifiableList(signatories);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDepositaryOrg(String org) {
        this.depositaryOrg = org;
    }

    public void addSignatory(String country) {
        if (!signatories.contains(country)) {
            signatories.add(country);
        }
    }

    public int getSignatoryCount() {
        return signatories.size();
    }

    public boolean isSignatory(String country) {
        return signatories.contains(country);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d) - %d signatories, %s",
                name, type, signedDate.getYear(), signatories.size(), status);
    }

    // Notable treaties
    public static Treaty unCharter() {
        Treaty t = new Treaty("Charter of the United Nations", Type.PEACE, LocalDate.of(1945, 6, 26));
        t.setStatus(Status.IN_FORCE);
        t.setDepositaryOrg("United Nations");
        return t;
    }

    public static Treaty parisAgreement() {
        Treaty t = new Treaty("Paris Agreement", Type.ENVIRONMENTAL, LocalDate.of(2015, 12, 12));
        t.setStatus(Status.IN_FORCE);
        t.setDepositaryOrg("United Nations");
        return t;
    }
}
