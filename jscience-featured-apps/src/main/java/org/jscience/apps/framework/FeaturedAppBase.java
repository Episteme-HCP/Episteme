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

package org.jscience.apps.framework;

import org.jscience.ui.i18n.I18nManager;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Locale;
import java.util.prefs.Preferences;

import org.jscience.ui.AppProvider;
import org.jscience.ui.ThemeManager;
import org.jscience.ui.IconLoader;

/**
 * Abstract base class for all JScience Killer Apps.
 * Provides standard application framework with menus, toolbars, status bar,
 */
public abstract class FeaturedAppBase extends Application implements AppProvider {

    protected BorderPane rootPane;
    protected Stage primaryStage;
    protected MenuBar menuBar;
    protected ToolBar toolBar;

    protected HBox statusBar;
    protected Label statusLabel;
    protected ProgressBar progressBar;

    protected I18nManager i18n = I18nManager.getInstance();
    protected Preferences prefs;
    protected File currentFile;
    protected boolean isDirty = false;

    protected UndoManager undoManager = new UndoManager();
    protected javafx.beans.property.BooleanProperty simulationControlsVisible = new javafx.beans.property.SimpleBooleanProperty(
            true);
    private String currentTheme = "light";

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        this.prefs = Preferences.userNodeForPackage(getClass());

        // Initialize I18n
        I18nManager.getInstance().addBundle("org.jscience.apps.i18n.messages_apps");

        // Load saved preferences
        loadPreferences();

        // Build UI
        rootPane = new BorderPane();

        // Menu Bar and Toolbar
        AppMenuFactory menuFactory = new AppMenuFactory(this);
        menuBar = menuFactory.createMenuBar();
        VBox topContainer = new VBox(menuBar, createToolBar());
        rootPane.setTop(topContainer);

        // Main content (subclass provides this)
        rootPane.setCenter(createMainContent());

        // Status Bar
        statusBar = createStatusBar();
        rootPane.setBottom(statusBar);

        // Scene
        Scene scene = new Scene(rootPane, getDefaultWidth(), getDefaultHeight());
        applyTheme(scene);

