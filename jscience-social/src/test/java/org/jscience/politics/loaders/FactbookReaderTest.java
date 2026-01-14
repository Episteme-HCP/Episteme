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

package org.jscience.politics.loaders;

import org.jscience.politics.Country;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FactbookReaderTest {

    @Test
    void testLoad_ValidXml() throws Exception {
        String xml = """
                <countries>
                    <country name="Testland" code="TST">
                        <capital>Test City</capital>
                        <area>10,000 sq km</area>
                        <population>1,000,000</population>
                        <region>Europe</region>
                    </country>
                    <country name="Otherland" code="OTH">
                        <capital>Other City</capital>
                        <population>500000</population>
                    </country>
                </countries>
                """;

        FactbookReader loader = new FactbookReader();
        try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            List<Country> countries = loader.load(is);

            assertEquals(2, countries.size());

            Country c1 = countries.get(0);
            assertEquals("Testland", c1.getName());
            assertEquals("TST", c1.getAlpha2());
            assertEquals("Test City", c1.getCapital());
            assertEquals(10000.0, c1.getAreaSqKm().doubleValue());
            assertEquals(1000000L, c1.getPopulationLong());
            assertEquals("Europe", c1.getContinent());

            Country c2 = countries.get(1);
            assertEquals("Otherland", c2.getName());
            assertEquals("OTH", c2.getAlpha2());
            assertEquals(500000L, c2.getPopulationLong());
        }
    }

    @Test
    void testLoad_Empty() throws Exception {
        String xml = "<countries></countries>";

        FactbookReader loader = new FactbookReader();
        try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            List<Country> countries = loader.load(is);
            assertTrue(countries.isEmpty());
        }
    }

    @Test
    void testLoad_MalformedXml() {
        String xml = "<countries><country>Unclosed Tag";

        FactbookReader loader = new FactbookReader();
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        assertThrows(Exception.class, () -> loader.load(is));
    }
}
