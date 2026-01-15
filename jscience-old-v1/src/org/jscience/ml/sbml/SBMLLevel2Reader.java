package org.jscience.ml.sbml;

import org.jscience.ml.sbml.jep.JEP;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.*;

/**
 * Reads a SBML Level 2 file into a {@link Model}.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 * @author Marc Vass
 */

public final class SBMLLevel2Reader extends DefaultHandler {
    private final static int NORMAL = 0;
    private final static int ANNOTATIONS = 1;
    private final static int NOTES = 2;
    private final static int RDF = 3;
    private final static int REACTIONMODE = 4;
    private final static int REACTIONMODEREACTANTS = 5;
    private final static int REACTIONMODEPRODUCTS = 6;
    private final static int REACTIONMODEKINETICLAW = 7;
    private final static int REACTIONMODEMODIFIERS = 8;

    private final static String NAMESPACE_MATHML = "http://www.w3.org/1998/Math/MathML";
    private final static String NAMESPACE_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private final static String NAMESPACE_SBML = "http://www.sbml.org/sbml/level2";

    private boolean inAssignmentBlock;
    private boolean inDelayBlock;
    private boolean inMathBlock;
    private boolean inTriggerBlock;
    private Map namespacePrefixToURI;
    private int lastReactionMode;
    private int mode;
    private Model model;
    private PrintStream errorStream;
    private Stack stack;

    private KineticLaw localKineticLaw;
    private SBase localElement;
    private SBase localInnerElement;
    private StringBuffer localText;

    public static Model read(String filename) throws Exception {
        return read(new BufferedReader(new FileReader(filename)));
    }

    private static String createAttributesText(Attributes attributes, Map namespacePrefixToURI) {
        int attributesCount = attributes.getLength();
        StringBuffer text = new StringBuffer("");
        if (attributesCount != 0)
            for (int attributesIndex = 0; attributesIndex < attributesCount; attributesIndex++)
                text.append(" " + attributes.getQName(attributesIndex) + "=\"" + attributes.getValue(attributesIndex) + "\"");
        if (namespacePrefixToURI != null)
            for (Iterator namespaceIterator = namespacePrefixToURI.entrySet().iterator(); namespaceIterator.hasNext();)
            {
                Map.Entry entry = (Map.Entry) namespaceIterator.next();
                String key = entry.getKey().toString().trim();
                text.append((key.length() == 0 ? " xmlns" : (" xmlns:" + key)) + "=\"" + entry.getValue() + "\"");
            }
        return text.toString();
    }

    private static String getParseExceptionInfo(SAXParseException spe) {
        return "[" + spe.getSystemId() + "] Line " + spe.getLineNumber() + ": " + spe.getMessage();
    }

    public static Model read(Reader reader) throws Exception {
        Model model = new Model();
        SBMLLevel2Reader sbml = new SBMLLevel2Reader(model);
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        XMLReader xmlReader = spf.newSAXParser().getXMLReader();
        xmlReader.setContentHandler(sbml);
        xmlReader.setErrorHandler(sbml);
        xmlReader.parse(new InputSource(reader));
        return model;
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        String text = new String(ch, start, length).trim();
        if (text.length() != 0)
            localText.append(text);
    }

    public void endElement(String namespace, String localName, String qName) throws SAXException {
        if (mode == RDF) {
            if (localName.equals("RDF") && namespace.equals(NAMESPACE_RDF)) {
                ((SBase) stack.peek()).setRDF(localText.toString() + "</rdf:RDF>");
                mode = localElement instanceof Reaction ? lastReactionMode : NORMAL;
            } else
                localText.append("</" + qName + ">");
        } else if (mode == NOTES) {
            if (localName.equals("notes") && namespace.equals(NAMESPACE_SBML)) {
                ((SBase) stack.peek()).getNotes().add(localText.toString() + "</notes>");
                mode = localElement instanceof Reaction ? lastReactionMode : NORMAL;
            } else
                localText.append("</" + qName + ">");
        } else if (mode == ANNOTATIONS) {
            if (localName.equals("annotation") && namespace.equals(NAMESPACE_SBML)) {
                ((SBase) stack.peek()).getAnnotations().add(localText.toString() + "</annotation>");
                mode = localElement instanceof Reaction ? lastReactionMode : NORMAL;
            } else
                localText.append("</" + qName + ">");
        } else if (namespace.equals(NAMESPACE_SBML))
            endSBMLElement(localName);
        else if (namespace.equals(NAMESPACE_MATHML)) {
            if (localName.equals("math")) {
                setTextOfMathElement(localText.toString() + "</math:math>\n");
                inMathBlock = false;
            } else
                localText.append(JEP.isMathMLFunction(localName) ? ("<math:" + localName + "/>\n") : ("</math:" + localName + ">\n"));
        } else
            throw new SAXException("Found unknown element [" + namespace + "]" + localName + ".");
    }

