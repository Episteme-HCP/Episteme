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
 * package name.
 */
package org.jscience.ml.gml.xml.schema;

import org.jscience.ml.gml.xml.XMLException;

import java.io.InputStream;

import java.lang.reflect.Field;

import java.util.Hashtable;


/**
 * Encapsulates the XML schema but this version does not do much on its
 * own. The schema parsing job is done by SchemaParser. Every XML schema has
 * therefore a schema parser, which is also available to all subclasses via
 * the getParser() method.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 *
 * @see org.jscience.ml.gml.xml.schema.SchemaParser
 */
public class XMLSchema implements SchemaLocator {
    /** DOCUMENT ME! */
    private static Hashtable builtInTypes_;

    static {
        // populate the table of built-in types
        builtInTypes_ = new Hashtable();

        Field[] fields = XMLSchema.class.getFields();

        for (int ii = 0; ii < fields.length; ii++) {
            if (fields[ii].getName().endsWith("SIMPLE_TYPE")) {
                try {
                    String fieldValue = (String) fields[ii].get(null);
                    builtInTypes_.put(fieldValue, fieldValue);
                } catch (IllegalAccessException iae) {
                    // do nothing
                }
            }
        }
    }

    /** DOCUMENT ME! */
    public static final String DEFAULT_NAMESPACE_PREFIX = "";

    /** DOCUMENT ME! */
    public static final String TARGET_NAMESPACE_ATTRIBUTE = "targetNamespace";

    /** DOCUMENT ME! */
    public static final String NAME_ATTRIBUTE = "name";

    /** DOCUMENT ME! */
    public static final String REF_ATTRIBUTE = "ref";

    /** DOCUMENT ME! */
    public static final String TYPE_ATTRIBUTE = "type";

    /** DOCUMENT ME! */
    public static final String ABSTRACT_ATTRIBUTE = "abstract";

    /** DOCUMENT ME! */
    public static final String VALUE_ATTRIBUTE = "value";

    /** DOCUMENT ME! */
    public static final String BASE_ATTRIBUTE = "base";

    /** DOCUMENT ME! */
    public static final String SUBSTITUTIONGROUP_ATTRIBUTE = "substitutionGroup";

    /** DOCUMENT ME! */
    public static final String SCHEMA_LOCATION_ATTRIBUTE = "schemaLocation";

    /** DOCUMENT ME! */
    public static final String NAMESPACE_ATTRIBUTE = "namespace";

    /** DOCUMENT ME! */
    public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";

    /** DOCUMENT ME! */
    public static final String XML_SCHEMA_INSTANCE_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

    /** DOCUMENT ME! */
    public static final String XML_PREFIX = "xml";

    /** DOCUMENT ME! */
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";

    /** DOCUMENT ME! */
    public static final String XMLNS_ATTR = "xmlns";

    /** DOCUMENT ME! */
    public static final String ALL_ELEMENT = "all";

    /** DOCUMENT ME! */
    public static final String ANNOTATION_ELEMENT = "annotation";

    /** DOCUMENT ME! */
    public static final String ANY_ELEMENT = "any";

    /** DOCUMENT ME! */
    public static final String ANY_ATTRIBUTE_ELEMENT = "anyAttribute";

    /** DOCUMENT ME! */
    public static final String APP_INFO_ELEMENT = "appInfo";

    /** DOCUMENT ME! */
    public static final String ATTRIBUTE_ELEMENT = "attribute";

    /** DOCUMENT ME! */
    public static final String ATTRIBUTE_GROUP_ELEMENT = "attributeGroup";

    /** DOCUMENT ME! */
    public static final String CHOICE_ELEMENT = "choice";

    /** DOCUMENT ME! */
    public static final String COMPLEX_CONTENT_ELEMENT = "complexContent";

    /** DOCUMENT ME! */
    public static final String COMPLEX_TYPE_ELEMENT = "complexType";

    /** DOCUMENT ME! */
    public static final String DOCUMENTATION_ELEMENT = "documentation";

    /** DOCUMENT ME! */
    public static final String ELEMENT_ELEMENT = "element";

    /** DOCUMENT ME! */
    public static final String ENUMERATION_ELEMENT = "enumeration";

    /** DOCUMENT ME! */
    public static final String EXTENSION_ELEMENT = "extension";

    /** DOCUMENT ME! */
    public static final String FIELD_ELEMENT = "field";

    /** DOCUMENT ME! */
    public static final String GROUP_ELEMENT = "group";

    /** DOCUMENT ME! */
    public static final String IMPORT_ELEMENT = "import";

    /** DOCUMENT ME! */
    public static final String INCLUDE_ELEMENT = "include";

    /** DOCUMENT ME! */
    public static final String KEY_ELEMENT = "key";

    /** DOCUMENT ME! */
    public static final String KEY_REF_ELEMENT = "keyref";

    /** DOCUMENT ME! */
    public static final String LENGTH_ELEMENT = "length";

