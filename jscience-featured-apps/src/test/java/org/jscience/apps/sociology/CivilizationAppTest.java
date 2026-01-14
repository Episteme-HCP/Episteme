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

package org.jscience.apps.sociology;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.stage.Stage;
import org.jscience.ui.i18n.I18nManager;

@ExtendWith(ApplicationExtension.class)
public class CivilizationAppTest {

    @BeforeAll
    public static void setupSpec() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");

        // Enforce English locale for deterministic testing
        java.util.Locale.setDefault(java.util.Locale.ENGLISH);
        // Also reset the I18nManager singleton to use English
        I18nManager.getInstance().setLocale(java.util.Locale.ENGLISH);
    }

    @Start
    public void start(Stage stage) throws Exception {
        new CivilizationApp().start(stage);
    }

    @Test
    public void testLaunchAndI18n(FxRobot robot) {
        // Verify that the status label is correctly localized
        String expectedStatus = "STABLE";
        org.hamcrest.MatcherAssert.assertThat(robot.lookup(hasText(expectedStatus)).query(), isVisible());
    }

    @Test
    public void testMenuPresence(FxRobot robot) {
        I18nManager i18n = I18nManager.getInstance();

        // Verify "Run" button exists (localized)
        String runText = i18n.get("civilization.button.run");
        verifyThat(".toggle-button", hasText(runText));

        // Verify File menu exists (localized)
        String fileMenuText = i18n.get("menu.file");
        org.hamcrest.MatcherAssert.assertThat(
                "File menu should be visible",
                robot.lookup(hasText(fileMenuText)).tryQuery().isPresent(),
                org.hamcrest.Matchers.is(true));

        // Verify Edit menu is ABSENT (localized)
        String editMenuText = i18n.get("menu.edit");
        org.hamcrest.MatcherAssert.assertThat(
                "Edit menu should be hidden",
                robot.lookup(hasText(editMenuText)).tryQuery().isPresent(),
                org.hamcrest.Matchers.is(false));
    }

    @Test
    public void testDynamicLanguageSwitch(FxRobot robot) throws InterruptedException {
        I18nManager i18n = I18nManager.getInstance();

        // 1. Switch to French
        // Note: runLater is used by listener, but we are in test thread?
        // setLocale triggers listener which does runLater.
        // We need to wait for FX thread.
        robot.interact(() -> i18n.setLocale(java.util.Locale.FRENCH));
        // Allow UI to refresh
        Thread.sleep(500);

        // Verify Chart Title in French
        // "Dynamique de Population"
        verifyThat(".chart-title", hasText("Dynamique de Population"));
        verifyThat(".toggle-button", hasText("Lancer"));

        // 2. Switch to English
        robot.interact(() -> i18n.setLocale(java.util.Locale.ENGLISH));
        Thread.sleep(500);

        // Verify Chart Title in English
        verifyThat(".chart-title", hasText("Population Dynamics"));
        verifyThat(".toggle-button", hasText("Run"));
    }
}
