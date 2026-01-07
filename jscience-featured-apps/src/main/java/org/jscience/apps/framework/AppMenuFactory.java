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

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.util.Locale;

/**
 * Factory for creating standardized application menus.
 * Provides consistent menu structure across all killer apps.
 *
 * @author Silvere Martin-Michiellot
 */
public class AppMenuFactory {

    private final FeaturedAppBase app;
    private final I18nManager i18n = I18nManager.getInstance();

    public AppMenuFactory(FeaturedAppBase app) {
        this.app = app;
    }

    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        if (app.hasFileMenu())
            menuBar.getMenus().add(createFileMenu());
        if (app.hasEditMenu())
            menuBar.getMenus().add(createEditMenu());
        if (app.hasViewMenu())
            menuBar.getMenus().add(createViewMenu());
        if (app.hasToolsMenu())
            menuBar.getMenus().add(createToolsMenu());
        if (app.hasPreferencesMenu())
            menuBar.getMenus().add(createPreferencesMenu());
        if (app.hasHelpMenu())
            menuBar.getMenus().add(createHelpMenu());

        return menuBar;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu(i18n.get("menu.file"));

        MenuItem newItem = new MenuItem(i18n.get("menu.file.new"), org.jscience.ui.IconLoader.getIcon("file-plus"));
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(e -> app.onNew());

        MenuItem openItem = new MenuItem(i18n.get("menu.file.open"), org.jscience.ui.IconLoader.getIcon("folder-open"));
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(e -> app.onOpen());

        MenuItem saveItem = new MenuItem(i18n.get("menu.file.save"), org.jscience.ui.IconLoader.getIcon("save"));
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(e -> app.onSave());

