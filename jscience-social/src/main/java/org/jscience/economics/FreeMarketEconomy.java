package org.jscience.economics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class representing a society with money (goods are not exchanged from hand to hand but using the medium of money). Especially, it defines the complete flow from raw materials to a product with a unique serial number.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//it is highly expected that you provide alternate implementations to this class
public class FreeMarketEconomy extends Economy {

    public FreeMarketEconomy(Set orgs, Bank centralBank) {

        super(orgs, centralBank);

    }

    //here is what could be done:
    //remove all organizations with capital well under 0 as they are bankrupt
    //central bank creates money
    //organizations gather capital from banks
    //organizations buy resources
    //resources are transformed into products according to work that is done
    //organizations sell to consumers
    //capital is given back to banks
    //may be new organizations are added to the system
    //
    //this is really the raw mechanism from which considerable variation may be observed in real cases
    //
    //although we step from dt, we don't use that value
    //all calls are therefore meant to be atomic
    public void step(double dt) {

        Iterator iterator;
        double result;
        Organization organization;
        Organization otherOrganization;
        HashSet tempSet;
        Resource resource;
        double value;

        //remove all Organizations with capital well under 0
        iterator = getOrganizations().iterator();
        result = 0;
        tempSet = new HashSet();
        while (iterator.hasNext()) {
            organization = (Organization) iterator.next();
            if (organization.getCapital() < 0) {
                tempSet.add(organization);
            }
        }
        getOrganizations().removeAll(tempSet);

        //organizations buy resources
        //organizations sell to consumers
        iterator = getOrganizations().iterator();
        while (iterator.hasNext()) {
            organization = (Organization) iterator.next();
            value = Math.floor(30 * Math.random());//buy 15 resources
            for (int i = 0; i < value; i++) {
                resource = getAllresources()
                XXXXXXXXXXXXX
                organization.buyResource(resource);
                work = factory.getWork();
                XXXXXXX          //resources are transformed into products according to work that is done
                otherOrganization.sellProduct(resource);
            }
        }

        //capital is given back to banks
        iterator = getOrganizations().iterator();
        while (iterator.hasNext()) {
            organization = (Organization) iterator.next();
        }

        //may be new organizations are added to the system
        //bank create money
        //factories gather capital
        value = Math.floor(2 * Math.random());//create 1 new bank
        for (int i = 0; i < value; i++) {
            organization = new Bank(new String(Math.random()));//random name generator used
            getOrganizations().add(organization);
            getCentralBank().createMoney(organization, 10000);
            organization.addCapital(10000);
        }
        value = Math.floor(10 * Math.random());//create 5 new factories
        for (int i = 0; i < value; i++) {
            organization = new Factory(new String(Math.random()));//random name generator used
            getOrganizations().add(organization);
            getCentralBank().createMoney(organization, 10000);
            organization.addCapital(10000);
        }

    }

}
