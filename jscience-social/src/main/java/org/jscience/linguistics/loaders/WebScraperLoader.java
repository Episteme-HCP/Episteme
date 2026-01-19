package org.jscience.linguistics.loaders;

import org.jscience.linguistics.Corpus;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;

/**
 * Generates a Corpus from web sources (RSS feeds, article URLs).
 */
public class WebScraperLoader extends LinguisticResourceReader<Corpus> {

    private final String baseUrl;

    public WebScraperLoader(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Corpus load(String resourceId) throws Exception {
        Corpus corpus = new Corpus();
        
        URL url = URI.create(baseUrl + (resourceId != null ? resourceId : "")).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", "JScience-WebScraper/1.0");
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            // Extract text from HTML (simple regex, for real use consider jsoup)
            String text = stripHtml(content.toString());
            corpus.addDocument(text);
        }
        
        return corpus;
    }

    private String stripHtml(String html) {
        // Remove script and style tags
        String noScript = Pattern.compile("<script[^>]*>.*?</script>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
                                 .matcher(html).replaceAll("");
        String noStyle = Pattern.compile("<style[^>]*>.*?</style>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE)
                                .matcher(noScript).replaceAll("");
        // Remove all HTML tags
        String noTags = Pattern.compile("<[^>]+>").matcher(noStyle).replaceAll(" ");
        // Decode HTML entities
        String decoded = noTags.replace("&nbsp;", " ").replace("&amp;", "&")
                               .replace("&lt;", "<").replace("&gt;", ">");
        // Normalize whitespace
        return decoded.replaceAll("\\s+", " ").trim();
    }

    @Override
    public String getResourcePath() {
        return baseUrl;
    }

    @Override
    public Class<Corpus> getResourceType() {
        return Corpus.class;
    }

    @Override
    public String getName() {
        return "Web Scraper Corpus Loader";
    }

    @Override
    public String getDescription() {
        return "Generates a linguistic Corpus from web pages and RSS feeds.";
    }
}
