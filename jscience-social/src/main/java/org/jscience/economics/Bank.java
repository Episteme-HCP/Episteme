/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.economics;

import java.util.*;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Represents a financial institution (bank).
 * <p>
 * Modernized from v1 to use Real for precise financial calculations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Bank {

    public enum Type {
        COMMERCIAL, INVESTMENT, CENTRAL, SAVINGS, COOPERATIVE, ONLINE
    }

    private final String name;
    private final String swiftCode;
    private final Type type;
    private final String headquarters;
    private final List<Account> accounts = new ArrayList<>();
    private Real totalAssets;

    public Bank(String name, String swiftCode, Type type, String headquarters) {
        this.name = name;
        this.swiftCode = swiftCode;
        this.type = type;
        this.headquarters = headquarters;
        this.totalAssets = Real.ZERO;
    }

    public String getName() {
        return name;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public Type getType() {
        return type;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Real getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Real assets) {
        this.totalAssets = assets;
    }

    public Account openAccount(String accountNumber, String holderName, Currency currency) {
        Account account = new Account(accountNumber, holderName, this, currency);
        accounts.add(account);
        return account;
    }

    public void closeAccount(Account account) {
        accounts.remove(account);
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public int getAccountCount() {
        return accounts.size();
    }

    /**
     * Convert currency (simplified - uses 1:1 rate by default).
     */
    public Money convert(Money amount, Currency targetCurrency) {
        // In real implementation, would use exchange rates
        return new Money(amount.getAmount(), targetCurrency);
    }

    @Override
    public String toString() {
        return String.format("Bank '%s' (%s) [%s] - %d accounts",
                name, swiftCode, type, accounts.size());
    }

    /**
     * Represents a bank account.
     */
    public static class Account {
        private final String accountNumber;
        private final String holderName;
        private final Bank bank;
        private final Currency currency;
        private Real balance;

        public Account(String accountNumber, String holderName, Bank bank, Currency currency) {
            this.accountNumber = accountNumber;
            this.holderName = holderName;
            this.bank = bank;
            this.currency = currency;
            this.balance = Real.ZERO;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getHolderName() {
            return holderName;
        }

        public Bank getBank() {
            return bank;
        }

        public Currency getCurrency() {
            return currency;
        }

        public Real getBalance() {
            return balance;
        }

        public void deposit(Real amount) {
            if (amount.compareTo(Real.ZERO) > 0) {
                balance = balance.add(amount);
            }
        }

        public boolean withdraw(Real amount) {
            if (amount.compareTo(Real.ZERO) > 0 && balance.compareTo(amount) >= 0) {
                balance = balance.subtract(amount);
                return true;
            }
            return false;
        }

        public boolean transfer(Account target, Real amount) {
            if (withdraw(amount)) {
                target.deposit(amount);
                return true;
            }
            return false;
        }

        @Override
        public String toString() {
            return String.format("Account %s (%s) at %s: %s%.2f",
                    accountNumber, holderName, bank.getName(),
                    currency.getSymbol(), balance.doubleValue());
        }
    }

    // Notable banks
    public static Bank federalReserve() {
        Bank fed = new Bank("Federal Reserve", "FRNYUS33", Type.CENTRAL, "Washington D.C., USA");
        fed.setTotalAssets(Real.of(8_000_000_000_000.0)); // $8 trillion
        return fed;
    }

    public static Bank ecb() {
        Bank ecb = new Bank("European Central Bank", "ECBFDEFF", Type.CENTRAL, "Frankfurt, Germany");
        ecb.setTotalAssets(Real.of(8_500_000_000_000.0)); // €8.5 trillion
        return ecb;
    }
}