        MenuItem saveAsItem = new MenuItem(i18n.get("menu.file.saveAs"));
        saveAsItem.setAccelerator(
                new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveAsItem.setOnAction(e -> app.onSaveAs());

        // Export submenu
        Menu exportMenu = new Menu(i18n.get("menu.file.export"));
        MenuItem exportPng = new MenuItem(i18n.get("menu.file.export.png"));
        exportPng.setOnAction(e -> app.onExport("png"));
        MenuItem exportCsv = new MenuItem(i18n.get("menu.file.export.csv"));
        exportCsv.setOnAction(e -> app.onExport("csv"));
        MenuItem exportPdf = new MenuItem(i18n.get("menu.file.export.pdf"));
        exportPdf.setOnAction(e -> app.onExport("pdf"));
        exportMenu.getItems().addAll(exportPng, exportCsv, exportPdf);

        MenuItem exitItem = new MenuItem(i18n.get("menu.file.exit"), org.jscience.ui.IconLoader.getIcon("log-out"));
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(e -> app.onExit());

        menu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(),
                saveItem, saveAsItem, new SeparatorMenuItem(),
                exportMenu, new SeparatorMenuItem(), exitItem);
        return menu;
    }

    private Menu createEditMenu() {
        Menu menu = new Menu(i18n.get("menu.edit"));

        MenuItem undoItem = new MenuItem(i18n.get("menu.edit.undo"), org.jscience.ui.IconLoader.getIcon("undo"));
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoItem.setOnAction(e -> app.onUndo());

        MenuItem redoItem = new MenuItem(i18n.get("menu.edit.redo"), org.jscience.ui.IconLoader.getIcon("redo"));
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redoItem.setOnAction(e -> app.onRedo());

        MenuItem cutItem = new MenuItem(i18n.get("menu.edit.cut"), org.jscience.ui.IconLoader.getIcon("scissors"));
        cutItem.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

        MenuItem copyItem = new MenuItem(i18n.get("menu.edit.copy"), org.jscience.ui.IconLoader.getIcon("copy"));
        copyItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

        MenuItem pasteItem = new MenuItem(i18n.get("menu.edit.paste"), org.jscience.ui.IconLoader.getIcon("clipboard"));
        pasteItem.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));

        MenuItem findItem = new MenuItem(i18n.get("menu.edit.find"));
        findItem.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        findItem.setOnAction(e -> app.onFind());

        MenuItem replaceItem = new MenuItem(i18n.get("menu.edit.replace"));
        replaceItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
        replaceItem.setOnAction(e -> app.onReplace());

        MenuItem selectAllItem = new MenuItem(i18n.get("menu.edit.selectAll"));
        selectAllItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));

        menu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(),
                cutItem, copyItem, pasteItem, new SeparatorMenuItem(),
                findItem, replaceItem, new SeparatorMenuItem(), selectAllItem);
        return menu;
    }

    private Menu createViewMenu() {
        Menu menu = new Menu(i18n.get("menu.view"));

        MenuItem zoomIn = new MenuItem(i18n.get("menu.view.zoomIn"), org.jscience.ui.IconLoader.getIcon("zoom-in"));
        zoomIn.setAccelerator(new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN));
        zoomIn.setOnAction(e -> app.onZoomIn());

        MenuItem zoomOut = new MenuItem(i18n.get("menu.view.zoomOut"), org.jscience.ui.IconLoader.getIcon("zoom-out"));
        zoomOut.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN));
        zoomOut.setOnAction(e -> app.onZoomOut());

        MenuItem fitWindow = new MenuItem(i18n.get("menu.view.fitToWindow"));
        fitWindow.setOnAction(e -> app.onFitToWindow());

        MenuItem fullScreen = new MenuItem(i18n.get("menu.view.fullScreen"),
                org.jscience.ui.IconLoader.getIcon("maximize"));
        fullScreen.setAccelerator(new KeyCodeCombination(KeyCode.F11));
        fullScreen.setOnAction(e -> app.onFullScreen());

        // Theme submenu
        Menu themeMenu = new Menu(i18n.get("menu.view.theme"));
        RadioMenuItem lightTheme = new RadioMenuItem(i18n.get("menu.view.theme.light"),
                org.jscience.ui.IconLoader.getIcon("sun"));
        RadioMenuItem darkTheme = new RadioMenuItem(i18n.get("menu.view.theme.dark"),
                org.jscience.ui.IconLoader.getIcon("moon"));
        ToggleGroup themeGroup = new ToggleGroup();
        lightTheme.setToggleGroup(themeGroup);
        darkTheme.setToggleGroup(themeGroup);
        lightTheme.setSelected(true);
        lightTheme.setOnAction(e -> app.setTheme("light"));
        darkTheme.setOnAction(e -> app.setTheme("dark"));
        themeMenu.getItems().addAll(lightTheme, darkTheme);

        menu.getItems().addAll(zoomIn, zoomOut, fitWindow, new SeparatorMenuItem(),
                fullScreen, new SeparatorMenuItem(), themeMenu);
        return menu;
    }

    private Menu createToolsMenu() {
        Menu menu = new Menu(i18n.get("menu.tools"));

        MenuItem runItem = new MenuItem(i18n.get("menu.tools.run"), org.jscience.ui.IconLoader.getIcon("play"));
        runItem.setAccelerator(new KeyCodeCombination(KeyCode.F5));
        runItem.setOnAction(e -> app.onRun());

        MenuItem pauseItem = new MenuItem(i18n.get("menu.tools.pause"), org.jscience.ui.IconLoader.getIcon("pause"));
        pauseItem.setOnAction(e -> app.onPause());

        MenuItem stopItem = new MenuItem(i18n.get("menu.tools.stop"), org.jscience.ui.IconLoader.getIcon("square"));
        stopItem.setOnAction(e -> app.onStop());

        MenuItem resetItem = new MenuItem(i18n.get("menu.tools.reset"),
                org.jscience.ui.IconLoader.getIcon("rotate-ccw"));
        resetItem.setOnAction(e -> app.onReset());

        MenuItem paramsItem = new MenuItem(i18n.get("menu.tools.parameters"),
                org.jscience.ui.IconLoader.getIcon("settings"));
        paramsItem.setOnAction(e -> app.onShowParameters());

        MenuItem consoleItem = new MenuItem(i18n.get("menu.tools.console"),
                org.jscience.ui.IconLoader.getIcon("terminal"));
        consoleItem.setOnAction(e -> app.onShowConsole());

        menu.getItems().addAll(runItem, pauseItem, stopItem, new SeparatorMenuItem(),
                resetItem, new SeparatorMenuItem(), paramsItem, consoleItem);
        return menu;
    }

    private Menu createPreferencesMenu() {
        Menu menu = new Menu(i18n.get("menu.preferences"));

        // Language submenu
        Menu langMenu = new Menu(i18n.get("menu.preferences.language"), org.jscience.ui.IconLoader.getIcon("globe"));
        ToggleGroup langGroup = new ToggleGroup();
        for (Locale locale : I18nManager.getSupportedLocales()) {
            String name = locale.getDisplayLanguage(locale);
            if (name.length() > 1) {
                name = name.substring(0, 1).toUpperCase(locale) + name.substring(1);
            }
            RadioMenuItem langItem = new RadioMenuItem(name);
            langItem.setToggleGroup(langGroup);
            langItem.setSelected(locale.equals(i18n.getCurrentLocale()));
            langItem.setOnAction(e -> {
                i18n.setLocale(locale);
                app.onLanguageChanged(locale);
            });
            langMenu.getItems().add(langItem);
        }

        MenuItem defaultsItem = new MenuItem(i18n.get("menu.preferences.defaults"));
        defaultsItem.setOnAction(e -> app.onRestoreDefaults());

        MenuItem shortcutsItem = new MenuItem(i18n.get("menu.preferences.shortcuts"));
        shortcutsItem.setOnAction(e -> app.onShowShortcuts());

        menu.getItems().addAll(langMenu, new SeparatorMenuItem(), defaultsItem, shortcutsItem);
        return menu;
    }

    private Menu createHelpMenu() {
        Menu menu = new Menu(i18n.get("menu.help"));

        MenuItem docsItem = new MenuItem(i18n.get("menu.help.documentation"));
        docsItem.setAccelerator(new KeyCodeCombination(KeyCode.F1));
        docsItem.setOnAction(e -> app.onShowDocumentation());

        MenuItem tutorialItem = new MenuItem(i18n.get("menu.help.tutorials"));
        tutorialItem.setOnAction(e -> app.onShowTutorials());

        MenuItem aboutItem = new MenuItem(i18n.get("menu.help.about"), org.jscience.ui.IconLoader.getIcon("info"));
        aboutItem.setOnAction(e -> app.onShowAbout());

        menu.getItems().addAll(docsItem, tutorialItem, new SeparatorMenuItem(), aboutItem);
        return menu;
    }
}
