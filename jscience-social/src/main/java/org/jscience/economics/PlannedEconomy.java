package org.jscience.economics;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A class representing a concrete implementation of an Economy for planned economics.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//it is highly expected that you provide alternate implementations to this class
public class PlannedEconomy extends Economy {

    public PlannedEconomy(Set orgs, Bank centralBank) {

        super(orgs, centralBank);

    }

    //scheduler
    //you have to build up the scheduler yourselves as you have to decide what your society needs

    //here is what is done:
    //don't remove all organizations with capital well under 0 as they are supported by the central plan
    //central bank creates money
    //organizations are given away "capital" by the central plan thinkers
    //organizations "buy" resources or they are simply given to them by the central plan thinkers
    //resources are transformed into products according to work that is done
    //organizations sell to consumers or give away to them according to the plan
    //"capital" is given back to banks or not as central plans thinkers also own the banks
    //may be new organizations are added to the system
    //
    //this is really the raw mechanism from which considerable variation may be observed in real cases
    //
    //Our basic implementation is not really different from the FreeMarket system (except we don't remove bankrupt organizations).
    //You should consider an additional mechanism:
    //a more correct way to think about a planned economy may be to imagine a single StateOrganization
    //with branches in every industry, therefore exchanging resources internally within itself
    //(thus with no money or so called "sales" even if resources have a value)
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
        Factory factory;
        double value;

        //remove all Organizations with capital well under 0
        iterator = getOrganizations().iterator();
        result = 0;
        tempSet = new HashSet();
        while (iterator.hasNext()) {
            organization = (Organization) iterator.next();
            if (organization.getCapital() < -10000) {
                tempSet.add(organization);
            }
        }
        getOrganizations().removeAll(tempSet);

        //factories buy resources
        //factories sell to consumers
        iterator = getOrganizations().iterator();
        while (iterator.hasNext()) {
            organization = (Organization) iterator.next();
            value = Math.floor(30 * Math.random());//buy 15 resources
            for (int i = 0; i < value; i++) {
                resource = getAllresources()
                XXXXXXXXXXXXX
                organization.buyResource(resource);
                otherOrganization.sellProduct(resource);
            }
        }
        //resources are transformed into products according to work that is done
        iterator = getOrganizations().iterator();
        while (iterator.hasNext()) {
            organization = (Organization) iterator.next();
            if (organization instanceof Factory) {
                value = Math.floor(10 * Math.random());//do 5 works
                for (int i = 0; i < value; i++) {
                    factory = (Factory) organization;
                    work = factory.getWork();
                    XXXXXXX
                    factory.consumeResources(work);
                }
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
        value = Math.floor(20 * Math.random());//create 10 new consumers
        for (int i = 0; i < value; i++) {
            organization = new Consumer(new String(Math.random()));//random name generator used
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
