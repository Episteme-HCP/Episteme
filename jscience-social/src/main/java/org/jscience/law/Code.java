/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.law;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import org.jscience.util.identity.Identifiable;

/**
 * Represents a legal code (system of laws).
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Code implements Identifiable<String> {

    private final String name;
    private final Map<String, Article> articles = new TreeMap<>();

    public Code(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return name; // Name acts as ID for a Code
    }

    public String getName() {
        return name;
    }

    public void addArticle(Article article) {
        articles.put(article.getId(), article);
    }

    public Article getArticle(String id) {
        return articles.get(id);
    }

    public Map<String, Article> getArticles() {
        return Collections.unmodifiableMap(articles);
    }

    @Override
    public String toString() {
        return String.format("%s (%d articles)", name, articles.size());
    }
}
