package org.jscience.mathematics.logic.connector;

import org.junit.jupiter.api.Test;
import org.jscience.mathematics.logic.connector.MetamathImporter;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MetamathTest {

    @Test
    public void testImportOnePlusOne() throws IOException, FormalSystemImporter.ParseException {
        MetamathImporter importer = new MetamathImporter();
        String filePath = "src/test/resources/one_plus_one.mm";

        try (FileReader reader = new FileReader(filePath)) {
            Map<String, Object> result = importer.importSystem(reader);

            assertNotNull(result);

            List<String> constants = (List<String>) result.get("constants");
            assertTrue(constants.contains("0"));
            assertTrue(constants.contains("S"));
            assertTrue(constants.contains("+"));
            assertTrue(constants.contains("="));

            Map<String, String> axioms = (Map<String, String>) result.get("axioms");
            // Note: The current importer implementation uses auto-generated keys "axiom_N"
            // It doesn't capture the labels "ax-1", etc. yet.
            // We should verify that we have axioms.
            assertFalse(axioms.isEmpty());

            // Check content of axioms roughly
            boolean foundAdd0 = false;
            for (String content : axioms.values()) {
                if (content.contains("a + 0 = a")) {
                    foundAdd0 = true;
                    break;
                }
            }
            assertTrue(foundAdd0, "Should find addition axiom");

            Map<String, String> theorems = (Map<String, String>) result.get("theorems");
            assertFalse(theorems.isEmpty());

            boolean foundThm = false;
            for (String content : theorems.values()) {
                if (content.contains("1 + 1 = 2")) {
                    foundThm = true;
                    break;
                }
            }
            assertTrue(foundThm, "Should find 1+1=2 theorem");
        }
    }
}
