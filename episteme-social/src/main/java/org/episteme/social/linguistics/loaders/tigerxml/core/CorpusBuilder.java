/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.linguistics.loaders.tigerxml.core;

import org.episteme.social.linguistics.loaders.tigerxml.AnnotationMetadata;
import org.episteme.social.linguistics.loaders.tigerxml.Corpus;
import org.episteme.social.linguistics.loaders.tigerxml.Sentence;
import org.episteme.social.linguistics.loaders.tigerxml.tools.DomTools;
import org.w3c.dom.*;

/**
 * Static utility for building {@link Corpus} objects from TIGER-XML DOM elements.
 * Handles metadata parsing, annotation header specifications, and sentence collection.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * @version 2.0 (Modernized)
 */
public final class CorpusBuilder {

    private CorpusBuilder() {}

    /**
     * Builds a {@link Corpus} from the document root.
     *
     * @param corp the corpus to populate.
     * @param root the {@code <corpus>} element.
     */
    public static void buildCorpus(Corpus corp, Element root) {
        if (corp.getVerbosity() > 0) {
            System.err.println("TigerXML: Parsing corpus...");
            DomTools.checkElementName(root, "corpus");
        }

        // 1. Corpus Attributes
        NamedNodeMap attrs = root.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr) attrs.item(i);
            corp.addAttribute(attr.getName(), attr.getValue());
        }
        corp.setId(root.getAttribute("id"));

        // 2. Header / Annotation Metadata
        Element head = DomTools.getElement(root, "head");
        if (head != null) {
            parseHeader(corp, head);
        }

        // 3. Body / Sentences
        Element body = DomTools.getElement(root, "body");
        if (body != null) {
            NodeList sNodes = body.getElementsByTagName("s");
            for (int i = 0; i < sNodes.getLength(); i++) {
                Element sElem = (Element) sNodes.item(i);
                if (sElem.hasAttribute("id")) {
                    corp.addSentence(new Sentence(sElem, corp));
                }
            }
        }
    }

    private static void parseHeader(Corpus corp, Element head) {
        Element annotation = DomTools.getElement(head, "annotation");
        if (annotation == null) return;

        AnnotationMetadata meta = new AnnotationMetadata();
        NodeList featureNodes = annotation.getElementsByTagName("feature");
        
        for (int i = 0; i < featureNodes.getLength(); i++) {
            Element fElem = (Element) featureNodes.item(i);
            String name = fElem.getAttribute("name");
            String domain = fElem.getAttribute("domain");
            
            AnnotationMetadata.Feature feature = new AnnotationMetadata.Feature(name, domain);
            
            NodeList valueNodes = fElem.getElementsByTagName("value");
            for (int j = 0; j < valueNodes.getLength(); j++) {
                Element vElem = (Element) valueNodes.item(j);
                feature.addValue(vElem.getAttribute("name"), vElem.getTextContent().trim());
            }
            meta.addFeature(feature);
        }
        corp.setAnnotationMetadata(meta);
    }
}

