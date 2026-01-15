package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


/**
 * This class defines a grouping of components - {@link MathDefinition}s,
 * {@link Compartment}s, {@link Species}, {@link Reaction}s, {@link
 * Parameter}s, {@link Event}s, {@link Rule}s, and {@link UnitDefinition}s.
 * This code is licensed under the DARPA BioCOMP Open Source License. See
 * LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class Model extends SBaseId {
    /** DOCUMENT ME! */
    private SBase compartmentsElement = new SBase();

    /** DOCUMENT ME! */
    private SBase eventsElement = new SBase();

    /** DOCUMENT ME! */
    private SBase functionDefinitionsElement = new SBase();

    /** DOCUMENT ME! */
    private SBase parametersElement = new SBase();

    /** DOCUMENT ME! */
    private SBase reactionsElement = new SBase();

    /** DOCUMENT ME! */
    private SBase rulesElement = new SBase();

    /** DOCUMENT ME! */
    private SBase sbmlElement = new SBase();

    /** DOCUMENT ME! */
    private SBase speciesElement = new SBase();

    /** DOCUMENT ME! */
    private SBase unitDefinitionsElement = new SBase();

    /** DOCUMENT ME! */
    private Vector compartments = new Vector();

    /** DOCUMENT ME! */
    private Vector events = new Vector();

    /** DOCUMENT ME! */
    private Vector functionDefinitions = new Vector();

    /** DOCUMENT ME! */
    private Vector parameters = new Vector();

    /** DOCUMENT ME! */
    private Vector reactions = new Vector();

    /** DOCUMENT ME! */
    private Vector rules = new Vector();

    /** DOCUMENT ME! */
    private Vector species = new Vector();

    /** DOCUMENT ME! */
    private List unitDefinitions = new ArrayList();

/**
     * Creates a new Model object.
     *
     * @param name DOCUMENT ME!
     */
    public Model(String name) {
        super(null, name);
    }

