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

    @Override
    public void start(Stage primaryStage) {
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
        List<DemoProvider> viewers = new ArrayList<>();

        try {
            ServiceLoader<DemoProvider> loader = ServiceLoader.load(DemoProvider.class);
            for (DemoProvider provider : loader) {
                if (provider.isViewer()) {
                    viewers.add(provider);
                } else {
                    demosByCategory
                            .computeIfAbsent(provider.getCategory(), k -> new ArrayList<>())
                            .add(provider);
                }
            }
        } catch (Exception e) {
            logger.error("Error loading demos", e);
        }

        // Tabs
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab demosTab = new Tab("Demos");
        demosTab.setContent(createCategoryContent(demosByCategory));

        Tab viewersTab = new Tab("Viewers");
        viewersTab.setContent(createSimpleListContent(viewers));

        tabs.getTabs().addAll(demosTab, viewersTab);

        root.setCenter(tabs);

        Scene scene = new Scene(root, 1000, 750);
        // Add CSS to scene as well
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        primaryStage.setTitle("JScience Demonstration Suite");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Run diagnostics at startup (internal check)
        runDiagnostics();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu toolsMenu = new Menu("Tools");
        MenuItem diagnosticsItem = new MenuItem("Run Diagnostics");
        diagnosticsItem.setOnAction(e -> runDiagnostics());
        toolsMenu.getItems().add(diagnosticsItem);
        menuBar.getMenus().add(toolsMenu);
        return menuBar;
    }

    private void runDiagnostics() {
        logger.info("Starting JScience Demo Diagnostics...");
        logger.info("Java Version: {}", System.getProperty("java.version"));
        logger.info("Classpath: {}", System.getProperty("java.class.path"));

        try {
            logger.info("--- Attempting to load DemoProviders ---");
            ServiceLoader<DemoProvider> loader = ServiceLoader.load(DemoProvider.class);
            int count = 0;
            for (DemoProvider provider : loader) {
                logger.info("Found provider: {} - {}", provider.getClass().getName(), provider.getName());
                count++;
            }
            logger.info("Total providers found: {}", count);
        } catch (Throwable t) {
            logger.error("FAILED TO LOAD PROVIDERS", t);
        }
    }

    private VBox createHeader() {
        VBox header = new VBox(5);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header-box");
        header.setStyle("-fx-background-color: linear-gradient(to right, #1a2a6c, #b21f1f, #fdbb2d);");

        Label title = new Label("JScience Demonstration Suite");
        title.getStyleClass().add("header-label");
        title.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label subtitle = new Label("Interactive Scientific Visualizations & Simulations");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #eeeeee;");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private ScrollPane createCategoryContent(Map<String, List<DemoProvider>> demosByCategory) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("content-box");

        if (demosByCategory.isEmpty()) {
            content.getChildren().add(new Label("No demos found."));
        } else {
            for (Map.Entry<String, List<DemoProvider>> entry : demosByCategory.entrySet()) {
                TitledPane section = createSection(entry.getKey(), entry.getValue());
                content.getChildren().add(section);
            }
        }

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        return scroll;
    }

    private ScrollPane createSimpleListContent(List<DemoProvider> providers) {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        if (providers.isEmpty()) {
            content.getChildren().add(new Label("No generic viewers found."));
        } else {
            for (DemoProvider p : providers) {
                content.getChildren().add(createCard(p));
            }
        }

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        return scroll;
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

        Button btn = new Button("LAUNCH");
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
