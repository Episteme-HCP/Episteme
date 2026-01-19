package org.jscience.law.loaders;

import org.jscience.law.Statute;
import java.util.regex.*;

/**
 * Loader for Akoma Ntoso (XML-based) legal documents.
 * Simplified regex-based parser for demonstration.
 */
public final class AkomaNtosoLoader implements LegalDocumentLoader {

    @Override
    public Statute load(String xml) {
        String title = extract(xml, "<docTitle>(.*?)</docTitle>");
        return new Statute("AN-" + title.hashCode(), title, Statute.Type.REGULATION, "AkomaNtoso", 2024, Statute.Status.ENACTED);
    }

    private String extract(String xml, String regex) {
        Matcher m = Pattern.compile(regex, Pattern.DOTALL).matcher(xml);
        return m.find() ? m.group(1).trim() : "Unknown";
    }
}
