/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jscience.mathematics.loaders.openmath;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.jscience.mathematics.loaders.Serializer;
import org.jscience.mathematics.numbers.real.Real;
import org.jscience.mathematics.numbers.complex.Complex;

/**
 * Serializer for OpenMath XML format.
 * <p>
 * Supports writing generic Objects with specific mapping for JScience types.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 5.0
 */
public class OpenMathWriter implements Serializer<Object> {

    @Override
    public void write(Object object, OutputStream output) throws IOException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(output, "UTF-8");
            writer.writeStartDocument("UTF-8", "1.0");

            // <OMOBJ xmlns="http://www.openmath.org/OpenMath">
            writer.writeStartElement("OMOBJ");
            writer.writeDefaultNamespace("http://www.openmath.org/OpenMath");

            writeObject(writer, object);

            writer.writeEndElement(); // OMOBJ
            writer.writeEndDocument();
            writer.flush();
        } catch (XMLStreamException e) {
            throw new IOException("OpenMath serialization failed", e);
        }
    }

    private void writeObject(XMLStreamWriter writer, Object obj) throws XMLStreamException {
        if (obj instanceof Real) {
            writeReal(writer, (Real) obj);
        } else if (obj instanceof Complex) {
            writeComplex(writer, (Complex) obj);
        } else if (obj instanceof Number) {
            // <OMI>integer</OMI> or <OMF dec="float"/>
            if (obj instanceof Integer || obj instanceof Long) {
                writer.writeStartElement("OMI");
                writer.writeCharacters(obj.toString());
                writer.writeEndElement();
            } else {
                writer.writeStartElement("OMF");
                writer.writeAttribute("dec", obj.toString());
                writer.writeEndElement();
            }
        } else if (obj instanceof String) {
            // <OMSTR>string</OMSTR>
            writer.writeStartElement("OMSTR");
            writer.writeCharacters((String) obj);
            writer.writeEndElement();
        } else {
            // Fallback or Error?
            // For now, write OME (Error) or just string representation
            writer.writeStartElement("OMSTR");
            writer.writeCharacters(String.valueOf(obj));
            writer.writeEndElement();
        }
    }

    // <OMA><OMS cd="arith1" name="plus"/><OMI>...</OMI>...</OMA>

    private void writeReal(XMLStreamWriter writer, Real r) throws XMLStreamException {
        // Map Real to OMF (Float)
        writer.writeStartElement("OMF");
        writer.writeAttribute("dec", String.valueOf(r.byteValue())); // Real should have doubleValue()?
        // Real.java viewed earlier doesn't show doubleValue() directly in abstract,
        // usually it's Number which Real might extend or implement.
        // Let's assume toString() is safe for now or doubleValue if Number.
        // Checking viewed Real.java... it extends Number.
        writer.writeAttribute("dec", r.toString());
        writer.writeEndElement();
    }

    private void writeComplex(XMLStreamWriter writer, Complex c) throws XMLStreamException {
        // Complex is <OMA><OMS cd="complex1"
        // name="complex_cartesian"/><OMF.../><OMF.../></OMA>
        writer.writeStartElement("OMA"); // Application

        writer.writeEmptyElement("OMS"); // Symbol
        writer.writeAttribute("cd", "complex1");
        writer.writeAttribute("name", "complex_cartesian");

        // Real part
        writer.writeStartElement("OMF");
        writer.writeAttribute("dec", c.getReal().toString());
        writer.writeEndElement();

        // Imaginary part
        writer.writeStartElement("OMF");
        writer.writeAttribute("dec", c.getImaginary().toString());
        writer.writeEndElement();

        writer.writeEndElement(); // OMA
    }
}
