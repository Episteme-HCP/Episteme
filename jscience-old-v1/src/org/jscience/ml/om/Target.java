/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

/* ====================================================================
 * /Target.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;


import org.jscience.ml.om.util.SchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;


/**
 * The abstract class Target provides some common features that may be
 * used by the subclasses of an org.jscience.ml.om.ITarget.<br>
 * The Target class stores the name, alias names and the position of an
 * astronomical object. It also provides some basic access methods
 * for these attributes.
 * Additionally It implements a basic XML DOM
 * helper method that may be used by all subclasses that have to implement
 * the ITarget interface.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public abstract class Target extends SchemaElement implements ITarget {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    // Delimiter for alias name tokens
    private static final String ALIASNAMES_DELIMITER = ",";

    // ------------------
    // Instance Variables ------------------------------------------------
    // ------------------

    // Name of the astronomical object
    private String name = new String();

    // Alternative names of the astronomical object
    private List aliasNames = new LinkedList();

    // Celestial position of the astronomical object
    private EquPosition position = null;

    // Celestial constellation where the astronomical object can be found
    private String constellation = null;

    // Datasource that is the origin of this targets data (e.g. catalogue)
    private String dataSource = null;

    // If no Datasource is given, a observer has to be named that created this
    // target
    private IObserver observer = null;

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // ------------------------------------------------------------------- 

    /**
     * Constructs a new instance of a Target from a given DOM
     * target Element.<br>
     * Normally this constructor is called by a child class which itself
     * was called by org.jscience.ml.om.util.SchemaLoader.
     * Please mind that Target has to have a <observer> element, or a <datasource>
     * element. If a <observer> element is set, a array with Observers
     * must be passed to check, whether the <observer> link is valid.
     *
     * @param observers     Array of IObserver that might be linked from this observation,
     *                      can be <code>NULL</code> if datasource element is set
     * @param targetElement The origin XML DOM <target> Element
     * @throws SchemaException if given targetElement was <code>null</code>
     */
    protected Target(Node targetElement,
                     IObserver[] observers) throws SchemaException {

        if (targetElement == null) {
            throw new SchemaException("Given element is NULL. ");
        }

        Element child = null;
        NodeList children = null;

        Element target = (Element) targetElement;

        // Get mandatory ID
        String ID = target.getAttribute(SchemaElement.XML_ELEMENT_ATTRIBUTE_ID);
        if ((ID != null)
                && ("".equals(ID.trim()))
                ) {
            throw new SchemaException("Target must have a ID. ");
        }
        super.setID(ID);

        // Get mandatory name
        children = target.getElementsByTagName(Target.XML_ELEMENT_NAME);
        if ((children == null)
                || (children.getLength() != 1)
                ) {
            throw new SchemaException("Target must have exact one name. ");
        }
        child = (Element) children.item(0);
        String name = null;
        if (child == null) {
            throw new SchemaException("Target must have a name. ");
        } else {
            if (child.getFirstChild() != null) {
                name = child.getFirstChild().getNodeValue();  // Get name from text node                
            } else {
                throw new SchemaException("Target cannot have an empty name. ");
            }

            this.setName(name);
        }

        // Get datasource or observer             
        children = target.getElementsByTagName(ITarget.XML_ELEMENT_DATASOURCE);
        String datasource = null;
        if (children != null) {
            if (children.getLength() == 1) {
                child = (Element) children.item(0);
                datasource = child.getFirstChild().getNodeValue();
                this.setDatasource(datasource);
            } else if (children.getLength() > 1) {
                throw new SchemaException("Target can only have one datasource entry. ");
            }
        }

        children = target.getElementsByTagName(IObserver.XML_ELEMENT_OBSERVER);
        if (children != null) {
            if ((children.getLength() == 1)
                    && (datasource == null)
                    ) {
                child = (Element) children.item(0);
                String observerID = child.getFirstChild().getNodeValue();

                if ((observers != null)
                        && (observers.length > 0)
                        ) {
                    // Check if observer exists
                    boolean found = false;
                    for (int j = 0; j < observers.length; j++) {
                        if (observers[j].getID().equals(observerID)) {
                            found = true;
                            this.setObserver(observers[j]);
                            break;
                        }
                    }
                    if (found == false) {
                        throw new SchemaException("Target observer links to not existing observer element. ");
                    }
                } else {
                    throw new SchemaException("Passed IObserver array is empty or NULL. As no datasource is given, this is invalid. ");
                }
            } else if (children.getLength() > 1) {
                throw new SchemaException("Target can only have one observer entry. ");
            } else if (datasource == null) {
                throw new SchemaException("Target can only have a observer entry or a datasource entry. ");
            }
        } else if (datasource == null) {
            throw new SchemaException("Target must have datasource or observer specified as target origin. ");
        }

        // Get optional alias names
        children = target.getElementsByTagName(Target.XML_ELEMENT_ALIASNAME);
        if ((children != null)
                && (children.getLength() >= 1)
                ) {
            for (int j = 0; j < children.getLength(); j++) {
                this.addAliasName(children.item(j).getFirstChild().getNodeValue());
            }
        }

        // Get optional position
        children = target.getElementsByTagName(EquPosition.XML_ELEMENT_POSITION);
        EquPosition position = null;
        if (children != null) {
            if (children.getLength() == 1) {
                try {
                    position = new EquPosition(children.item(0));
                } catch (SchemaException schema) {
                    throw new SchemaException("Target cannot set position from element: " + name, schema);
                }
                this.setPosition(position);
            } else if (children.getLength() > 1) {
                throw new SchemaException("Target can only have one position. ");
            }
        }

        // Get optional constellation
        children = target.getElementsByTagName(ITarget.XML_ELEMENT_CONSTELLATION);
        String constellation = null;
        if (children != null) {
            if (children.getLength() == 1) {
                child = (Element) children.item(0);
                constellation = child.getFirstChild().getNodeValue();
                this.setConstellation(constellation);
            } else if (children.getLength() > 1) {
                throw new SchemaException("Target can only have one constellation. ");
            }
        }

    }


    // -------------------------------------------------------------------
    /**
     * Protected Constructor used by subclasses construction.
     *
     * @param name       The name of the astronomical object
     * @param datasource The datasource which is the origin of the
     *                   astronomical object
     * @throws IllegalArgumentException if name or datasource
     *                                  was <code>null</code>
     */
    protected Target(String name,
                     String datasource) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null. ");
        }

        if (datasource == null) {
            throw new IllegalArgumentException("Datasource cannot be null. ");
        }

        this.dataSource = datasource;

        this.name = name;

    }


    // -------------------------------------------------------------------
    /**
     * Protected Constructor used by subclasses construction.
     *
     * @param name     The name of the astronomical object
     * @param observer The observer which is the originator of the
     *                 astronomical object
     * @throws IllegalArgumentException if name or observer
     *                                  was <code>null</code>
     */
    protected Target(String name,
                     IObserver observer) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null. ");
        }

        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null. ");
        }

        this.observer = observer;

        this.name = name;

    }

    // -------------
    // SchemaElement -----------------------------------------------------
    // -------------

    // -------------------------------------------------------------------

    /**
     * Returns a display name for this element.<br>
     * The method differs from the toString() method as toString() shows
     * more technical information about the element. Also the formating of
     * toString() can spread over several lines.<br>
     * This method returns a string (in one line) that can be used as
     * displayname in e.g. a UI dropdown box.
     *
     * @return Returns a String with a one line display name
     * @see java.lang.Object.toString();
     */
    public String getDisplayName() {

        return this.name;

    }

    // -------
    // ITarget -----------------------------------------------------------
    // -------

    // -------------------------------------------------------------------

    /**
     * Adds this Target to a given parent XML DOM Element.
     * The Target element will be set as a child element of
     * the passed element.
     *
     * @param parent The parent element for this Target
     * @return Returns the element given as parameter with this
     *         Target as child element.<br>
     *         Might return <code>null</code> if parent was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public abstract Element addToXmlElement(Element element);


    // -------------------------------------------------------------------
    /**
     * Adds the target link to an given XML DOM Element
     * The target element itself will be attached to given elements
     * ownerDocument. If the ownerDocument has no target container, it will
     * be created.<br>
     * Example:<br>
     * &lt;parameterElement&gt;<br>
     * <b>&lt;targetLink&gt;123&lt;/targetLink&gt;</b><br>
     * &lt;/parameterElement&gt;<br>
     * <i>More stuff of the xml document goes here</i><br>
     * <b>&lt;targetContainer&gt;</b><br>
     * <b>&lt;target id="123"&gt;</b><br>
     * <i>target description goes here</i><br>
     * <b>&lt;/target&gt;</b><br>
     * <b>&lt;/targetContainer&gt;</b><br>
     * <br>
     *
     * @param parent The element under which the the target link is created
     * @return Returns the Element given as parameter with a additional
     *         target link, and the target element under the target container
     *         of the ownerDocument
     *         Might return <code>null</code> if element was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public Element addAsLinkToXmlElement(Element element) {

        if (element == null) {
            return null;
        }

        Document ownerDoc = element.getOwnerDocument();

        // Create the link element
        Element e_Link = ownerDoc.createElement(ITarget.XML_ELEMENT_TARGET);
        Node n_LinkText = ownerDoc.createTextNode(super.getID());
        e_Link.appendChild(n_LinkText);

        element.appendChild(e_Link);

        // Get container element
        Element container = null;
        boolean created = false;
        NodeList nodeList = ownerDoc.getElementsByTagName(RootElement.XML_TARGET_CONTAINER);
        if (nodeList.getLength() == 0) {  // we're the first element. Create container element
            container = ownerDoc.createElement(RootElement.XML_TARGET_CONTAINER);
            created = true;
        } else {
            container = (Element) nodeList.item(0);  // there should be only one container element
        }

        this.addToXmlElement(container);

        return element;

    }


    // -------------------------------------------------------------------
    /**
     * Adds a new alias name to the Target
     * e.g. name = M42 ; alias name = "Great Orion Nebulae"
     *
     * @param newAliasName A new alias name
     * @return Returns <code>true</code> if the alias name could be added.
     *         If <code>false</code> is returned the new alias was <code>null</code>
     *         or an empty String.
     */
    public boolean addAliasName(String newAliasName) {

        if (newAliasName == null
                || "".equals(newAliasName)
                ) {
            return false;
        }

        this.aliasNames.add(newAliasName);

        return true;

    }


    // -------------------------------------------------------------------
    /**
     * Sets an array of new alias names to this target.<br>
     * All current aliasNames will be deleted! If you want to add alias names
     * without deleting the existing ones, please use Target.addAliasNames(String) or
     * Target.addAliasName(String).<br>
     * If <code>null</code> is passed, the given alias names are deleted.
     *
     * @param newAliasNames An arry with new alias name
     */
    public void setAliasNames(String[] newAliasNames) {

        if ((newAliasNames == null)
                || (newAliasNames.length == 0)
                ) {
            this.aliasNames.clear();
            return;
        }

        this.aliasNames = Arrays.asList(newAliasNames);

    }


    // -------------------------------------------------------------------
    /**
     * Returns all alias names.<br>
     *
     * @return Returns a String array with all alias
     *         names. If no alias names were set <code>null</code>
     *         is returned.
     */
    public String[] getAliasNames() {

        if (aliasNames.size() == 0) {
            return null;
        }

        String[] result = new String[aliasNames.size()];

        int i = 0;
        ListIterator iterator = aliasNames.listIterator();
        while (iterator.hasNext()) {
            result[i++] = (String) iterator.next();
        }

        return result;

    }


    // -------------------------------------------------------------------
    /**
     * Removes a alias name from the target.<br>
     *
     * @param aliasName The alias name that should be removed
     * @return Returns <code>true</code> if the alias name
     *         could be removed from the target. If <code>false</code>
     *         is returned the given alias name could not be found
     *         in the targets alias name list or the parameter was
     *         <code>null<code> or contained a empty string.
     */
    public boolean removeAliasName(String aliasName) {

        if (aliasName == null
                || "".equals(aliasName)) {
            return false;
        }

        if (this.aliasNames.contains(aliasName)) {
            aliasNames.remove(aliasName);
            return true;
        }

        return false;

    }


    // -------------------------------------------------------------------
    /**
     * Returns the name of the target.<br>
     * The name should clearly identify the astronomical
     * object. Use alias names for colloquial names of
     * the object.
     *
     * @return Returns the name of the astronomical
     *         object
     */
    public String getName() {

        return name;

    }


    // -------------------------------------------------------------------
    /**
     * Returns the celestial constellation, where the target can be found.<br>
     * Might return <code>NULL</code> if constellation was never set
     *
     * @return The celestial constellation
     */
    public String getConstellation() {

        return this.constellation;

    }


    // -------------------------------------------------------------------
    /**
     * Sets the celestial constellation, where the target can be found.<br>
     *
     * @param constellation The celestial constellation of the target
     */
    public void setConstellation(String constellation) {

        this.constellation = constellation;

    }


    // -------------------------------------------------------------------
    /**
     * Sets the name of the target.<br>
     * The name should clearly identify the astronomical
     * object. For alternative names of the object add a
     * new alias name.<br>
     * If a name is already set to the target, the old name
     * will be overwritten with new new name.
     *
     * @return The name of the target
     * @throws IllegalArgumentException if name was <code>null</code>
     */
    public void setName(String name) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("name cannot be null. ");
        }

        this.name = name;

    }


    // -------------------------------------------------------------------
    /**
     * Returns the position of the target.<br>
     * The position of the target describes the location of
     * the astronomical object in any popular celestial
     * coordination system.
     *
     * @return The celestial position of the astronomical object
     *         Might return <code>null</code> if position was never set.
     */
    public EquPosition getPosition() {

        return position;

    }


    // -------------------------------------------------------------------
    /**
     * Returns the observer who is the originator of the target.<br>
     *
     * @return The observer who is the originator of this target.
     *         Might return <code>null</code> if observer was never set. (In this
     *         case a dataSource must exist)
     */
    public IObserver getObserver() {

        return this.observer;

    }


    // -------------------------------------------------------------------
    /**
     * Returns the datasource which is the origin of the target.<br>
     *
     * @return The datasource which is the origin of this target
     *         Might return <code>null</code> if datasource was never set. (In this
     *         case a observer must exist)
     */
    public String getDatasource() {

        return this.dataSource;

    }


    // -------------------------------------------------------------------
    /**
     * Sets the position of the target.<br>
     * The position of the target describes the location of
     * the astronomical object in a popular celestial
     * coordination system.
     *
     * @param position The position of the astronomical object
     *                 in a popular coordination system
     */
    public void setPosition(EquPosition position) {

        this.position = position;

    }


    // -------------------------------------------------------------------
    /**
     * Sets the datasource of the target.<br>
     *
     * @param datasource The datasource of the astronomical object
     */
    public void setDatasource(String datasource) {

        if (datasource != null) {
            this.observer = null;
            this.dataSource = datasource;
        }

    }


    // -------------------------------------------------------------------
    /**
     * Sets the observer who is the originator of the target.<br>
     *
     * @param observer The observer who is the originator of this target
     */
    public void setObserver(IObserver observer) {

        if (observer != null) {
            this.dataSource = null;
            this.observer = observer;
        }

    }

    // -----------------
    // Protected methods -------------------------------------------------
    // -----------------

    // -------------------------------------------------------------------

    /**
     * Creates an XML DOM Element for the Target.
     * The new Target element will be added as child
     * element to an given parent element.
     * The given parent element should be the target container.
     * All specialised subclasses may use this method to
     * create a Target element under which they may store
     * their addition data.<br>
     * Example:<br>
     * &lt;targetContainer&gt;<br>
     * &lt;target&gt;<br>
     * <i>More specialised stuff goes here</i><br>
     * &lt;/target&gt;<br>
     * &lt;/targetContainer&gt;
     *
     * @param parent The target container element
     * @return Returns the new created target element (which is a child
     *         of the passed container element)
     *         Might return <code>null</code> if parent was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    protected Element createXmlTargetElement(Element parent) {

        if (parent == null) {
            return null;
        }

        Document ownerDoc = parent.getOwnerDocument();

        Element e_Target = ownerDoc.createElement(XML_ELEMENT_TARGET);
        e_Target.setAttribute(XML_ELEMENT_ATTRIBUTE_ID, super.getID());

        parent.appendChild(e_Target);

        if (this.dataSource != null) {
            Element e_DataSource = ownerDoc.createElement(ITarget.XML_ELEMENT_DATASOURCE);
            Node n_DataSourceText = ownerDoc.createTextNode(this.dataSource);
            e_DataSource.appendChild(n_DataSourceText);
            e_Target.appendChild(e_DataSource);
        } else {
            this.observer.addAsLinkToXmlElement(e_Target, IObserver.XML_ELEMENT_OBSERVER);
        }

        Element e_Name = ownerDoc.createElement(XML_ELEMENT_NAME);
        Node n_NameText = ownerDoc.createTextNode(this.name);
        e_Name.appendChild(n_NameText);
        e_Target.appendChild(e_Name);

        if (aliasNames.size() != 0) {

            Element e_Alias = null;
            ListIterator iterator = aliasNames.listIterator();
            String alias = null;
            while (iterator.hasNext()) {

                alias = (String) iterator.next();

                e_Alias = ownerDoc.createElement(XML_ELEMENT_ALIASNAME);
                Node n_AliasText = ownerDoc.createTextNode(alias);
                e_Alias.appendChild(n_AliasText);
                e_Target.appendChild(e_Alias);

            }
        }

        if (position != null) {
            e_Target = position.addToXmlElement(e_Target);
        }

        if (constellation != null) {
            Element e_Constellation = ownerDoc.createElement(ITarget.XML_ELEMENT_CONSTELLATION);
            Node n_ConstellationText = ownerDoc.createTextNode(this.constellation);
            e_Constellation.appendChild(n_ConstellationText);
            e_Target.appendChild(e_Constellation);
        }

        return e_Target;

    }

    // --------------
    // Public methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------

    /**
     * Adds a comma seperated list of new alias names
     * to the Target.<br>
     *
     * @param aliasNames Comma seperated list with alternative names
     *                   of the astronomical object
     * @return Returns <code>true</code> if all alias names of the list
     *         could be added.
     *         If <code>false</code> is returned the new alias was <code>null</code>.
     */
    public boolean addAliasNames(String aliasNames) {

        if (aliasNames == null) {
            return false;
        }

        StringTokenizer tokenizer = new StringTokenizer(aliasNames, ALIASNAMES_DELIMITER);
        String token = null;
        if (tokenizer.hasMoreTokens()) {

            token = tokenizer.nextToken();

            this.aliasNames.add(token);

        }

        return true;

    }

}
