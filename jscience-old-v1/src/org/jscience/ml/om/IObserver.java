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
 * /IObserver.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;

import org.w3c.dom.Element;

import java.util.List;


/**
 * An IObserver describes person, who does astronomical observations.<br>
 * The IObserver interface provides access to at least the name and
 * surname of the person. Additionally address informations may be
 * stored here.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public interface IObserver extends ISchemaElement {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    /**
     * Constant for XML representation: IObserver element name.<br>
     * Example:<br>
     * &lt;observer&gt;<i>More stuff goes here</i>&lt;/observer&gt;
     */
    public static final String XML_ELEMENT_OBSERVER = "observer";

    /**
     * Constant for XML representation: Observers name element name.<br>
     * Example:<br>
     * &lt;observer&gt;
     * <br><i>More stuff goes here</i>
     * &lt;name&gt;<code>Observer name goes here</code>&lt;/name&gt;
     * <i>More stuff goes here</i>
     * &lt;/observer&gt;
     */
    public static final String XML_ELEMENT_NAME = "name";

    /**
     * Constant for XML representation: Observers surname element name.<br>
     * Example:<br>
     * &lt;observer&gt;
     * <br><i>More stuff goes here</i>
     * &lt;surname&gt;<code>Observer surname goes here</code>&lt;/surname&gt;
     * <i>More stuff goes here</i>
     * &lt;/observer&gt;
     */
    public static final String XML_ELEMENT_SURNAME = "surname";

    /**
     * Constant for XML representation: Observers contact element name.<br>
     * Example:<br>
     * &lt;observer&gt;
     * <br><i>More stuff goes here</i>
     * &lt;contact&gt;<code>Observer name goes here</code>&lt;/contact&gt;
     * <i>More stuff goes here</i>
     * &lt;/observer&gt;
     */
    public static final String XML_ELEMENT_CONTACT = "contact";

    /**
     * Constant for XML representation: Observers DeepSkyList (DSL) code.<br>
     * Example:<br>
     * &lt;observer&gt;
     * <br><i>More stuff goes here</i>
     * &lt;DSL&gt;<code>Observer DSL code goes here</code>&lt;/DSL&gt;
     * <i>More stuff goes here</i>
     * &lt;/observer&gt;
     */
    public static final String XML_ELEMENT_DSL = "DSL";

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------

    /**
     * Adds this Observer to a given parent XML DOM Element.
     * The Observer element will be set as a child element of
     * the passed element.
     *
     * @param parent The parent element for this Observer
     * @return Returns the element given as parameter with this
     *         Observer as child element.<br>
     *         Might return <code>null</code> if parent was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element element);


    // -------------------------------------------------------------------
    /**
     * Adds a Observer link to an given XML DOM Element.
     * The Observer element itself will be attached to given elements
     * ownerDocument. If the ownerDocument has no observer container, it will
     * be created.<br>
     * It might look a little odd that observers addAsLinkToXmlElement() method
     * takes two parameters, but it is nessary as IObserver is once used as
     * <coObserver> (under <session>) and used as <observer> under other elements.
     * This is why the name of the link element has to be specified.
     * The link element will be created under the passed parameter element.
     * Example:<br>
     * &lt;parameterElement&gt;<br>
     * <b>&lt;linkNameElement&gt;123&lt;/linkNameElement&gt;</b><br>
     * &lt;/parameterElement&gt;<br>
     * <i>More stuff of the xml document goes here</i><br>
     * <b>&lt;observerContainer&gt;</b><br>
     * <b>&lt;observer id="123"&gt;</b><br>
     * <i>Observer description goes here</i><br>
     * <b>&lt;/observer&gt;</b><br>
     * <b>&lt;/observerContainer&gt;</b><br>
     * <br>
     *
     * @param parent            The element at which the Observer link will be created.
     * @param NameOfLinkElement The name of the link element, which is set under the passed
     *                          element
     * @return Returns the Element given as parameter with the
     *         Observer as linked child element, and the elements ownerDocument
     *         with the additional Observer element
     *         Might return <code>null</code> if element was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public org.w3c.dom.Element addAsLinkToXmlElement(org.w3c.dom.Element parent,
                                                     String NameOfLinkElement);


    // -------------------------------------------------------------------
    /**
     * Returns a List with contact information of the
     * observer<br>
     * The returned List may contain e-Mail address, phone number,
     * fax number, postal adress, webpage....whatever.
     * No garantee is given what the list should/may contain, or in which
     * order the elements are placed.<br>
     * If no contact informations where given, the method might
     * return <code>null</code>
     *
     * @return a List with contact information of the observer, or
     *         <code>null</code> if not informations are given.
     */
    public java.util.List getContacts();


    // -------------------------------------------------------------------
    /**
     * Returns the name of the observer<br>
     * The name (and the surname) are the only mandatory
     * fields this interface requires.
     *
     * @return the name of the observer
     */
    public String getName();


    /**
     * Returns the surname of the observer<br>
     * The surname (and the name) are the only mandatory
     * fields this interface requires.
     *
     * @return the surname of the observer
     */
    public String getSurname();


    // -------------------------------------------------------------------    
    /**
     * Returns the DeepSkyList (DSL) Code of the observer<br>
     * Might return <code>NULL</code> if observer has no DSL code
     *
     * @return the DeepSkyList (DSL) Code of the observer, or <code>NULL</code>
     *         if DSL was never set
     */
    public String getDSLCode();


    /**
     * Adds a new contact information to the observer.<br>
     *
     * @param newContact the additional contact information
     * @return <b>true</b> if the new contact information
     *         could be added successfully. <b>false</b> if the
     *         new contact information could not be added.
     */
    public boolean addContact(String newContact);


    // -------------------------------------------------------------------    
    /**
     * Sets the contact information to the observer.<br>
     * All current contacts will be deleted!<br>
     * If you want to add a contact use addContact(String)<br>
     *
     * @param newContacts new list of contact informations
     * @return <b>true</b> if the new contact information
     *         could be set successfully. <b>false</b> if the
     *         new contact information could not be set.
     */
    public boolean setContacts(List newContacts);


    // -------------------------------------------------------------------    
    /**
     * Sets the DeepSkyList (DSL) Code of the observer<br>
     *
     * @param DSLCode the DeepSkyList (DSL) Code of the observer
     */
    public void setDSLCode(String DSLCode);


    /**
     * Sets a new name to the observer.<br>
     * As the name is mandatory it cannot be <code>null</code>
     *
     * @param name the new name of the observer
     * @throws IllegalArgumentException if the given name is <code>null</code>
     */
    public void setName(String name) throws IllegalArgumentException;


    /**
     * Sets a new surname to the observer.<br>
     * As the surname is mandatory it cannot be <code>null</code>
     *
     * @param surname the new surname of the observer
     * @throws IllegalArgumentException if the given surname is <code>null</code>
     */
    public void setSurname(String surname) throws IllegalArgumentException;


}
