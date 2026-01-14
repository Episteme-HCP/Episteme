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

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * Helps in resolving namespaces. Namespaces are statically maintained, so
 * this could pose a problem for program dealing with different files and
 * perhaps namespaces with conflicting Ids.
 *
 * @author Milan Trninic, revised by Aleksandar Milanovic
 * @version 1.1
 */
public class NamespaceResolver {
    /** DOCUMENT ME! */
    private static NamespaceResolver _instance = null;

    /** DOCUMENT ME! */
    private HashMap _byPrefix = new HashMap();

    /** DOCUMENT ME! */
    private HashMap _byUri = new HashMap();

/**
     *
     */
    private NamespaceResolver() {
        _instance = this;
        _byPrefix.put(XMLSchema.XMLNS_ATTR, XMLSchema.XML_SCHEMA_NAMESPACE);
        _byPrefix.put(XMLSchema.XML_PREFIX, XMLSchema.XML_NAMESPACE);
        _byUri.put(XMLSchema.XML_SCHEMA_NAMESPACE, XMLSchema.XMLNS_ATTR);
        _byUri.put(XMLSchema.XML_NAMESPACE, XMLSchema.XML_PREFIX);
    }

    /**
     * Returns an instance of this class' object.
     *
     * @return DOCUMENT ME!
     */
    public static NamespaceResolver getInstance() {
        if (_instance == null) {
            _instance = new NamespaceResolver();
        }

        return _instance;
    }

    /**
     * 
     *
     * @param prefix DOCUMENT ME!
     * @param uri DOCUMENT ME!
     */
    public void addNamespace(String prefix, String uri) {
        _byPrefix.put(prefix, uri);
        _byUri.put(uri, prefix);
    }

    /**
     * 
     *
     * @param prefix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String resolveNamespacePrefix(String prefix) {
        return (String) _byPrefix.get(prefix);
    }

    /**
     * 
     *
     * @param prefix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String namespace(String prefix) {
        return resolveNamespacePrefix(prefix);
    }

    /**
     * 
     *
     * @param namespace DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String prefix(String namespace) {
        return (String) _byUri.get(namespace);
    }

    /**
     * 
     *
     * @param prefix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean prefixExists(String prefix) {
        return (!(namespace(prefix) == null));
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public String[] getAllPrefixes() {
        int size = _byPrefix.size();
        Set values = _byPrefix.keySet();
        String[] prefixes = new String[size];
        values.toArray(prefixes);

        return prefixes;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public String[] getAllNamespaces() {
        int size = _byPrefix.size();
        Collection values = _byPrefix.values();
        String[] namespaces = new String[size];
        values.toArray(namespaces);

        return namespaces;
    }

    /**
     * 
     *
     * @return DOCUMENT ME!
     */
    public String[] getAllExpressions() {
        String value = _byPrefix.toString();
        value = value.substring(1, value.length() - 1);

        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        int size = tokenizer.countTokens();
        String[] expressions = new String[size];
        int index = 0;

        while (tokenizer.hasMoreTokens()) {
            expressions[index++] = tokenizer.nextToken();
        }

        return expressions;
    }
}
