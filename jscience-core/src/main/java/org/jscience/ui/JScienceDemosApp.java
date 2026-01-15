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

package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.ui.i18n.I18n;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

/**
 * JScience Main Application.
 * <p>
 * Central entry point for discovering and launching all scientific
 * applications,
 * demos, and visualization tools.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JScienceDemosApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(JScienceDemosApp.class);
    private Stage primaryStage;
    private URL cssResource;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        buildUI();
        primaryStage.show();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();

        // Load CSS with null protection
        // Using likely old path or trying standard path
        // Load CSS with null protection
        // Using likely old path or trying standard path
        cssResource = getClass().getResource("/org/jscience/ui/theme.css");
        if (cssResource == null) {
            cssResource = getClass().getResource("/org/jscience/ui/style.css");
        }

        if (cssResource != null) {
            // we will add it to the scene later
        } else {
            logger.warn("theme.css not found, using default styling");
        }

        // Header
        VBox header = createHeader();

        // Menu Bar
        MenuBar menuBar = createMenuBar();
        VBox topContainer = new VBox(menuBar, header);
        root.setTop(topContainer);

        // Discovery
        Map<String, List<Viewer>> demosByCategory = discoverAndSortProviders();

        // Single unified content - no tabs
        VBox allContent = new VBox(15);
        allContent.setPadding(new Insets(20));
        allContent.getStyleClass().add("content-box");

        // Add demos by category
        if (demosByCategory.isEmpty()) {
            allContent.getChildren().add(new Label(I18n.getInstance().get("app.nodemos")));
        } else {
            // Demos sections
            for (Map.Entry<String, List<Viewer>> entry : demosByCategory.entrySet()) {
                // Restore logic from old app: skip social sciences if requested?
                // The user said "completely different", implying the old state was preferred.
                // The old state explicitly skipped "Social Sciences".
                if ("Social Sciences".equalsIgnoreCase(entry.getKey())
                        || "Sciences Sociales".equalsIgnoreCase(entry.getKey())) {
                    continue;
                }

                TitledPane section = createSection(entry.getKey(), entry.getValue());
                allContent.getChildren().add(section);
            }
        }

        ScrollPane scroll = new ScrollPane(allContent);
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        Scene scene = new Scene(root, 1000, 750);
        // Add CSS to scene as well
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        primaryStage.setTitle(I18n.getInstance().get("app.header.title", "JScience Demos"));
        primaryStage.setScene(scene);

        // Apply global theme preference
        ThemeManager.getInstance().applyTheme(scene);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Language Menu
        // Adapting old LanguageMenu logic to be inline if LanguageMenu class is
        // missing?
        // Old code used `new LanguageMenu(this::buildUI)`. Assuming LanguageMenu class
        // exists.
        // If not, I'll fallback to standard menu construction like in the new app.

        Menu languageMenu = new Menu(I18n.getInstance().get("menu.preferences.language", "Language"));
        ToggleGroup langGroup = new ToggleGroup();

        for (Locale locale : I18n.getInstance().getSupportedLocales()) {
            String label = locale.getDisplayLanguage(locale);
            if (label.length() > 0) {
                label = label.substring(0, 1).toUpperCase() + label.substring(1);
            }

            RadioMenuItem item = new RadioMenuItem(label);
            item.setToggleGroup(langGroup);
            item.setSelected(locale.getLanguage().equals(I18n.getInstance().getLocale().getLanguage()));
            item.setOnAction(e -> {
                I18n.getInstance().setLocale(locale);
                buildUI();
            });
            languageMenu.getItems().add(item);
        }

        // Theme Menu (Delegated to ThemeManager)
        Menu themeMenu = new Menu(I18n.getInstance().get("app.menu.theme", "Theme"));
        ToggleGroup themeGroup = new ToggleGroup();
        String currentTheme = ThemeManager.getInstance().getCurrentTheme();

        RadioMenuItem modenaItem = new RadioMenuItem(I18n.getInstance().get("app.menu.theme.modena", "Modena (Light)"));
        modenaItem.setToggleGroup(themeGroup);
        modenaItem.setSelected("Modena".equalsIgnoreCase(currentTheme));
        modenaItem.setOnAction(e -> {
            ThemeManager.getInstance().setTheme("Modena");
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem caspianItem = new RadioMenuItem(I18n.getInstance().get("app.menu.theme.caspian", "Caspian"));
        caspianItem.setToggleGroup(themeGroup);
        caspianItem.setSelected("Caspian".equalsIgnoreCase(currentTheme));
        caspianItem.setOnAction(e -> {
            ThemeManager.getInstance().setTheme("Caspian");
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem highContrastItem = new RadioMenuItem(
                I18n.getInstance().get("app.menu.theme.highcontrast", "High Contrast"));
        highContrastItem.setToggleGroup(themeGroup);
        highContrastItem.setSelected("HighContrast".equalsIgnoreCase(currentTheme));
        highContrastItem.setOnAction(e -> {
            ThemeManager.getInstance().setTheme("HighContrast");
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem darkItem = new RadioMenuItem(I18n.getInstance().get("menu.view.theme.dark", "Dark"));
        darkItem.setToggleGroup(themeGroup);
        darkItem.setSelected("Dark".equalsIgnoreCase(currentTheme));
        darkItem.setOnAction(e -> {
             ThemeManager.getInstance().setTheme("Dark");
             ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        themeMenu.getItems().addAll(modenaItem, caspianItem, highContrastItem, darkItem);

        menuBar.getMenus().addAll(languageMenu, themeMenu);
        return menuBar;
    }

    private VBox createHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header-box");
        // Style moved to theme.css (.header-box)

        Label title = new Label(I18n.getInstance().get("app.header.title", "JScience Demos"));
        title.getStyleClass().add("header-label");
        // Inline style removed

        Label subtitle = new Label(I18n.getInstance().get("app.header.subtitle", "Scientific Applications & Tools"));
        subtitle.getStyleClass().add("header-subtitle");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private TitledPane createSection(String category, List<Viewer> demos) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("section-box"); // Styled in CSS

        for (Viewer demo : demos) {
            box.getChildren().add(createCard(demo));
        }

        TitledPane pane = new TitledPane(category, box);
        pane.setCollapsible(true);
        pane.setExpanded(true);
        return pane;
    }

    private HBox createCard(Viewer demo) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15));
        row.getStyleClass().add("demo-card");

        Button btn = new Button(I18n.getInstance().get("app.button.launch", "Launch"));
        btn.getStyleClass().add("launch-button");
        btn.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80;");

        btn.setOnAction(e -> {
            try {
                launchDemo(demo);
            } catch (Exception ex) {
                showError("Failed to launch: " + demo.getName(), ex.getMessage(), ex);
            }
        });

        VBox info = new VBox(5);
        String titleText = demo.getName();
        String prefix = demo.getCategory() + " : ";
        if (titleText.startsWith(prefix)) {
            titleText = titleText.substring(prefix.length());
        }

        Label name = new Label(titleText);
        name.getStyleClass().add("card-title");

        String desc = demo.getDescription();
        Label description = new Label(desc != null ? desc : "");
        description.setWrapText(true);
        description.getStyleClass().add("description-label");

        info.getChildren().addAll(name, description);
        row.getChildren().addAll(btn, info);
        HBox.setHgrow(info, Priority.ALWAYS);

        return row;
    }

    private void launchDemo(Viewer demo) {
        Stage stage = new Stage();
        stage.setTitle(demo.getName());
        try {
            demo.show(stage);
            // Auto-apply theme to all demos
            if (stage.getScene() != null) {
                ThemeManager.getInstance().applyTheme(stage.getScene());
            }
        } catch (Exception e) {
            logger.error("Failed to launch demo: " + demo.getClass().getName(), e);
            showError("Launch Error", "Could not start " + demo.getName(), e);
        }
    }

    private void showError(String title, String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(I18n.getInstance().get("status.error", "Error"));
        alert.setHeaderText(title);
        alert.setContentText(message + "\n" + ex.getMessage());
        alert.show(); // Simplified
    }

    // --- Data Discovery (Kept from Modern Version) ---

    private Map<String, List<Viewer>> discoverAndSortProviders() {
        Map<MasterControlDiscovery.ProviderType, Map<String, List<Viewer>>> discovery = MasterControlDiscovery
                .getInstance().getProvidersByType();

        Map<String, List<Viewer>> consolidated = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        // Add Apps and Demos
        mergeProviders(consolidated, discovery.get(MasterControlDiscovery.ProviderType.APP));
        mergeProviders(consolidated, discovery.get(MasterControlDiscovery.ProviderType.DEMO));

        // Deduplicate and Sort Items within Categories
        for (List<Viewer> list : consolidated.values()) {
            // Deduplicate by class name
            Set<String> seenClasses = new HashSet<>();
            list.removeIf(p -> !seenClasses.add(p.getClass().getName()));

            // Sort by Name
            list.sort(Comparator.comparing(Viewer::getName));
        }

        return consolidated;
    }

    private void mergeProviders(Map<String, List<Viewer>> target, Map<String, List<Viewer>> source) {
        if (source == null)
            return;
        for (Map.Entry<String, List<Viewer>> entry : source.entrySet()) {
            // Translate Category Name
            String key = entry.getKey();
            String catName = I18n.getInstance().get("category." + key.toLowerCase().replace(" ", "_"), key);

            target.computeIfAbsent(catName, k -> new ArrayList<>()).addAll(entry.getValue());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
