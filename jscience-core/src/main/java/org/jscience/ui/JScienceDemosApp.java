/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        buildUI();
        primaryStage.show();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();

        // 1. Menu Bar
        VBox topContainer = new VBox();
        topContainer.getChildren().add(createMenuBar());

        // Header
        VBox header = new VBox(5);
        header.getStyleClass().add("header-box");
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER);
        header.setStyle("-fx-background-color: #333333;");

        Label title = new Label(I18n.getInstance().get("app.header.title", "JScience Ecosystem"));
        title.getStyleClass().add("header-title");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label subtitle = new Label(I18n.getInstance().get("app.header.subtitle", "Scientific Applications & Tools"));
        subtitle.getStyleClass().add("header-subtitle");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #cccccc;");

        header.getChildren().addAll(title, subtitle);
        topContainer.getChildren().add(header);

        root.setTop(topContainer);

        // 2. Main Content
        VBox content = new VBox(10);
        content.getStyleClass().add("content-box");
        content.setPadding(new Insets(10));
        content.setStyle("-fx-background-color: #f4f4f4;");

        // Discovery and Grouping
        Map<String, List<ViewerProvider>> categorizedProviders = discoverAndSortProviders();

        // Accordion for Categories
        Accordion accordion = new Accordion();

        for (Map.Entry<String, List<ViewerProvider>> entry : categorizedProviders.entrySet()) {
            String category = entry.getKey();
            List<ViewerProvider> items = entry.getValue();

            // create list view for this category
            ListView<ViewerProvider> listView = new ListView<>();
            listView.getItems().addAll(items);
            listView.setCellFactory(lv -> new DemoListCell());
            listView.setPrefHeight(items.size() * 50 + 20); // Basic auto-height

            // Double click action
            listView.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) {
                    ViewerProvider selected = listView.getSelectionModel().getSelectedItem();
                    if (selected != null) {
                        launchDemo(selected);
                    }
                }
            });

            TitledPane pane = new TitledPane(category + " (" + items.size() + ")", listView);
            pane.getStyleClass().add("titled-pane");
            accordion.getPanes().add(pane);
        }

        if (!accordion.getPanes().isEmpty()) {
            accordion.setExpandedPane(accordion.getPanes().get(0));
        }

        // Add accordion to content box
        content.getChildren().add(accordion);

        ScrollPane scroll = new ScrollPane(content);
        scroll.getStyleClass().add("scroll-pane");
        scroll.setFitToWidth(true);
        root.setCenter(scroll);

        // Styling
        URL cssResource = getClass().getResource("/org/jscience/ui/style.css");
        Scene scene = new Scene(root, 1000, 700);
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }
        ThemeManager.getInstance().applyTheme(scene);

        primaryStage.setTitle(I18n.getInstance().get("app.header.title", "JScience Ecosystem"));
        primaryStage.setScene(scene);
    }

    private Map<String, List<ViewerProvider>> discoverAndSortProviders() {
        Map<MasterControlDiscovery.ProviderType, Map<String, List<ViewerProvider>>> discovery = MasterControlDiscovery
                .getInstance().getProvidersByType();

        Map<String, List<ViewerProvider>> consolidated = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        // Add Apps and Demos
        mergeProviders(consolidated, discovery.get(MasterControlDiscovery.ProviderType.APP));
        mergeProviders(consolidated, discovery.get(MasterControlDiscovery.ProviderType.DEMO));

        // Deduplicate and Sort Items within Categories
        for (List<ViewerProvider> list : consolidated.values()) {
            // Deduplicate by class name
            Set<String> seenClasses = new HashSet<>();
            list.removeIf(p -> !seenClasses.add(p.getClass().getName()));

            // Sort by Name
            list.sort(Comparator.comparing(ViewerProvider::getName));
        }

        return consolidated;
    }

    private void mergeProviders(Map<String, List<ViewerProvider>> target, Map<String, List<ViewerProvider>> source) {
        if (source == null)
            return;
        for (Map.Entry<String, List<ViewerProvider>> entry : source.entrySet()) {
            // Translate Category Name
            String key = entry.getKey();
            String catName = I18n.getInstance().get("category." + key.toLowerCase().replace(" ", "_"), key);

            target.computeIfAbsent(catName, k -> new ArrayList<>()).addAll(entry.getValue());
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File Menu
        Menu fileMenu = new Menu(I18n.getInstance().get("menu.file", "File"));
        MenuItem exitItem = new MenuItem(I18n.getInstance().get("menu.exit", "Exit"));
        exitItem.setOnAction(e -> primaryStage.close());
        fileMenu.getItems().add(exitItem);

        // View Menu (Language & Theme)
        Menu viewMenu = new Menu(I18n.getInstance().get("menu.view", "View"));

        // Language Submenu
        Menu langMenu = new Menu(I18n.getInstance().get("menu.language", "Language"));
        ToggleGroup langGroup = new ToggleGroup();

        RadioMenuItem enItem = new RadioMenuItem("English");
        enItem.setToggleGroup(langGroup);
        enItem.setSelected(Locale.ENGLISH.getLanguage().equals(I18n.getInstance().getLocale().getLanguage()));
        enItem.setOnAction(e -> {
            I18n.getInstance().setLocale(Locale.ENGLISH);
            buildUI();
        });

        RadioMenuItem frItem = new RadioMenuItem("FranÃƒÂ§ais");
        frItem.setToggleGroup(langGroup);
        frItem.setSelected(Locale.FRENCH.getLanguage().equals(I18n.getInstance().getLocale().getLanguage()));
        frItem.setOnAction(e -> {
            I18n.getInstance().setLocale(Locale.FRENCH);
            buildUI();
        });

        langMenu.getItems().addAll(enItem, frItem);

        // Theme Submenu
        Menu themeMenu = new Menu(I18n.getInstance().get("menu.theme", "Theme"));
        ToggleGroup themeGroup = new ToggleGroup();

        for (String theme : Arrays.asList("Modena", "Caspian", "HighContrast")) {
            RadioMenuItem themeItem = new RadioMenuItem(theme);
            themeItem.setToggleGroup(themeGroup);
            themeItem.setSelected(
                    System.getProperty("jscience.theme", "Modena").equalsIgnoreCase(theme.replace(" ", "")));
            themeItem.setOnAction(e -> {
                System.setProperty("jscience.theme", theme.replace(" ", ""));
                ThemeManager.getInstance().toggleTheme(); // Trigger refresh helper
                ThemeManager.getInstance().applyTheme(primaryStage.getScene());
            });
            themeMenu.getItems().add(themeItem);
        }

        viewMenu.getItems().addAll(langMenu, themeMenu);

        menuBar.getMenus().addAll(fileMenu, viewMenu);
        return menuBar;
    }

    private void launchDemo(ViewerProvider demo) {
        Stage stage = new Stage();
        stage.setTitle(demo.getName());
        try {
            demo.show(stage);
        } catch (Exception e) {
            logger.error("Failed to launch demo: " + demo.getClass().getName(), e);
            new Alert(Alert.AlertType.ERROR, "Could not start " + demo.getName() + ":\n" + e.getMessage()).show();
        }
    }

    // Custom Cell
    private static class DemoListCell extends ListCell<ViewerProvider> {
        @Override
        protected void updateItem(ViewerProvider item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox box = new VBox(2);
                Label title = new Label(item.getName());
                title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
                Label desc = new Label(item.getDescription() != null ? item.getDescription() : "");
                desc.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");
                desc.setWrapText(true);
                box.getChildren().addAll(title, desc);
                setGraphic(box);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
