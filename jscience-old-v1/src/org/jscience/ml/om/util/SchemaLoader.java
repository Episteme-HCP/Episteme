/* ====================================================================
 * /util/SchemaLoader.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */

package org.jscience.ml.om.util;


import org.jscience.ml.om.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


/**
 * The SchemaLoader provides loading facilities to
 * load (parse) a XML Schmea file.<br>
 * You can see this as a Factory of the Schema
 * Objects.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public class SchemaLoader {

    // ------------------
    // Instance Variables ------------------------------------------------
    // ------------------

    // Array of all obervations that have been found in the XML Document
    private IObservation[] observations = null;

    // Array of all session that have been found in the XML Document
    private ISession[] sessions = null;

    // Array of all targets that have been found in the XML Document    
    private ITarget[] targets = null;

    // Array of all observers that have been found in the XML Document    
    private IObserver[] observers = null;

    // Array of all sites that have been found in the XML Document    
    private ISite[] sites = null;

    // Array of all scopes that have been found in the XML Document    
    private IScope[] scopes = null;

    // Array of all eyepieces that have been found in the XML Document    
    private IEyepiece[] eyepieces = null;

    // Array of all imager that have been found in the XML Document    
    private IImager[] imagers = null;

    // ---------------------
    // Public Static Methods ---------------------------------------------
    // ---------------------

    // -------------------------------------------------------------------  

    /**
     * Gets a ITarget object (e.g. DeepSkyTarget??) from a given xsiType.
     *
     * @param xsiType     The unique xsi:Type that identifies the object/element
     * @param currentNode The XML Node that represents the object
     *                    e.g. <target>...</target>
     * @param observers   A array of Observers that are needed to instanciate
     *                    a object of type Target
     * @return A ITarget that represents the given node as Java object
     * @throws SchemaException if the given node is not well formed according
     *                         to the Schema specifications
     */
    public static ITarget getTargetFromXSIType(String xsiType,
                                               Node currentNode,
                                               IObserver[] observers) throws SchemaException {

        return (ITarget) SchemaLoader.getObjectFromXSIType(xsiType, currentNode, observers);

    }

    // -------------------------------------------------------------------  
    /**
     * Gets a IFinding object (e.g. DeepSkyFinding) from a given xsiType.
     *
     * @param xsiType     The unique xsi:Type that identifies the object/element
     * @param currentNode The XML Node that represents the object
     *                    e.g. <result>...</result>
     * @return A IFinding that represents the given node as Java object
     * @throws SchemaException if the given node is not well formed according
     *                         to the Schema specifications
     */
    public static IFinding getFindingFromXSIType(String xsiType,
                                                 Node currentNode) throws SchemaException {

        return (IFinding) SchemaLoader.getObjectFromXSIType(xsiType, currentNode, null);

    }

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------

    public IObservation[] getObservations() {

        return this.observations;

    }

    // -------------------------------------------------------------------
    public ISession[] getSessions() {

        return this.sessions;

    }

    // -------------------------------------------------------------------
    public ITarget[] getTargets() {

        return this.targets;

    }

    // -------------------------------------------------------------------
    public IObserver[] getObservers() {

        return this.observers;

    }

    // -------------------------------------------------------------------
    public ISite[] getSites() {

        return this.sites;

    }

    // -------------------------------------------------------------------
    public IScope[] getScopes() {

        return this.scopes;

    }

    // -------------------------------------------------------------------
    public IEyepiece[] getEyepieces() {

        return this.eyepieces;

    }

    // -------------------------------------------------------------------
    public IImager[] getImagers() {

        return this.imagers;

    }

    // -------------------------------------------------------------------
    /**
     * Loads/parses a XML File
     *
     * @param schemaFile The XML SchemaFile which should be parsed
     * @throws FGCAException   if schema File cannot be accessed
     * @throws SchemaException if XML File is not valid
     */
    public RootElement load(File xmlFile,
                            File schemaFile) throws FGCAException,
            SchemaException {

        // Check if file is OK
        if ((xmlFile == null)
                || !(xmlFile.exists())
                || (xmlFile.isDirectory())
                ) {
            throw new FGCAException("XML file is null, does not exist or is directory. ");
        }

        // Try to parse and validate file
        try {

            System.setProperty("javax.xml.parsers.DocumentBuilderFactory", "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            dbf.setNamespaceAware(true);
            dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            dbf.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", schemaFile.getAbsoluteFile());

            DocumentBuilder db = dbf.newDocumentBuilder();
            Validator handler = new Validator();
            db.setErrorHandler(handler);

            Document doc = db.parse(xmlFile);

            return this.load(doc);

        } catch (SAXException sax) {
            throw new SchemaException("Unable to parse xml file: " + xmlFile.getAbsolutePath(), sax);
        } catch (IOException ioe) {
            throw new FGCAException("Unable to access xml file: " + xmlFile.getAbsolutePath(), ioe);
        }

        catch (ParserConfigurationException pce) {
            throw new SchemaException("Parser configuration wrong: " + xmlFile.getAbsolutePath(), pce);
        }

    }


    // -------------------------------------------------------------------
    /**
     * Loads/parses a XML Document
     *
     * @param doc The XML Document which should be parsed
     * @throws FGCAException   if doc is <code>NULL</code> or empty
     * @throws SchemaException if XML File is not valid
     */
    public RootElement load(Document doc) throws FGCAException,
            SchemaException {

        // Check if document is OK
        if ((doc == null)
                || (!doc.hasChildNodes())
                ) {
            throw new FGCAException("XML Schema is NULL or has no child nodes. ");
        }

        Element rootElement = doc.getDocumentElement();

        // Get elements here
        // Don't change the sequence of retrieving the elements, or we might
        // run in dependecy problems!

        Node element = null;
        NodeList elementContainer = null;

        // --------- Observer -----------
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_OBSERVER_CONTAINER);
        if (elementContainer.getLength() != 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_OBSERVER_CONTAINER + " element. ");
        }
        element = elementContainer.item(0);
        observers = createObserverElements(element);

        // --------- Target -----------
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_TARGET_CONTAINER);
        if (elementContainer.getLength() != 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_TARGET_CONTAINER + " element. ");
        }
        element = elementContainer.item(0);
        targets = createTargetElements(element, observers);

        // --------- Site -----------
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_SITE_CONTAINER);
        if (elementContainer.getLength() != 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_SITE_CONTAINER + " element. ");
        }
        element = elementContainer.item(0);
        sites = createSiteElements(element);

        // --------- Scope -----------
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_SCOPE_CONTAINER);
        if (elementContainer.getLength() != 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_SCOPE_CONTAINER + " element. ");
        }
        element = elementContainer.item(0);
        scopes = createScopeElements(element);

        // --------- Eyepiece -----------
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_EYEPIECE_CONTAINER);
        if (elementContainer.getLength() != 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_EYEPIECE_CONTAINER + " element. ");
        }
        element = elementContainer.item(0);
        eyepieces = createEyepieceElements(element);

        // --------- Imager -----------
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_IMAGER_CONTAINER);
        if (elementContainer.getLength() > 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_IMAGER_CONTAINER + " element. ");
        } else if (elementContainer.getLength() == 1) {
            element = elementContainer.item(0);
            imagers = createImagerElements(element);
        }

        // --------- Session -----------        
        elementContainer = rootElement.getElementsByTagName(RootElement.XML_SESSION_CONTAINER);
        if (elementContainer.getLength() != 1) {
            throw new FGCAException("Schema XML can only have one " + RootElement.XML_SESSION_CONTAINER + " element. ");
        }
        element = elementContainer.item(0);
        sessions = createSessionElements(element);

        // --------- Observation -----------
        this.observations = createObservationElements(rootElement);

        RootElement obs = new RootElement();
        for (int i = 0; i < observations.length; i++) {
            obs.addObservation(observations[i]);
        }

        return obs;

    }

    // ----------------------
    // Private Static Methods --------------------------------------------
    // ----------------------

    // -------------------------------------------------------------------

    /**
     * Loads objects for a given xsiType via reflection
     *
     * @param xsiType     The xsiType that specifies the Object
     * @param currentNode The XML node that represents the Object
     *                    e.g. <target>...</target>
     * @param observers   Needed for Target Objects, can be <code>null</code>
     *                    for Findings
     */
    private static Object getObjectFromXSIType(String xsiType,
                                               Node currentNode,
                                               IObserver[] observers) throws SchemaException {

        // Resolve xsiType to java classname
        String classname = null;
        try {
            classname = ConfigLoader.getClassnameFromType(xsiType);
        } catch (ConfigException ce) {
            throw new SchemaException("Unable to get classname from xsi:type.\n" + ce.getMessage());
        }

        // Get Java class            
        Class currentClass = null;
        try {
            currentClass = Class.forName(classname);
        } catch (ClassNotFoundException cnfe) {
            throw new SchemaException("Unable to find class: " + classname + "\n" + cnfe.getMessage());
        }

        // Get constructors for class
        Constructor[] constructors = currentClass.getConstructors();
        Object object = null;
        if (constructors.length > 0) {
            try {
                Class[] parameters = null;
                for (int i = 0; i < constructors.length; i++) {
                    parameters = constructors[i].getParameterTypes();
                    if (observers == null) {   // create IFinding (Constructor has one parameter)
                        if ((parameters.length == 1)
                                && (parameters[0].isInstance(currentNode))
                                ) {
                            object = constructors[i].newInstance(new Object[]{currentNode});
                            break;
                        }
                    } else {                    // create ITarget (Constructor takes 2 parameters)
                        if ((parameters.length == 2)
                                && (parameters[0].isInstance(currentNode))
                                && (parameters[1].isInstance(observers))
                                ) {
                            object = constructors[i].newInstance(new Object[]{currentNode, observers});
                            break;
                        }
                    }
                }
            } catch (InstantiationException ie) {
                throw new SchemaException("Unable to instantiate class: " + classname + "\n" + ie.getMessage());
            } catch (InvocationTargetException ite) {
                throw new SchemaException("Unable to invocate class: " + classname + "\n" + ite.getMessage());
            } catch (IllegalAccessException iae) {
                throw new SchemaException("Unable to access class: " + classname + "\n" + iae.getMessage());
            }
        } else {
            throw new SchemaException("Unable to load class: " + classname + "\nMaybe class has no default constructor. ");
        }

        return object;

    }

    // ---------------
    // Private Methods ---------------------------------------------------
    // ---------------

    // -------------------------------------------------------------------        

    private IObservation[] createObservationElements(Node observations)
            throws SchemaException {

        Element e = (Element) observations;
        NodeList observationList = e.getElementsByTagName(IObservation.XML_ELEMENT_OBSERVATION);

        // Cannot use array here as loading of observation might fail (target loading might fail cause of XSI type, 
        // so this might cause observation loading to fail as well....
        ArrayList obs = new ArrayList(observationList.getLength());

        for (int i = 0; i < observationList.getLength(); i++) {

            try {
                obs.add(new Observation(observationList.item(i),
                        this.targets,
                        this.observers,
                        this.sites,
                        this.scopes,
                        this.sessions,
                        this.eyepieces,
                        this.imagers
                ));
            } catch (SchemaException se) {
                System.err.println(se + "\nContinue loading next observation...\n");
                continue;
            }

        }

        return (IObservation[]) obs.toArray(new IObservation[]{});

    }


    // -------------------------------------------------------------------
    private ITarget[] createTargetElements(Node targets, IObserver[] observers)
            throws SchemaException {

        Element e = (Element) targets;
        NodeList targetList = e.getElementsByTagName(ITarget.XML_ELEMENT_TARGET);

        // As loading of target might fail (unknown XSI type) we do not know the amount of successfuly loaded elements..
        ArrayList targetElements = new ArrayList(targetList.getLength());

        // Helper classes
        Node currentNode = null;
        Node attribute = null;

        for (int i = 0; i < targetList.getLength(); i++) {

            currentNode = targetList.item(i);

            // Get classname from xsi:type
            NamedNodeMap attributes = currentNode.getAttributes();
            if ((attributes != null)
                    && (attributes.getLength() != 0)
                    ) {
                attribute = attributes.getNamedItem(ITarget.XML_XSI_TYPE);
                if (attribute != null) {
                    String xsiType = attribute.getNodeValue();

                    Object object = null;
                    try {
                        object = SchemaLoader.getTargetFromXSIType(xsiType, currentNode, observers);
                    } catch (SchemaException se) {
                        System.err.println(se + "\nContinue with next target element...\n");
                        continue;
                    }
                    if (object != null) {
                        ITarget currentTarget = null;
                        if (object instanceof ITarget) {
                            currentTarget = (ITarget) object;
                            targetElements.add(currentTarget);
                        } else {
                            throw new SchemaException("Unable to load class of type: " + xsiType + "\nClass seems not to be of type ITarget. ");
                        }
                    } else {
                        throw new SchemaException("Unable to load class of type: " + xsiType);
                    }
                } else {
                    throw new SchemaException("No attribute specified: " + ITarget.XML_XSI_TYPE);
                }
            } else {
                throw new SchemaException("No attribute specified: " + ITarget.XML_XSI_TYPE);
            }


        }

        return (ITarget[]) targetElements.toArray(new ITarget[]{});
    }


    // -------------------------------------------------------------------
    private ISession[] createSessionElements(Node sessions)
            throws SchemaException {

        Element e = (Element) sessions;
        NodeList sessionList = e.getElementsByTagName(ISession.XML_ELEMENT_SESSION);

        ISession[] sessionElements = new ISession[sessionList.getLength()];

        for (int i = 0; i < sessionList.getLength(); i++) {
            sessionElements[i] = new Session(sessionList.item(i),
                    this.observers,
                    this.sites);
        }

        return sessionElements;

    }


    // -------------------------------------------------------------------
    private IObserver[] createObserverElements(Node observers)
            throws SchemaException {

        Element e = (Element) observers;

        NodeList observerList = e.getElementsByTagName(IObserver.XML_ELEMENT_OBSERVER);

        IObserver[] observerElements = new IObserver[observerList.getLength()];

        for (int i = 0; i < observerList.getLength(); i++) {
            observerElements[i] = new Observer(observerList.item(i));
        }

        return observerElements;

    }


    // -------------------------------------------------------------------    
    private ISite[] createSiteElements(Node sites)
            throws SchemaException {

        Element e = (Element) sites;

        NodeList siteList = e.getElementsByTagName(ISite.XML_ELEMENT_SITE);

        ISite[] siteElements = new ISite[siteList.getLength()];

        for (int i = 0; i < siteList.getLength(); i++) {
            siteElements[i] = new Site(siteList.item(i));
        }

        return siteElements;

    }


    // -------------------------------------------------------------------
    private IScope[] createScopeElements(Node scopes)
            throws SchemaException {

        Element e = (Element) scopes;

        NodeList scopeList = e.getElementsByTagName(IScope.XML_ELEMENT_SCOPE);

        IScope[] scopeElements = new IScope[scopeList.getLength()];

        for (int i = 0; i < scopeList.getLength(); i++) {
            scopeElements[i] = new Scope(scopeList.item(i));
        }

        return scopeElements;

    }


    // -------------------------------------------------------------------
    private IEyepiece[] createEyepieceElements(Node eyepieces)
            throws SchemaException {

        Element e = (Element) eyepieces;

        NodeList eyepieceList = e.getElementsByTagName(IEyepiece.XML_ELEMENT_EYEPIECE);

        IEyepiece[] eyepieceElements = new IEyepiece[eyepieceList.getLength()];

        for (int i = 0; i < eyepieceList.getLength(); i++) {
            eyepieceElements[i] = new Eyepiece(eyepieceList.item(i));
        }

        return eyepieceElements;

    }


    // -------------------------------------------------------------------
    private IImager[] createImagerElements(Node imagers)
            throws SchemaException {

        Element e = (Element) imagers;

        NodeList imagerList = e.getElementsByTagName(IImager.XML_ELEMENT_IMAGER);

        IImager[] imagerElements = new IImager[imagerList.getLength()];

        for (int i = 0; i < imagerList.getLength(); i++) {
            // @todo: This will fail if imager is not CCD
            imagerElements[i] = new CCDImager(imagerList.item(i));
        }

        return imagerElements;

    }

    // -------------
    // Inner Classes ----------------------------------------------------------
    // -------------

    private class Validator extends DefaultHandler {

        public void error(SAXParseException exception) throws SAXException {

            System.err.println("XML Schema error: " + exception);

        }

        public void fatalError(SAXParseException exception) throws SAXException {

            System.err.println("XML Schema fatal error: " + exception);

        }

        public void warning(SAXParseException exception) throws SAXException {

            System.out.println("XML Schema warning: " + exception);

        }

    }

}
