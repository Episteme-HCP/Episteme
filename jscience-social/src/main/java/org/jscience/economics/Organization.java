package org.jscience.economics;

import org.jscience.biology.human.Human;
import org.jscience.biology.human.HumanSpecies;

import org.jscience.economics.money.Account;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Amount;
import org.jscience.measure.Identification;
import org.jscience.measure.Identified;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing an organization basic facts. It can be a company, a
 * familly... or an individual (consumer).
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//the clients are all the consumers/organizations buying resources from this organization
//and consumers/organizations themselves are in turn providers of resources
//nevertheless, organization keep specific records of their clients depending or their consuming habits
//often having a file of recent customers.
//also accounts for institutions, a very important acknowledged organization normally without competitors.
//this also could be a syndicate, a non profit organization (you have to subclass this class and provide support for members, which are in a way clients)
//an insurance company or whatever
//there are several reasons to subclass this class:
//we do not account for the fact that some resources might have decayed in the meantime:
//work will always be a success if it can and there will be no loss
//this may be an important factor for example in the food industry
//neither is the fact that the capital also decays for external causes (money value changes) or internal causes (you still have to pay for the place where the buildings are or electricity the basic price even if not used)
//also not taken into account is the fact that working takes time and some delays may occur (because some machines break or are already in use by another work, workers are on strike...)
//also, factories are able to do only a limited set of works (they are specialized) as the result of the knowledge of people that work inside and the avaialable machines are only able to do some works
//for example a factory which mines the soil is not able to cut the hair of people (although this seems to be an easy job)
//although there is support for accounts and worker, there is no money flow, and therefore your accounts are never decreased by buying resources or making people work
//we do not take into account that you actually need a "real" factory (buidlings and machines that have been removed from the capital to be used)
//and a "real" workforce (men that will get paid even if they don't work)
//finally, you can always buy a ressource, change the price and ressell it.
//(this is usually forbidden in many countries)
//moreover, you can always sell even if there is no one to buy.
//there is no underlying "intelligent production model" (in which you build only things people would buy):
//said in other words, is up to you to actually decide to do a work or another (an automatic model based on actual gain or loss could be used)
//you can use a factory subclass to define operational market where you actually trade real goods
//this class also accounts for money markets, stock exchange, bourse and the like
//we do not define workplaces here although a factory may be split actually into many places
public class Organization extends Community implements Property,
    Identified {
    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Identification identification; //the legal number

    /** DOCUMENT ME! */
    private Amount<Money> value;

    /** DOCUMENT ME! */
    private Set owners; //the people involved

    /** DOCUMENT ME! */
    private Organigram organigram;

    /** DOCUMENT ME! */
    private Set accounts;

    /** DOCUMENT ME! */
    private Amount<Money> capital;

    /** DOCUMENT ME! */
    private Set providers;

    /** DOCUMENT ME! */
    private Set clients;

    /**
     * Creates a new Organization object.
     *
     * @param name DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param owners DOCUMENT ME!
     * @param place DOCUMENT ME!
     * @param accounts DOCUMENT ME!
     */
    public Organization(String name, Identification identification, Set owners,
        BusinessPlace place, Set accounts) {
        super(new HumanSpecies(), place);

        Iterator iterator;
        boolean valid;

        if ((name != null) && (name.length() > 0) && (identification != null) &&
                (owners != null) && (accounts != null) &&
                (accounts.size() > 0)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                iterator = accounts.iterator();
                valid = true;

                while (iterator.hasNext() && valid) {
                    valid = iterator.next() instanceof Account;
                }

                if (valid) {
                    this.name = name;
                    this.identification = identification;
                    this.value = Amount.valueOf(0, Currency.USD);
                    this.owners = owners;
                    this.organigram = new Organigram(name, getIndividuals());
                    this.accounts = accounts;
                    this.capital = Amount.valueOf(0, Currency.USD);
                    this.providers = Collections.EMPTY_SET;
                    this.clients = Collections.EMPTY_SET;
                } else {
                    throw new IllegalArgumentException(
                        "The accounts Set must contain only Accounts.");
                }
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Organization constructor can't have null arguments and name and accounts can't be empty.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setName(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty name.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification getIdentification() {
        return identification;
    }

    /**
     * DOCUMENT ME!
     *
     * @param identification DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setIdentification(Identification identification) {
        if (identification != null) {
            this.identification = identification;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null identification.");
        }
    }

    //this is the price if sold by owners
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setValue(Amount<Money> value) {
        if (value != null) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("You can't set a null value.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOwners() {
        return owners;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOwner(Human owner) {
        if (owner != null) {
            owners.add(owner);
        } else {
            throw new IllegalArgumentException("You can't add a null owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     */
    public void removeOwner(Human owner) {
        if (owner != null) {
            //if (owners.contains(owner)) {
            //if (owners.size() > 1) {
            owners.remove(owner);

            // } else {
            //  throw new IllegalArgumentException("You can't remove last owner.");
            // }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owners DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOwners(Set owners) {
        Iterator iterator;
        boolean valid;

        //if ((owners != null) && (owners.size() > 0)) {
        if (owners != null) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.owners = owners;
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }

            //} else {
            //  throw new IllegalArgumentException("You can't set a null or empty owners set.");
            //}
        } else {
            throw new IllegalArgumentException(
                "You can't set a null owners set.");
        }
    }

    //may return null
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Organigram getOrganigram() {
        return organigram;
    }

    //should be the top element of the organigram
    /**
     * DOCUMENT ME!
     *
     * @param organigram DOCUMENT ME!
     */
    public void setOrganigram(Organigram organigram) {
        this.organigram = organigram;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getProviders() {
        return providers;
    }

    //you can add a provider even if you haven't actually bought anything from him
    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    public void addProvider(Organization organization) {
        if (organization != null) {
            providers.add(organization);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null Organization.");
        }
    }

    //you can remove a provider usually when you haven't bought anything for some time
    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    public void removeProvider(Organization organization) {
        providers.remove(organization);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getClients() {
        return clients;
    }

    //you can add a clients even if you haven't actually bought anything from him
    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    public void addClient(Organization organization) {
        if (organization != null) {
            clients.add(organization);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null Organization.");
        }
    }

    //you can remove a clients usually when you haven't bought anything for some time
    /**
     * DOCUMENT ME!
     *
     * @param organization DOCUMENT ME!
     */
    public void removeClient(Organization organization) {
        clients.remove(organization);
    }

    //the capital value is changed accordingly but NOT accounts
    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param otherParty DOCUMENT ME!
     * @param wantedResources DOCUMENT ME!
     */
    public void buyResources(Amount<Money> value, Organization otherParty,
        Set wantedResources) {
        Iterator iterator;
        boolean valid;
        Set currentResources;

        if ((value != null) && (otherParty != null) &&
                (wantedResources != null)) {
            valid = true;
            iterator = wantedResources.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Resource;
            }

            if (valid) {
                iterator = wantedResources.iterator();

                while (iterator.hasNext() && valid) {
                    valid = otherParty.getResources().contains(iterator.next());
                }

                if (valid) { //probably many useless calls here
                    currentResources = otherParty.getResources();
                    currentResources.removeAll(wantedResources);
                    otherParty.setResources(currentResources);
                    currentResources = getResources();
                    currentResources.addAll(wantedResources);
                    setResources(currentResources);
                    otherParty.getCapital().plus(value);
                    setCapital(getCapital().minus(value));
                } else {
                    throw new IllegalArgumentException(
                        "All wantedResources should be owned by otherParty.");
                }
            } else {
                throw new IllegalArgumentException(
                    "wantedResources should be a Set of Resources.");
            }
        } else {
            throw new IllegalArgumentException(
                "price, otherParty and wantedResources can't be null.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getCapital() {
        return capital;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setCapital(Amount<Money> value) {
        if (value!=null) {
            if (value.isGreaterThan(Amount.valueOf(0, Currency.USD))) {
          capital = value;
        } else throw new IllegalArgumentException("Capital must be greater than 0.");
        } else throw new IllegalArgumentException("You cannot set a null capital.");
      }

     //the money related issues
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAccounts() {
        return accounts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param account DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
        } else {
            throw new IllegalArgumentException("You can't add a null Account.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param account DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeAccount(Account account) {
        if (account != null) {
            if (accounts.contains(account)) {
                if (accounts.size() > 1) {
                    accounts.remove(account);
                } else {
                    throw new IllegalArgumentException(
                        "You can't remove last Account.");
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param accounts DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setAccounts(Set accounts) {
        Iterator iterator;
        boolean valid;

        if ((accounts != null) && (accounts.size() > 0)) {
            valid = true;
            iterator = accounts.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Account;
            }

            if (valid) {
                this.accounts = accounts;
            } else {
                throw new IllegalArgumentException(
                    "The Set of Accounts should contain only Accounts.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of Accounts shouldn't be null or empty.");
        }
    }

    //all elements should be workers and on the opposite all workers should be in the organigram
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getWorkers() {
/**
         * Set set; Iterator iterator; HashSet result; result = new HashSet();
         * if (organigram != null) { set = organigram.getAllChildren();
         * set.add(organigram); iterator = set.iterator(); while
         * (iterator.hasNext()) { result.addAll(((Organigram)
         * iterator.next()).getWorkers()); }} return result;
         */
        return organigram.getAllWorkers();
    }

    //perhaps we should also provide a setter/getter for country in which this organization is built
}
