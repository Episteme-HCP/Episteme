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
package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jscience.ui.i18n.I18n;

/**
 * JScience Master Demo Launcher.
 * <p>
 * Discovers and displays all available scientific demonstrations and viewers
 * from jscience-core, jscience-natural, and jscience-social modules using
 * the ServiceLoader mechanism.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JScienceDemoApp extends Application {

    private static final Logger logger = LoggerFactory.getLogger(JScienceDemoApp.class);
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        buildUI();
        primaryStage.show();
    }

    private void buildUI() {
        BorderPane root = new BorderPane();

        // Load CSS with null protection
        var cssResource = getClass().getResource("theme.css");
        if (cssResource != null) {
            root.getStylesheets().add(cssResource.toExternalForm());
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
        Map<String, List<DemoProvider>> demosByCategory = new TreeMap<>();

        try {
            ServiceLoader<DemoProvider> loader = ServiceLoader.load(DemoProvider.class);
            for (DemoProvider provider : loader) {
                if (!provider.isViewer()) {
                    demosByCategory
                            .computeIfAbsent(provider.getCategory(), k -> new ArrayList<>())
                            .add(provider);
                }
            }
        } catch (Throwable e) {
            System.err.println("CRITICAL DEMO LOAD ERROR:");
            e.printStackTrace();
            logger.error("Error loading demos", e);
        }

        // Single unified content - no tabs
        VBox allContent = new VBox(15);
        allContent.setPadding(new Insets(20));
        allContent.getStyleClass().add("content-box");

        // Add demos by category
        if (demosByCategory.isEmpty()) {
            allContent.getChildren().add(new Label(I18n.getInstance().get("app.nodemos")));
        } else {
            // Demos sections
            for (Map.Entry<String, List<DemoProvider>> entry : demosByCategory.entrySet()) {
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

        primaryStage.setTitle(I18n.getInstance().get("app.header.title"));
        primaryStage.setScene(scene);

        // Apply global theme preference
        ThemeManager.getInstance().applyTheme(scene);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Language Menu
        Menu languageMenu = new Menu(I18n.getInstance().get("app.menu.language"));
        ToggleGroup langGroup = new ToggleGroup();

        RadioMenuItem englishItem = new RadioMenuItem("English");
        englishItem.setToggleGroup(langGroup);
        englishItem.setSelected(Locale.getDefault().getLanguage().equals("en"));
        englishItem.setOnAction(e -> setLocale(Locale.ENGLISH));

        RadioMenuItem frenchItem = new RadioMenuItem("Français");
        frenchItem.setToggleGroup(langGroup);
        frenchItem.setSelected(Locale.getDefault().getLanguage().equals("fr"));
        frenchItem.setOnAction(e -> setLocale(Locale.FRENCH));

        languageMenu.getItems().addAll(englishItem, frenchItem);

        // Theme Menu
        Menu themeMenu = new Menu(I18n.getInstance().get("app.menu.theme"));
        ToggleGroup themeGroup = new ToggleGroup();

        RadioMenuItem darkItem = new RadioMenuItem(I18n.getInstance().get("app.menu.theme.dark"));
        darkItem.setToggleGroup(themeGroup);
        darkItem.setSelected(ThemeManager.getInstance().isDarkTheme());
        darkItem.setOnAction(e -> {
            ThemeManager.getInstance().setDarkTheme(true);
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem lightItem = new RadioMenuItem(I18n.getInstance().get("app.menu.theme.light"));
        lightItem.setToggleGroup(themeGroup);
        lightItem.setSelected(!ThemeManager.getInstance().isDarkTheme());
        lightItem.setOnAction(e -> {
            ThemeManager.getInstance().setDarkTheme(false);
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        themeMenu.getItems().addAll(darkItem, lightItem);

        menuBar.getMenus().addAll(languageMenu, themeMenu);
        return menuBar;
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        I18n.getInstance().setLocale(locale);
        buildUI();
    }

    private VBox createHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header-box");
        header.setStyle("-fx-background-color: linear-gradient(to right, #1a2a6c, #b21f1f, #fdbb2d);");

        Label title = new Label(I18n.getInstance().get("app.header.title"));
        title.getStyleClass().add("header-label");
        title.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label subtitle = new Label(I18n.getInstance().get("app.header.subtitle"));
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #eeeeee;");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private TitledPane createSection(String category, List<DemoProvider> demos) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: #252526;"); // Inner dark

        for (DemoProvider demo : demos) {
            box.getChildren().add(createCard(demo));
        }

        TitledPane pane = new TitledPane(category, box);
        pane.setCollapsible(true);
        pane.setExpanded(true);
        return pane;
    }

    private HBox createCard(DemoProvider demo) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15));
        row.setStyle(
                "-fx-background-color: #333333; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 1); -fx-background-radius: 5;");

        Button btn = new Button(I18n.getInstance().get("app.button.launch"));
        btn.getStyleClass().add("launch-button");
        btn.setStyle("-fx-background-color: #007acc; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80;");

        btn.setOnAction(e -> {
            try {
                demo.show(new Stage());
            } catch (Exception ex) {
                showError("Failed to launch: " + demo.getName(), ex.getMessage(), ex);
            }
        });

        VBox info = new VBox(5);
        Label name = new Label(demo.getName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        String desc = demo.getDescription();
        Label description = new Label(desc != null ? desc : "");
        description.setWrapText(true);
        description.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 12px;");

        info.getChildren().addAll(name, description);
        row.getChildren().addAll(btn, info);
        HBox.setHgrow(info, Priority.ALWAYS);

        return row;
    }

    private void showError(String title, String message, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        TextArea area = new TextArea(ex.toString());
        alert.getDialogPane().setExpandableContent(area);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
