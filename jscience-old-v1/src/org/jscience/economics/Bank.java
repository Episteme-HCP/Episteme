package org.jscience.economics;

import org.jscience.economics.money.Account;
import org.jscience.economics.money.ChangeSource;
import org.jscience.economics.money.Currency;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a bank.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//banks give away money for organizations to have more capital
//banks get money back from organizations and usually ask for more than was actually given
public class Bank extends Organization implements ChangeSource {
    /** DOCUMENT ME! */
    private Set clientAccounts;

/**
     * Creates a new Bank object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param owners         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     */
    public Bank(String name, Identification identification, Set owners,
        BusinessPlace place, Set accounts) {
        super(name, identification, owners, place, accounts);
        clientAccounts = Collections.EMPTY_SET;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getClientAccounts() {
        return clientAccounts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param account DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addClientAccount(Account account) {
        if ((account != null) && (account.getBank().equals(this))) {
            clientAccounts.add(account);
        } else {
            throw new IllegalArgumentException("You can't add a null Account.");
        }
    }

    //or close account (you should first be sure the account is empty (this is unchecked))
    /**
     * DOCUMENT ME!
     *
     * @param account DOCUMENT ME!
     */
    public void removeClientAccount(Account account) {
        clientAccounts.remove(account);
    }

    /**
     * DOCUMENT ME!
     *
     * @param accounts DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setClientAccounts(Set accounts) {
        Iterator iterator;
        boolean valid;
        Object currentElement;

        if (accounts != null) {
            valid = true;
            iterator = accounts.iterator();

            while (iterator.hasNext() && valid) {
                currentElement = iterator.next();
                valid = (currentElement instanceof Account) &&
                    (((Account) currentElement).getBank().equals(this));
            }

            if (valid) {
                this.clientAccounts = accounts;
            } else {
                throw new IllegalArgumentException(
                    "The Set of Accounts should contain only Accounts from this bank.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of Accounts shouldn't be null.");
        }
    }

    //this method should be overridden to provide actual change
    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     * @param source DOCUMENT ME!
     * @param target DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getConverted(float amount, Currency source, Currency target) {
        if ((source != null) && (target != null)) {
            return amount;
        } else {
            throw new IllegalArgumentException(
                "You can't convert null Currencies.");
        }
    }
}
