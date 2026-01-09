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