    public void error(SAXParseException spe) throws SAXException {
        String message = "Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException {
        String message = "Fatal Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }

    public void startElement(String namespace, String localName, String qName, Attributes attributes) throws SAXException {
        if (mode == NOTES || mode == ANNOTATIONS || mode == RDF)
            localText.append("<" + qName + createAttributesText(attributes, namespacePrefixToURI) + ">");
        else if (namespace.equals(NAMESPACE_SBML)) {
            if (inMathBlock)
                throw new SAXException("SBML tag " + localName + " encountered while in MathML mode.");
            startSBMLElement(localName, attributes);
        } else if (namespace.equals(NAMESPACE_MATHML)) {
            if (localName.equals("math")) {
                inMathBlock = true;
                localText = new StringBuffer("<math:math>\n");
            } else if (inMathBlock) {
                if (!JEP.isMathMLFunction(localName)) {
                    localText.append("<math:" + localName + createAttributesText(attributes, null) + ">");
                    if (!localName.equals("ci") && !localName.equals("cn"))
                        localText.append("\n");
                }
            } else
                throw new SAXException("Math tag " + localName + " encountered when not in MathML mode.");
        } else if (namespace.equals(NAMESPACE_RDF)) {
            if (inMathBlock)
                throw new SAXException("RDF tag " + localName + " encountered while in MathML mode.");
            if (localName.equals("RDF"))
                mode = RDF;
            else
                throw new SAXException("RDF tag " + localName + " encountered when not in RDF mode.");
            ((SBase) stack.peek()).setRDF("<rdf:RDF" + createAttributesText(attributes, namespacePrefixToURI) + ">");
        } else
            throw new SAXException("Found unknown element [" + namespace + "]" + localName + ".");
        namespacePrefixToURI.clear();
    }

    public void startPrefixMapping(String prefix, String uri) {
        namespacePrefixToURI.put(prefix, uri);
    }

    public void warning(SAXParseException spe) throws SAXException {
        errorStream.println("Warning: " + getParseExceptionInfo(spe));
    }

    private SBMLLevel2Reader(Model model) {
        this.model = model;
        errorStream = System.err;
        namespacePrefixToURI = new HashMap();
        stack = new Stack();
    }

    private void endSBMLElement(String localName) throws SAXException {
        if (localName.equals("functionDefinition"))
            model.getFunctionDefinitions().add(localElement);
        else if (localName.equals("compartment"))
            model.getCompartments().add(localElement);
        else if (localName.equals("parameter")) {
            if (mode == REACTIONMODEKINETICLAW) {
                localKineticLaw.getParameter().add(localInnerElement);
                localInnerElement = null;
            } else
                model.getParameters().add(localElement);
        } else if (localName.equals("reaction"))
            model.getReactions().add(localElement);
        else if (localName.equals("speciesReference")) {
            if (mode == REACTIONMODEREACTANTS)
                ((Reaction) (localElement)).getReactant().add(localInnerElement);
            else if (mode == REACTIONMODEPRODUCTS)
                ((Reaction) (localElement)).getProduct().add(localInnerElement);
            else
                throw new SAXException("Mode " + mode + " not allowed for speciesReference.");
            localInnerElement = null;
        } else if (localName.equals("modifierSpeciesReference")) {
            ((Reaction) (localElement)).getModifier().add(localInnerElement);
            localInnerElement = null;
        } else if (localName.equals("kineticLaw")) {
            ((Reaction) (localElement)).setKineticLaw(localKineticLaw);
            localKineticLaw = null;
        } else if (localName.equals("species"))
            model.getSpecies().add(localElement);
        else
        if (localName.equals("algebraicRule") || localName.equals("rateRule") || localName.equals("assignmentRule"))
            model.getRules().add(localElement);
        else if (localName.equals("unit")) {
            ((UnitDefinition) (localElement)).getUnits().add(localInnerElement);
            localInnerElement = null;
        } else if (localName.equals("unitDefinition"))
            model.getUnitDefinitions().add(localElement);
        else if (localName.equals("event"))
            model.getEvents().add(localElement);
        else if (localName.equals("eventAssignment")) {
            ((Event) (localElement)).getEventAssignment().add(localInnerElement);
            localInnerElement = null;
            inAssignmentBlock = false;
        } else if (localName.equals("delay")) {
            inDelayBlock = false;
            return;
        } else if (localName.equals("trigger")) {
            inTriggerBlock = false;
            return;
        }
        stack.pop();
    }

    private void setTextOfMathElement(String text) throws SAXException {
        if (localKineticLaw != null)
            localKineticLaw.setMath(text);
        else if (localElement == null)
            throw new SAXException("Cannot set math for a null element.");
        else if (localElement instanceof MathElement)
            ((MathElement) localElement).setMath(text);
        else if (localElement instanceof Event) {
            if (inDelayBlock)
                ((Event) localElement).setDelay(text);
            else if (inTriggerBlock)
                ((Event) localElement).setTrigger(text);
            else if (inAssignmentBlock) {
                EventAssignment assignment = (EventAssignment) localInnerElement;
                assignment.setMath(text);
            } else
                throw new SAXException("Can only set math for an event when in delay, trigger, or assignment.");
        } else if (localElement instanceof Reaction && localInnerElement instanceof SpeciesReference)
            ((StoichiometryMath) stack.peek()).setMath(text);
        else
            throw new SAXException("Cannot set math for SBML element " + localElement.getClass().getName() + ".");
    }

    private void startSBMLElement(String localName, Attributes attributes) throws SAXException {
        if (localName.equals("model")) {
            localElement = model;
            stack.push(localElement);
            model.setMetaid(attributes.getValue("metaid"));
            model.setId(attributes.getValue("id"));
            model.setName(attributes.getValue("name"));
        } else if (localName.equals("notes")) {
            mode = NOTES;
            localText = new StringBuffer("<notes" + createAttributesText(attributes, namespacePrefixToURI) + ">");
        } else if (localName.equals("annotation")) {
            mode = ANNOTATIONS;
            localText = new StringBuffer("<annotation" + createAttributesText(attributes, namespacePrefixToURI) + ">");
        } else if (localName.equals("delay"))
            inDelayBlock = true;
        else if (localName.equals("trigger"))
            inTriggerBlock = true;
        else if (localName.equals("unit")) {
            Unit unit = new Unit(UnitManager.findBaseUnit(attributes.getValue("kind")));
            localInnerElement = unit;
            stack.push(unit);
            unit.setMetaid(attributes.getValue("metaid"));
            if (attributes.getIndex("exponent") != -1)
                unit.setExponent(Integer.parseInt(attributes.getValue("exponent")));
            if (attributes.getIndex("multiplier") != -1)
                unit.setMultiplier(Double.parseDouble(attributes.getValue("multiplier")));
            if (attributes.getIndex("scale") != -1)
                unit.setScale(Integer.parseInt(attributes.getValue("scale")));
            if (attributes.getIndex("offset") != -1)
                unit.setOffset(Double.parseDouble(attributes.getValue("offset")));
        } else if (localName.equals("unitDefinition")) {
            localElement = new UnitDefinition(attributes.getValue("id"), attributes.getValue("name"));
            stack.push(localElement);
            ((UnitDefinition) localElement).setMetaid(attributes.getValue("metaid"));
        } else if (localName.equals("compartment")) {
            localElement = new Compartment();
            stack.push(localElement);
            ((Compartment) localElement).setMetaid(attributes.getValue("metaid"));
            ((Compartment) localElement).setId(attributes.getValue("id"));
            ((Compartment) localElement).setName(attributes.getValue("name"));
            if (attributes.getIndex("constant") != -1)
                ((Compartment) localElement).setConstant(Boolean.valueOf(attributes.getValue("constant")) == Boolean.TRUE);
            if (attributes.getIndex("size") != -1)
                ((Compartment) localElement).setSize(attributes.getValue("size"));
            if (attributes.getIndex("outside") != -1)
                ((Compartment) localElement).setOutside(attributes.getValue("outside"));
            if (attributes.getIndex("spatialDimensions") != -1)
                ((Compartment) localElement).setSpatialDimensions(attributes.getValue("spatialDimensions"));
            if (attributes.getIndex("units") != -1)
                ((Compartment) localElement).setUnits(attributes.getValue("units"));
        } else if (localName.equals("parameter")) {
            Parameter parameter = new Parameter();
            stack.push(parameter);
            if (mode == REACTIONMODEKINETICLAW)
                localInnerElement = parameter;
            else
                localElement = parameter;
            parameter.setMetaid(attributes.getValue("metaid"));
            parameter.setId(attributes.getValue("id"));
            parameter.setName(attributes.getValue("name"));
            if (attributes.getIndex("constant") != -1)
                parameter.setConstant(Boolean.valueOf(attributes.getValue("constant")) == Boolean.TRUE);
            if (attributes.getIndex("units") != -1)
                parameter.setUnits(attributes.getValue("units"));
            if (attributes.getIndex("value") != -1)
                parameter.setValue(Double.parseDouble(attributes.getValue("value")));
        } else if (localName.equals("species")) {
            localElement = new Species();
            stack.push(localElement);
            ((Species) localElement).setMetaid(attributes.getValue("metaid"));
            ((Species) localElement).setId(attributes.getValue("id"));
            ((Species) localElement).setName(attributes.getValue("name"));
            if (attributes.getIndex("boundaryCondition") != -1)
                ((Species) localElement).setBoundaryCondition(Boolean.valueOf(attributes.getValue("boundaryCondition")) == Boolean.TRUE);
            if (attributes.getIndex("charge") != -1)
                ((Species) localElement).setCharge(attributes.getValue("charge"));
            if (attributes.getIndex("compartment") != -1)
                ((Species) localElement).setCompartment(attributes.getValue("compartment"));
            if (attributes.getIndex("constant") != -1)
                ((Species) localElement).setConstant(Boolean.valueOf(attributes.getValue("constant")) == Boolean.TRUE);
            if (attributes.getIndex("hasOnlySubstanceUnits") != -1)
                ((Species) localElement).setHasOnlySubstanceUnits(Boolean.valueOf(attributes.getValue("hasOnlySubstanceUnits")) == Boolean.TRUE);
            if (attributes.getIndex("initialAmount") != -1)
                ((Species) localElement).setInitialAmount(Double.parseDouble(attributes.getValue("initialAmount")));
            if (attributes.getIndex("initialConcentration") != -1)
                ((Species) localElement).setInitialConcentration(Double.parseDouble(attributes.getValue("initialConcentration")));
            if (attributes.getIndex("spatialSizeUnits") != -1)
                ((Species) localElement).setSpatialSizeUnits(attributes.getValue("spatialSizeUnits"));
            if (attributes.getIndex("substanceUnits") != -1)
                ((Species) localElement).setSubstanceUnits(attributes.getValue("substanceUnits"));
        } else if (localName.equals("reaction")) {
            localElement = new Reaction();
            lastReactionMode = mode = REACTIONMODE;
            stack.push(localElement);
            ((Reaction) localElement).setMetaid(attributes.getValue("metaid"));
            ((Reaction) localElement).setId(attributes.getValue("id"));
            ((Reaction) localElement).setName(attributes.getValue("name"));
            if (attributes.getIndex("fast") != -1)
                ((Reaction) localElement).setFast(Boolean.valueOf(attributes.getValue("fast")) == Boolean.TRUE);
            if (attributes.getIndex("reversible") != -1)
                ((Reaction) localElement).setReversible(Boolean.valueOf(attributes.getValue("reversible")) == Boolean.TRUE);
        } else if (localName.equals("kineticLaw")) {
            localKineticLaw = new KineticLaw();
            lastReactionMode = mode = REACTIONMODEKINETICLAW;
            stack.push(localKineticLaw);
            localKineticLaw.setMetaid(attributes.getValue("metaid"));
            if (attributes.getIndex("substanceUnits") != -1)
                localKineticLaw.setSubstanceUnits(attributes.getValue("substanceUnits"));
            if (attributes.getIndex("timeUnits") != -1)
                localKineticLaw.setTimeUnits(attributes.getValue("timeUnits"));
        } else if (localName.equals("speciesReference")) {
            SpeciesReference reference = new SpeciesReference();
            localInnerElement = reference;
            stack.push(reference);
            reference.setMetaid(attributes.getValue("metaid"));
            if (attributes.getIndex("species") != -1)
                reference.setSpecies(attributes.getValue("species"));
            if (attributes.getIndex("stoichiometry") != -1)
                reference.setStoichiometry(Double.parseDouble(attributes.getValue("stoichiometry")));
        } else if (localName.equals("stoichiometryMath")) {
            StoichiometryMath math = new StoichiometryMath();
            ((SpeciesReference) stack.peek()).setStoichiometryMath(math);
            stack.push(math);
        } else if (localName.equals("modifierSpeciesReference")) {
            ModifierSpeciesReference reference = new ModifierSpeciesReference();
            localInnerElement = reference;
            stack.push(reference);
            reference.setMetaid(attributes.getValue("metaid"));
            if (attributes.getIndex("species") != -1)
                reference.setSpecies(attributes.getValue("species"));
        } else if (localName.equals("event")) {
            localElement = new Event();
            stack.push(localElement);
            ((Event) localElement).setMetaid(attributes.getValue("metaid"));
            ((Event) localElement).setId(attributes.getValue("id"));
            ((Event) localElement).setName(attributes.getValue("name"));
            if (attributes.getIndex("timeUnits") != -1)
                ((Event) localElement).setTimeUnits(attributes.getValue("timeUnits"));
        } else if (localName.equals("eventAssignment")) {
            EventAssignment assignment = new EventAssignment();
            localInnerElement = assignment;
            stack.push(assignment);
            inAssignmentBlock = true;
            assignment.setMetaid(attributes.getValue("metaid"));
            if (attributes.getIndex("variable") != -1)
                assignment.setVariable(attributes.getValue("variable"));
        } else if (localName.equals("algebraicRule")) {
            localElement = new AlgebraicRule();
            stack.push(localElement);
        } else if (localName.equals("assignmentRule")) {
            localElement = new AssignmentRule();
            stack.push(localElement);
            if (attributes.getIndex("variable") != -1)
                ((AssignmentRule) localElement).setVariable(attributes.getValue("variable"));
        } else if (localName.equals("rateRule")) {
            localElement = new RateRule();
            stack.push(localElement);
            if (attributes.getIndex("variable") != -1)
                ((RateRule) localElement).setVariable(attributes.getValue("variable"));
        } else if (localName.equals("sbml"))
            stack.push(model.getSbmlElement());
        else if (localName.equals("listOfEventAssignments"))
            stack.push(((Event) localElement).getAssignmentsElement());
        else if (localName.equals("listOfReactants")) {
            lastReactionMode = mode = REACTIONMODEREACTANTS;
            stack.push(((Reaction) localElement).getReactantsElement());
        } else if (localName.equals("listOfProducts")) {
            lastReactionMode = mode = REACTIONMODEPRODUCTS;
            stack.push(((Reaction) localElement).getProductsElement());
        } else if (localName.equals("listOfModifiers")) {
            lastReactionMode = mode = REACTIONMODEMODIFIERS;
            stack.push(((Reaction) localElement).getModifiersElement());
        } else if (localName.equals("listOfFunctionDefinitions")) {
            model.setFunctionDefinitions(new Vector());
            stack.push(model.getFunctionDefinitionsElement());
        } else if (localName.equals("listOfCompartments")) {
            model.setCompartments(new Vector());
            stack.push(model.getCompartmentsElement());
        } else if (localName.equals("listOfUnitDefinitions")) {
            stack.push(model.getUnitDefinitionsElement());
        } else if (localName.equals("listOfSpecies")) {
            model.setSpecies(new Vector());
            stack.push(model.getSpeciesElement());
        } else if (localName.equals("listOfParameters")) {
            if (mode == REACTIONMODEKINETICLAW)
                stack.push(((KineticLaw) stack.peek()).getParametersElement());
            else {
                model.setParameters(new Vector());
                stack.push(model.getParametersElement());
            }
        } else if (localName.equals("listOfRules")) {
            model.setRules(new Vector());
            stack.push(model.getRulesElement());
        } else if (localName.equals("listOfEvents")) {
            model.setEvents(new Vector());
            stack.push(model.getEventsElement());
        } else if (localName.equals("listOfReactions")) {
            model.setReactions(new Vector());
            stack.push(model.getReactionsElement());
        } else if (localName.equals("listOfUnits"))
            stack.push(((UnitDefinition) stack.peek()).getListOfUnitsElement());
        else if (localName.equals("functionDefinition")) {
            localElement = new FunctionDefinition();
            stack.push(localElement);
            ((FunctionDefinition) localElement).setId(attributes.getValue("id"));
            ((FunctionDefinition) localElement).setName(attributes.getValue("name"));
        } else
            throw new SAXException("Found unknown SBML element " + localName + ".");
    }
}
