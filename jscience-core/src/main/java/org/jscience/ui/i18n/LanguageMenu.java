package org.jscience.ui.i18n;

import javafx.scene.control.Menu;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import java.util.Locale;

/**
 * A Menu component that allows switching the application language.
 */
public class LanguageMenu extends Menu {

    private final Runnable onLocaleChange;

    public LanguageMenu(Runnable onLocaleChange) {
        this.onLocaleChange = onLocaleChange;

        // Initial label
        setText(I18n.getInstance().get("app.menu.language"));

        ToggleGroup langGroup = new ToggleGroup();

        // Add supported languages
        addLanguageItem("English (US)", Locale.US, langGroup);
        addLanguageItem("Français (France)", Locale.FRANCE, langGroup);
        addLanguageItem("Deutsch (Germany)", Locale.GERMANY, langGroup);
        addLanguageItem("Español (Spain)", new Locale.Builder().setLanguage("es").setRegion("ES").build(), langGroup);
        addLanguageItem("中文 (China)", Locale.CHINA, langGroup);
    }

    private void addLanguageItem(String text, Locale locale, ToggleGroup group) {
        RadioMenuItem item = new RadioMenuItem(text);
        item.setToggleGroup(group);

        // Check if this is the current language
        if (I18n.getInstance().getLocale().getLanguage().equals(locale.getLanguage())) {
            item.setSelected(true);
        }

        item.setOnAction(e -> {
            if (I18n.getInstance().get("app.menu.language").equals(getText())) {
                // Optimization: if no change, maybe skip? But Locale object might differ.
            }
            Locale.setDefault(locale);
            I18n.getInstance().setLocale(locale);
            if (onLocaleChange != null) {
                onLocaleChange.run();
            }
        });
        getItems().add(item);
    }
}
