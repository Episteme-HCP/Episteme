
package org.jscience.biology.loaders;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Loader for NCBI Taxonomy database.
 * <p>
 * Fetches taxonomic information from NCBI E-utilities API.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NCBITaxonomyLoader {

    private static final String BASE_URL;
    private static final String SEARCH_URL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi";

    static {
        Properties props = new Properties();
        try (InputStream is = NCBITaxonomyLoader.class.getResourceAsStream("/jscience.properties")) {
            if (is != null)
                props.load(is);
        } catch (Exception e) {
            /* ignore */ }
        BASE_URL = props.getProperty("api.ncbi.taxonomy.base",
                "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi");
    }

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Fetches taxonomy by NCBI taxon ID.
     *
     * @param taxId the NCBI taxonomy ID
     * @return the species entry, or empty if not found
     */
    public Optional<org.jscience.biology.taxonomy.Species> fetchByTaxId(long taxId) {
        try {
            String url = BASE_URL + "?db=taxonomy&id=" + taxId + "&retmode=xml";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseXmlResponse(response.body(), taxId);
            }
        } catch (Exception e) {
            System.err.println("NCBI fetch failed: " + e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Searches for taxa by scientific name.
     *
     * @param name the scientific name to search
     * @return list of matching taxon IDs
     */
    public List<Long> searchByName(String name) {
        List<Long> ids = new ArrayList<>();
        try {
            String url = SEARCH_URL + "?db=taxonomy&term=" +
                    java.net.URLEncoder.encode(name, "UTF-8") + "&retmode=json";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JsonNode root = mapper.readTree(response.body());
                JsonNode idList = root.path("esearchresult").path("idlist");
                if (idList.isArray()) {
                    for (JsonNode id : idList) {
                        ids.add(Long.parseLong(id.asText()));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("NCBI search failed: " + e.getMessage());
        }
        return ids;
    }

    private Optional<org.jscience.biology.taxonomy.Species> parseXmlResponse(String xml, long taxId) {
        // Simple XML parsing - extract key fields
        String scientificName = extractXmlValue(xml, "ScientificName");
        String rank = extractXmlValue(xml, "Rank");
        String lineage = extractXmlValue(xml, "Lineage");
        String division = extractXmlValue(xml, "Division");

        if (scientificName != null) {
            org.jscience.biology.taxonomy.Species s = new org.jscience.biology.taxonomy.Species(scientificName,
                    scientificName);
            // Map NCBI fields to Species attributes or specific fields
            s.addAttribute("ncbi_taxid", String.valueOf(taxId));
            if (rank != null)
                s.addAttribute("rank", rank); // Species doesn't have generic rank field setter exposed as string
                                              // easily, but has specific ones.
            // Parse hierarchy from lineage if possible, but for now store as attribute
            if (lineage != null)
                s.addAttribute("lineage", lineage);
            if (division != null)
                s.addAttribute("division", division);

            return Optional.of(s);
        }
        return Optional.empty();
    }

    private String extractXmlValue(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        int end = xml.indexOf(close);
        if (start >= 0 && end > start) {
            return xml.substring(start + open.length(), end).trim();
        }
        return null;
    }

    /**
     * Loads species from a specific resource path (JSON).
     * 
     * @param resourcePath Classpath to the JSON file.
     * @return List of species found in the file.
     */
    public List<org.jscience.biology.taxonomy.Species> loadFromResource(String resourcePath) {
        List<org.jscience.biology.taxonomy.Species> speciesList = new ArrayList<>();
        try {
            InputStream is = NCBITaxonomyLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                is = NCBITaxonomyLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            if (is == null && !resourcePath.startsWith("/")) {
                is = NCBITaxonomyLoader.class.getResourceAsStream("/" + resourcePath);
            }

            if (is == null) {
                System.err.println("NCBITaxonomyLoader: Resource not found: " + resourcePath);
                return speciesList;
            }

            JsonNode root = mapper.readTree(is);

            if (root.isArray()) {
                for (JsonNode node : root) {
                    speciesList.add(parseSpeciesJson(node));
                }
            } else if (root.has("species")) {
                for (JsonNode node : root.get("species")) {
                    speciesList.add(parseSpeciesJson(node));
                }
            }

            return speciesList;
        } catch (Exception e) {
            e.printStackTrace();
            return speciesList;
        }
    }

    private org.jscience.biology.taxonomy.Species parseSpeciesJson(JsonNode node) {
        String commonName = node.has("commonName") ? node.get("commonName").asText()
                : (node.has("common_name") ? node.get("common_name").asText() : "");

        String scientificName = node.has("scientificName") ? node.get("scientificName").asText()
                : (node.has("scientific_name") ? node.get("scientific_name").asText() : "");

        org.jscience.biology.taxonomy.Species s = new org.jscience.biology.taxonomy.Species(commonName, scientificName);

        if (node.has("kingdom"))
            s.setKingdom(node.get("kingdom").asText());
        if (node.has("phylum"))
            s.setPhylum(node.get("phylum").asText());
        if (node.has("class"))
            s.setTaxonomicClass(node.get("class").asText());
        if (node.has("order"))
            s.setOrder(node.get("order").asText());
        if (node.has("family"))
            s.setFamily(node.get("family").asText());
        if (node.has("genus"))
            s.setGenus(node.get("genus").asText());

        if (node.has("attributes")) {
            Iterator<String> fieldNames = node.get("attributes").fieldNames();
            while (fieldNames.hasNext()) {
                String key = fieldNames.next();
                s.addAttribute(key, node.get("attributes").get(key).asText());
            }
        }
        return s;
    }
}
