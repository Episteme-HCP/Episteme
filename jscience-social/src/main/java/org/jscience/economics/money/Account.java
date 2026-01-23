package org.jscience.economics.money;

import org.jscience.biology.Human;
import org.jscience.economics.Bank;
import org.jscience.economics.Property;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.Identified;
import org.jscience.util.Named;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A class representing a bank account on which you can store money or
 * properties on some things.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.1
 */
@Persistent
public class Account implements Property, Named, Identified<String> {
    private final Bank bank;
    private Set<Human> owners;
    
    @Id
    private final Identification identification;
    
    @Attribute
    private final String name;
    
    private Money amount;
    private Map<Share, Integer> shares;

    @Override
    public String getId() {
        return identification != null ? identification.getId() : name;
    }

    /**
     * Creates a new Account object.
     *
     * @param name           the account name
     * @param currency       the currency
     */
    public Account(String name, Currency currency) {
        this(null, new HashSet<>(), null, name, Money.of(0, currency));
    }

    /**
     * Creates a new Account object.
     *
     * @param bank           the bank holding this account
     * @param owners         the owners of this account
     * @param identification the account ID
     * @param name           the account name
     * @param amount         initial balance
     */
    public Account(Bank bank, Set<Human> owners, Identification identification,
                   String name, Money amount) {
        this(bank, owners, identification, name, amount, new HashMap<>());
    }

    /**
     * Creates a new Account object with shares.
     */
    public Account(Bank bank, Set<Human> owners, Identification identification,
                   String name, Money amount, Map<Share, Integer> shares) {
        this.bank = bank; // May be null for cash collections
        this.identification = identification;
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        if (name.isEmpty()) throw new IllegalArgumentException("Account name cannot be empty");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null");
        
        this.owners = owners != null ? new HashSet<>(owners) : new HashSet<>();
        this.shares = shares != null ? new HashMap<>(shares) : new HashMap<>();
    }

    public Bank getBank() {
        return bank;
    }

    public Set<Human> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public void addOwner(Human owner) {
        owners.add(Objects.requireNonNull(owner, "Owner cannot be null"));
    }

    public void removeOwner(Human owner) {
        if (owners.size() <= 1) {
            throw new IllegalArgumentException("Cannot remove last owner");
        }
        owners.remove(owner);
    }

    public void setOwners(Set<Human> owners) {
        this.owners = new HashSet<>(Objects.requireNonNull(owners, "Owners set cannot be null"));
        if (this.owners.isEmpty()) throw new IllegalArgumentException("Owners set cannot be empty");
    }

    @Override
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String getName() {
        return name;
    }

    public Money getValue() {
        return amount;
    }

    public Currency getCurrency() {
        return (Currency) amount.getUnit();
    }

    public void addAmount(Money amount) {
        this.amount = this.amount.add(amount);
    }

    public void subtractAmount(Money amount) {
        this.amount = this.amount.subtract(amount);
    }

    public boolean isEmpty() {
        return (amount.getValue().doubleValue() == 0) && (shares.isEmpty());
    }

    public Map<Share, Integer> getShares() {
        return Collections.unmodifiableMap(shares);
    }

    public void addShare(Share share, int quantity) {
        Objects.requireNonNull(share, "Share cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        shares.merge(share, quantity, Integer::sum);
    }

    public void removeShare(Share share, int quantity) {
        Objects.requireNonNull(share, "Share cannot be null");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        
        int current = shares.getOrDefault(share, 0);
        if (current < quantity) {
            throw new IllegalArgumentException("Not enough shares to remove");
        }
        if (current == quantity) {
            shares.remove(share);
        } else {
            shares.put(share, current - quantity);
        }
    }

    public void setShares(Map<Share, Integer> shares) {
        this.shares = new HashMap<>(Objects.requireNonNull(shares, "Shares map cannot be null"));
    }
}
