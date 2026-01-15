package org.jscience.medicine;

import org.jscience.economics.Organization;
import org.jscience.economics.money.Money;
import org.jscience.geography.Place;
import org.jscience.measure.Amount;
import org.jscience.measure.Identification;

import java.util.Date;


/**
 * The Treatment class provides some useful information for curing an
 * individual.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we could also take into account the fact that the same treatment may cure multiple pathologies
//also treatments work only for some species, and even better have to be adapted to specific individuals
//yet the aim of this class (used alone) is rather to describe the specific/generic treatment for a pathology
//see patient if you want to use actual treatments
//we should also take into account many fields and constants from the NCD
//also see http://en.wikipedia.org/wiki/International_Nonproprietary_Name and
//http://en.wikipedia.org/wiki/Anatomical_Therapeutic_Chemical_Classification_System
public class Treatment extends org.jscience.economics.resources.Object {
    /**
     * DOCUMENT ME!
     */
    private Pathology pathology;

    /**
     * DOCUMENT ME!
     */
    private String presentation;

    /**
     * DOCUMENT ME!
     */
    private String route;

    /**
     * DOCUMENT ME!
     */
    private String formula;

    /**
     * DOCUMENT ME!
     */
    private String dosage;

    /**
     * Creates a new Treatment object.
     *
     * @param organization   DOCUMENT ME!
     * @param name           DOCUMENT ME!
     * @param description    DOCUMENT ME!
     * @param value          DOCUMENT ME!
     * @param amount         DOCUMENT ME!
     * @param productionDate DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param pathology      DOCUMENT ME!
     * @param presentation   DOCUMENT ME!
     * @param route          DOCUMENT ME!
     * @param formula        DOCUMENT ME!
     * @param dosage         DOCUMENT ME!
     */
    public Treatment(String name,
                     String description, Amount amount, Organization organization, Place productionPlace,
                     Date productionDate, Identification identification, Amount<Money> value,
                     Pathology pathology, String presentation, String route, String formula,
                     String dosage) {
        super(name, description, amount, organization, productionPlace, productionDate,
                identification, value);

        if ((pathology != null) && (presentation != null) &&
                (presentation.length() > 0) && (route != null) &&
                (route.length() > 0) && (formula != null) &&
                (formula.length() > 0) && (dosage != null) &&
                (dosage.length() > 0)) {
            this.pathology = pathology;
            this.presentation = presentation;
            this.route = route;
            this.formula = formula;
            this.dosage = dosage;
        } else {
            throw new IllegalArgumentException(
                    "The Treatment constructor doesn't accept null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Pathology getPathology() {
        return pathology;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPresentation() {
        return presentation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRoute() {
        return route;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFormula() {
        return formula;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDosage() {
        return dosage;
    }
}
