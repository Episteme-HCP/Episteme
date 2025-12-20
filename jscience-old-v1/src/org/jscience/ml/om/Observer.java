/* ====================================================================
 * /Observer.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;


import org.jscience.ml.om.util.SchemaException;
import org.w3c.dom.*;

import java.util.*;


/**
 * An Observer describes person, who does astronomical observations.<br>
 * The Observer class provides access to at least the name and
 * surname of the person. Additionally address informations may be
 * stored here.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public class Observer extends SchemaElement implements IObserver {

    // ------------------
    // Instance Variables ------------------------------------------------
    // ------------------

    // The observers name
    private String name = new String();

    // The observers surname
    private String surname = new String();

    // The sites latitude in degrees
    private LinkedList contacts = new LinkedList();

    // Observer code in DeepSkyList (DSL)
    private String DSL = null;

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // -------------------------------------------------------------------    

    /**
     * Constructs a new Observer instance from a given XML Schema Node.
     * Normally this constructor is only used by
     * org.jscience.ml.om.util.SchemaLoader
     *
     * @param observer The XML Schema Node that represents this Observer object
     * @throws IllegalArgumentException if the given parameter is <code>null</code>
     * @throws SchemaException          if the given Node does not match the XML
     *                                  Schema specifications
     */
    public Observer(Node observer)
            throws SchemaException,
            IllegalArgumentException {

        if (observer == null) {
            throw new IllegalArgumentException("Parameter observer node cannot be NULL. ");
        }

        // Cast to element as we need some methods from it
        Element observerElement = (Element) observer;

        // Helper classes
        Element child = null;
        NodeList children = null;

        // Getting data
        // First mandatory stuff and down below optional data

        // Get ID from element
        NamedNodeMap attributes = observerElement.getAttributes();
        if ((attributes == null)
                || (attributes.getLength() == 0)
                ) {
            throw new SchemaException("Observer must have a unique ID. ");
        }
        String ID = observerElement.getAttribute(ISchemaElement.XML_ELEMENT_ATTRIBUTE_ID);
        super.setID(ID);

        // Get mandatory name
        children = ((Element) observerElement).getElementsByTagName(IObserver.XML_ELEMENT_NAME);
        if ((children == null)
                || (children.getLength() != 1)
                ) {
            throw new SchemaException("Observer must have exact one name. ");
        }
        child = (Element) children.item(0);
        String name = null;
        if (child == null) {
            throw new SchemaException("Observer must have a name. ");
        } else {
            if (child.getFirstChild() != null) {
                name = child.getFirstChild().getNodeValue();
            } else {
                throw new SchemaException("Observer cannot have a empty name. ");
            }

            this.setName(name);
        }

        // Get mandatory surname
        child = null;
        children = ((Element) observerElement).getElementsByTagName(IObserver.XML_ELEMENT_SURNAME);
        if ((children == null)
                || (children.getLength() != 1)
                ) {
            throw new SchemaException("Observer must have exact one surname. ");
        }
        child = (Element) children.item(0);
        String surname = null;
        if (child == null) {
            throw new SchemaException("Observer must have a surname. ");
        } else {
            surname = child.getFirstChild().getNodeValue();
            this.setSurname(surname);
        }

        // Get optional contacts
        child = null;
        children = ((Element) observerElement).getElementsByTagName(IObserver.XML_ELEMENT_CONTACT);
        if (children != null) {
            for (int x = 0; x < children.getLength(); x++) {
                child = (Element) children.item(x);
                if (child != null) {
                    this.addContact(child.getFirstChild().getNodeValue());
                } else {
                    throw new SchemaException("Problem retrieving contact information from Observer. ");
                }
            }
        }

        // Get optional DSL code
        child = null;
        children = observerElement.getElementsByTagName(IObserver.XML_ELEMENT_DSL);
        String DSLCode = null;
        if (children != null) {
            if (children.getLength() == 1) {
                child = (Element) children.item(0);
                if (child != null) {
                    DSLCode = child.getFirstChild().getNodeValue();
                    this.setDSLCode(DSLCode);
                } else {
                    throw new SchemaException("Problem while retrieving magnification from scope. ");
                }
            } else if (children.getLength() > 1) {
                throw new SchemaException("Observer can have only one DSL Code. ");
            }
        }

    }


    // -------------------------------------------------------------------    
    /**
     * Constructs a new instance of an Observer.
     *
     * @param name    The observers name
     * @param surname The observers surname
     * @throws IllegalArgumentException if one of the given parameters is
     *                                  <code>null</code>
     */
    public Observer(String name,
                    String surname) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null. ");
        }
        this.name = name;

        if (surname == null) {
            throw new IllegalArgumentException("Surname cannot be null. ");
        }
        this.surname = surname;

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

        return this.getSurname() + ", " + this.getName();

    }

    // ------
    // Object ------------------------------------------------------------
    // ------

    // -------------------------------------------------------------------

    /**
     * Overwrittes toString() method from java.lang.Object.<br>
     * Returns the name, surname and contact informations of this
     * observer.
     *
     * @return This observers name, surname and contact informations
     * @see java.lang.Object
     */
    public String toString() {

        StringBuffer buffer = new StringBuffer();
        buffer.append("Observer: Name=");
        buffer.append(name);

        buffer.append(" Surname=");
        buffer.append(surname);

        buffer.append(" Contacts=");
        if ((contacts != null)
                && (!contacts.isEmpty())
                ) {
            ListIterator iterator = contacts.listIterator();
            while (iterator.hasNext()) {

                buffer.append((String) iterator.next());

                if (iterator.hasNext()) {
                    buffer.append(" --- ");
                }
            }
        }

        if (this.DSL != null) {
            buffer.append(" DSL=");
            buffer.append(this.DSL);
        }

        return buffer.toString();

    }


    // -------------------------------------------------------------------
    /**
     * Overwrittes hashCode() method from java.lang.Object.<br>
     * Returns a hashCode for the string returned from toString() method.
     *
     * @return a hashCode value
     * @see java.lang.Object
     */
    public int hashCode() {

        return this.toString().hashCode();

    }


    // -------------------------------------------------------------------
    /**
     * Overwrittes equals(Object) method from java.lang.Object.<br>
     * Checks if this Observer and the given Object are equal. The given
     * object is equal with this Observer, if it implements the IObserver
     * interface and if its name, surname and contact informations equals
     * this Observers data.<br>
     *
     * @param obj The Object to compare this Observer with.
     * @return <code>true</code> if the given Object is an instance of
     *         IObserver and its name, surname and contact informations are
     *         equal to this Observers data.<br>
     *         (Name, surname comparism is <b>not</b> casesensitive)
     * @see java.lang.Object
     */
    public boolean equals(Object obj) {

        if (obj == null
                || !(obj instanceof IObserver)
                ) {
            return false;
        }

        IObserver observer = (IObserver) obj;

        if (!observer.getName().toLowerCase().equals(name.toLowerCase())) {
            return false;
        }

        if (!observer.getSurname().toLowerCase().equals(surname.toLowerCase())) {
            return false;
        }

        // Sort contact list from given object
        List objectContacts = sortContactList(observer.getContacts());

        // dublicate this Observers contacts, that the original
        // contact list stays unchanged, while we sort and compare the results
        List contactList = new LinkedList(contacts);
        //	Sort internal contact list
        contactList = sortContactList(contactList);

        // Calls AbstractList.equals(Object) as both list should be sorted
        if (!contactList.equals(objectContacts)) {
            return false;
        }

        return true;

    }

    // ---------
    // IObserver ---------------------------------------------------------
    // ---------     

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
    public Element addToXmlElement(Element element) {

        if (element == null) {
            return null;
        }

        Document ownerDoc = element.getOwnerDocument();

        // Check if this element doesn't exist so far
        NodeList nodeList = element.getElementsByTagName(IObserver.XML_ELEMENT_OBSERVER);
        if (nodeList.getLength() > 0) {
            Node currentNode = null;
            NamedNodeMap attributes = null;
            for (int i = 0; i < nodeList.getLength(); i++) {   // iterate over all found nodes
                currentNode = nodeList.item(i);
                attributes = currentNode.getAttributes();
                Node idAttribute = attributes.getNamedItem(SchemaElement.XML_ELEMENT_ATTRIBUTE_ID);
                if ((idAttribute != null)    // if ID attribute is set and equals this objects ID, return existing element
                        && (idAttribute.getNodeValue().trim().equals(super.getID().trim()))
                        ) {
                    return element;
                }
            }
        }

        Element e_Observer = ownerDoc.createElement(XML_ELEMENT_OBSERVER);
        e_Observer.setAttribute(XML_ELEMENT_ATTRIBUTE_ID, super.getID());

        element.appendChild(e_Observer);

        Element e_Name = ownerDoc.createElement(XML_ELEMENT_NAME);
        Node n_NameText = ownerDoc.createTextNode(this.name);
        e_Name.appendChild(n_NameText);
        e_Observer.appendChild(e_Name);

        Element e_Surname = ownerDoc.createElement(XML_ELEMENT_SURNAME);
        Node n_SurnameText = ownerDoc.createTextNode(this.surname);
        e_Surname.appendChild(n_SurnameText);
        e_Observer.appendChild(e_Surname);

        if ((contacts != null)
                && !(contacts.isEmpty())
                ) {
            Element e_Contact = null;
            ListIterator iterator = contacts.listIterator();
            String contact = null;
            while (iterator.hasNext()) {

                contact = (String) iterator.next();

                e_Contact = ownerDoc.createElement(XML_ELEMENT_CONTACT);
                Node n_ContactText = ownerDoc.createTextNode(contact);
                e_Contact.appendChild(n_ContactText);
                e_Observer.appendChild(e_Contact);

            }
        }

        if (this.DSL != null) {
            Element e_DSL = ownerDoc.createElement(XML_ELEMENT_DSL);
            Node n_DSLText = ownerDoc.createTextNode(this.DSL);
            e_DSL.appendChild(n_DSLText);
            e_Observer.appendChild(e_DSL);
        }

        return element;

    }


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
     * @param element           The element at which the Observer link will be created.
     * @param NameOfLinkElement The name of the link element, which is set under the passed
     *                          element
     * @return Returns the Element given as parameter with the
     *         Observer as linked child element, and the elements ownerDocument
     *         with the additional Observer element
     *         Might return <code>null</code> if element was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public Element addAsLinkToXmlElement(Element element, String NameOfLinkElement) {

        if (element == null) {
            return null;
        }

        Document ownerDoc = element.getOwnerDocument();

        // Create the link element
        Element e_Link = ownerDoc.createElement(NameOfLinkElement);
        Node n_LinkText = ownerDoc.createTextNode(super.getID());
        e_Link.appendChild(n_LinkText);

        element.appendChild(e_Link);

        // Get or create the container element        
        Element e_Observers = null;
        NodeList nodeList = ownerDoc.getElementsByTagName(RootElement.XML_OBSERVER_CONTAINER);
        if (nodeList.getLength() == 0) {  // we're the first element. Create container element
            e_Observers = ownerDoc.createElement(RootElement.XML_OBSERVER_CONTAINER);
            ownerDoc.getDocumentElement().appendChild(e_Observers);
        } else {
            e_Observers = (Element) nodeList.item(0);  // there should be only one container element
        }

        this.addToXmlElement(e_Observers);

        return element;

    }


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
    public List getContacts() {

        return contacts;

    }


    // -------------------------------------------------------------------    
    /**
     * Adds a new contact information to the observer.<br>
     *
     * @param newContact the additional contact information
     * @return <b>true</b> if the new contact information
     *         could be added successfully. <b>false</b> if the
     *         new contact information could not be added.
     */
    public boolean addContact(String newContact) {

        if (newContact == null
                || "".equals(newContact)
                ) {
            return false;
        }

        this.contacts.add(newContact);

        return true;

    }


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
    public boolean setContacts(List newContacts) {

        if (newContacts == null) {
            return false;
        }

        this.contacts = new LinkedList(newContacts);

        return true;

    }


    // -------------------------------------------------------------------    
    /**
     * Returns the name of the observer<br>
     * The name (and the surname) are the only mandatory
     * fields this interface requires.
     *
     * @return the name of the observer
     */
    public String getName() {

        return name;

    }


    // -------------------------------------------------------------------    
    /**
     * Returns the DeepSkyList (DSL) Code of the observer<br>
     * Might return <code>NULL</code> if observer has no DSL code
     *
     * @return the DeepSkyList (DSL) Code of the observer, or <code>NULL</code>
     *         if DSL was never set
     */
    public String getDSLCode() {

        return this.DSL;

    }


    // -------------------------------------------------------------------    
    /**
     * Returns the surname of the observer<br>
     * The surname (and the name) are the only mandatory
     * fields this interface requires.
     *
     * @return the surname of the observer
     */
    public String getSurname() {

        return surname;

    }


    // -------------------------------------------------------------------    
    /**
     * Sets the DeepSkyList (DSL) Code of the observer<br>
     *
     * @param DSLCode the DeepSkyList (DSL) Code of the observer
     */
    public void setDSLCode(String DSLCode) {

        if ((DSLCode != null)
                && ("".equals(DSLCode.trim()))
                ) {
            this.DSL = null;
            return;
        }

        this.DSL = DSLCode;

    }


    // -------------------------------------------------------------------    
    /**
     * Sets a new name to the observer.<br>
     * As the name is mandatory it cannot be <code>null</code>
     *
     * @param name the new name of the observer
     * @throws IllegalArgumentException if the given name is <code>null</code>
     */
    public void setName(String name) throws IllegalArgumentException {

        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null. ");
        }

        this.name = name;

    }


    // -------------------------------------------------------------------    
    /**
     * Sets a new surname to the observer.<br>
     * As the surname is mandatory it cannot be <code>null</code>
     *
     * @param surname the new surname of the observer
     * @throws IllegalArgumentException if the given surname is <code>null</code>
     */
    public void setSurname(String surname) {

        if (surname == null) {
            throw new IllegalArgumentException("Surname cannot be null. ");
        }

        this.surname = surname;

    }

    // ---------------
    // Private Methods ---------------------------------------------------
    // ---------------

    // -------------------------------------------------------------------

    private List sortContactList(List contacts) {

        Collections.sort(contacts, new Comparator() {
            public int compare(Object o1,
                               Object o2) {

                String s1 = (String) o1;
                String s2 = (String) o2;

                return s1.compareTo(s2);

            }

            public boolean equals(Object obj) {
                // implementation not needed for our usage
                return false;
            }
        }
        );

        return contacts;

    }

}