/**
     * Creates a new instance of Model
     */
    public Model() {
        this(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param compartment DOCUMENT ME!
     */
    public void addCompartment(Compartment compartment) {
        compartments.add(compartment);
    }

    /**
     * DOCUMENT ME!
     *
     * @param definition DOCUMENT ME!
     */
    public void addFunctionDefinition(FunctionDefinition definition) {
        functionDefinitions.add(definition);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parameter DOCUMENT ME!
     */
    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param reaction DOCUMENT ME!
     */
    public void addReaction(Reaction reaction) {
        reactions.add(reaction);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    public void addSpecies(Species s) {
        species.add(s);
    }

    /**
     * Getter for property compartments.
     *
     * @return Value of property compartments.
     */
    public Vector getCompartments() {
        return compartments;
    }

    /**
     * Getter for property compartmentsElement.
     *
     * @return Value of property compartmentsElement.
     */
    public SBase getCompartmentsElement() {
        return compartmentsElement;
    }

    /**
     * Getter for property events.
     *
     * @return Value of property events.
     */
    public Vector getEvents() {
        return events;
    }

    /**
     * Getter for property eventsElement.
     *
     * @return Value of property eventsElement.
     */
    public SBase getEventsElement() {
        return eventsElement;
    }

    /**
     * Getter for property functionDefinitions.
     *
     * @return Value of property functionDefinitions.
     */
    public Vector getFunctionDefinitions() {
        return functionDefinitions;
    }

    /**
     * Getter for property functionDefinitionsElement.
     *
     * @return Value of property functionDefinitionsElement.
     */
    public SBase getFunctionDefinitionsElement() {
        return functionDefinitionsElement;
    }

    /**
     * Getter for property parameters.
     *
     * @return Value of property parameters.
     */
    public Vector getParameters() {
        return parameters;
    }

    /**
     * Getter for property parametersElement.
     *
     * @return Value of property parametersElement.
     */
    public SBase getParametersElement() {
        return parametersElement;
    }

    /**
     * Getter for property reactions.
     *
     * @return Value of property reactions.
     */
    public Vector getReactions() {
        return reactions;
    }

    /**
     * Getter for property reactionsElement.
     *
     * @return Value of property reactionsElement.
     */
    public SBase getReactionsElement() {
        return reactionsElement;
    }

    /**
     * Getter for property rules.
     *
     * @return Value of property rules.
     */
    public Vector getRules() {
        return rules;
    }

    /**
     * Getter for property rulesElement.
     *
     * @return Value of property rulesElement.
     */
    public SBase getRulesElement() {
        return rulesElement;
    }

    /**
     * Getter for property sbmlElement.
     *
     * @return Value of property sbmlElement.
     */
    public SBase getSbmlElement() {
        return sbmlElement;
    }

    /**
     * Getter for property species.
     *
     * @return Value of property species.
     */
    public Vector getSpecies() {
        return species;
    }

    /**
     * Getter for property speciesElement.
     *
     * @return Value of property speciesElement.
     */
    public SBase getSpeciesElement() {
        return speciesElement;
    }

    /**
     * Getter for property unitDefinitions.
     *
     * @return Value of property unitDefinitions.
     */
    public List getUnitDefinitions() {
        return unitDefinitions;
    }

    /**
     * Getter for property unitDefinitionsElement.
     *
     * @return Value of property unitDefinitionsElement.
     */
    public SBase getUnitDefinitionsElement() {
        return unitDefinitionsElement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasLocalParameters() {
        for (int i = 0; i < reactions.size(); i++) {
            Reaction r = (Reaction) reactions.get(i);
            KineticLaw l = r.getKineticLaw();

            if (l.getParameter().size() > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Setter for property compartments.
     *
     * @param compartments New value of property compartments.
     */
    public void setCompartments(Vector compartments) {
        this.compartments = compartments;
    }

    /**
     * Setter for property compartmentsElement.
     *
     * @param compartmentsElement New value of property compartmentsElement.
     */
    public void setCompartmentsElement(SBase compartmentsElement) {
        this.compartmentsElement = compartmentsElement;
    }

    /**
     * Setter for property events.
     *
     * @param events New value of property events.
     */
    public void setEvents(Vector events) {
        this.events = events;
    }

    /**
     * Setter for property eventsElement.
     *
     * @param eventsElement New value of property eventsElement.
     */
    public void setEventsElement(SBase eventsElement) {
        this.eventsElement = eventsElement;
    }

    /**
     * Setter for property functionDefinitions.
     *
     * @param functionDefinitions New value of property functionDefinitions.
     */
    public void setFunctionDefinitions(Vector functionDefinitions) {
        this.functionDefinitions = functionDefinitions;
    }

    /**
     * Setter for property functionDefinitionsElement.
     *
     * @param functionDefinitionsElement New value of property
     *        functionDefinitionsElement.
     */
    public void setFunctionDefinitionsElement(SBase functionDefinitionsElement) {
        this.functionDefinitionsElement = functionDefinitionsElement;
    }

    /**
     * Setter for property parameters.
     *
     * @param parameters New value of property parameters.
     */
    public void setParameters(Vector parameters) {
        this.parameters = parameters;
    }

    /**
     * Setter for property parametersElement.
     *
     * @param parametersElement New value of property parametersElement.
     */
    public void setParametersElement(SBase parametersElement) {
        this.parametersElement = parametersElement;
    }

    /**
     * Setter for property reactions.
     *
     * @param reactions New value of property reactions.
     */
    public void setReactions(Vector reactions) {
        this.reactions = reactions;
    }

    /**
     * Setter for property reactionsElement.
     *
     * @param reactionsElement New value of property reactionsElement.
     */
    public void setReactionsElement(SBase reactionsElement) {
        this.reactionsElement = reactionsElement;
    }

    /**
     * Setter for property rules.
     *
     * @param rules New value of property rules.
     */
    public void setRules(Vector rules) {
        this.rules = rules;
    }

    /**
     * Setter for property rulesElement.
     *
     * @param rulesElement New value of property rulesElement.
     */
    public void setRulesElement(SBase rulesElement) {
        this.rulesElement = rulesElement;
    }

    /**
     * Setter for property sbmlElement.
     *
     * @param sbmlElement New value of property sbmlElement.
     */
    public void setSbmlElement(SBase sbmlElement) {
        this.sbmlElement = sbmlElement;
    }

    /**
     * Setter for property species.
     *
     * @param species New value of property species.
     */
    public void setSpecies(Vector species) {
        this.species = species;
    }

    /**
     * Setter for property speciesElement.
     *
     * @param speciesElement New value of property speciesElement.
     */
    public void setSpeciesElement(SBase speciesElement) {
        this.speciesElement = speciesElement;
    }

    /**
     * Setter for property unitDefinitionsElement.
     *
     * @param unitDefinitionsElement New value of property
     *        unitDefinitionsElement.
     */
    public void setUnitDefinitionsElement(SBase unitDefinitionsElement) {
        this.unitDefinitionsElement = unitDefinitionsElement;
    }

    /**
     * Get the SBML representation for this class.
     *
     * @return The class's SBML representation
     */
    public String toString() {
        StringBuffer s = new StringBuffer(sbmlElement.toString());
        s.append("<model");

        if (id != null) {
            s.append(" id=\"" + id + "\"");
        }

        if (name != null) {
            s.append(" name=\"" + name + "\"");
        }

        s.append(">\n");
        s.append(super.toString());
        printList(s, functionDefinitions,
            "<listOfFunctionDefinitions>" + functionDefinitionsElement,
            "</listOfFunctionDefinitions>");

        if (unitDefinitions.size() != 0) {
            s.append("<listOfUnitDefinitions>" + unitDefinitionsElement + "\n");

            for (Iterator iterator = unitDefinitions.iterator();
                    iterator.hasNext();) {
                SBase unit = (SBase) iterator.next();

                if (unit instanceof UnitDefinition) {
                    s.append(unit);
                }
            }

            s.append("</listOfUnitDefinitions>\n");
        }

        printList(s, compartments,
            "<listOfCompartments>" + compartmentsElement,
            "</listOfCompartments>");
        printList(s, species, "<listOfSpecies>" + speciesElement,
            "</listOfSpecies>");
        printList(s, parameters, "<listOfParameters>" + parametersElement,
            "</listOfParameters>");
        printList(s, rules, "<listOfRules>" + rulesElement, "</listOfRules>");
        printList(s, events, "<listOfEvents>" + eventsElement, "</listOfEvents>");
        printList(s, reactions, "<listOfReactions>" + reactionsElement,
            "</listOfReactions>");
        s.append("</model>\n");

        return s.toString();
    }
}
