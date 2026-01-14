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

/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name. Fixed #479280 - infinite loop in schema parsing if two
 * features from different namespaces have the same local name. Fixed #479516
 * - prefix for XML Schema need not be xsd. Now, prefixes do not matter in
 * general. Fixed #479283 - no exception is thrown if the schema contains an
 * enumeration.
 */
package org.jscience.ml.gml.xml.schema;

import org.jaxen.saxpath.SAXPathException;

import org.jdom.*;

import org.jscience.ml.gml.GMLSchema;
import org.jscience.ml.gml.xml.util.XMLUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.*;


/**
 * Parses the schema and builds a schema graph. Also, answers questions
 * such as whether an element is a GML feature.
 *
 * @author First cut by Milan Trninic, enhanced by Aleksandar Milanovic
 * @version 1.1
 */
public class SchemaParser {
    /** DOCUMENT ME! */
    private String _targetNamespace;

    /** DOCUMENT ME! */
    private String _targetPrefix;

    /** DOCUMENT ME! */
    private String _location;

    /** DOCUMENT ME! */
    private String _schemaString;

    /** DOCUMENT ME! */
    private Document _schema;

    /** DOCUMENT ME! */
    private SchemaGraph _schemaGraph = new SchemaGraph();

    /** DOCUMENT ME! */
    private Element _root;

    /** DOCUMENT ME! */
    private NamespaceResolver _nsResolver = NamespaceResolver.getInstance();

    // imported schemas (i.e. schema parsers)
    /** DOCUMENT ME! */
    private LinkedList _imports;

    // included schemas (i.e. schema parsers)
    /** DOCUMENT ME! */
    private LinkedList _includes;

    /** DOCUMENT ME! */
    private Hashtable _substTable;

/**
     * Empty constructor. Call parser() to do some real parsing.
     *
     * @see #parse(String)
     */
    public SchemaParser() {
    }

    /**
     * Parses the specified schema and all included and imported
     * schemas.
     *
     * @param urlString DOCUMENT ME!
     *
     * @return 0 on success, -1 on failure
     */
    public int parse(String urlString) {
        if (urlString.indexOf("http://") != 0) {
            urlString = "file:///" + urlString;
        }

        String schemaString = doGet(urlString);
        setLocation(urlString);
        setSchema(schemaString);

        int result = internalParse();

        return result;
    }

    /**
     * Parses the specified schema and all included and imported
     * schemas.
     *
     * @return 0 on success, -1 on failure
     */
    private int internalParse() {
        _imports = new LinkedList();
        _includes = new LinkedList();

        if (checkSchema() != 0) {
            return -1;
        }

        _root = (Element) _schema.getRootElement();

        if (_root == null) {
            return -1;
        }

        _targetNamespace = _root.getAttributeValue(XMLSchema.TARGET_NAMESPACE_ATTRIBUTE);

        try {
            URL uri = new URL(_targetNamespace);
        } catch (Exception exception) {
            return -1;
        }

        extractNamespaces();

        if (verifyNamespaces() != 0) {
            return -1;
        }

        if (importImports() != 0) {
            return -1;
        }

        if (includeIncludes() != 0) {
            return -1;
        }

        _schemaGraph.clean();

        Date parseStartTime = new Date();

        if (makeTypeGraph() != 0) {
            return -1;
        }

        //        Date parseEndTime = new Date();
        //        long elapsedTime = parseEndTime.getTime() - parseStartTime.getTime();
        //        System.out.println("Schema " + _location + " parsed in " + elapsedTime
        //            + " msec.");
        //        System.out.println("Schema " + _location);
        //        _schemaGraph.dump();
        //        System.out.println("---------------------------------------------------");
        return 0;
    }

    /**
     * Checks whether schema is available.
     *
     * @return DOCUMENT ME!
     */
    private int checkSchema() {
        if (_location == null) {
            return -1;
        }

        if (_schemaString == null) {
            _schemaString = doGet(_location);

            if ((_schemaString == null) || _schemaString.equals("")) {
                return -1;
            }
        }

        if (_schema == null) {
            _schema = setSchema(_schemaString);
        }

        int result = (_schema == null) ? (-1) : 0;

        return result;
    }

    /**
     * Sets this parser to a new schema. Upon calling parse() this
     * schema will be parsed.
     *
     * @param schemaString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Document setSchema(String schemaString) {
        Document schema = XMLUtils.string2jdom(schemaString);
        _schema = schema;
        _schemaString = schemaString;

        return _schema;
    }

    /**
     * Set schema location (for the next parse attempt)
     *
     * @param location DOCUMENT ME!
     */
    private void setLocation(String location) {
        _location = location;
    }

    /**
     * Extracts all namespaces from the current schema.
     */
    private void extractNamespaces() {
        // add all namespaces other than that of the root element
        List namespaces = _root.getAdditionalNamespaces();
        ListIterator namespaceIterator = namespaces.listIterator();

        while (namespaceIterator.hasNext()) {
            Namespace nextNamespace = (Namespace) namespaceIterator.next();
            String prefix = nextNamespace.getPrefix();
            String uri = nextNamespace.getURI();
            _nsResolver.addNamespace(prefix, uri);

            if (uri.equals(_targetNamespace)) {
                _targetPrefix = prefix;
            }
        }

        // add the namespace of the root element
        Namespace rootElemNs = _root.getNamespace();
        _nsResolver.addNamespace(rootElemNs.getPrefix(), rootElemNs.getURI());
    }