    /** DOCUMENT ME! */
    public static final String LIST_ELEMENT = "list";

    /** DOCUMENT ME! */
    public static final String MAX_INCLUSIVE_ELEMENT = "maxInclusive";

    /** DOCUMENT ME! */
    public static final String MAX_LENGTH_ELEMENT = "maxLength";

    /** DOCUMENT ME! */
    public static final String MIN_INCLUSIVE_ELEMENT = "minInclusive";

    /** DOCUMENT ME! */
    public static final String MIN_LENGTH_ELEMENT = "minLength";

    /** DOCUMENT ME! */
    public static final String PATTERN_ELEMENT = "pattern";

    /** DOCUMENT ME! */
    public static final String REDEFINE_ELEMENT = "redefine";

    /** DOCUMENT ME! */
    public static final String RESTRICTION_ELEMENT = "restriction";

    /** DOCUMENT ME! */
    public static final String SCHEMA_ELEMENT = "schema";

    /** DOCUMENT ME! */
    public static final String SELECTOR_ELEMENT = "selector";

    /** DOCUMENT ME! */
    public static final String SEQUENCE_ELEMENT = "sequence";

    /** DOCUMENT ME! */
    public static final String SIMPLE_CONTENT_ELEMENT = "simpleContent";

    /** DOCUMENT ME! */
    public static final String SIMPLE_TYPE_ELEMENT = "simpleType";

    /** DOCUMENT ME! */
    public static final String UNION_ELEMENT = "union";

    /** DOCUMENT ME! */
    public static final String UNIQUE_ELEMENT = "unique";

    //    public static final String ANY_TYPE = "anyType";
    /** DOCUMENT ME! */
    public static final String REFERENCE_XML_TYPE = "ref"; // used in SchemaNode

    /** DOCUMENT ME! */
    public static final String XML_SCHEMA_ELEMENT_TYPE = "<XML_SCHEMA_ELEMENT_TYPE>";

    /** DOCUMENT ME! */
    public static final String RECURSIVE_ELEMENT = "recursive";

    /** DOCUMENT ME! */
    public static final String STRING_SIMPLE_TYPE = "string";

    /** DOCUMENT ME! */
    public static final String NORMALIZED_STRING_SIMPLE_TYPE = "normalizedString";

    /** DOCUMENT ME! */
    public static final String TOKEN_SIMPLE_TYPE = "token";

    /** DOCUMENT ME! */
    public static final String BYTE_SIMPLE_TYPE = "byte";

    /** DOCUMENT ME! */
    public static final String UNSIGNED_BYTE_SIMPLE_TYPE = "unsignedByte";

    /** DOCUMENT ME! */
    public static final String BASE64_BINARY_SIMPLE_TYPE = "base64Binary";

    /** DOCUMENT ME! */
    public static final String HEX_BINARY_SIMPLE_TYPE = "hexBinary";

    /** DOCUMENT ME! */
    public static final String INTEGER_SIMPLE_TYPE = "integer";

    /** DOCUMENT ME! */
    public static final String POSITIVE_INTEGER_SIMPLE_TYPE = "positiveInteger";

    /** DOCUMENT ME! */
    public static final String NEGATIVE_INTEGER_SIMPLE_TYPE = "negativeInteger";

    /** DOCUMENT ME! */
    public static final String NON_NEGATIVE_INTEGER_SIMPLE_TYPE = "nonNegativeInteger";

    /** DOCUMENT ME! */
    public static final String NON_POSITIVE_INTEGER_SIMPLE_TYPE = "nonPositiveInteger";

    /** DOCUMENT ME! */
    public static final String INT_SIMPLE_TYPE = "int";

    /** DOCUMENT ME! */
    public static final String UNSIGNED_INT_SIMPLE_TYPE = "unsignedInt";

    /** DOCUMENT ME! */
    public static final String LONG_SIMPLE_TYPE = "long";

    /** DOCUMENT ME! */
    public static final String UNSIGNED_LONG_SIMPLE_TYPE = "unsignedLong";

    /** DOCUMENT ME! */
    public static final String SHORT_SIMPLE_TYPE = "short";

    /** DOCUMENT ME! */
    public static final String UNSIGNED_SHORT_SIMPLE_TYPE = "unsignedShort";

    /** DOCUMENT ME! */
    public static final String DECIMAL_SIMPLE_TYPE = "decimal";

    /** DOCUMENT ME! */
    public static final String FLOAT_SIMPLE_TYPE = "float";

    /** DOCUMENT ME! */
    public static final String DOUBLE_SIMPLE_TYPE = "double";

    /** DOCUMENT ME! */
    public static final String BOOLEAN_SIMPLE_TYPE = "boolean";

    /** DOCUMENT ME! */
    public static final String TIME_SIMPLE_TYPE = "time";

    /** DOCUMENT ME! */
    public static final String DATE_TIME_SIMPLE_TYPE = "dateTime";

    /** DOCUMENT ME! */
    public static final String DURATION_SIMPLE_TYPE = "duration";

