package org.jscience.chemistry.electrochemistry;

import org.jscience.util.SimpleJson;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Standard Electrode Potentials (E°) at 25°C.
 * Relative to Standard Hydrogen Electrode (SHE).
 * Loads data from potentials.json.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI
 * @since 5.0
 */
public class StandardPotentials {

    private static final Map<String, Double> POTENTIALS = new HashMap<>();

    static {
        loadPotentials();
    }

    @SuppressWarnings("unchecked")
    private static void loadPotentials() {
        try (InputStream is = StandardPotentials.class.getResourceAsStream("potentials.json")) {
            if (is == null) {
                System.err.println("StandardPotentials: Resource not found: potentials.json");
                return;
            }

            String jsonText = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Object result = SimpleJson.parse(jsonText);

            if (result instanceof Map) {
                Map<String, Object> root = (Map<String, Object>) result;
                List<Object> list = (List<Object>) root.get("potentials");

                if (list != null) {
                    for (Object item : list) {
                        Map<String, Object> entry = (Map<String, Object>) item;
                        String reaction = (String) entry.get("reaction");
                        Double e0 = ((Number) entry.get("E0")).doubleValue();
                        POTENTIALS.put(reaction, e0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the standard potential for a given half-reaction representation.
     * 
     * @param halfReaction Key string (e.g., "Cu2+ + 2e- -> Cu")
     * @return E° in Volts, or null if not found
     */
    public static Double getPotential(String halfReaction) {
        return POTENTIALS.get(halfReaction);
    }

    /**
     * Lists all registered half-reactions.
     */
    public static java.util.Set<String> getAvailableReactions() {
        return POTENTIALS.keySet();
    }
}
