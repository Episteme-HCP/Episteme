package org.jscience.biology.loaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jscience.biology.Vitamin;
import org.jscience.io.AbstractResourceReader;
import org.jscience.io.MiniCatalog;

import java.io.InputStream;
import java.util.*;

/**
 * Loads and manages vitamins data from JSON.
 * Acts as a catalog of vitamins.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VitaminReader extends AbstractResourceReader<Vitamin> implements MiniCatalog<Vitamin> {

    private static final VitaminReader INSTANCE = new VitaminReader();
    private final Map<String, Vitamin> vitamins = new LinkedHashMap<>();

    private VitaminReader() {
        loadAllVitamins();
    }

    public static VitaminReader getInstance() {
        return INSTANCE;
    }

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.I18n.getInstance().get("category.biology", "Biology");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.vitamins.desc", "Vitamin data loader.");
    }

    @Override
    public String getLongDescription() {
        return org.jscience.ui.i18n.I18n.getInstance().get("reader.vitamins.longdesc", "Loads vitamin nutritional information including RDA, sources, and functions from JSON.");
    }

    @Override
    public String getResourcePath() {
        return "/org/jscience/biology/";
    }

    @Override
    public Class<Vitamin> getResourceType() {
        return Vitamin.class;
    }

    @Override
    protected Vitamin loadFromSource(String id) throws Exception {
        return vitamins.get(id);
    }

    @Override
    protected MiniCatalog<Vitamin> getMiniCatalog() {
        return this;
    }

    private void loadAllVitamins() {
        try {
            List<Vitamin> list = loadFromResource("vitamins.json");
            for (Vitamin v : list) {
                vitamins.put(v.id, v);
            }
        } catch (Exception e) {
            System.err.println("Error loading vitamins: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Vitamin> loadFromResource(String resourcePath) {
        try {
            InputStream is = VitaminReader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                is = VitaminReader.class.getResourceAsStream("/" + resourcePath);
            }
            if (is == null) {
                is = VitaminReader.class.getResourceAsStream("/org/jscience/biology/" + resourcePath);
            }
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> data = mapper.readValue(is,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            List<Vitamin> vitamins = new ArrayList<>();
            for (Map<String, Object> entry : data) {
                vitamins.add(new Vitamin(
                        (String) entry.get("id"),
                        (String) entry.get("name"),
                        (List<String>) entry.get("aliases"),
                        (String) entry.get("solubility"),
                        ((Number) entry.get("rdaAdult")).doubleValue(),
                        (String) entry.get("rdaUnit"),
                        (List<String>) entry.get("sources"),
                        (List<String>) entry.get("functions")));
            }
            return vitamins;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load vitamins from " + resourcePath, e);
        }
    }

    @Override
    public List<Vitamin> getAll() {
        return new ArrayList<>(vitamins.values());
    }

    @Override
    public Optional<Vitamin> findByName(String name) {
        return vitamins.values().stream()
                .filter(v -> v.name.equalsIgnoreCase(name) ||
                        v.id.equalsIgnoreCase(name) ||
                        (v.aliases != null && v.aliases.stream().anyMatch(a -> a.equalsIgnoreCase(name))))
                .findFirst();
    }

    @Override
    public int size() {
        return vitamins.size();
    }

    public List<Vitamin> getFatSoluble() {
        return vitamins.values().stream().filter(Vitamin::isFatSoluble).toList();
    }

    public List<Vitamin> getWaterSoluble() {
        return vitamins.values().stream().filter(Vitamin::isWaterSoluble).toList();
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] {"JSON 1.0"};
    }

    @Override public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("reader.vitamins.name", "Vitamin Reader"); }
}
