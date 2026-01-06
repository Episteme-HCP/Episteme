package org.jscience.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.jscience.ui.i18n.I18n;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Automated UI Crawler to verify localization.
 * Launches the Master Control and spider-crawls through tabs and nodes
 * to identify hardcoded strings or missing keys.
 */
public class LocalizationCrawlerTest extends ApplicationTest {

    private static final Locale TARGET_LOCALE = Locale.SIMPLIFIED_CHINESE;
    // Patter for pure ASCII text (English/formulas).
    // We expect Chinese content to have non-ASCII characters.
    // However, some valid text is just numbers or formulas "f(x) =", so we must be
    // careful.
    // A heuristic: If text has > 3 letters and is ALL ASCII, it's suspicious in
    // Chinese mode.
    private static final Pattern SUSPICIOUS_ENGLISH_PATTERN = Pattern.compile("[A-Za-z ]{4,}");

    @BeforeAll
    public static void setupHeadless() {
        System.setProperty("java.awt.headless", "true");
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
    }

    @Override
    public void start(Stage stage) {
        // Force Chinese locale for testing
        I18n.getInstance().setLocale(TARGET_LOCALE);

        JScienceMasterControl app = new JScienceMasterControl();
        app.start(stage);
    }

    @Test
    public void crawlAndVerifyLocalization() throws InterruptedException {
        WaitForAsyncUtils.waitForFxEvents();

        // Root node
        Parent root = lookup(".root").queryParent();
        Assertions.assertNotNull(root, "Root node not found");

        List<String> errors = new ArrayList<>();

        // crawl the main dashboard tabs
        // Note: queryAll tabs might not be clickable directly if we don't traverse the
        // TabPane selection model
        // Better: Find the TabPane and iterate its tabs

        TabPane mainTabs = lookup(".tab-pane").query();
        int tabCount = mainTabs.getTabs().size();

        for (int i = 0; i < tabCount; i++) {
            final int index = i;
            interact(() -> mainTabs.getSelectionModel().select(index));
            WaitForAsyncUtils.waitForFxEvents();

            Tab currentTab = mainTabs.getSelectionModel().getSelectedItem();
            String tabTitle = currentTab.getText();
            System.out.println("Checking Tab: " + tabTitle);

            checkString(tabTitle, "Tab Title: " + tabTitle, errors);

            // Check content of the tab
            Node content = currentTab.getContent();
            if (content instanceof Parent) {
                crawlNode((Parent) content, "Tab[" + tabTitle + "]", errors);
            }
        }

        if (!errors.isEmpty()) {
            System.err.println("Localization Errors Found: " + errors.size());
            errors.forEach(System.err::println);
            Assertions.fail("Found " + errors.size() + " localization issues:\n" + String.join("\n", errors));
        }
    }

    private void crawlNode(Parent parent, String context, List<String> errors) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Labeled) {
                String text = ((Labeled) node).getText();
                checkString(text, context + " -> " + node.getClass().getSimpleName() + " '" + text + "'", errors);
            } else if (node instanceof TextInputControl) {
                String text = ((TextInputControl) node).getText();
                checkString(text, context + " -> Input '" + text + "'", errors);
            }

            if (node instanceof Parent) {
                crawlNode((Parent) node, context, errors);
            }
        }
    }

    private void checkString(String text, String context, List<String> errors) {
        if (text == null || text.trim().isEmpty())
            return;

        // Check for "!key!" pattern
        if (text.startsWith("!") && text.endsWith("!")) {
            errors.add("[MISSING KEY] " + context);
        }

        // Check for Logic/Hardcoded English heuristic
        // If we are in Chinese, and we see "General" or "Computing", that's likely
        // hardcoded.
        // We verify if it matches our suspicious pattern AND isn't excluded.
        if (SUSPICIOUS_ENGLISH_PATTERN.matcher(text).matches()) {
            // Exceptions: Library names (JBlas, XChart), specific formulas
            if (isAllowedEnglish(text)) {
                return;
            }
            errors.add("[HARDCODED ENGLISH] " + context);
        }
    }

    private boolean isAllowedEnglish(String text) {
        // List of words that are effectively names or brands and might not be
        // translated
        // Or common math terms if we accept them
        Set<String> whitelist = Set.of(
                "JScience", "JavaFX", "JBlas", "JCUDA", "JOCL", "ND4J", "XChart", "Jzy3d", "EJML", "Modena", "Caspian",
                "GitHub", "MIT License", "Google DeepMind", "Gemini AI",
                "f(x) =", "g(x) =", "z = f(x, y) =");
        if (whitelist.contains(text))
            return true;

        // If it looks like a variable assignment e.g. "z = -"
        if (text.contains("="))
            return true;

        return false;
    }
}
