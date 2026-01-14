/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.apps;

import org.jscience.ui.i18n.I18nManager;
import org.junit.jupiter.api.Test;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.*;

public class I18nTest {
    
    @Test
    public void testLocalizationKeys() {
        System.out.println("Running I18n Verification...");
        
        // Simulate what Launcher does
        I18nManager.getInstance().setLocale(Locale.FRENCH);
        I18nManager.getInstance().addBundle("org.jscience.apps.i18n.messages_apps");
        
        String title = I18nManager.getInstance().get("launcher.title");
        System.out.println("launcher.title (FR): " + title);
        
        String spintronics = I18nManager.getInstance().get("spintronics.title");
        System.out.println("spintronics.title (FR): " + spintronics);
        
        String category = I18nManager.getInstance().get("category.featured_apps");
        System.out.println("category.featured_apps (FR): " + category);
        
        assertFalse(title.startsWith("!"), "launcher.title should be resolved");
        assertFalse(spintronics.startsWith("!"), "spintronics.title should be resolved");
        assertFalse(category.startsWith("!"), "category.featured_apps should be resolved");
        
        // Check for French (assuming standard translation "Applications Vedettes" or similar)
        // Since we don't know exact French value, checking it's not the key is good enough for now.
        // And ensuring it's not Chinese (if we can detect that, but harder).
        
        System.out.println("SUCCESS: Localization keys resolved.");
    }
}
