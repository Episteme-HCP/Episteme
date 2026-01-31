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

package org.jscience.core.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jscience.core.ui.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.*;

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

        cssResource = getClass().getResource("/org/jscience/ui/theme.css");
        if (cssResource == null) {
            cssResource = getClass().getResource("/org/jscience/ui/style.css");
        }

        if (cssResource == null) {
            logger.warn("theme.css not found, using default styling");
        }

        VBox header = createHeader();
        MenuBar menuBar = createMenuBar();
        VBox topContainer = new VBox(menuBar, header);
        root.setTop(topContainer);

        TabPane mainTabs = new TabPane();
        mainTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        Tab demoTab = new Tab(I18N.getInstance().get("demosapp.tab.demos", "Applications & Demos"));
        demoTab.setContent(createDiscoveryView());
        
        Tab libTab = new Tab(I18N.getInstance().get("demosapp.tab.libraries", "Libraries"));
        libTab.setContent(createLibraryView());
        
        mainTabs.getTabs().addAll(demoTab, libTab);
        root.setCenter(mainTabs);

        Scene scene = new Scene(root, 1000, 750);
        if (cssResource != null) {
            scene.getStylesheets().add(cssResource.toExternalForm());
        }

        primaryStage.setTitle(I18N.getInstance().get("demosapp.header.title", "JScience Demos"));
        primaryStage.setScene(scene);

        ThemeManager.getInstance().applyTheme(scene);
    }
    
    private javafx.scene.Node createDiscoveryView() {
        Map<String, List<Viewer>> demosByCategory = discoverAndSortProviders();

        VBox allContent = new VBox(15);
        allContent.setPadding(new Insets(20));
        allContent.getStyleClass().add("content-box");

        if (demosByCategory.isEmpty()) {
            allContent.getChildren().add(new Label(I18N.getInstance().get("demosapp.nodemos")));
        } else {
            for (Map.Entry<String, List<Viewer>> entry : demosByCategory.entrySet()) {
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
        scroll.setStyle("-fx-background-color: transparent;");
        return scroll;
    }
    
    private javafx.scene.Node createLibraryView() {
        VBox libContent = new VBox(20);
        libContent.setPadding(new Insets(30));
        libContent.setAlignment(Pos.TOP_LEFT);
        
        Label libTitle = new Label("System Libraries & Engines");
        libTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #007acc;");
        return libContent;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu languageMenu = new Menu(I18N.getInstance().get("menu.preferences.language", "Language"));
        ToggleGroup langGroup = new ToggleGroup();

        for (Locale locale : I18N.getInstance().getSupportedLocales()) {
            String label = locale.getDisplayLanguage(locale);
            if (label.length() > 0) {
                label = label.substring(0, 1).toUpperCase() + label.substring(1);
            }

            RadioMenuItem item = new RadioMenuItem(label);
            item.setToggleGroup(langGroup);
            item.setSelected(locale.getLanguage().equals(I18N.getInstance().getLocale().getLanguage()));
            item.setOnAction(e -> {
                I18N.getInstance().setLocale(locale);
                buildUI();
            });
            languageMenu.getItems().add(item);
        }

        Menu themeMenu = new Menu(I18N.getInstance().get("demosapp.menu.theme", "Theme"));
        ToggleGroup themeGroup = new ToggleGroup();
        String currentTheme = ThemeManager.getInstance().getCurrentTheme();

        RadioMenuItem modenaItem = new RadioMenuItem(I18N.getInstance().get("demosapp.menu.theme.modena", "Modena (Light)"));
        modenaItem.setToggleGroup(themeGroup);
        modenaItem.setSelected("Modena".equalsIgnoreCase(currentTheme));
        modenaItem.setOnAction(e -> {
            ThemeManager.getInstance().setTheme("Modena");
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem caspianItem = new RadioMenuItem(I18N.getInstance().get("demosapp.menu.theme.caspian", "Caspian"));
        caspianItem.setToggleGroup(themeGroup);
        caspianItem.setSelected("Caspian".equalsIgnoreCase(currentTheme));
        caspianItem.setOnAction(e -> {
            ThemeManager.getInstance().setTheme("Caspian");
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem highContrastItem = new RadioMenuItem(
                I18N.getInstance().get("demosapp.menu.theme.highcontrast", "High Contrast"));
        highContrastItem.setToggleGroup(themeGroup);
        highContrastItem.setSelected("HighContrast".equalsIgnoreCase(currentTheme));
        highContrastItem.setOnAction(e -> {
            ThemeManager.getInstance().setTheme("HighContrast");
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        });

        RadioMenuItem darkItem = new RadioMenuItem(I18N.getInstance().get("menu.view.theme.dark", "Dark"));
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

        Label title = new Label(I18N.getInstance().get("demosapp.header.title", "JScience Demos"));
        title.getStyleClass().add("header-label");

        Label subtitle = new Label(I18N.getInstance().get("demosapp.header.subtitle", "Scientific Applications & Tools"));
        subtitle.getStyleClass().add("header-subtitle");

        header.getChildren().addAll(title, subtitle);
        return header;
    }

    private TitledPane createSection(String category, List<Viewer> demos) {
        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        box.getStyleClass().add("section-box"); 
        
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

        Button btn = new Button(I18N.getInstance().get("demosapp.button.launch", "Launch"));
        btn.getStyleClass().add("launch-button");
        btn.getStyleClass().add("font-bold");

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
        alert.setTitle(I18N.getInstance().get("status.error", "Error"));
        alert.setHeaderText(title);
        alert.setContentText(message + "\n" + ex.getMessage());
        alert.show(); 
    }

    private Map<String, List<Viewer>> discoverAndSortProviders() {
        Map<MasterControlDiscovery.ProviderType, Map<String, List<Viewer>>> discovery = MasterControlDiscovery
                .getInstance().getProvidersByType();

        Map<String, List<Viewer>> consolidated = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        mergeProviders(consolidated, discovery.get(MasterControlDiscovery.ProviderType.APP));
        mergeProviders(consolidated, discovery.get(MasterControlDiscovery.ProviderType.DEMO));

        for (List<Viewer> list : consolidated.values()) {
            Set<String> seenClasses = new HashSet<>();
            list.removeIf(p -> !seenClasses.add(p.getClass().getName()));
            list.sort(Comparator.comparing(Viewer::getName));
        }

        return consolidated;
    }

    private void mergeProviders(Map<String, List<Viewer>> target, Map<String, List<Viewer>> source) {
        if (source == null)
            return;
        for (Map.Entry<String, List<Viewer>> entry : source.entrySet()) {
            String key = entry.getKey();
            String catName = I18N.getInstance().get("category." + key.toLowerCase().replace(" ", "_"), key);

            target.computeIfAbsent(catName, k -> new ArrayList<>()).addAll(entry.getValue());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