    /**
     * Verifies that all namespaces in the schema document are
     * recognized.
     *
     * @return DOCUMENT ME!
     */
    private int verifyNamespaces() {
        // find all elements in the schema document
        List descendants = null;

        try {
            descendants = XMLUtils.executeXPathQuery(_root, "//*");
        } catch (SAXPathException spe) {
            spe.printStackTrace();
        }

        int length = descendants.size();

        for (int index = 0; index < length; index++) {
            Element element = (Element) descendants.get(index);
            String name = element.getName();
            String prefix = element.getNamespacePrefix();

            if (!prefix.equals(XMLSchema.DEFAULT_NAMESPACE_PREFIX) &&
                    !_nsResolver.prefixExists(prefix)) {
                return -1;
            }

            List attributes = element.getAttributes();
            int size = attributes.size();

            for (int jndex = 0; jndex < size; jndex++) {
                Attribute attribute = (Attribute) attributes.get(jndex);
                String attributeName = attribute.getName();
                String attributePrefix = attribute.getNamespacePrefix();

                if (!attributePrefix.equals(XMLSchema.DEFAULT_NAMESPACE_PREFIX) &&
                        !_nsResolver.prefixExists(attributePrefix)) {
                    return -1;
                }
            }
        }

        return 0;
    }

    /**
     * Loads all schema imports (as SchemaParser objects).
     *
     * @return DOCUMENT ME!
     */
    private int importImports() {
        _imports = new LinkedList();

        Namespace xsdNamespace = getXSDNamespace();

        List imports = _root.getChildren(XMLSchema.IMPORT_ELEMENT, xsdNamespace);
        int length = imports.size();

        for (int index = 0; index < length; index++) {
            Element importElement = (Element) imports.get(index);
            Attribute namespace = importElement.getAttribute(XMLSchema.NAMESPACE_ATTRIBUTE);
            Attribute location = importElement.getAttribute(XMLSchema.SCHEMA_LOCATION_ATTRIBUTE);
            SchemaParser newParser = new SchemaParser();

            String locationStr = createSchemaUriString(_location,
                    location.getValue());
            newParser.setLocation(locationStr);

            int result = newParser.internalParse();

            if (result != 0) {
                return -1;
            }

            _imports.add(newParser);
        }

        return 0;
    }

    /**
     * Loads all schema includes (as SchemaParser objects).
     *
     * @return DOCUMENT ME!
     */
    private int includeIncludes() {
        _includes = new LinkedList();

        Namespace xsdNamespace = getXSDNamespace();
        List includes = _root.getChildren(XMLSchema.INCLUDE_ELEMENT,
                xsdNamespace);
        int length = includes.size();

        for (int index = 0; index < length; index++) {
            Element includeElement = (Element) includes.get(index);
            String location = includeElement.getAttributeValue(XMLSchema.SCHEMA_LOCATION_ATTRIBUTE);
            SchemaParser newParser = new SchemaParser();

            // false means this is not main, but secondary schema
            location = createSchemaUriString(_location, location);
            newParser.setLocation(location);

            int result = newParser.internalParse();

            if (result != 0) {
                return -1;
            }

            _includes.add(newParser);
        }

        return 0;
    }

    /**
     * Builds the graph of all elements and types. This method is
     * invoked at the beginning of the parsing.
     *
     * @return DOCUMENT ME!
     */
    private int makeTypeGraph() {
        /**
         * 
         * @todo Check the QName below. It doesn't make sense
         */
        SchemaNode schemaGraphRoot = _schemaGraph.setRoot(QName.getQName(
                    XMLSchema.XML_SCHEMA_NAMESPACE, "schema"),
                QName.getQName(XMLSchema.XML_NAMESPACE, "XMLSchema"));

        // go find all the global elements
        List globalElements = _root.getChildren();
        addChildren(schemaGraphRoot, globalElements);

        return 0;
    }

    /**
     * Adds the given children of the given schema graph node.
     *
     * @param graphNode DOCUMENT ME!
     * @param children DOCUMENT ME!
     */
    private void addChildren(SchemaNode graphNode, List children) {
        ListIterator childIterator = children.listIterator();

        while (childIterator.hasNext()) {
            Element nextChild = (Element) childIterator.next();
            processElementNode(graphNode, nextChild);
        }
    }