    /** DOCUMENT ME! */
    public static final String DATE_SIMPLE_TYPE = "date";

    /** DOCUMENT ME! */
    public static final String G_MONTH_SIMPLE_TYPE = "gMonth";

    /** DOCUMENT ME! */
    public static final String G_YEAR_SIMPLE_TYPE = "gYear";

    /** DOCUMENT ME! */
    public static final String G_YEAR_MONTH_SIMPLE_TYPE = "gYearMonth";

    /** DOCUMENT ME! */
    public static final String G_DAY_SIMPLE_TYPE = "gDay";

    /** DOCUMENT ME! */
    public static final String G_MONTH_DAY_SIMPLE_TYPE = "gMonthDay";

    /** DOCUMENT ME! */
    public static final String NAME_SIMPLE_TYPE = "Name";

    /** DOCUMENT ME! */
    public static final String QNAME_SIMPLE_TYPE = "QName";

    /** DOCUMENT ME! */
    public static final String NC_NAME_SIMPLE_TYPE = "NCName";

    /** DOCUMENT ME! */
    public static final String ANY_URI_SIMPLE_TYPE = "anyUri";

    /** DOCUMENT ME! */
    public static final String LANGUAGE_SIMPLE_TYPE = "language";

    /** DOCUMENT ME! */
    public static final String ID_SIMPLE_TYPE = "ID";

    /** DOCUMENT ME! */
    public static final String ID_REF_SIMPLE_TYPE = "IDREF";

    /** DOCUMENT ME! */
    public static final String ID_REFS_SIMPLE_TYPE = "IDREFS";

    /** DOCUMENT ME! */
    public static final String ENTITY_SIMPLE_TYPE = "ENTITY";

    /** DOCUMENT ME! */
    public static final String ENTITIES_SIMPLE_TYPE = "ENTITIES";

    /** DOCUMENT ME! */
    public static final String NOTATION_SIMPLE_TYPE = "NOTATION";

    /** DOCUMENT ME! */
    public static final String NM_TOKEN_SIMPLE_TYPE = "NMTOKEN";

    /** DOCUMENT ME! */
    public static final String NM_TOKENS_SIMPLE_TYPE = "NMTOKENS";

    // this is not really a simple type but any type :)
    /** DOCUMENT ME! */
    public static final String ANY_TYPE_SIMPLE_TYPE = "anyType";

    // deprecated types
    //    public static final String BINARY_SIMPLE_TYPE = "binary";
    //    public static final String TIME_DURATION_SIMPLE_TYPE = "timeDuration";
    //    public static final String RECURRING_DURATION_SIMPLE_TYPE = "recurringDuration";
    //    public static final String TIME_INSTANT_SIMPLE_TYPE = "timeInstant";
    //    public static final String TIME_PERIOD_SIMPLE_TYPE = "timePeriod";
    //    public static final String MONTH_SIMPLE_TYPE = "month";
    //    public static final String YEAR_SIMPLE_TYPE = "year";
    //    public static final String CENTURY_SIMPLE_TYPE = "century";
    //    public static final String RECURRING_DATE_SIMPLE_TYPE = "recurringDate";
    //    public static final String RECURRING_DAY_SIMPLE_TYPE = "recurringDay";
    // the schema parser object, which actually does the whole job
    /** DOCUMENT ME! */
    private SchemaParser parser_;

/**
     * Passes the location to the schema parser object.
     *
     * @param location DOCUMENT ME!
     * @throws XMLException Thrown when the schema could not be parsed.
     */
    public XMLSchema(String location) throws XMLException {
        parser_ = new SchemaParser();

        if (parser_.parse(location) == -1) {
            throw new XMLException("Could not parse the schema at location " +
                location);
        }
    }

    // SchemaLocator interface implementation
    // !! Currently not implemented
    /**
     * 
     *
     * @param url DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws . DOCUMENT ME!
     */
    public XMLSchema retrieveSchemaFromURL(String url) {
        throw new java.lang.NoSuchMethodError("retrieveSchemaFromURL");
    }

    /**
     * 
     *
     * @param fileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws . DOCUMENT ME!
     */
    public XMLSchema retrieveSchemaFromFile(String fileName) {
        throw new java.lang.NoSuchMethodError("retrieveSchemaFromFile");
    }

    /**
     * 
     *
     * @param schemaStream DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws . DOCUMENT ME!
     */
    public XMLSchema retrieveSchemaFromStream(InputStream schemaStream) {
        throw new java.lang.NoSuchMethodError("retrieveSchemaFromStream");
    }

    /**
     * Returns the schema parser (to subclasses).
     *
     * @return DOCUMENT ME!
     */
    protected SchemaParser getParser() {
        return parser_;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isBuiltInType(QName type) {
        return type.getNsUri().equals(XML_SCHEMA_NAMESPACE) &&
        builtInTypes_.containsKey(type.getLocalName());
    }
}