        stage.setTitle(getAppTitle());
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> {
            e.consume();
            onExit();
        });
        stage.show();

        // Post-init
        onAppReady();

        // Listen for locale changes
        I18nManager.getInstance().addListener(this::handleLocaleChange);
    }

    private void handleLocaleChange(java.util.Locale locale) {
        javafx.application.Platform.runLater(() -> {
            // Update Title
            if (primaryStage != null) {
                primaryStage.setTitle(getAppTitle());
            }

            // Rebuild Menu and Toolbar
            AppMenuFactory factory = new AppMenuFactory(this);
            menuBar = factory.createMenuBar();

            // Update top container
            VBox top = (VBox) rootPane.getTop();
            if (top != null) {
                if (!top.getChildren().isEmpty()) {
                    top.getChildren().set(0, menuBar);
                }
                if (top.getChildren().size() > 1) {
                    top.getChildren().set(1, createToolBar());
                }
            }

            // Allow subclasses to update their content
            updateLocalizedUI();
        });
    }

    /**
     * Override this to update UI elements when language changes.
     */
    protected void updateLocalizedUI() {
        // Default impl does nothing
    }

    // ===== AppProvider Implementation =====

    @Override
    public boolean isDemo() {
        return false;
    }

    @Override
    public String getCategory() {
        return I18nManager.getInstance().get("category.featured_apps");
    }

    @Override
    public String getName() {
        return getAppTitle().replace(" - JScience", "");
    }

    @Override
    public void show(Stage stage) {
        try {
            start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== Abstract methods - subclasses must implement =====

    /** Returns the application title (localized). */
    protected abstract String getAppTitle();

    /** Creates the main content area of the application. */
    protected abstract Region createMainContent();

    /** Called when the application is fully initialized. */
    protected void onAppReady() {
    }

    /** Default window width. */
    protected double getDefaultWidth() {
        return 1024;
    }

    /** Default window height. */
    protected double getDefaultHeight() {
        return 768;
    }

    // Menu Capability Flags
    public boolean hasFileMenu() {
        return true;
    }

    public boolean hasEditMenu() {
        return true;
    }

    public boolean hasViewMenu() {
        return true;
    }

    public boolean hasToolsMenu() {
        return true;
    }

    public boolean hasPreferencesMenu() {
        return true;
    }

    public boolean hasHelpMenu() {
        return true;
    }

    /**
     * Entry point for launching logic.
     */

    // ===== Toolbar =====

    protected ToolBar createToolBar() {
        toolBar = new ToolBar();

        Button runBtn = new Button(i18n.get("toolbar.run"), IconLoader.getIcon("play"));
        runBtn.setContentDisplay(ContentDisplay.LEFT);
        runBtn.setOnAction(e -> onRun());

        Button pauseBtn = new Button(i18n.get("toolbar.pause"), IconLoader.getIcon("pause"));
        pauseBtn.setContentDisplay(ContentDisplay.LEFT);
        pauseBtn.setOnAction(e -> onPause());

        Button stopBtn = new Button(i18n.get("toolbar.stop"), IconLoader.getIcon("square"));
        stopBtn.setContentDisplay(ContentDisplay.LEFT);
        stopBtn.setOnAction(e -> onStop());

        Button resetBtn = new Button(i18n.get("toolbar.reset"), IconLoader.getIcon("rotate-ccw"));
        resetBtn.setContentDisplay(ContentDisplay.LEFT);
        resetBtn.setOnAction(e -> onReset());

        toolBar.getItems().addAll(runBtn, pauseBtn, stopBtn, new Separator(), resetBtn);

        // Bind visibility of simulation controls
        runBtn.visibleProperty().bind(simulationControlsVisible);
        runBtn.managedProperty().bind(runBtn.visibleProperty());
        pauseBtn.visibleProperty().bind(simulationControlsVisible);
        pauseBtn.managedProperty().bind(pauseBtn.visibleProperty());
        stopBtn.visibleProperty().bind(simulationControlsVisible);
        stopBtn.managedProperty().bind(stopBtn.visibleProperty());
        resetBtn.visibleProperty().bind(simulationControlsVisible);
        resetBtn.managedProperty().bind(resetBtn.visibleProperty());

        // Subclasses can add more items
        customizeToolBar(toolBar);

        return toolBar;
    }

    /** Override to add custom toolbar items. */
    protected void customizeToolBar(ToolBar toolBar) {
    }

    // ===== Status Bar =====

    protected HBox createStatusBar() {
        HBox bar = new HBox(10);
        bar.setPadding(new Insets(5, 10, 5, 10));
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setStyle("-fx-background-color: #e0e0e0;");

        statusLabel = new Label(i18n.get("status.ready"));
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(150);
        progressBar.setVisible(false);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bar.getChildren().addAll(statusLabel, spacer, progressBar);
        return bar;
    }

    protected void setStatus(String message) {
        statusLabel.setText(message);
    }

    protected void setProgress(double value) {
        progressBar.setVisible(value > 0 && value < 1);
        progressBar.setProgress(value);
    }

    // ===== Theme =====

    protected void applyTheme(Scene scene) {
        ThemeManager.getInstance().setTheme(currentTheme); // Ensure manager knows current (if local preference was
                                                           // loaded)
        // OR better: use manager as source of truth.
        // But KillerAppBase loads its own prefs in start().
        // Let's assume ThemeManager prevails for global consistency.
        ThemeManager.getInstance().applyTheme(scene);
        this.currentTheme = ThemeManager.getInstance().getCurrentTheme();
    }

    public void setTheme(String theme) {
        this.currentTheme = theme;
        ThemeManager.getInstance().setTheme(theme);
        if (primaryStage != null && primaryStage.getScene() != null) {
            ThemeManager.getInstance().applyTheme(primaryStage.getScene());
        }
    }

    // ===== File Operations =====

    public void onNew() {
        if (confirmUnsavedChanges()) {
            currentFile = null;
            isDirty = false;
            doNew();
        }
    }

    protected void doNew() {
        // Subclasses override
    }

    public void onOpen() {
        if (confirmUnsavedChanges()) {
            FileChooser chooser = new FileChooser();
            chooser.setTitle(i18n.get("menu.file.open"));
            configureFileChooser(chooser);
            File file = chooser.showOpenDialog(primaryStage);
            if (file != null) {
                currentFile = file;
                doOpen(file);
                isDirty = false;
            }
        }
    }

    protected void doOpen(File file) {
        try {
            byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
            deserializeState(data);
            setStatus(i18n.get("status.opened"));
        } catch (java.io.IOException e) {
            showError(i18n.get("dialog.error.title"), "Error opening file: " + e.getMessage());
        }
    }

    public void onSave() {
        if (currentFile == null) {
            onSaveAs();
        } else {
            doSave(currentFile);
            isDirty = false;
        }
    }

    public void onSaveAs() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(i18n.get("menu.file.saveAs"));
        configureFileChooser(chooser);
        File file = chooser.showSaveDialog(primaryStage);
        if (file != null) {
            currentFile = file;
            doSave(file);
            isDirty = false;
        }
    }

    protected void doSave(File file) {
        byte[] data = serializeState();
        if (data != null) {
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(file)) {
                fos.write(data);
                setStatus(i18n.get("status.saved"));
            } catch (java.io.IOException e) {
                showError(i18n.get("dialog.error.title"), "Error saving file: " + e.getMessage());
            }
        }
    }

    /** Override to provide binary state for saving. */
    protected byte[] serializeState() {
        return null;
    }

    /** Override to restore state from binary data. */
    protected void deserializeState(byte[] data) {
    }

    protected void configureFileChooser(FileChooser chooser) {
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JScience App Data", "*.jsand"));
    }

    public void onExport(String format) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(i18n.get("menu.file.export"));
        switch (format) {
            case "png" -> chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Image", "*.png"));
            case "csv" -> chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
            case "pdf" -> chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Document", "*.pdf"));
        }
        File file = chooser.showSaveDialog(primaryStage);
        if (file != null) {
            doExport(file, format);
        }
    }

    protected void doExport(File file, String format) {
        // Subclasses override
    }

    public void onExit() {
        if (confirmUnsavedChanges()) {
            savePreferences();
            primaryStage.close();
        }
    }

    protected boolean confirmUnsavedChanges() {
        if (!isDirty)
            return true;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(i18n.get("dialog.confirm.unsaved"));
        alert.setContentText(i18n.get("dialog.confirm.unsaved"));
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        var result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.YES) {
                onSave();
                return true;
            } else if (result.get() == ButtonType.NO) {
                return true;
            }
        }
        return false;
    }

    // ===== Edit Operations =====

    public void onUndo() {
        undoManager.undo();
    }

    public void onRedo() {
        undoManager.redo();
    }

    public void onFind() {
    }

    public void onReplace() {
    }

    // ===== View Operations =====

    public void onZoomIn() {
    }

    public void onZoomOut() {
    }

    public void onFitToWindow() {
    }

    public void onFullScreen() {
        primaryStage.setFullScreen(!primaryStage.isFullScreen());
    }

    // ===== Tools Operations =====

    public void onRun() {
    }

    public void onPause() {
    }

    public void onStop() {
    }

    public void onReset() {
    }

    public void onShowParameters() {
    }

    public void onShowConsole() {
    }

    // ===== Preferences =====

    public void onLanguageChanged(Locale locale) {
        // Rebuild menus with new locale
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setContentText("Language changed. Restart application to apply.");
        info.showAndWait();
    }

    public void onRestoreDefaults() {
        try {
            prefs.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onShowShortcuts() {
    }

    protected void loadPreferences() {
        String lang = prefs.get("language", Locale.getDefault().getLanguage());
        i18n.setLocale(Locale.of(lang));
        currentTheme = prefs.get("theme", "light");
    }

    protected void savePreferences() {
        prefs.put("language", i18n.getCurrentLocale().getLanguage());
        prefs.put("theme", currentTheme);
    }

    // ===== Help =====

    public void onShowDocumentation() {
        HelpDialog dialog = new HelpDialog(i18n.get("menu.help.documentation"));
        dialog.addTopic("General", i18n.get("menu.help.documentation"),
                "Documentation for " + getAppTitle() + ".\n\n(No specific documentation provided for this app yet.)",
                null);
        addAppHelpTopics(dialog);
        dialog.showAndWait();
    }

    public void onShowTutorials() {
        HelpDialog dialog = new HelpDialog(i18n.get("menu.help.tutorials"));
        dialog.addTopic("General", "Getting Started",
                "Welcome to " + getAppTitle() + "!\n\nUse the toolbar to control the simulation.", null);
        addAppTutorials(dialog);
        dialog.showAndWait();
    }

    protected void addAppHelpTopics(HelpDialog dialog) {
        // Subclasses override
    }

    protected void addAppTutorials(HelpDialog dialog) {
        // Subclasses override
    }

    public void onShowAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle(i18n.get("menu.help.about"));
        about.setHeaderText(getAppTitle());
        about.setContentText("JScience Killer Apps\nVersion " + i18n.get("app.version") +
                "\n\nÃ‚Â© 2025 Silvere Martin-Michiellot\nPowered by Gemini AI");
        about.showAndWait();
    }

    // ===== Utility =====

    protected void markDirty() {
        isDirty = true;
    }

    protected void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    protected void setSimulationControlsVisible(boolean visible) {
        this.simulationControlsVisible.set(visible);
    }
}