    /**
     * Handles a schema element based on its tag. Main tags are:
     * element, complexContent, restriction, attribute, etc.
     *
     * @param parentNode DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    private void processElementNode(SchemaNode parentNode, Element element) {
        String localName = element.getName();

        Namespace xsdNamespace = getXSDNamespace();

        if (!element.getNamespaceURI().equals(XMLSchema.XML_SCHEMA_NAMESPACE)) {
            // if not an XML schema definition, go back
            return;
        }

        if (localName.equals(XMLSchema.ALL_ELEMENT)) {
            List children = element.getChildren();
            addChildren(parentNode, children);
        } else if (localName.equals(XMLSchema.ANNOTATION_ELEMENT)) {
            // do nothing;
        } else if (localName.equals(XMLSchema.ANY_ELEMENT)) {
            SchemaNode newNode = new SchemaNode(XMLSchema.ANY_ELEMENT,
                    QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
                        XMLSchema.ANY_ELEMENT),
                    QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
                        XMLSchema.XML_SCHEMA_ELEMENT_TYPE));
            _schemaGraph.addNode(parentNode, newNode);
        } else if (localName.equals(XMLSchema.ANY_ATTRIBUTE_ELEMENT)) {
            SchemaNode newNode = new SchemaNode(XMLSchema.ANY_ATTRIBUTE_ELEMENT,
                    QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
                        XMLSchema.ANY_ATTRIBUTE_ELEMENT),
                    QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
                        XMLSchema.XML_SCHEMA_ELEMENT_TYPE));
            _schemaGraph.addNode(parentNode, newNode);
        } else if (localName.equals(XMLSchema.APP_INFO_ELEMENT)) {
            // do nothing
        } else if (localName.equals(XMLSchema.ATTRIBUTE_ELEMENT)) {
            addAttribute(parentNode, element);
        } else if (localName.equals(XMLSchema.ATTRIBUTE_GROUP_ELEMENT)) {
            addGroup(parentNode, element, XMLSchema.ATTRIBUTE_GROUP_ELEMENT);
        } else if (localName.equals(XMLSchema.CHOICE_ELEMENT)) {
            SchemaNode newNode = new SchemaNode(XMLSchema.CHOICE_ELEMENT,
                    QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
                        XMLSchema.CHOICE_ELEMENT), null);
            _schemaGraph.addNode(parentNode, newNode);

            List children = element.getChildren();
            addChildren(newNode, children);
        } else if (localName.equals(XMLSchema.COMPLEX_CONTENT_ELEMENT)) {
            List children = element.getChildren();
            addChildren(parentNode, children);
        } else if (localName.equals(XMLSchema.COMPLEX_TYPE_ELEMENT)) {
            String complexTypeName = element.getAttributeValue(XMLSchema.NAME_ATTRIBUTE);
            SchemaNode complexTypeNode = new SchemaNode(XMLSchema.COMPLEX_TYPE_ELEMENT,
                    QName.getQName(_targetNamespace, complexTypeName),
                    QName.getQName(xsdNamespace.getURI(),
                        XMLSchema.COMPLEX_TYPE_ELEMENT));
            List children = element.getChildren();
            addChildren(complexTypeNode, children);
            _schemaGraph.addNode(parentNode, complexTypeNode);
        } else if (localName.equals(XMLSchema.DOCUMENTATION_ELEMENT)) {
            // do nothing
        } else if (localName.equals(XMLSchema.ELEMENT_ELEMENT)) {
            addChildElement(parentNode, element);
        } else if (localName.equals(XMLSchema.ENUMERATION_ELEMENT)) {
            SchemaNode newNode = new SchemaNode(XMLSchema.ENUMERATION_ELEMENT,
                    QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
                        XMLSchema.ENUMERATION_ELEMENT), null);
            _schemaGraph.addNode(parentNode, newNode);

            /**
             * 
             * @todo What to do with the value?
             */
            String value = element.getAttributeValue(XMLSchema.VALUE_ATTRIBUTE);
        } else if (localName.equals(XMLSchema.EXTENSION_ELEMENT)) {
            String baseType = element.getAttributeValue(XMLSchema.BASE_ATTRIBUTE);
            String baseTypeLocalName = removePrefix(baseType);
            String baseTypePrefix = getPrefix(baseType);
            String baseTypeNs = _nsResolver.namespace(baseTypePrefix);
            QName baseTypeQName = QName.getQName(baseTypeNs, baseTypeLocalName);
            parentNode.addToTypeChain(baseTypeQName);

            if (!isBuiltInType(baseTypeQName)) {
                // try to get it as a complex type
                SchemaNode baseTypeNode = getGlobalConstructFromAnywhere(XMLSchema.COMPLEX_TYPE_ELEMENT,
                        baseTypeNs, baseTypeLocalName);

                if (baseTypeNode == null) {
                    baseTypeNode = getGlobalConstructFromAnywhere(XMLSchema.SIMPLE_TYPE_ELEMENT,
                            baseTypeNs, baseTypeLocalName);

                    if (baseTypeNode == null) {
                        throw new IllegalStateException("Cannot find the " +
                            "base type " + baseTypeLocalName + " node");
                    }
                }

                for (int ii = 0; ii < baseTypeNode.getChildCount(); ii++) {
                    SchemaNode clonedChild = baseTypeNode.getChildAt(ii)
                                                         .deepClone();
                    _schemaGraph.addNode(parentNode, clonedChild);
                }

                parentNode.getTypeChain().addAll(baseTypeNode.getTypeChain());
            }

            // add the new fields now
            List children = element.getChildren();
            addChildren(parentNode, children);
        } else if (localName.equals(XMLSchema.FIELD_ELEMENT)) {
            // do nothing
        } else if (localName.equals(XMLSchema.GROUP_ELEMENT)) {
            addGroup(parentNode, element, XMLSchema.GROUP_ELEMENT);

/**
             * @todo Cannot use addGroup for non-attributes
             */
        } else if (localName.equals(XMLSchema.IMPORT_ELEMENT)) {
            ;
        }
        //        else if (localName.equals(XMLSchema.INCLUDE_ELEMENT));
        //        else if (localName.equals(XMLSchema.KEY_ELEMENT));
        //        else if (localName.equals(XMLSchema.KEY_REF_ELEMENT));
        //        else if (localName.equals(XMLSchema.LENGTH_ELEMENT));
        //        else if (localName.equals(XMLSchema.LIST_ELEMENT));
        //        else if (localName.equals(XMLSchema.MAX_INCLUSIVE_ELEMENT));
        //        else if (localName.equals(XMLSchema.MAX_LENGTH_ELEMENT));
        //        else if (localName.equals(XMLSchema.MIN_INCLUSIVE_ELEMENT));
        //        else if (localName.equals(XMLSchema.MIN_LENGTH_ELEMENT));
        //        else if (localName.equals(XMLSchema.PATTERN_ELEMENT));
        //        else if (localName.equals(XMLSchema.REDEFINE_ELEMENT));
        else if (localName.equals(XMLSchema.RESTRICTION_ELEMENT)) {
            //            List children = element.getChildren();
            //            addChildren(parentNode, children);
            String baseType = element.getAttributeValue(XMLSchema.BASE_ATTRIBUTE);
            String baseTypeLocalName = removePrefix(baseType);
            String baseTypePrefix = getPrefix(baseType);
            String baseTypeNs = _nsResolver.namespace(baseTypePrefix);
            QName baseTypeQName = QName.getQName(baseTypeNs, baseTypeLocalName);

            //            QName anyTypeQName = QName.getQName(XMLSchema.XML_SCHEMA_NAMESPACE,
            //                                                XMLSchema.ANY_TYPE_SIMPLE_TYPE)
            parentNode.addToTypeChain(baseTypeQName);

            SchemaNode restrictedNode = null;

            if (!isBuiltInType(baseTypeQName)) //            if (!baseTypeQName.equals(anyTypeQName))
             {
                // the restricted type is not the nominal anyType, find it
                restrictedNode = getGlobalConstructFromAnywhere(XMLSchema.COMPLEX_TYPE_ELEMENT,
                        baseTypeNs, baseTypeLocalName);

                if (restrictedNode == null) {
                    restrictedNode = getGlobalConstructFromAnywhere(XMLSchema.SIMPLE_TYPE_ELEMENT,
                            baseTypeNs, baseTypeLocalName);

                    if (restrictedNode == null) {
                        throw new IllegalStateException("Cannot find the " +
                            "restricted type node");
                    }
                }

                // attach the inheritance info to the parent (complexType)
                // we need nothing else from the restricted type
                Vector superTypeChain = (Vector) restrictedNode.getTypeChain()
                                                               .clone();
                parentNode.getTypeChain().addAll(superTypeChain);
            }

            // add the children
            List children = element.getChildren();
            addChildren(parentNode, children);
        }
        //        else if (localName.equals(XMLSchema.SCHEMA_ELEMENT));
        //        else if (localName.equals(XMLSchema.SELECTOR_ELEMENT));
        else if (localName.equals(XMLSchema.SEQUENCE_ELEMENT)) {
            List children = element.getChildren();
            addChildren(parentNode, children);
        } else if (localName.equals(XMLSchema.SIMPLE_CONTENT_ELEMENT)) {
            List children = element.getChildren();
            addChildren(parentNode, children);
        } else if (localName.equals(XMLSchema.SIMPLE_TYPE_ELEMENT)) {
            String simpleTypeName = element.getAttributeValue(XMLSchema.NAME_ATTRIBUTE);
            SchemaNode simpleTypeNode = new SchemaNode(XMLSchema.SIMPLE_TYPE_ELEMENT,
                    QName.getQName(_targetNamespace, simpleTypeName),
                    QName.getQName(xsdNamespace.getURI(),
                        XMLSchema.SIMPLE_TYPE_ELEMENT));
            _schemaGraph.addNode(parentNode, simpleTypeNode);
        }

        //        else if (localName.equals(XMLSchema.UNION_ELEMENT));
        //        else if (localName.equals(XMLSchema.UNIQUE_ELEMENT));
    }

    /**
     * Adds attribute and element group elements to the type graph.
     *
     * @param parentNode DOCUMENT ME!
     * @param groupElem DOCUMENT ME!
     * @param groupTagName DOCUMENT ME!
     */
    private void addGroup(SchemaNode parentNode, Element groupElem,
        String groupTagName) {
        String groupRef = groupElem.getAttributeValue(XMLSchema.REF_ATTRIBUTE);
        String groupName = groupElem.getAttributeValue(XMLSchema.NAME_ATTRIBUTE);

        if (!((groupRef != null) ^ (groupName != null)
    /**
         * && groupType != null
         */
            )) {
            // invalid combination
            // must have either ref or (name and type) attributes
            return;
        }

        if (groupRef != null) // is a reference
         {
            String groupPrefix = getPrefix(groupRef);
            String groupNamespace = _nsResolver.namespace(groupPrefix);
            String groupLocalName = removePrefix(groupRef);
            SchemaNode groupNode = getGlobalConstructFromAnywhere(XMLSchema.ATTRIBUTE_GROUP_ELEMENT,
                    groupNamespace, groupLocalName);

            if (groupNode == null) {
                // couldn't find the global (attribute) group
                return;
            }

            SchemaNode referenceNode = new SchemaNode(XMLSchema.REFERENCE_XML_TYPE,
                    groupNode);
            _schemaGraph.addNode(parentNode, referenceNode);
        } else // no reference
         {
            SchemaNode attrGroupNode = new SchemaNode(XMLSchema.ATTRIBUTE_GROUP_ELEMENT,
                    QName.getQName(_targetNamespace, groupName),
                    QName.getQName(getXSDNamespace().getURI(),
                        XMLSchema.ATTRIBUTE_GROUP_ELEMENT));
            _schemaGraph.addNode(parentNode, attrGroupNode);

            List children = groupElem.getChildren();
            addChildren(attrGroupNode, children);
        }
    }

    /**
     * Adds an attribute to the type graph.
     *
     * @param parentNode DOCUMENT ME!
     * @param attrElement DOCUMENT ME!
     */
    private void addAttribute(SchemaNode parentNode, Element attrElement) {
        String attrRef = attrElement.getAttributeValue(XMLSchema.REF_ATTRIBUTE);
        String attrName = attrElement.getAttributeValue(XMLSchema.NAME_ATTRIBUTE);
        String attrType = attrElement.getAttributeValue(XMLSchema.TYPE_ATTRIBUTE);

        if (!((attrRef != null) ^ ((attrName != null) && (attrType != null)))) {
            // invalid combination
            // must have either ref or (name and type) attributes
            return;
        }

        if (attrRef != null) // if reference (of a global attribute)
         {
            if (attrRef.equals("")) {
                // invalid definition
                return;
            }

            String attrPrefix = getPrefix(attrRef);
            String attrNsUri = _nsResolver.namespace(attrPrefix);
            String attrLocalName = removePrefix(attrRef);

            SchemaNode refAttrNode = getGlobalConstructFromAnywhere(XMLSchema.ATTRIBUTE_ELEMENT,
                    attrNsUri, attrLocalName);

            if (refAttrNode == null) {
                // referenced attribute not found
                return;
            }

            SchemaNode refNode = new SchemaNode(XMLSchema.REFERENCE_XML_TYPE,
                    refAttrNode);
            _schemaGraph.addNode(parentNode, refNode);
        } else // new attr definition
         {
            if (attrName.equals("") || attrType.equals("")) {
                // invalid definition
                return;
            }

            // if global attrib, target namespace is its ns uri, otherwise none
            String attrNsUri = (parentNode == _schemaGraph.getRoot())
                ? _targetNamespace : "";
            String attrLocalName = removePrefix(attrName);
            String attrTypePrefix = getPrefix(attrType);
            String attrTypeNsUri = _nsResolver.namespace(attrTypePrefix);
            String attrTypeLocalName = removePrefix(attrType);

            SchemaNode graphNode = new SchemaNode(XMLSchema.ATTRIBUTE_ELEMENT,
                    QName.getQName(attrNsUri, attrLocalName),
                    QName.getQName(attrTypeNsUri, attrTypeLocalName));
            _schemaGraph.addNode(parentNode, graphNode);
        }
    }

    /**
     * Handles a child element, which can also be a reference.
     *
     * @param parentNode DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    private void addChildElement(SchemaNode parentNode, Element element) {
        String elemName = element.getAttributeValue(XMLSchema.NAME_ATTRIBUTE);
        String elemPrefixedType = element.getAttributeValue(XMLSchema.TYPE_ATTRIBUTE);
        String elemRef = element.getAttributeValue(XMLSchema.REF_ATTRIBUTE);

        if (!(((elemName != null) && (elemPrefixedType != null)) ^
                (elemRef != null))) {
            // invalid combination
            // must have either ref or (name and type) attributes
            return;
        }

        if (elemRef != null) // is a reference
         {
            String refNsUri = _nsResolver.namespace(getPrefix(elemRef));
            String refLocalName = removePrefix(elemRef);
            QName refQName = QName.getQName(refNsUri, refLocalName);

            // obtain the referenced global element
            SchemaNode refNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                    refNsUri, refLocalName);

            if (refNode == null) {
                return;
            }

            SchemaNode referenceNode = new SchemaNode(XMLSchema.REFERENCE_XML_TYPE,
                    refNode);
            _schemaGraph.addNode(parentNode, referenceNode);

            return;
        }

        // not a reference
        String elemTypePrefix = getPrefix(elemPrefixedType);
        String elemTypeNsUri = _nsResolver.namespace(elemTypePrefix);
        String elemTypeLocalName = removePrefix(elemPrefixedType);
        QName elemTypeQName = QName.getQName(elemTypeNsUri, elemTypeLocalName);
        QName elemQName = QName.getQName(_targetNamespace, elemName);

        SchemaNode graphNode = new SchemaNode(XMLSchema.ELEMENT_ELEMENT,
                elemQName, elemTypeQName);
        _schemaGraph.addNode(parentNode, graphNode);

        Attribute isAbsAttrib = element.getAttribute(XMLSchema.ABSTRACT_ATTRIBUTE);
        boolean isAbstract = false;

        try {
            if (isAbsAttrib != null) {
                isAbstract = isAbsAttrib.getBooleanValue();
            }
        } catch (DataConversionException e) {
            // do nothing
        }

        graphNode.setIsAbstract(isAbstract);

        boolean builtInType = isBuiltInType(elemTypeQName);
        SchemaNode elemTypeNode = null;

        if (!builtInType) {
            elemTypeNode = getGlobalConstructFromAnywhere(XMLSchema.COMPLEX_TYPE_ELEMENT,
                    elemTypeQName.getNsUri(), elemTypeQName.getLocalName());

            if (elemTypeNode == null) {
                elemTypeNode = getGlobalConstructFromAnywhere(XMLSchema.SIMPLE_TYPE_ELEMENT,
                        elemTypeQName.getNsUri(), elemTypeQName.getLocalName());

                if (elemTypeNode == null) {
                    throw new IllegalStateException(
                        "Schema node for element type " +
                        elemTypeQName.getLocalName() + " cannot be found");
                }
            }

            // copy children from the type node to the element node
            for (int ii = 0; ii < elemTypeNode.getChildCount(); ii++) {
                SchemaNode elemTypeComponentNode = elemTypeNode.getChildAt(ii);
                _schemaGraph.addNode(graphNode,
                    elemTypeComponentNode.deepClone());
            }

            // copy the inheritance info
            graphNode.getTypeChain().addAll(elemTypeNode.getTypeChain());
        }

        Attribute substGroup = element.getAttribute(XMLSchema.SUBSTITUTIONGROUP_ATTRIBUTE);

        if (substGroup != null) {
            String prefixedSubstGroup = substGroup.getValue();
            String substGroupNsUri = _nsResolver.namespace(getPrefix(
                        prefixedSubstGroup));
            String substGroupLocalName = removePrefix(prefixedSubstGroup);
            QName substGroupQName = QName.getQName(substGroupNsUri,
                    substGroupLocalName);
            graphNode.setSubstGroup(substGroupQName);
        }
    }

    /**
     * Determines whether something is an XML-schema simple type.
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isBuiltInType(QName type) {
        return XMLSchema.isBuiltInType(type);
    }

    /**
     * Retrieves a global construct from an external schema.
     *
     * @param construct DOCUMENT ME!
     * @param namespace DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SchemaNode getGlobalConstructFromOutside(String construct,
        String namespace, String localName) {
        for (int index = 0; index < _includes.size(); index++) {
            SchemaParser includedSchema = (SchemaParser) _includes.get(index);
            SchemaNode container = includedSchema.getGlobalConstructFromAnywhere(construct,
                    namespace, localName);

            if (container != null) {
                return container;
            }
        }

        for (int index = 0; index < _imports.size(); index++) {
            SchemaParser importedSchema = (SchemaParser) _imports.get(index);
            SchemaNode container = importedSchema.getGlobalConstructFromAnywhere(construct,
                    namespace, localName);

            if (container != null) {
                return container;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param construct DOCUMENT ME!
     * @param namespace DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SchemaNode getGlobalConstructFromThisSchema(String construct,
        String namespace, String localName) {
        if (!namespace.equals(_targetNamespace)) {
            // if namespaces don't match, return right away
        }

        QName globalElementQName = QName.getQName(namespace, localName);
        SchemaNode globalElementNode = _schemaGraph.getGlobalConstruct(construct,
                globalElementQName);

        if (globalElementNode != null) {
            return globalElementNode;
        }

        // try to find it in the schema, perhaps it hasn't been processed yet
        List globalElements = XMLUtils.getGlobalElementsWithAttribute(_root,
                construct, getXSDNamespace(), XMLSchema.NAME_ATTRIBUTE,
                localName);
        Element element = (globalElements.size() > 0)
            ? (Element) globalElements.get(0) : null;

        if (element == null) {
            // couldn't find the global element
            return null;
        }

        // passing the schema root as the parent since this is a global construct
        processElementNode(_schemaGraph.getRoot(), element);
        globalElementNode = _schemaGraph.getGlobalConstruct(construct,
                globalElementQName);

        return globalElementNode;
    }

    /**
     * Retrieves a global element with the given name attribute.
     *
     * @param construct DOCUMENT ME!
     * @param namespace DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SchemaNode getGlobalConstructFromAnywhere(String construct,
        String namespace, String localName) {
        SchemaNode globalElementNode = getGlobalConstructFromThisSchema(construct,
                namespace, localName);

        if (globalElementNode == null) {
            globalElementNode = getGlobalConstructFromOutside(construct,
                    namespace, localName);
        }

        return globalElementNode;
    }

    /**
     * Extracts the prefix from a fully qualified name.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String getPrefix(String name) {
        int colon = name.indexOf(":");

        if (colon == -1) {
            return XMLSchema.DEFAULT_NAMESPACE_PREFIX;
        }

        String result = name.substring(0, colon);

        return result;
    }

    /**
     * Removes the prefix from a fully qualified name.
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String removePrefix(String type) {
        int colon = type.indexOf(":");

        if (colon == -1) {
            return type;
        }

        String result = type.substring(colon + 1);

        return result;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    private String getTargetNamespace() {
        return _targetNamespace;
    }

    /**
     * Sets the location of the schema.
     *
     * @param location DOCUMENT ME!
     * @param referenceLocation DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int setLocation(String location, String referenceLocation) {
        location = createSchemaUriString(location, referenceLocation);

        if (location == null) {
            return -1;
        }

        _location = location;

        return 0;
    }

    /**
     * Constructs the schema URI from its base and relative locations.
     *
     * @param baseLocation DOCUMENT ME!
     * @param relativeLocation DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String createSchemaUriString(String baseLocation,
        String relativeLocation) {
        boolean isUri = true;

        // is it already absolute and a URI?
        try {
            URL url = new URL(relativeLocation);
        } catch (MalformedURLException exception) {
            isUri = false;
        }

        // URI and absolute
        if (isUri) {
            return relativeLocation;
        }

        // not URI, or not absolute.
        // is it absolute?
        File schemaFile = new File(relativeLocation);

        // if so it wasn't a URI, make one and return
        if (schemaFile.isAbsolute()) {
            return "file:///" + relativeLocation;
        }

        // not absolute
        baseLocation = baseLocation.replace('\\', '/');

        int index = baseLocation.lastIndexOf('/');

        if (index < 0) {
            return null;
        }

        baseLocation = baseLocation.substring(0, index);
        baseLocation = baseLocation + "/" + relativeLocation;

        try {
            URL url = new URL(baseLocation);
        } catch (MalformedURLException exception) {
            baseLocation = "file:///" + baseLocation;
        }

        return baseLocation;
    }

    /**
     * 
     *
     * @param fullName1 DOCUMENT ME!
     * @param fullName2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isNameEqual(String fullName1, String fullName2) {
        int colon = fullName1.indexOf(":");
        String prefix1;
        String name1;

        if (colon != -1) {
            prefix1 = fullName1.substring(0, colon);
            name1 = fullName1.substring(colon + 1);
        } else {
            prefix1 = XMLSchema.DEFAULT_NAMESPACE_PREFIX;
            name1 = fullName1;
        }

        colon = fullName2.indexOf(":");

        String prefix2;
        String name2;

        if (colon != -1) {
            prefix2 = fullName2.substring(0, colon);
            name2 = fullName2.substring(colon + 1);
        } else {
            prefix2 = XMLSchema.DEFAULT_NAMESPACE_PREFIX;
            name2 = fullName2;
        }

        if (name1.equals(name2)) {
            if (prefix1.equals(XMLSchema.DEFAULT_NAMESPACE_PREFIX) ||
                    prefix2.equals(XMLSchema.DEFAULT_NAMESPACE_PREFIX) ||
                    prefix1.equals(prefix2)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves a schema for the given URL using the HTTP GET method.
     *
     * @param uriString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String doGet(String uriString) {
        URL uri = null;

        try {
            uri = new URL(uriString);
        } catch (MalformedURLException exception) {
            return "";
        }

        String reply = "";

        try {
            InputStream input = uri.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                        input));
            String line = reader.readLine();

            while (line != null) {
                reply += line;
                line = reader.readLine();
            }

            reader.close();
            input.close();
        } catch (Exception exception) {
            return null;
        }

        return reply;
    }

    /**
     * This is the public API used by the GML4J library
     *
     * @param namespace DOCUMENT ME!
     * @param potentialFeature DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /**
     * Determines whether the given element name from the specified
     * namespace is a GML feature.
     *
     * @param namespace DOCUMENT ME!
     * @param potentialFeature DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeature(String namespace, String potentialFeature) {
        QName potentialFeatureQName = QName.getQName(namespace, potentialFeature);
        QName absFeatureTypeQName = QName.getQName(GMLSchema.GML_NS_URI,
                GMLSchema.BASE_FEATURE_TYPE);

        //        QName absFeatureQName =
        //            QName.getQName(GMLSchema.GML_NS_URI, GMLSchema.BASE_FEATURE_ELEMENT);
        SchemaNode potentialFeatureNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                namespace, potentialFeature);

        if (potentialFeatureNode == null) {
            return false;
        }

        return potentialFeatureNode.isSuperTypeOrThisType(absFeatureTypeQName);

        //        return isSubstitutable(potentialFeatureQName, absFeatureQName);
    }

    /**
     * Determines whether the given element name from the specified
     * namespace is a GML geometry.
     *
     * @param namespace DOCUMENT ME!
     * @param potentialGeometry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeometry(String namespace, String potentialGeometry) {
        QName geomQName = QName.getQName(namespace, potentialGeometry);
        QName absGeomTypeQName = QName.getQName(GMLSchema.GML_NS_URI,
                GMLSchema.BASE_GEOMETRY_TYPE);

        //        QName absGeomQName =
        //            QName.getQName(GMLSchema.GML_NS_URI, GMLSchema.BASE_GEOMETRY_ELEMENT);
        SchemaNode potentialGeomNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                namespace, potentialGeometry);

        if (potentialGeomNode == null) {
            return false;
        }

        return potentialGeomNode.isSuperTypeOrThisType(absGeomTypeQName);

        //        return isSubstitutable(geomQName, absGeomQName);
    }

    /**
     * Determines whether the element name from the namespace is a
     * (top-level) property of a feature or geometry.
     *
     * @param propertyNamespace DOCUMENT ME!
     * @param potentialProperty DOCUMENT ME!
     * @param parentNamespace DOCUMENT ME!
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isProperty(String propertyNamespace,
        String potentialProperty, String parentNamespace, String parent) {
        QName parentQName = QName.getQName(parentNamespace, parent);
        QName propertyQName = QName.getQName(propertyNamespace,
                potentialProperty);

        // exactly this child does not exist, try to find an equivalent one
        SchemaNode parentNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                parentQName.getNsUri(), parentQName.getLocalName());

        if (parentNode == null) {
            return false;
        }

        for (int ii = 0; ii < parentNode.getChildCount(); ii++) {
            SchemaNode childNode = parentNode.getChildAt(ii);
            QName childName = childNode.getName();

            if (childName.equals(propertyQName) ||
                    isSubstitutable(propertyQName, childNode.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether the given element name from the specified
     * namespace is a GML feature collection.
     *
     * @param namespace DOCUMENT ME!
     * @param potentialFeatureCollection DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFeatureCollection(String namespace,
        String potentialFeatureCollection) {
        QName featCollQName = QName.getQName(namespace,
                potentialFeatureCollection);
        SchemaNode featCollNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                namespace, potentialFeatureCollection);

        if (featCollNode == null) {
            // not in schema
            return false;
        }

        //        QName baseFeatCollQName =
        //            QName.getQName(GMLSchema.GML_NS_URI,
        //                           GMLSchema.BASE_FEATURE_COLLECTION_ELEMENT);
        QName baseFeatCollTypeQName = QName.getQName(GMLSchema.GML_NS_URI,
                GMLSchema.BASE_FEATURE_COLLECTION_TYPE);

        return featCollNode.isSuperTypeOrThisType(baseFeatCollTypeQName);

        //        return isSubstitutable(featCollQName, baseFeatCollQName);
    }

    /**
     * Determines whether the given element name from the specified
     * namespace is a GML geometry collection.
     *
     * @param namespace DOCUMENT ME!
     * @param potentialGeometryCollection DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGeometryCollection(String namespace,
        String potentialGeometryCollection) {
        QName geomCollQName = QName.getQName(namespace,
                potentialGeometryCollection);

        //        QName absGeomCollQName =
        //            QName.getQName(GMLSchema.GML_NS_URI,
        //                           GMLSchema.BASE_GEOMETRY_COLLECTION_ELEMENT);
        QName absGeomCollTypeQName = QName.getQName(GMLSchema.GML_NS_URI,
                GMLSchema.BASE_GEOMETRY_COLLECTION_TYPE);

        SchemaNode geomCollNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                namespace, potentialGeometryCollection);

        if (geomCollNode == null) {
            // not in schema
            return false;
        }

        return geomCollNode.isSuperTypeOrThisType(absGeomCollTypeQName);

        //        return isSubstitutable(geomCollQName, absGeomCollQName);
    }

    /**
     * Determines whether an element is a Coord
     *
     * @param namespace DOCUMENT ME!
     * @param potentialCoord DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCoord(String namespace, String potentialCoord) {
        QName coordQName = QName.getQName(namespace, potentialCoord);
        QName baseCoordQName = QName.getQName(GMLSchema.GML_NS_URI,
                GMLSchema.BASE_COORD_TYPE);

        if (_schemaGraph.isOfType(coordQName, baseCoordQName)) {
            return true;
        }

        // go over includes and imports
        ListIterator schemaIterator = _includes.listIterator();

        while (schemaIterator.hasNext()) {
            SchemaParser parser = (SchemaParser) schemaIterator.next();

            if (parser.isCoord(namespace, potentialCoord)) {
                return true;
            }
        }

        schemaIterator = _imports.listIterator();

        while (schemaIterator.hasNext()) {
            SchemaParser parser = (SchemaParser) schemaIterator.next();

            if (parser.isCoord(namespace, potentialCoord)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines whether an element is a Coordinates.
     *
     * @param namespace DOCUMENT ME!
     * @param potentialCoordinates DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCoordinates(String namespace, String potentialCoordinates) {
        QName coordinatesQName = QName.getQName(namespace, potentialCoordinates);
        QName baseCoordinatesQName = QName.getQName(GMLSchema.GML_NS_URI,
                GMLSchema.BASE_COORDINATES_TYPE);

        if (_schemaGraph.isOfType(coordinatesQName, baseCoordinatesQName)) {
            return true;
        }

        // go over includes and imports
        ListIterator schemaIterator = _includes.listIterator();

        while (schemaIterator.hasNext()) {
            SchemaParser parser = (SchemaParser) schemaIterator.next();

            if (parser.isCoordinates(namespace, potentialCoordinates)) {
                return true;
            }
        }

        schemaIterator = _imports.listIterator();

        while (schemaIterator.hasNext()) {
            SchemaParser parser = (SchemaParser) schemaIterator.next();

            if (parser.isCoordinates(namespace, potentialCoordinates)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Tests whether an element is in an substitution group.
     *
     * @param substitutionCandidateNamespace The namespace of the element that
     *        is tested for substitutability.
     * @param substitutionCandidate The local name of the element that is
     *        tested for substitutability.
     * @param substitutionGroupHeadNamespace The namespace of the substitution
     *        group head element.
     * @param substitutionGroupHead The local name of the substitution group
     *        head element.
     *
     * @return DOCUMENT ME!
     */
    public boolean testSubstitutability(String substitutionCandidateNamespace,
        String substitutionCandidate, String substitutionGroupHeadNamespace,
        String substitutionGroupHead) {
        QName scQName = QName.getQName(substitutionCandidateNamespace,
                substitutionCandidate);
        QName sghQName = QName.getQName(substitutionGroupHeadNamespace,
                substitutionGroupHead);
        boolean result = isSubstitutable(scQName, sghQName);

        return result;
    }

    /**
     * Returns the prefix of the XML schema namespace. Usually its xsd
     * or an empty string.
     *
     * @return DOCUMENT ME!
     */
    private Namespace getXSDNamespace() {
        Namespace xsdNs = Namespace.getNamespace(XMLSchema.XML_SCHEMA_NAMESPACE);

        return xsdNs;
    }

    /**
     * Tests substitutability of an element with another element.
     *
     * @param elementName DOCUMENT ME!
     * @param substGroupName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean isSubstitutable(QName elementName, QName substGroupName) {
        SchemaNode globElemNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                elementName.getNsUri(), elementName.getLocalName());
        SchemaNode substGroupNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                substGroupName.getNsUri(), substGroupName.getLocalName());

        if ((globElemNode == null) || (substGroupNode == null)) {
            // could find the global elements in question
            return false;
        }

        if (!globElemNode.isSuperTypeOrThisType(substGroupNode.getType())) {
            // there is no inheritance relationship between the respective types
            return false;
        }

        QName globElemSubstGroup = globElemNode.getSubstGroup();

        while (globElemSubstGroup != null) {
            if (globElemSubstGroup.equals(substGroupName)) {
                return true;
            }

            SchemaNode globalElemSubstGroupNode = getGlobalConstructFromAnywhere(XMLSchema.ELEMENT_ELEMENT,
                    globElemSubstGroup.getNsUri(),
                    globElemSubstGroup.getLocalName());
            globElemSubstGroup = (globalElemSubstGroupNode == null) ? null
                                                                    : globalElemSubstGroupNode.getSubstGroup();
        }

        // no more substitution groups to look for
        return false;
    }

    /**
     * This are the helper methods for API
     *
     * @param unknown DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /**
     * 
     *
     * @param unknown DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String resolveNamespace(String unknown) {
        //is it a prefix?
        String result = _nsResolver.namespace(unknown);

        if (result != null) {
            return unknown;
        }

        // no. Is it a namespace?
        result = _nsResolver.prefix(unknown);

        if (result != null) {
            return result;
        }

        // no, it simply doesn't exist
        return "";
    }
}
