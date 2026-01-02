package org.jscience.politics.loaders;

import org.jscience.politics.Country;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FactbookLoaderTest {

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

        FactbookLoader loader = new FactbookLoader();
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

        FactbookLoader loader = new FactbookLoader();
        try (InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
            List<Country> countries = loader.load(is);
            assertTrue(countries.isEmpty());
        }
    }

    @Test
    void testLoad_MalformedXml() {
        String xml = "<countries><country>Unclosed Tag";

        FactbookLoader loader = new FactbookLoader();
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

        assertThrows(Exception.class, () -> loader.load(is));
    }
}
