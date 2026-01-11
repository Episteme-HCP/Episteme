/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.mathematics.loaders.openmath;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.jscience.mathematics.loaders.Deserializer;
import org.jscience.mathematics.numbers.real.Real;

/**
 * Deserializer for OpenMath XML format.
 * <p>
 * Reads OpenMath XML and constructs generic Objects.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class OpenMathReader implements Deserializer<Object>, org.jscience.io.ResourceReader<Object> {

    @Override
    public Object load(String resourceId) throws Exception {
        if (resourceId.startsWith("http") || resourceId.startsWith("file:")) {
            try (InputStream is = new java.net.URI(resourceId).toURL().openStream()) {
                return read(is);
            }
        }
        try (InputStream is = getClass().getResourceAsStream(resourceId)) {
            if (is == null)
                throw new java.io.IOException("OpenMath resource not found: " + resourceId);
            return read(is);
        }
    }

    @Override
    public String getName() {
        return "loader.openmath.name";
    }

    @Override
    public String getCategory() {
        return "category.mathematics";
    }

    @Override
    public String getDescription() {
        return "loader.openmath.desc";
    }

    @Override
    public String getResourcePath() {
        return "/";
    }

    @Override
    public Class<Object> getResourceType() {
        return Object.class;
    }

    @Override
    public Object read(InputStream input) throws IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(input);
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if ("OMOBJ".equals(reader.getLocalName())) {
                        return parseObject(reader);
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new IOException("OpenMath parsing failed", e);
        }
        return null;
    }

    private Object parseObject(XMLStreamReader reader) throws XMLStreamException {
        // Move to next element start
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("OMI".equals(tag)) {
                    return Integer.parseInt(reader.getElementText());
                } else if ("OMF".equals(tag)) {
                    String dec = reader.getAttributeValue(null, "dec");
                    if (dec != null) {
                        // Return JScience Real
                        return Real.of(dec);
                    }
                    // Handle hex?
                } else if ("OMSTR".equals(tag)) {
                    return reader.getElementText();
                } else if ("OMA".equals(tag)) {
                    // Application - handle complex, matrices later
                    // Skip for now or return placeholder
                    skipElement(reader);
                    return "OMA (Complex Structure)";
                } else {
                    // OMS, OMV, etc.
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT && "OMOBJ".equals(reader.getLocalName())) {
                break;
            }
        }
        return null;
    }

    private void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        while (reader.hasNext() && depth > 0) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT)
                depth++;
            if (event == XMLStreamConstants.END_ELEMENT)
                depth--;
        }
    }
}
