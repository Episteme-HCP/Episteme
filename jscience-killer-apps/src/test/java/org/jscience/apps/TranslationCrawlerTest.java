package org.jscience.apps;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;

import javafx.stage.Stage;
import org.jscience.ui.JScienceDashboard;
import org.jscience.ui.i18n.I18n;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@ExtendWith(ApplicationExtension.class)
public class TranslationCrawlerTest {

    private final List<String> suspiciousTexts = new ArrayList<>();

    @Start
    public void start(Stage stage) {
        // Enable test mode to easily detect missing keys
        I18n.setTestMode(true);
        // Force Locale to English for consistent headless traversal
        java.util.Locale.setDefault(java.util.Locale.ENGLISH);
        new JScienceDashboard().start(stage);
    }

    @BeforeEach
    void setup() throws IOException {
        Files.createDirectories(Paths.get("target/screenshots"));
    }

    @AfterEach
    void tearDown() {
        I18n.setTestMode(false);
        if (!suspiciousTexts.isEmpty()) {
            System.err.println("---------------------------------------------------");
            System.err.println("SUSPICIOUS OR MISSING TRANSLATIONS FOUND:");
            suspiciousTexts.forEach(System.err::println);
            System.err.println("---------------------------------------------------");

            try {
                java.nio.file.Path path = java.nio.file.Paths.get("target/translation_report.txt");
                java.nio.file.Files.write(path, suspiciousTexts);
                System.out.println("Report written to " + path.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void crawlAllTabsAndApps(FxRobot robot) {
        // --- Pass 1: English ---
        System.out.println("--- Pass 1: English ---");
        setLocale(robot, "English");
        Map<String, String> englishTexts = crawlDashboard(robot, "EN");

        // --- Pass 2: French ---
        System.out.println("--- Pass 2: French ---");
        setLocale(robot, "Français");
        Map<String, String> frenchTexts = crawlDashboard(robot, "FR");

        // --- Comparison ---
        compareTranslations(englishTexts, frenchTexts);
    }

    private void setLocale(FxRobot robot, String langName) {
        try {
            // Click on Language tab
            robot.clickOn("Language");
            WaitForAsyncUtils.waitForFxEvents();

            // Look for the language in the ListView
            ListView<?> langList = robot.lookup("#lang-list").queryAs(ListView.class);
            if (langList != null) {
                robot.interact(() -> {
                    for (Object item : langList.getItems()) {
                        if (item.toString().contains(langName)) {
                            langList.getSelectionModel().select(langList.getItems().indexOf(item));
                            break;
                        }
                    }
                });
                WaitForAsyncUtils.waitForFxEvents();
            }
        } catch (Exception e) {
            System.err.println("Could not set locale to " + langName + ": " + e.getMessage());
        }
    }

    private Map<String, String> crawlDashboard(FxRobot robot, String suffix) {
        Map<String, String> results = new HashMap<>();
        String[] tabs = { "General", "Computing", "Libraries", "Loaders", "Apps", "Devices" };

        for (String tabId : tabs) {
            try {
                System.out.println("Opening tab: " + tabId);
                // We added IDs like tab-general, tab-computing, etc.
                robot.clickOn("#tab-" + tabId.toLowerCase());
                WaitForAsyncUtils.waitForFxEvents();
                captureScreenshot(robot, "Tab_" + tabId + "_" + suffix);

                // Collect all visible strings
                collectStrings(robot.targetWindow().getScene().getRoot(), results, tabId);
            } catch (Exception e) {
                System.err.println("Error crawling tab " + tabId + ": " + e.getMessage());
            }
        }
        return results;
    }

    private void collectStrings(Parent root, Map<String, String> results, String context) {
        for (Node node : root.lookupAll(".label")) {
            if (node instanceof Labeled) {
                String text = ((Labeled) node).getText();
                if (text != null && !text.isEmpty()) {
                    results.put(context + "_" + node.toString(), text);
                    checkNode(node);
                }
            }
        }
        for (Node node : root.lookupAll(".button")) {
            if (node instanceof Labeled) {
                String text = ((Labeled) node).getText();
                if (text != null && !text.isEmpty()) {
                    results.put(context + "_" + node.toString(), text);
                    checkNode(node);
                }
            }
        }
    }

    private void compareTranslations(Map<String, String> en, Map<String, String> fr) {
        System.out.println("Comparing results (" + en.size() + " EN vs " + fr.size() + " FR)...");
        int diffCount = 0;

        for (String key : en.keySet()) {
            if (fr.containsKey(key)) {
                String enText = en.get(key);
                String frText = fr.get(key);
                if (!enText.equals(frText)) {
                    diffCount++;
                } else {
                    // Some things are expected to be the same (numbers, names, "JScience")
                    if (enText.matches(".*[a-zA-Z]{3,}.*") && !enText.equals("JScience") && !enText.contains("2025")) {
                        // Suspicious if long text is identical in EN and FR
                        // sameCount++;
                    }
                }
            }
        }
        System.out.println("Found " + diffCount + " translated strings.");
    }

    private void checkNode(Node node) {
        analyzeNode(node);
    }

    private void analyzeNode(Node node) {
        String text = null;
        if (node instanceof Labeled) {
            text = ((Labeled) node).getText();
        } else if (node instanceof TextInputControl) {
            text = ((TextInputControl) node).getText();
        } else if (node instanceof ComboBoxBase) {
            // value?
        }

        if (text != null && !text.isEmpty()) {
            // Check for test markers
            if (text.startsWith("!!!") && text.endsWith("!!!")) {
                suspiciousTexts.add("MISSING KEY: " + text + " in " + node);
            }
            // Check for hardcoded English (heuristic: contains words, no markers, but we
            // are in Test Mode so strictly missing keys are marked)
            // But if the dev hardcoded "Hello" instead of i18n.get("hello"), it WON'T have
            // markers.
            // Heuristic: if text matches [A-Z][a-z]+ and isn't marked, it might be
            // hardcoded.
            // But valid translated text looks like that too.
            // How to detect hardcoded strings?
            // - If I switch language to French, and it remains English?
            // That requires running test in multiple languages.

            // For now, listing suspicious patterns if any.
            // But "!!!" is the definitive check for MISSING keys.

            // Hardcoded check:
            // Since we didn't switch language in this single pass, checking "!!!" is the
            // best we can do for "Missing Key".
            // For "Hardcoded", we'd need to compare runs.
            // But verify: user said "check ... for missing translation".
        }
    }

    private void captureScreenshot(FxRobot robot, String name) {
        try {
            javafx.scene.image.Image image = robot.capture(robot.targetWindow().getScene().getRoot()).getImage();
            java.awt.image.BufferedImage bImage = javafx.embed.swing.SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bImage, "png", new File("target/screenshots/" + name + ".png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
