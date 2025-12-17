package org.jscience.resources.chemistry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jscience.chemistry.Element;
import org.jscience.chemistry.PeriodicTable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import java.util.logging.Logger;

/**
 * Loads chemistry data (elements, molecules) from JSON.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class ChemistryDataLoader {

    private static final Logger LOGGER = Logger.getLogger(ChemistryDataLoader.class.getName());
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static void loadElements() {
        try (InputStream is = ChemistryDataLoader.class.getResourceAsStream("/org/jscience/chemistry/elements.json")) {
            if (is == null) {
                LOGGER.warning("elements.json not found in /org/jscience/chemistry/");
                return;
            }

            List<ElementData> elements = MAPPER.readValue(is, new TypeReference<List<ElementData>>() {
            });

            // Populate PeriodicTable
            for (ElementData data : elements) {
                // Map string category to enum (simple mapping for now, defaults to UNKNOWN if
                // not found)
                Element.ElementCategory cat = Element.ElementCategory.UNKNOWN;
                try {
                    cat = Element.ElementCategory.valueOf(data.category.toUpperCase().replace(" ", "_"));
                } catch (Exception e) {
                    // ignore, keep unknown
                }

                Element element = new Element(
                        data.atomicNumber,
                        data.symbol,
                        data.name,
                        org.jscience.measure.Quantities.create(data.atomicMass,
                                org.jscience.measure.Units.GRAM.multiply(1.66053906660e-24)), // AMU to kg approx (or
                                                                                              // use Unified Atomic Mass
                                                                                              // Unit if available)
                        0, 0, // Group/Period placeholders
                        cat,
                        data.electronegativity,
                        null, null // Melting/Boiling point unknown
                );

                PeriodicTable.registerElement(element);
                LOGGER.info("Registered element: " + data.symbol);
            }

        } catch (IOException e) {
            LOGGER.severe("Failed to load elements.json: " + e.getMessage());
        }
    }

    // DTO for JSON mapping
    private static class ElementData {
        public String name;
        public String symbol;
        public int atomicNumber;
        public double atomicMass;
        public String category;
        public double electronegativity;
    }
}
